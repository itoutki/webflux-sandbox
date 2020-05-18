package com.example.webflux.sandbox.thymeleaf;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String encodedPassword;
    private Date birthday; // TODO LocalDateに直す
    private String zip;
    private String address;
    private String cardNumber;
    private Date cardExpirationDate; // TODO LocalDateに直す
    private String cardSecurityCode;

    public Account() {
        this.id = "id";
        this.name = "name_" + id;
        this.email = "email_" + id;
        this.encodedPassword = "password_" + id;
        this.birthday = new Date();
        this.zip = "zip_" + id;
        this.address = "address_" + id;
        this.cardNumber = "cardNumber_" + id;
        this.cardExpirationDate = new Date();
        this.cardSecurityCode = "cardSecurityCode_" + id;
    }

    public Account(String id) {
        this.id = id;
        this.name = "name_" + id;
        this.email = "email_" + id;
        this.encodedPassword = "password_" + id;
        this.birthday = new Date();
        this.zip = "zip_" + id;
        this.address = "address_" + id;
        this.cardNumber = "cardNumber_" + id;
        this.cardExpirationDate = new Date();
        this.cardSecurityCode = "cardSecurityCode_" + id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    public String getLastFourOfCardNumber() {
        if (cardNumber == null) {
            return "";
        }
        return cardNumber.substring(cardNumber.length() - 4);
    }
}