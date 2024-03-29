package com.java8.action;

import com.java8.action.entity.Dish;
import com.java8.action.entity.Trader;
import com.java8.action.entity.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChapterTest5 {

	static List<Dish> menu;
	static List<Integer> nums;
	static List<Transaction> transactions;

	static {
		menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french fries", true, 530, Dish.Type.OTHER),
				new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER),
				new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH),
				new Dish("salmon", false, 450, Dish.Type.FISH));

		nums = Arrays.asList(1, 3, 5, 7, 9, 11, 13);

		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");
		transactions = Arrays.asList(
				new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000),
				new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710),
				new Transaction(mario, 2012, 700),
				new Transaction(alan, 2012, 950)
		);
	}

	/**
	 * 由值创建流, 由数组创建流, 由文件生成流, 由函数生成无限流
	 */
	@Test
	public void test24() {
		IntStream.rangeClosed(1, 100);//根据数值范围创建数值流
		Stream<String> stream = Stream.of("java8", "盖聂", "少司命");//由值创建流
		int sum = Arrays.stream(new int[]{1, 2, 3, 4}).sum();//由数组创建流
		//由文件生成流 ===>下面示例Files.lines得到一个流,流中的每个元素对应文件中的一行
		try (Stream<String> lines = Files.lines(Paths.get("1.txt"), Charset.defaultCharset())) {
			long count = lines.flatMap(line -> Arrays.stream(line.split(" ")))
					.distinct()
					.count();
		} catch (IOException ex) {
		}
		//由函数生成流: 创建无限流
		Stream.iterate(0, n -> n + 1)
				.limit(10)
				.forEach(System.out::println);
		Stream.iterate(new int[]{0, 1}, arr -> new int[]{arr[1], arr[0] + arr[1]}) //创建一个斐波纳契元祖序列
				.limit(10)
				.forEach(arr -> System.out.println("(" + arr[0] + ", " + arr[1] + ")"));
		Stream.generate(Math::random)
				.limit(5)
				.forEach(System.out::println);
	}

	/**
	 * 根据数值范围创建数值流 --->取出[1,100]中的所有勾股数
	 */
	@Test
	public void test23() {
		IntStream.rangeClosed(1, 100)
				.boxed()
				.flatMap(a -> IntStream.rangeClosed(a, 100)
						.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
						.mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}))
				.map(Arrays::toString)
				.forEach(System.out::println);
	}

	/**
	 * 原始类型流特化
	 *
	 * @see IntStream
	 * @see LongStream
	 * @see DoubleStream
	 */
	@Test
	public void test22() {
		int calories = menu.stream().mapToInt(Dish::getCalories).sum(); //映射到数值流 mapToXxx
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
		//转换回基本类型对应的对象流
		Stream<Integer> stream = intStream.boxed(); //intStream.mapToObj(Integer::valueOf);
		//默认值OptionalInt
		List<Dish> list = new ArrayList<>();
		OptionalInt optionalInt = list.stream().mapToInt(Dish::getCalories).max();
		System.out.println(optionalInt.orElse(88)); //result: 88
		// 数值范围
		long count = IntStream.rangeClosed(1, 102).filter(i -> i % 3 == 0).count();
		System.out.println(count);//result: 34
	}

	@Test
	public void test21() {
		//(1) 找出2011年发生的所有交易,并按交易额排序(从低到高)。
		List<Transaction> list = transactions.stream().filter(i -> 2011 == i.getYear()).sorted(Comparator.comparing(Transaction::getValue)).collect(Collectors.toList());
		//(2) 交易员都在哪些不同的城市工作过?
		Set<String> cities = transactions.stream().map(Transaction::getTrader).map(Trader::getCity).collect(Collectors.toSet());
		//(3) 查找所有来自于剑桥的交易员,并按姓名排序。
		List<Trader> trades = transactions.stream().map(Transaction::getTrader).filter(i -> "Cambridge".equals(i.getCity())).distinct().sorted(Comparator.comparing(Trader::getName)).collect(Collectors.toList());
		//(4) 返回所有交易员的姓名字符串,按字母顺序排序。
		String names = transactions.stream().map(Transaction::getTrader).distinct().map(Trader::getName).sorted().reduce("", (a, b) -> a + b);
		//(5) 有没有交易员是在米兰工作的?
		boolean flag = transactions.stream().map(Transaction::getTrader).anyMatch(trader -> "Milan".equals(trader.getCity()));
		//(6) 打印生活在剑桥的交易员的所有交易的总额。
		Integer sum = transactions.stream().filter(i -> "Cambridge".equals(i.getTrader().getCity())).map(Transaction::getValue).reduce(0, Integer::sum);
		//(7) 所有交易中,最高的交易额是多少?
		Integer max = transactions.stream().map(Transaction::getValue).reduce(0, Integer::max);
		//(8) 找到交易额最小的交易。
		Optional<Transaction> first = transactions.stream().min(Comparator.comparingInt(Transaction::getValue));
		System.out.println(first.get());
	}

	/**
	 * 归约操作（将流归约成一个值）----》 reduce()方法
	 */
	@Test
	public void test20() {
		Integer sum1 = nums.stream().reduce(0, Integer::sum);
		System.out.println(sum1);
		Optional<Integer> o1 = nums.stream().reduce(Integer::sum);//求和
		System.out.println(o1.get());
		Optional<Integer> o2 = nums.stream().reduce(Integer::max);//最大值
		System.out.println(o2.get());
		Integer count = menu.stream().map(d -> 1).reduce(0, Integer::sum);//计算流中元素的个数
		menu.stream().count();
	}

	@Test
	public void test19() {
		menu.stream()
				.filter(Dish::isVegetarian)
				.findAny()
				.ifPresent(i -> System.out.println(i.getName()));//会在Optional包含值的时候执行给定的代码块
	}

	/**
	 * 流扁平化
	 */
	@Test
	public void test18() {
		List<String> words = Arrays.asList("hello", "world");
		List<String> list = words.stream()
				.map(i -> i.split(""))
				.flatMap(Arrays::stream)//流扁平化，形成一个新的流
				.distinct()
				.collect(Collectors.toList());
		System.out.println(list);
	}

	/**
	 * 获取每道菜的名称的长度
	 */
	@Test
	public void test17() {
		List<Integer> list = menu.stream()
				.map(Dish::getName)
				.map(String::length)
				.collect(Collectors.toList());
	}
}
