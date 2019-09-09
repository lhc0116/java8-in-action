package com.java8.action.entity;

import lombok.Data;

/**
 * @author li huaichuan
 * @date 19-9-9
 */
@Data
public class Apple {

    private String color;
    private Integer weight;

    /*public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }*/
}
