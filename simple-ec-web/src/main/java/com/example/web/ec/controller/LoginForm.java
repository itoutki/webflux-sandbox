package com.example.web.ec.controller;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class LoginForm implements Serializable {
    private static final Long serialVersionUID = 1L;

    @NotEmpty
    private String email;

    public LoginForm() {
    }

    public LoginForm(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
