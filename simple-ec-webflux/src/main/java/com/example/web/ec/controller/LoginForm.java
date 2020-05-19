package com.example.web.ec.controller;

public class LoginForm {
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
