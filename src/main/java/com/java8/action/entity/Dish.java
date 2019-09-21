package com.java8.action.entity;

import lombok.Data;

/**
 * @author li huaichuan
 * @date 19-9-16
 */
@Data
public class Dish {

    private String name;
    private boolean vegetarian;
    private int calories;
    private Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public enum Type {
        MEAT, FISH, OTHER
    }

    public enum CaloricLevel {
        DIET, NORMAL, FAT
    }
}
