package com.example.webflux.sandbox.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/")
    public String index(@RequestParam int n, Model model) {
        Flux<Account> accounts = Flux.range(0, n)
                .map(i -> new Account(String.valueOf(i)));
        Flux<Account> anotherAccounts = Flux.zip(Flux.range(n, n), Flux.interval(Duration.ofMillis(200)))
                .map(T -> new Account(String.valueOf(T.getT1())));
        model.addAttribute("accounts", accounts);
        model.addAttribute("anothers", anotherAccounts);

        return "biglist";
    }

    @GetMapping("/dd")
    public String dd(@RequestParam int n, Model model) {
        Flux<Account> accounts = Flux.zip(Flux.range(0, n), Flux.interval(Duration.ofMillis(100)))
                .map(T -> T.getT1())
                .map(i -> new Account(String.valueOf(i)));
//        Flux<Account> anotherAccounts = Flux.zip(Flux.range(n, n), Flux.interval(Duration.ofMillis(200)))
//                .map(T -> new Account(String.valueOf(T.getT1())));
        model.addAttribute("accounts", new ReactiveDataDriverContextVariable(accounts, 10));
//        model.addAttribute("anothers", new ReactiveDataDriverContextVariable(anotherAccounts, 5));
        return "biglist";
    }
}
