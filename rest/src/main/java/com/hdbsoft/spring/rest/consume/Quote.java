package com.hdbsoft.spring.rest.consume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    public String type;
    public Value value;

    @Override
    public String toString() {
        return String.format("Quote[type=%s, value=%s]", type, value);
    }
}
