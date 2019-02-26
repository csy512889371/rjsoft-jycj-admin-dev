package com.rjsoft.jycj.admin.web.controller;

import com.rjsoft.uums.service.provider.dto.BlogTopicDto;
import com.rjsoft.uums.service.provider.spi.BlogTopicSpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @Autowired
    private BlogTopicSpi blogTopicSpi;

    @GetMapping(value = "/echoSuccess")
    public List<BlogTopicDto> echoSuccess() {
        List<BlogTopicDto> blogTopicList = blogTopicSpi.getBlogTopicList();
        return blogTopicList;
    }
}
