package com.ilqjx.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForePageController {

    @GetMapping("/home")
    public String home() {
        return "fore/home";
    }

}
