package com.example.web.ec.controller;

import com.example.web.ec.model.Account;
import com.example.web.ec.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
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

//    @PostMapping()
//    public String login(@Validated LoginForm loginForm, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            logger.warn("validation error");
//            return "login";
//        }
//
//        try {
//            Account account = accountService.findOne(loginForm.getEmail());
//            model.addAttribute("account", account);
//            return "account";
//        } catch (NoSuchElementException e) {
//            return "redirect:/login";
//        }
//    }

    @PostMapping()
    public Mono<String> login(@Validated Mono<LoginForm> loginFormMono, Model model) {
        return loginFormMono
                .map(l -> {
                    model.addAttribute("loginForm", l);
                    return accountService.findOne(l.getEmail());
                })
                .map(a -> {
                    model.addAttribute("account", a);
                    return "account";
                })
                .onErrorResume(WebExchangeBindException.class, e -> {
                    logger.warn("validation error");
                    logger.info("attribute {}{}", BindingResult.MODEL_KEY_PREFIX, Conventions.getVariableName(new LoginForm()));
                    model.addAttribute("loginForm", e.getTarget());
                    model.addAttribute(BindingResult.MODEL_KEY_PREFIX + Conventions.getVariableName(new LoginForm()), e.getBindingResult());
                    return Mono.just("login");
                })
                .onErrorResume(NoSuchElementException.class, e -> {
                    logger.warn("no such element");

//                    // TODO ここで元のLoginFormをModelに詰めて返したい
//                    if (!model.containsAttribute("loginForm")) {
//                        model.addAttribute("loginForm", new LoginForm());
//                    }

                    return Mono.just("login");
                });
    }

}
