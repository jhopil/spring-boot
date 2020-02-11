package com.hdbsoft.spring.jms;

public class Email {

    public String to;
    public String body;

    public Email() {}

    public Email(String to, String body) {
        this.to = to;
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("Email{to=%s, body=%s}", to, body);
    }
}
