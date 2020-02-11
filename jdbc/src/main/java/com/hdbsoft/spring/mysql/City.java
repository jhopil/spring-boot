package com.hdbsoft.spring.mysql;

import javax.persistence.*;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer id;

    public String name;

    //@Column(name="countrycode")
    public String countryCode;

    public String district;
    public Integer population;
}
