package com.ofektom.springtry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringTryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTryApplication.class, args);
    }

}
