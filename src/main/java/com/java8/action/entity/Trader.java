package com.java8.action.entity;

import lombok.Data;

/**
 * @author li huaichuan
 * @date 19-9-17
 */
@Data
public class Trader {

    private String name;
    private String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
