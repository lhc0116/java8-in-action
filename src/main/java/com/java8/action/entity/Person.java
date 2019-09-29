package com.java8.action.entity;

import lombok.Data;

import java.util.Optional;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/28
 */
@Data
public class Person {

	private Car car;
	private int age;

	public Optional<Car> getCar() {
		return Optional.ofNullable(car);
	}
}
