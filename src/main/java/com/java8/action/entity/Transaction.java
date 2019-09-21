package com.java8.action.entity;

import lombok.Data;

/**
 * @author li huaichuan
 * @date 19-9-17
 */
@Data
public class Transaction {

    private Trader trader;
    private int year;
    private int value;
    private String currency;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
}
