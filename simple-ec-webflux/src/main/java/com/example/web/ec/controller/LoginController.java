package com.example.web.ec.controller;

import com.example.web.ec.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

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
    public Mono<String> show(
            @SessionAttribute(name = "flash_org.springframework.validation.BindingResult.loginForm",
                    required = false)
                    BindingResult bindingResult,
            @SessionAttribute(name = "flash_loginForm", required = false)
                    LoginForm loginForm,
            Model model, WebSession session, SessionStatus sessionStatus) {
        if (bindingResult != null) {
            model.addAttribute("org.springframework.validation.BindingResult.loginForm", bindingResult);
            session.getAttributes().remove("flash_org.springframework.validation.BindingResult.loginForm");
        }

        if (loginForm != null) {
            model.addAttribute("loginForm", loginForm);
            session.getAttributes().remove("flash_loginForm");
        }

        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", Mono.just(new LoginForm()));
        }

        return Mono.just("login");
    }

//    @PostMapping()
//    public String login0(@Validated LoginForm loginForm, BindingResult bindingResult, Model model) {
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

//    @PostMapping()
//    public Mono<String> login1(@Validated @ModelAttribute("loginForm") Mono<LoginForm> loginFormMono, Model model) {
//        return loginFormMono
//                // パターン1
//                .map(l -> {
//                    return accountService.findOne(l.getEmail());
//                })
//                .map(a -> {
//                    model.addAttribute("account", a);
//                    return "account";
//                })
//                // エラーハンドリング
//                // 1. バリデーションエラー
//                .onErrorResume(WebExchangeBindException.class, e -> {
//                    logger.warn("validation error");
//                    logger.info("attribute {}{}", BindingResult.MODEL_KEY_PREFIX, e.getObjectName());
//                    model.addAttribute(BindingResult.MODEL_KEY_PREFIX + e.getObjectName(), e.getBindingResult());
//                    return Mono.just("login");
//                })
//                // 2. 業務エラー
//                .onErrorResume(NoSuchElementException.class, e -> {
//                    logger.warn("no such element");
//                    return Mono.just("login");
//                });
//    }

//    @PostMapping()
//    public Mono<String> login2(@Validated @ModelAttribute("loginForm") Mono<LoginForm> loginFormMono, Model model) {
//        return loginFormMono
//                // パターン2
//                .flatMap(l -> Mono.just(accountService.findOne(l.getEmail())))
//                .flatMap(a -> Mono.defer(() -> {
//                    model.addAttribute("account", a);
//                    return Mono.just("account");
//                }))
//                // エラーハンドリング
//                // 1. バリデーションエラー
//                .onErrorResume(WebExchangeBindException.class, e -> {
//                    logger.warn("validation error");
//                    logger.info("attribute {}{}", BindingResult.MODEL_KEY_PREFIX, e.getObjectName());
//                    model.addAttribute(BindingResult.MODEL_KEY_PREFIX + e.getObjectName(), e.getBindingResult());
//                    return Mono.just("login");
//                })
//                // 2. 業務エラー
//                .onErrorResume(NoSuchElementException.class, e -> {
//                    logger.warn("no such element");
//                    return Mono.just("login");
//                });
//    }

    @PostMapping()
    public Mono<String> login(@Validated @ModelAttribute("loginForm") Mono<LoginForm> loginFormMono,
                              Model model,
                              WebSession session) {
        return loginFormMono
                // パターン3
                .map(l -> accountService.findOne(l.getEmail()))
                .doOnNext(a -> {
                    model.addAttribute("account", a);
                })
                .map(a -> "account")
                // エラーハンドリング
                // 1. バリデーションエラー
                .onErrorResume(WebExchangeBindException.class, e -> {
                    logger.warn("validation error");
                    logger.info("attribute {}{}", BindingResult.MODEL_KEY_PREFIX, e.getObjectName());
                    session.getAttributes().put("flash_" + BindingResult.MODEL_KEY_PREFIX + e.getObjectName(), e.getBindingResult());
                    session.getAttributes().put("flash_" + e.getObjectName(), e.getTarget());
                    model.addAttribute(BindingResult.MODEL_KEY_PREFIX + e.getObjectName(), e.getBindingResult());
                    return Mono.just("redirect:/login");
                })
                // 2. 業務エラー
                .onErrorResume(NoSuchElementException.class, e -> {
                    logger.warn("no such element");
                    return Mono.just("login");
                });
    }
}
