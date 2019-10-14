package com.java8.action.service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/10/14
 */
public class Shop {

	public Future<Double> getPriceAsync(String product) {
		CompletableFuture f = new CompletableFuture();
		new Thread(() -> {
			f.complete(calculatePrice(product));
		}).start();
		return f;
	}

	private double calculatePrice(String product) {
		delay();
		return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
