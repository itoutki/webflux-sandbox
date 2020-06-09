package com.example.webflux.sandbox.session;

import com.example.webflux.sandbox.thymeleaf.SampleForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@SessionAttributes("sessionForm")
public class SessionController {
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @GetMapping("/session")
    public Mono<String> show(Model model, WebSession webSession) {
        if (!model.containsAttribute("sampleForm")) {
            model.addAttribute("sampleForm", new SampleForm());
        }
        return Mono.just("session");
    }

    @PostMapping("/session/create")
    public Mono<String> create(@ModelAttribute("sampleForm") SampleForm sampleForm, Model model) {
        logger.info("text: {}", sampleForm.getText());
        model.addAttribute("sampleForm", sampleForm);
        SessionForm sessionForm = new SessionForm();
        sessionForm.setText(sampleForm.getText());
        model.addAttribute("sessionForm", sessionForm);
        return Mono.just("session");
    }

    @PostMapping("/session/invalidate")
    public Mono<String> invalidate(WebSession webSession, Model model) {
        webSession.invalidate();
        if (!model.containsAttribute("sampleForm")) {
            model.addAttribute("sampleForm", new SampleForm());
        }
        return Mono.just("session");
    }

    @PostMapping("/session/complete")
    public Mono<String> complete(SessionStatus sessionStatus, Model model) {
        sessionStatus.setComplete();
        if (!model.containsAttribute("sampleForm")) {
            model.addAttribute("sampleForm", new SampleForm());
        }
        return Mono.just("session");
    }
}
