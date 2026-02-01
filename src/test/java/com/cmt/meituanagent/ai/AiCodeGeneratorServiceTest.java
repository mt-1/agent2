package com.cmt.meituanagent.ai;

import com.cmt.meituanagent.ai.model.HtmlCodeResult;
import com.cmt.meituanagent.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiCodeGeneratorServiceTest
{

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode()
    {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(1, "请生成一个简单的 HTML 页面, 不超过30行");
        Assertions.assertNotNull(result);

    }

    @Test
    void generateMultiFileCode()
    {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("请生成一个简单的页面, 不超过30行");
        Assertions.assertNotNull(result);

    }

}
