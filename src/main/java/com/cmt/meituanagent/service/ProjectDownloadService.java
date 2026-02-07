package com.cmt.meituanagent.service;


import jakarta.servlet.http.HttpServletResponse;

public interface ProjectDownloadService{
    // 下载项目压缩包
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
