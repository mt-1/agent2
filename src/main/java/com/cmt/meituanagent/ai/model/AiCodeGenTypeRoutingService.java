package com.cmt.meituanagent.ai.model;


import com.cmt.meituanagent.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

// AI代码生成类型智能路由服务 使用结构化输出直接返回枚举类型
public interface AiCodeGenTypeRoutingService {

    // 根据用户需求路由代码生成类型
    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum routeCodeGenType(String userPrompt);


}
