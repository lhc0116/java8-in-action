package com.java8.action;

import com.java8.action.consts.Discount;
import com.java8.action.consts.Money;
import com.java8.action.service.ExchangeRateService;
import com.java8.action.service.Quote;
import com.java8.action.service.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
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

	@Autowired
	private ExchangeRateService exchangeRateService;

	/**
	 * 对之前的功能进行改造,不同商店的价格不再像之前那样总是在一个时刻返回，而是随着商店折扣价格返回的顺序逐一地打印输出
	 * @see CompletableFuture#thenAccept(Consumer) 接受执行完毕后的返回值做参数,进行消费
	 * @see CompletableFuture#allOf(CompletableFuture[]) 需要等待所有的计算执行完毕
	 * @see CompletableFuture#anyOf(CompletableFuture[]) 任意一个计算执行完毕,就不再等待
	 */
	@Test
	public void test9() {
		List<Shop> shops = Arrays.asList(
				new Shop("王小波书店", true),
				new Shop("杭州沈记古旧书店", true),
				new Shop("猫的天空之城概念书店", true),
				new Shop("纯真年代书吧", true),
				new Shop("南山书屋", true),
				new Shop("西西弗书店", true),
				new Shop("新华书店", true),
				new Shop("钟书阁", true),
				new Shop("云门书屋", true));
		System.out.println("当前处理异步请求的线程池大小为" + Math.min(shops.size(), 100) + "\t" + ",商店数量为" + shops.size());
		long start = System.nanoTime();
		Stream<CompletableFuture<String>> priceStreams = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceAndDiscount("我们仨"), executor))
				.map(future -> future.thenApply(Quote::parse)) //解析商店返回的数据没有网络I/O, 采用同步操作
				.map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
		CompletableFuture[] futures = priceStreams.map(future -> future.thenAccept(
				result -> System.out.println(result + ",\t\t\t\t\t\tdone in " + (System.nanoTime() - start) / 1000_000 + " msecs")))
				.toArray(size -> new CompletableFuture[size]);
		CompletableFuture.allOf(futures).join();
		System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
	}

	/**
	 * <p>调用商店价格服务和汇率服务
	 * <p>如果想要将两个完全无关联的异步请求的结果整合起来,则可以通过 {@link CompletableFuture#thenCombine(CompletionStage, BiFunction)} 方法来实现
	 * <p>thenCombineAsync()会导致BiFunction中定义的合并操作被提交到线程池中，由另一个任务以异步的方式执行
	 */
	@Test
	public void test8() {
		long start = System.nanoTime();
		//当查询商店价格和当前汇率的两个异步请求都完成计算后,通过thenCombine()将他们的结果进行整合,得出对应币种的价格
		CompletableFuture<String> exchangeRatePrice = CompletableFuture.supplyAsync(() -> new Shop("王小波书店").getPrice("沉默的大多数"))
				.thenCombine(CompletableFuture.supplyAsync(() -> exchangeRateService.getRate(Money.EUR, Money.USD)), (price, rate) -> String.format("price is %.2f美元", price * rate));
		System.out.println(exchangeRatePrice.join());
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	/**
	 * <p>调用商店价格服务和厂家折扣服务
	 * <p>使用自定义的执行器来处理异步请求, 并将多个有关联的异步请求通过 {@link CompletableFuture#thenCompose(Function)} 以级联的方式进行组合
	 * <p>thenCompose()方法允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作
	 * <p>CompletableFuture类中提供了许多以Async后缀结尾的方法。通常而言，名称中不带Async的方法和它的前一个任务一样，在同一个线程中运行；
	 * 而名称以Async结尾的方法会将后续的任务提交到一个线程池，所以每个任务是由不同的线程处理的。
	 */
	@Test
	public void test7() {
		System.out.println("当前处理异步请求的线程池大小为" + Math.min(shops.size(), 100) + "\t" + ",商店数量为" + shops.size());
		long start = System.nanoTime();
		List<CompletableFuture<String>> priceFutures = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceAndDiscount("Java8 In Action"), executor))
				.map(future -> future.thenApply(Quote::parse)) //解析商店返回的数据没有网络I/O, 采用同步操作
				.map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
				.collect(Collectors.toList());
		List<String> prices = priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
		System.out.println(prices);
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	/**
	 * 使用顺序流和并行流来处理同步请求.  调用商店价格服务和厂家折扣服务
	 */
	@Test
	public void test6() {
		System.out.println("当前机器有" + Runtime.getRuntime().availableProcessors() + "个可用的处理器");//测试跑的机器为4
		long start = System.nanoTime();
		List<String> discountPrices = shops.parallelStream()
				.map(shop -> shop.getPriceAndDiscount("人间失格"))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
		System.out.println(discountPrices);
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	/**
	 * 使用自定义的执行器来处理异步请求.   调用商店价格服务
	 */
	@Test
	public void test5() {
		System.out.println("当前处理异步请求的线程池大小为" + Math.min(shops.size(), 100) + "\t" + "商店数量为" + shops.size());
		long start = System.nanoTime();
		List<CompletableFuture<String>> futureList = shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getName(), shop.getPrice("三体")), executor))
				.collect(Collectors.toList());
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
		List<String> prices = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
		System.out.println(prices);
		System.out.println((System.nanoTime() - start) / 1000_000 + " msecs");
	}

	/**
	 * 使用默认的通用线程池处理异步请求.   调用商店价格服务
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
	 * 使用顺序流和并行流处理同步请求.   调用商店价格服务
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

	/**
	 * Java8之前的异步请求处理方式
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void test1() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<String> f = executorService.submit(() -> "ceshishanghu");
		String s = f.get(3, TimeUnit.SECONDS);
		System.out.println(s);
	}
}
