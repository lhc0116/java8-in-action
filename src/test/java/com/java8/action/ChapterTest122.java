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
	public void test10() {
		CompletableFuture<String> f1 = CompletableFuture.supplyAsync(this::get);
		CompletableFuture<Void> f2 = CompletableFuture.allOf();
		CompletableFuture<Void> f3 = f1.runAfterEither(f2, () -> System.out.println("执行一个任务，没有入参"));
		f3.join();
	}

	public String get() {
		delay();//这里会延时一秒钟
		return "CompletableFuture 1";
	}

	@Test
	public void test9() {
		CompletableFuture<String> f1 = CompletableFuture.supplyAsync(this::get);
		CompletableFuture<String> f2 = CompletableFuture.completedFuture("CompletableFuture 2");
		CompletableFuture<Integer> f3 = f1.applyToEither(f2, res -> {
			System.out.println("res = " + res);
			return res.length();
		});
		System.out.println("f3.join() = " + f3.join());
	}

	public void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test8() {
		CompletableFuture<String> f1 = CompletableFuture.completedFuture("CompletableFuture 1");
		CompletableFuture<String> f2 = CompletableFuture.supplyAsync(this::get);
		CompletableFuture<Void> both = f1.acceptEither(f2, System.out::println);
		both.join();
	}

	@Test
	public void test7() {
		CompletableFuture<Integer> f1 = CompletableFuture.completedFuture(9523);
		CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "");
		CompletableFuture<Void> both = f1.runAfterBoth(f2, () -> System.out.println("执行一个任务，没有入参"));
		both.join();
	}

	@Test
	public void test6() {
		CompletableFuture<Integer> f1 = CompletableFuture.completedFuture(9523);
		CompletableFuture<String> f2 = CompletableFuture.supplyAsync(this::get);
		CompletableFuture<Void> both = f1.thenAcceptBoth(f2, (num, str) -> System.out.println("num = " + num + ", str = " + str));
		both.join();
	}

	@Test
	public void test5() {
		CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
			//测试抛异常后,exceptionally()方法接受并处理
			//int x = 1 / 0;
			return "这是一个栗子";
		}).exceptionally(ex -> ex.getCause().getMessage());
		System.out.println("f.join() = " + f.join());
	}

	@Test
	public void test4() {
		CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
			//测试抛异常后,whenComplete()方法接受并处理
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
			//测试抛异常后,handle()方法接受并处理
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

}
