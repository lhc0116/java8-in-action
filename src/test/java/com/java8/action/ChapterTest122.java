package com.java8.action;

import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/11/3
 */
public class ChapterTest122 {

	@Test
	public void test4() {
		CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
			//测试抛异常后,handleAsync()方法接受并处理
			int x = 1 / 0;
			return "这是一个栗子";
		}).whenComplete((res, ex) -> {
			System.out.println("whenComplete res = " + res);
			if (Objects.nonNull(ex)) {
				System.out.println("whenComplete ex" + ex.getCause().getMessage());
			}
		});
		System.out.println("f.join() = " + f.join());
	}

	@Test
	public void test3() {
		CompletableFuture<String> f = CompletableFuture.completedFuture("CompletableFuture 1");
		CompletableFuture<String> f1 = f.thenCompose(res -> {
			System.out.println("thenCompose res = " + res);
			return CompletableFuture.supplyAsync(() -> "CompletableFuture 2");
		});
		System.out.println(f1.join());
		CompletableFuture<Integer> f3 = CompletableFuture.completedFuture(998);
		CompletableFuture<String> f4 = f.thenCombine(f3, (str, num) -> {
			System.out.println("str = " + str + ", num= " + num);
			return str + num;
		});
		System.out.println(f4.join());
	}

	@Test
	public void test2() {
		CompletableFuture<Void> f = CompletableFuture.supplyAsync(() -> {
			//测试抛异常后,handleAsync()方法接受并处理
			int x = 1 / 0;
			return "这是一个栗子";
		}).handle((res, ex) -> {
			System.out.println("handle res = " + res);
			if (Objects.nonNull(ex)) {
				System.out.println("handle ex" + ex.getCause().getMessage());
			}
			return Objects.nonNull(ex) ? 0 : 1;
		}).thenApply(res -> {
			System.out.println("thenApply res = " + res);
			return res == 1 ? "success" : "error";
		}).thenAccept(res -> System.out.println("thenAccept res = " + res)
		).thenRun(() -> System.out.println("没有参数, 异步执行一个没有返回值的任务"));
		f.join();
	}

	public String get() {
		delay();
		return "异步任务结果";
	}

	public void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
