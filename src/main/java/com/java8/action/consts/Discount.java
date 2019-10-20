package com.java8.action.consts;

import com.java8.action.service.Quote;

/**
 * 折扣服务
 * @author huaichuanli
 * @version 1.0
 * @date 2019/10/20
 */
public class Discount {

	public enum Code {
		NONE(0),
		SILVER(5),
		GOLD(10),
		PLATINUM(15),
		DIAMOND(20);

		private int percentage;

		Code(int percentage) {
			this.percentage = percentage;
		}

		public int getPercentage() {
			return percentage;
		}
	}

	/**
	 * 模拟调用折扣服务接口, 会发生网络IO, 这里假设延时1秒钟
	 * @param quote
	 * @return
	 */
	public static String applyDiscount(Quote quote) {
		return String.format("%s discount price is %.2f", quote.getShopName(), apply(quote.getPrice(), quote.getDiscountCode()));
	}

	public static double apply(double price, Code code) {
		delay();
		return price * (100 - code.getPercentage()) / 100;
	}

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
