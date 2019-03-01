package com.rjsoft.jycj.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/jycj-admin")
public class IndexController {

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "这是一条thymeleaf的信息");
        List<Integer> scores = new ArrayList<>();
        scores.add(100);
        scores.add(67);
        scores.add(4);
        model.addAttribute("scores", scores);

        return "index";
    }
}
