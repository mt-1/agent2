package com.cmt.meituanagent.core.parser;

// 代码解析器策略接口
public interface CodeParser<T> {

    // 解析代码内容
    T parseCode(String codeContent);

}
