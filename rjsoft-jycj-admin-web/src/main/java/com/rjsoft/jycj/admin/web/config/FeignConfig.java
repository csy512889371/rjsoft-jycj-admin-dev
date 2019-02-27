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
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    /*
        覆盖ribbon的超时机制
    */
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(env.getProperty("feign.connectTimeoutMillis",Integer.class, 10*1000),
                env.getProperty("feign.readTimeoutMillis",Integer.class, 60*1000));
    }

    /*
        period是请求重试的间隔算法参数
        maxPeriod是请求间隔的最大时间
        maxAttempts是重试的最大次数
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(env.getProperty("feign.period",Long.class, 100L),
                env.getProperty("feign.maxPeriod",Long.class, TimeUnit.SECONDS.toMillis(1L)),
                env.getProperty("feign.maxAttempts",Integer.class, 3));
    }

    /*@Bean
    public RequestInterceptor feignBasicAuthRequestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }*/

/*    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        HttpHeaderParamFilter filter = new HttpHeaderParamFilter();
        filterRegistrationBean.setFilter(filter);
        List<String> patterns = new ArrayList<>();
        patterns.add("*//*");
        filterRegistrationBean.setUrlPatterns(patterns);
        return filterRegistrationBean;
    }*/

}
