package com.cmt.meituanagent.service;


public interface ScreenshotService {

    /**
     * 通用截图服务，可以得到访问地址
     * @param url 网页URL
     * @return 截图URL
     */
    String generateAndUploadScreenshot(String url);
}
