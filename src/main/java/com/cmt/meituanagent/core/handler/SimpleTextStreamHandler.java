package com.cmt.meituanagent.core.handler;

import cn.hutool.core.util.StrUtil;
import com.cmt.meituanagent.model.entity.User;
import com.cmt.meituanagent.model.enums.ChatHistoryMessageTypeEnum;
import com.cmt.meituanagent.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SimpleTextStreamHandler {

    // HTML、MULTI_FILE处理流
    public Flux<String> handle(Flux<String> originFlux,
                               ChatHistoryService chatHistoryService,
                                long appId, User loginUser){
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originFlux
                .map(chunk -> {
                    aiResponseBuilder.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    // 流式响应完成后，添加AI消息到对话历史
                    String aiResponse = aiResponseBuilder.toString();
                    if(StrUtil.isNotBlank(aiResponse)){
                        chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                    }
                })
                .doOnError(error -> {
                    // AI回复失败也要记录错误消息
                    String errorMsg = "AI回复失败" + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMsg, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }

}
