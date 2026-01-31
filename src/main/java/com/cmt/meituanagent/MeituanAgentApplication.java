package com.cmt.meituanagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.cmt.meituanagent.mapper")
public class MeituanAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeituanAgentApplication.class, args);
    }

}
