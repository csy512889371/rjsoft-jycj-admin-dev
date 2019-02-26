package com.rjsoft.jycj.admin.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @GetMapping(value = "/echoSuccess")
    public String echoSuccess() {
        return "成功";
    }
}
