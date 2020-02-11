package com.hdbsoft.spring.spring.test;

public class Hello {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void print(HelloPrint helloPrint) {
        helloPrint.print(name);
    }
}
