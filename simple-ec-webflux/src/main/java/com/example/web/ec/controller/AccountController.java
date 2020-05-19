package com.example.web.ec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

    @GetMapping("/account")
    public String show() {
        return "account";
    }
}
