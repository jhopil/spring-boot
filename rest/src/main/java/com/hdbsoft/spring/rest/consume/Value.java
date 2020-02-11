package com.hdbsoft.spring.rest.consume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    public long id;
    public String quote;

    @Override
    public String toString() {
        return String.format("Value[id=%d, quote=%s]", id, quote);
    }
}
