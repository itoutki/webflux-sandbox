package com.example.web.ec.controller;

import com.example.web.ec.model.Account;
import com.example.web.ec.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public String show(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }

        return "login";
    }

    @PostMapping()
    public String login(LoginForm loginForm, Model model) {
        try {
            Account account = accountService.findOne(loginForm.getEmail());
            model.addAttribute("account", account);
            return "account";
        } catch (NoSuchElementException e) {
            return "redirect:/login";
        }
    }
}
