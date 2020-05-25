package com.example.web.ec.controller;

import com.example.web.ec.model.Account;
import com.example.web.ec.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private AccountService accountService;
    private AccountForm accountForm;

    public LoginController(AccountService accountService, AccountForm accountForm) {
        this.accountService = accountService;
        this.accountForm = accountForm;
    }

    @GetMapping
    public String show(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    @PostMapping
    public String login(@Validated LoginForm loginForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        logger.info("start: login");
        if (bindingResult.hasErrors()) {
            logger.warn("validation error");
            redirectAttributes.addFlashAttribute("loginForm", loginForm);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + Conventions.getVariableName(loginForm), bindingResult);
            return "redirect:/login";
        }

        try {
            Account account = accountService.findOne(loginForm.getEmail());
            accountForm.setAccount(account);
            return "redirect:/goods";
        } catch (NoSuchElementException e) {
            logger.warn("no such element exception requested email : {}", loginForm.getEmail());
            redirectAttributes.addFlashAttribute("loginForm", loginForm);
            return "redirect:/login";
        }
    }
}
