package com.java8.action;

import com.java8.action.entity.Apple;
import com.java8.action.function.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Java8InActionApplicationTests {

	static List<Apple> inventory;

	static {
		inventory = new ArrayList<>();
	}

	@Test
	public void test2() {
		inventory.parallelStream().filter(apple -> apple.getWeight() > 150).collect(toList());
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
