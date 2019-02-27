package com.rjsoft.jycj.admin.web.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("就业促进管理API接口管理")
                .description("就业促进管理API接口管理")
                .version("1.0")
                .build();
    }
}
