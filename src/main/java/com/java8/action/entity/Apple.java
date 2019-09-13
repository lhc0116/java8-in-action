package com.java8.action.entity;

import lombok.Data;

import java.awt.*;

/**
 * @author li huaichuan
 * @date 19-9-9
 */
@Data
public class Apple {

    private String color;
    private Integer weight;

    public Apple() {
    }

    public Apple(Integer weight) {
        this.weight = weight;
    }

    public Apple(String color, Integer weight) {
        this.color = color;
        this.weight = weight;
    }



    /*public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }*/
}
