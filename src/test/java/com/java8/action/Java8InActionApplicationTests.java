package com.java8.action;

import com.java8.action.entity.Apple;
import com.java8.action.function.FunctionInterface1;
import com.java8.action.function.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Java8InActionApplicationTests {

	static List<Apple> inventory;

	static {
		inventory = new ArrayList<>();
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
