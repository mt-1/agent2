package com.cmt.meituanagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class MeituanAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeituanAgentApplication.class, args);
    }

}
