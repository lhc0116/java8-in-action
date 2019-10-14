package com.java8.action;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
public class ChapterTest11 {

	@Test
	public void test1() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<String> f = executorService.submit(() -> "ceshishanghu");
		String s = f.get(3, TimeUnit.SECONDS);
		System.out.println(s);
	}
}
