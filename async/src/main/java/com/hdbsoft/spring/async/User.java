package com.hdbsoft.spring.async;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public String name;
    public String blog;

    @Override
    public String toString() {
        return String.format("User[name=%s, blog=%s]", name, blog);
    }
}
