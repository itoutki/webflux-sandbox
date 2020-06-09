package com.example.webflux.sandbox.session;

import java.io.Serializable;

public class SessionForm implements Serializable {
    public static final long serialVersionUID = 1L;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
