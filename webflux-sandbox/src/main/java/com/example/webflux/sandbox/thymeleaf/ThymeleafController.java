package com.example.webflux.sandbox.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

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

    /**
     *
     * 何らかの処理を実行し、結果をFluxで受け取る場合
     * - 正常 1件以上のデータが返ってくる
     * - 準正常 0件のデータが返ってくる
     * - 異常
     *
     * TODO 件数によって処理を分岐させたいケースはあるか？
     * TODO
     */
    @GetMapping("/flux")
    public Mono<String> flux(@RequestParam(required = false) String email, Model model) {
        Flux<Account> accounts = getFlux(email).log("getFlux");

        // パターン1
        // 単純にFluxをmodelに追加する
        //
//        model.addAttribute("accounts", accounts);
//        return Mono.just("biglist");

        // パターン2
        // Fluxが返すデータの件数が1件以上あるかどうかで分岐する
        // 元のFluxをmodelに追加する
        // thymeleafでの値解決とhasElements以降の処理で、2回subscribeされることになる
        // TODO getFluxがHTTPリクエストやDB接続の場合、2回実行される？
        Mono<String> v = accounts.hasElements().log("hasElements")
                .map(result -> {
                    if (result) {
                        model.addAttribute("accounts", accounts);
                        return "biglist";
                    } else {
                        return "error";
                    }
                }).log("map")
                .onErrorResume(e -> Mono.just("error"));
        return v;

        // パターン3
        // Fluxが返すデータの件数が1件以上あるかどうかで分岐する
        // collectListでMono<List<Account>>に変換後、Listの件数を見て分岐する
        //
//        Mono<String> v = accounts
//                .collectList().log("collectList")
//                .map(aa -> {
//                    if (aa.size() > 0) {
//                        model.addAttribute("accounts", aa);
//                        return "biglist";
//                    } else {
//                        return "biglist";
//                    }
//                }).log("map")
//                .onErrorResume(e -> Mono.just("biglist"));
//        return v;
    }

    @GetMapping("/mono")
    public Mono<String> mono(@RequestParam(required = false) String email, Model model) {
        Mono<Account> account = getMono(email).log();
//        model.addAttribute("accounts", accounts);


        // パターン1
        //
//        Mono<String> v = account.hasElement()
//                .map(result -> {
//                    if (result) {
//                        model.addAttribute("accounts", account.flux());
//                        return "biglist";
//                    } else {
//                        return "null";
//                    }
//                });

        // パターン2
        Mono<String> v = account
                .map(a -> {
                    model.addAttribute("accounts", List.of(a));
                    return "biglist";
                })
                .switchIfEmpty(Mono.just("biglist"))
                .onErrorResume(e -> Mono.just("biglist")).log();

        return v;
    }

    public Flux<Account> getFlux(String param) {
        if (param == null || param.isEmpty() || param.equals("empty")) return Flux.empty();
        if (param.equals("error")) return Flux.error(new CustomException("Flux error"));
        return Flux.just(new Account(param));
    }

    public Mono<Account> getMono(String param) {
        if (param == null || param.isEmpty() || param.equals("empty")) return Mono.empty();
        if (param.equals("error")) return Mono.error(new CustomException("Mono error"));
        return Mono.just(new Account(param));
    }

    static class CustomException extends Exception {
        public CustomException() {
            super("custom exception");
        }

        public CustomException(String message) {
            super(message);
        }
    }

}
