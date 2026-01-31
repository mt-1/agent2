package com.cmt.meituanagent.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cmt.meituanagent.ai.model.HtmlCodeResult;
import com.cmt.meituanagent.ai.model.MultiFileCodeResult;
import com.cmt.meituanagent.model.enums.CodeGenTypeEnum;


import java.io.File;

@Deprecated
public class CodeFileSaver {

    public static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    public static File saveHtmlCodeResult(HtmlCodeResult result)
    {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        return new File(baseDirPath);
    }

    public static File saveMultiFileCodeResult(MultiFileCodeResult result)
    {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        return new File(baseDirPath);
    }

    // 路径：tmp/code_output/{bizType}_{snowflakeId}
    private static String buildUniqueDir(String bizType){
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    // 写入文件
    private static void writeToFile(String baseDirPath, String fileName, String content)
    {
        String filePath = baseDirPath + File.separator + fileName;
        FileUtil.writeString(content, filePath, "utf-8");
    }

}
