package com.java8.action;

import com.java8.action.service.Shop;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.*;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
public class ChapterTest11 {

	@Test
	public void test2() {
		Shop shop = new Shop("bestshop");
		long start = System.nanoTime();
		Future<Double> future = shop.getPriceAsync("人间失格");
		long invocationTime = (System.nanoTime() - start) / 1000_000L;
		System.out.println(invocationTime + "msecs");
		System.out.println("这里可以处理其他的任务");

		try {
			Double price = future.get(10, TimeUnit.SECONDS);
			System.out.printf("price is %.2f%n", price);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		long retrievalTime = (System.nanoTime() - start) / 1000_000L;
		System.out.println("complete time " + retrievalTime + "msecs");
	}

	@Test
	public void test1() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<String> f = executorService.submit(() -> "ceshishanghu");
		String s = f.get(3, TimeUnit.SECONDS);
		System.out.println(s);
	}
}
