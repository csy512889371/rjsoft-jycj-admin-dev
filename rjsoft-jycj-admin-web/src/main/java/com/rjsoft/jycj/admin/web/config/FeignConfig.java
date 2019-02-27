package com.rjsoft.jycj.admin.web.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;


@Configuration
public class FeignConfig {

    @Autowired
    private Environment env;

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /*
      覆盖ribbon的超时机制
      ConnectTimeout：Http建立连接的超时时间。
      ReadTimeout：Http请求读取响应的超时时间
    */
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(env.getProperty("feign.connectTimeoutMillis", Integer.class, 10 * 1000),
                env.getProperty("feign.readTimeoutMillis", Integer.class, 60 * 1000));
    }

    /*
        计算重试的时间间隔。区间呈指数增长每次尝试时，以nextenterval*的速率=1.5（其中1.5是退避系数），将最大间隔。从现在到下一次尝试的返回时间（纳秒）。
        period是请求重试的间隔算法参数
        maxPeriod是请求间隔的最大时间
        maxAttempts是重试的最大次数
     */
    @Bean
    public Retryer feignRetryer() {
        //return new Retryer.Default();
        //return Retryer.NEVER_RETRY;
        return new Retryer.Default(env.getProperty("feign.period", Long.class, 100L),
                env.getProperty("feign.maxPeriod", Long.class, TimeUnit.SECONDS.toMillis(1L)),
                env.getProperty("feign.maxAttempts", Integer.class, 3));
    }

}
