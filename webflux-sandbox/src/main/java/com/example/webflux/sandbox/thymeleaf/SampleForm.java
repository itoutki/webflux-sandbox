package com.example.webflux.sandbox.thymeleaf;

import javax.validation.constraints.NotEmpty;

public class SampleForm {

    @NotEmpty
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
