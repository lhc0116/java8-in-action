package com.java8.action.service;

import com.java8.action.consts.Money;
import org.springframework.stereotype.Service;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/10/20
 */
@Service
public class ExchangeRateService {

	public double getRate(Money m1, Money m2) {
		this.delay();
		if (Money.EUR.equals(m1) && Money.USD.equals(m2))
			return 1.12;
		return 6.68;
	}

	public void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
