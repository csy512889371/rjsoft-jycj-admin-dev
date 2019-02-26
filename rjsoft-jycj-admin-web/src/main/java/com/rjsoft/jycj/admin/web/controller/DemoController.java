package com.rjsoft.jycj.admin.web.controller;

import com.rjsoft.magina.spi.common.dto.BaseResponse;
import com.rjsoft.uums.service.provider.dto.BlogTopicDto;
import com.rjsoft.uums.service.provider.spi.BlogTopicSpi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/demo")
@Api(value = "demo", description = "例子-demo")
public class DemoController {

    @Autowired
    private BlogTopicSpi blogTopicSpi;

    @GetMapping(value = "/echoSuccess")
    @ApiOperation("test echoSuccess")
    public BaseResponse<List<BlogTopicDto>> echoSuccess() {
        BaseResponse<List<BlogTopicDto>> blogTopicListResp = blogTopicSpi.getBlogTopicList();
        return blogTopicListResp;
    }
}
