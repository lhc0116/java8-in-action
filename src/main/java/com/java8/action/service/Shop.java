package com.java8.action.service;

import com.java8.action.consts.Discount;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/10/14
 */
@Data
public class Shop {

	String name;
	boolean flag;

	public Shop(String name) {
		this.name = name;
	}

	public Shop(String name, boolean flag) {
		this.name = name;
		this.flag = flag;
	}

	public Future<Double> getPriceAsync(String product) {
		//这段代码和下面注释的部分是等效的. 即supplyAsync()提供了同样的错误管理机制来处理被调用者发生的异常
		return CompletableFuture.supplyAsync(() -> {
			int x = 1 / 0;
			return this.calculatePrice(product);
		});

		/*CompletableFuture future = new CompletableFuture();
		new Thread(() -> {
			try {
				int x = 1/0;
				double price = this.calculatePrice(product);
				future.complete(price);
			} catch (Exception e) {
				//如果被调用方发生异常, 就将该异常通过CompletableFuture抛出给调用方
				future.completeExceptionally(e);
			}
		}).start();
		return future;*/
	}

	public String getPriceAndDiscount(String product) {
		double price = calculatePrice(product) / 1000;
		//随机生成一个折扣服务代码
		Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
		return String.format("%s:%.2f:%s", this.name, price, code.name());
	}

	public double getPrice(String product) {
		return calculatePrice(product) / 1000;
	}

	private double calculatePrice(String product) {
		if (flag) randomDelay();
		else delay();
		return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void randomDelay() {
		Random random = new Random();
		int delay = 500 + random.nextInt(3000);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
