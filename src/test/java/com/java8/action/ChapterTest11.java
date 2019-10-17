package com.java8.action;

import com.java8.action.service.Shop;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
public class ChapterTest11 {

	static List<Shop> shops;

	static {
		shops = Arrays.asList(
				new Shop("王小波书店"),
				new Shop("杭州沈记古旧书店"),
				new Shop("猫的天空之城概念书店"),
				new Shop("纯真年代书吧"),
				new Shop("南山书屋"),
				new Shop("西西弗书店"),
				new Shop("新华书店"),
				new Shop("钟书阁"),
				new Shop("云门书屋"));
	}

	private Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), r -> {
		Thread thread = new Thread(r);
		//守护线程不会组织程序的终止
		thread.setDaemon(true);
		return thread;
	});


	/**
	 * 处理异步请求
	 */
	@Test
	public void test4() {
		System.out.println("当前机器有" + Runtime.getRuntime().availableProcessors() + "个可用的处理器");
		long start = System.nanoTime();
		List<CompletableFuture<String>> futureList = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getName(), shop.getPrice("三体"))))
				.collect(Collectors.toList());
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
		List<String> prices = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
		System.out.println(prices);
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	/**
	 * 使用顺序流和并行流处理同步请求
	 * <p>当使用并行流版本查询5个商店时,因为可以并行运行（通用线程池中处于可用状态的）的四个线程现在都处于繁忙状态，</p>
	 * <p>都在对前4个商店进行查询。第五个查询只能等到前面某一个操作完成释放出空闲线程才能继续, 因此会比之前多消耗大约 1 秒钟的时间</p>
	 */
	@Test
	public void test3() {
		System.out.println("当前机器有" + Runtime.getRuntime().availableProcessors() + "个可用的处理器");//测试跑的机器为4
		long start = System.nanoTime();
		List<String> prices = shops.parallelStream()
				.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice("三体")))
				.collect(Collectors.toList());
		System.out.println(prices);
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	@Test
	public void test2() {
		Shop shop = new Shop("bestshop");
		long start = System.nanoTime();
		Future<Double> future = shop.getPriceAsync("人间失格");
		long invocationTime = (System.nanoTime() - start) / 1000_000L;
		System.out.println(invocationTime + " msecs");
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
		System.out.println("complete time " + retrievalTime + " msecs");
	}

	@Test
	public void test1() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<String> f = executorService.submit(() -> "ceshishanghu");
		String s = f.get(3, TimeUnit.SECONDS);
		System.out.println(s);
	}
}
