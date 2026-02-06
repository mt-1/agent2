package com.cmt.meituanagent.manager;

import com.cmt.meituanagent.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;


    /**
     * 上传对象
     *
     * @param key  唯一键
     * @param file 文件
     * @return 上传结果
     */
    public PutObjectResult putObject(String key, File file){
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }


    // 上传文件到COS并返回访问URL
    public String uploadFile(String key, File file){
        PutObjectResult result = putObject(key, file);
        if(result != null){
            String url = String.format("%s/%s", cosClientConfig.getHost(), key);
            log.info("文件上传成功，访问URL: {}", url);
            return url;
        } else {
            log.error("文件上传失败: {}", file.getName());
            return null;
        }

    }


}
