package com.java8.action;

import com.java8.action.entity.Apple;
import com.java8.action.function.FunctionInterface1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.*;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Java8InActionApplicationTests {

	static List<Apple> inventory;

	static {
		inventory = new ArrayList<>();
	}


	/**
	 * 只要Lambda表达式和函数式接口的抽象方法签名(及函数描述符)相同，则同一个Lambda表达式可以与多个不同的函数式接口联系起来
	 */
	@Test
	public void test7() {
		Comparator<Apple> c1 = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
		ToIntBiFunction<Apple, Apple> c2 = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
		BiFunction<Apple, Apple, Integer> c3 = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
	}

	/**
	 * 使用IntPredicate避免自动装箱，提高性能
	 */
	@Test
	public void test6() {
		IntPredicate intPredicate = (int i) -> i % 2 == 1;
		intPredicate.test(1000);
		Predicate<Integer> predicate = (Integer i) -> i % 2 == 1;
		predicate.test(1000);
	}

	/**
	 * 关于Function接口
	 */
	@Test
	public void test5() {
		List<Integer> lengths = this.map(Arrays.asList("黄蓉", "张三丰", "测试"), str -> str.length());
	}

	public <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		list.forEach(i -> result.add(f.apply(i)));
		return result;
	}

	@Test
	public void test4() throws IOException {
		String result = processFile(br -> br.readLine() + br.readLine());
	}

	public String processFile(BufferedReaderProcessor brp) throws IOException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data.txt"))) {
			return brp.process(bufferedReader);
		}
	}

	@FunctionalInterface
	public interface BufferedReaderProcessor {
		String process(BufferedReader br) throws IOException;
	}

	public Callable<String> fetch() {
		return () -> "测试Lambda表达式";
	}

	/**
	 * 自定义函数式接口
	 */
	@Test
	public void test3() {
		FunctionInterface1<String, Integer, List, Map<String, Object>> f1 = (str, num, list) -> new HashMap<>(16);
	}

	@Test
	public void test2() {
		List<Apple> list = inventory.parallelStream().filter(apple -> apple.getWeight() > 150).collect(toList());
		new Thread(() -> System.out.println("删库跑路")).start();
	}

	@Test
	public void test1() {
		this.filterApples(inventory, apple -> "green".equals(apple.getColor()));
	}

	public List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
		List<Apple> result = new ArrayList<>();
		inventory.forEach(apple -> {
			if (predicate.test(apple)) {
				result.add(apple);
			}
		});
		return result;
	}
}
