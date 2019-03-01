package com.rjsoft.jycj.admin.web.controller;

import com.rjsoft.jycj.service.consumer.BlogTopicService;
import com.rjsoft.magina.spi.common.dto.BaseResponse;
import com.rjsoft.magina.spi.common.dto.PageFeign;
import com.rjsoft.uums.service.provider.dto.BlogTopicDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/jycj-admin/demo")
@Api(value = "demo", description = "例子-demo")
public class DemoController {

    @Autowired
    private BlogTopicService blogTopicService;

    @GetMapping(value = "/echoSuccess")
    @ResponseBody
    @ApiOperation("test echoSuccess")
    public BaseResponse<List<BlogTopicDto>> echoSuccess() {
        BaseResponse<List<BlogTopicDto>> blogTopicListResp = blogTopicService.getBlogTopicList();
        return blogTopicListResp;
    }

    @GetMapping(value = "/findPageBlogTopic")
    @ApiOperation("分页查询")
    public String findPageBlogTopic(Pageable pageable, Model model) {
        BaseResponse<PageFeign<BlogTopicDto>> pageBlogTopic = blogTopicService.findPageBlogTopic(pageable.getPageNumber(), pageable.getPageSize());

        PageFeign<BlogTopicDto> blogTopicPage = pageBlogTopic.getData();
        model.addAttribute("blogTopicPage", blogTopicPage);

        return "demo/blogTopicList";
    }
}
