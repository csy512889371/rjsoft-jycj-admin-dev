package com.rjsoft.jycj.admin.web.controller;

import com.rjsoft.magina.spi.common.dto.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jycj-admin/unitInfo")
public class UnitInfoController {


    @RequestMapping("/getHiberList")
    @ResponseBody
    public BaseResponse getHiberList() {

        return null;
    }
}
