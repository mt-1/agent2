package com.cmt.meituanagent.ai;

import com.cmt.meituanagent.ai.tools.*;
import com.cmt.meituanagent.exception.BusinessException;
import com.cmt.meituanagent.exception.ErrorCode;
import com.cmt.meituanagent.model.enums.CodeGenTypeEnum;
import com.cmt.meituanagent.service.ChatHistoryService;
import com.cmt.meituanagent.utils.SpringContextUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.time.Duration;

@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {

    @Resource(name="openAiChatModel")
    private ChatModel chatModel;

//    @Resource
//    private StreamingChatModel openAiStreamingChatModel;
//
//    @Resource
//    private StreamingChatModel reasoningStreamingChatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存键: {}, 原因: {}", key, cause);
            })
            .build();


    // 兼容
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML);
    }


    // 根据 appId获取带缓存服务
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        String cacheKey = buildCacheKey(appId, codeGenType);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenType));
    }

    // 创建新的AI服务实例
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        log.info("为appId: {}创建新的AI服务实例", appId);
        // 根据appId构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
        return switch (codeGenType) {
            // Vue项目生成使用工具调用和推理模型
            case VUE_PROJECT -> {
                // 使用多例模式的 StreamingChatModel 解决并发问题
                StreamingChatModel reasoningStreamingChatModel = SpringContextUtil.getBean("reasoningStreamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(reasoningStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .tools(
                            new FileWriteTool(),
                            new FileReadTool(),
                            new FileModifyTool(),
                            new FileDirReadTool(),
                            new FileDeleteTool()
                    )
                    // 处理工具调用幻觉
                    .hallucinatedToolNameStrategy(toolExecutionRequest ->
                            ToolExecutionResultMessage.from(toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()))
                    .build();
            }


            // HTML和多文件生成使用流式对话模型
            case HTML, MULTI_FILE -> {
                // 使用多例模式的 StreamingChatModel 解决并发问题
                StreamingChatModel openAiStreamingChatModel = SpringContextUtil.getBean("streamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(openAiStreamingChatModel)
                    .chatMemory(chatMemory)
                    .build();
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType.getValue());
        };
    }

    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGeneratorService(0L, CodeGenTypeEnum.HTML);
    }

    // 构造缓存键
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenType) {
        return appId + "_" + codeGenType;
    }


}
