package com.java8.action.service;

import com.java8.action.consts.Discount;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/10/20
 */
public class Quote {

	private String shopName;
	private double price;
	private Discount.Code discountCode;

	public Quote(String shopName, double price, Discount.Code discountCode) {
		this.shopName = shopName;
		this.price = price;
		this.discountCode = discountCode;
	}

	/**
	 * 解析商店接口返回的商店名称,价格及折扣代码
	 * @param str
	 * @return
	 */
	public static Quote parse(String str) {
		String[] arr = str.split(":");
		return new Quote(arr[0], Double.parseDouble(arr[1]), Discount.Code.valueOf(arr[2]));
	}

	public String getShopName() {
		return shopName;
	}

	public double getPrice() {
		return price;
	}

	public Discount.Code getDiscountCode() {
		return discountCode;
	}
}
