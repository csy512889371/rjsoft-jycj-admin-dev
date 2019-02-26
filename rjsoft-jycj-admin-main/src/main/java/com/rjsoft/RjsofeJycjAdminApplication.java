package com.rjsoft;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableEurekaClient
@EnableCircuitBreaker
@ComponentScan({
        "com.rjsoft"
})
@EnableFeignClients(basePackages = "com.rjsoft")
@SpringBootApplication
@Slf4j
public class RjsofeJycjAdminApplication {

    public static void main(String[] args) {
        log.info("start execute RjsofeJycjAdminApplication....\n");
        System.out.println("start execute RjsofeJycjAdminApplication....\n");
        new SpringApplicationBuilder(RjsofeJycjAdminApplication.class).bannerMode(Banner.Mode.OFF).run(args);
        System.out.println("end execute RjsofeJycjAdminApplication....\n");
        log.info("end execute RjsofeJycjAdminApplication....\n");
    }
}