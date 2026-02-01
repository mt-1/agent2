package com.cmt.meituanagent.service;

import com.cmt.meituanagent.model.dto.chathistory.ChatHistoryQueryRequest;
import com.cmt.meituanagent.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.cmt.meituanagent.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author 001
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    // 添加对话记录
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    // 根据应用ID删除对话记录
    boolean deleteByAppId(Long appId);

    // 游标查询包装类
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    // 游标查询消息历史记录
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);

    // 加载聊天消息
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
