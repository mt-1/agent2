package com.cmt.meituanagent.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

// 缓存key生成工具类
public class CacheKeyUtils {

    // 根据对象生成缓存key (JSON+MD5)
    public static String generateCacheKey(Object object){
        if(object == null){
            return DigestUtil.md5Hex("null");
        }
        // 先转json，再转MD5
        String jsonStr = JSONUtil.toJsonStr(object);
        return DigestUtil.md5Hex(jsonStr);
    }


}
