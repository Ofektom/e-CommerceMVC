package com.ofektom.springtry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping
    public String index(){
        return "index";
    }
}
