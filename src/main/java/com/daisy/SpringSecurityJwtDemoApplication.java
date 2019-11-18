package com.daisy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 程序主入口
 * @author wangyunpeng
 */
@SpringBootApplication
@RestController
@RequestMapping(value = "/")
public class SpringSecurityJwtDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtDemoApplication.class, args);
    }

    @RequestMapping
    public String hello() {
        return "hello spring security jwt";
    }

}
