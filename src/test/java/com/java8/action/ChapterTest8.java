package com.java8.action;

import com.java8.action.dao.DataBase;
import com.java8.action.entity.Customer;
import com.java8.action.entity.Dish;
import com.java8.action.entity.Dish.Type;
import com.java8.action.entity.Product;
import com.java8.action.factory.ProductFactory;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.function.*;

import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author li huaichuan
 * @date 19-9-26
 */
public class ChapterTest8 {

	static List<Dish> menu;

	static {
		menu = Arrays.asList(
				new Dish("pork", false, 800, Type.MEAT),
				new Dish("beef", false, 700, Type.MEAT),
				new Dish("chicken", false, 400, Type.MEAT),
				new Dish("french fries", true, 530, Type.OTHER),
				new Dish("rice", true, 350, Type.OTHER),
				new Dish("season fruit", true, 120, Type.OTHER),
				new Dish("pizza", true, 550, Type.OTHER),
				new Dish("prawns", false, 300, Type.FISH),
				new Dish("salmon", false, 450, Type.FISH));
	}

	/**
	 * 通过peek()方法可以查看Stream流水线操作中每一步的输出结果
	 */
	@Test
	public void test9() {
		List<Integer> numbers = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9);
		List<Integer> result = numbers.stream()
				.peek(x -> System.out.println("from stream: " + x))
				.map(x -> x + 17)
				.peek(x -> System.out.println("after map: " + x))
				.filter(x -> x % 2 == 0)
				.peek(x -> System.out.println("after filter: " + x))
				.limit(3)
				.peek(x -> System.out.println("after limit: " + x + "\n"))
				.collect(toList());
	}

	/**
	 * 使用Lambda表达式重构工厂模式
	 */
	@Test
	public void test8() {
		Product loan = ProductFactory.createProduct("loan");
		System.out.println(loan.getClass().getSimpleName());//result: Loan
	}

	/**
	 * 使用Lambda表达式重构责任链模式
	 */
	@Test
	public void test7() {
		HeaderTextProcessing p1 = new HeaderTextProcessing();
		SpellCheckerProcessing p2 = new SpellCheckerProcessing();
		p1.setSuccessor(p2);
		System.out.println(p1.handle("labdas")); // From Raoul, Mario and Alan: lambdas
		//可以通过Lambda表达式来实现相同的效果
		UnaryOperator<String> u1 = text -> "From Raoul, Mario and Alan: " + text;
		UnaryOperator<String> u2 = text -> text.replaceAll("labda", "lambda");
		Function<String, String> f = u1.andThen(u2);
		System.out.println(f.apply("labdas"));
	}

	abstract class ProcessingObject<T> {
		protected ProcessingObject<T> successor;

		public void setSuccessor(ProcessingObject<T> successor) {
			this.successor = successor;
		}

		public T handle(T t) {
			System.out.println(this.getClass().getName()); //这里的this指向的是子类
			T result = this.handleWork(t);
			return Objects.nonNull(successor) ? successor.handle(result) : result;
		}

		abstract T handleWork(T t);
	}

	public class HeaderTextProcessing extends ProcessingObject<String> {
		public String handleWork(String text) {
			return "From Raoul, Mario and Alan: " + text;
		}
	}

	public class SpellCheckerProcessing extends ProcessingObject<String> {
		public String handleWork(String text) {
			return text.replaceAll("labda", "lambda");
		}
	}

	/**
	 * <P>使用Lambda表达式重构观察者模式</P>
	 * 实际场景,观察者的逻辑有可能十分复杂，它们可能还持有状态，抑或定义了多个方法，诸如此类。在这些情形下，你还是应该继续使用实现类的方式
	 */
	@Test
	public void test6() {
		feed f1 = new feed();
		f1.registerObserver(new NYTimes());
		f1.registerObserver(new Guardian());
		f1.notifyObservers("jack ma said: i actually don't like money at all");
		//无需提供观察者子类实现, Lambda表达式实现同样的效果
		feed f2 = new feed();
		f2.registerObserver(tweet -> {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		});
		f2.registerObserver(tweet -> {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet another news in London... " + tweet);
			}
		});
		f2.notifyObservers("jack ma said: i actually don't like money at all");
	}

	interface Subject {
		void registerObserver(Observer o);

		void notifyObservers(String sweet);
	}

	class feed implements Subject {
		private final List<Observer> list = new ArrayList<>();

		@Override
		public void registerObserver(Observer o) {
			list.add(o);
		}

		@Override
		public void notifyObservers(String sweet) {
			list.forEach(o -> o.notify(sweet));
		}
	}

	interface Observer {
		void notify(String tweet);
	}

	class NYTimes implements Observer {
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		}
	}

	class Guardian implements Observer {
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet another news in London... " + tweet);
			}
		}
	}

	/**
	 * <P>使用Lambda表达式重构模板设计模式</P>
	 * 个人理解,这里没有绝对的优劣之分, 需根据具体场景,如果是在业务比较复杂,可变的部分比较多时,使用Lambda方式可能反而让代码的结构变得更混乱
	 */
	@Test
	public void test5() {
		new OnlineBankingLambda().processCustomer(9527, customer -> System.out.println("不同的行为参数化传递给模板方法"));
	}

	class OnlineBankingLambda {
		/**
		 * 模板方法: 封装不变部分，扩展可变部分
		 */
		public void processCustomer(int id, Consumer<Customer> consumer) {
			Customer c = DataBase.getCustomerById(id);
			//在Java8, 扩展的可变部分可以直接通过不同的行为参数化传递给模板方法, 不再需要创建一个子类去具体的实现.
			consumer.accept(c);
		}
	}

	abstract class OnlineBanking {
		/**
		 * 模板方法: 封装不变部分，扩展可变部分
		 */
		public void processCustomer(int id) {
			Customer c = DataBase.getCustomerById(id);
			this.makeCustomerHappy(c);
		}

		/**
		 * 可变部分由子类去实现
		 */
		abstract void makeCustomerHappy(Customer c);
	}

	/**
	 * 通过Lambda表达式来直接传递不同的策略, 不需要像Java8之前那样针对每个策略提供具体的实现.
	 *
	 * @throws IOException
	 */
	@Test
	public void test4() throws IOException {
		boolean r1 = new strategy(i -> i.length() > 8).test("djdjdsjdj");
	}

	class strategy {
		//假定这是一个自定义的策略
		private Predicate<String> predicate;

		public strategy(Predicate<String> predicate) {
			this.predicate = predicate;
		}

		public boolean test(String s) {
			return predicate.test(s);
		}
	}

	/**
	 * 从Lambda表达式到方法引用的转换
	 */
	@Test
	public void test3() {
		//对所有菜肴的热量求和
		Integer r1 = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
		Integer r2 = menu.stream().collect(summingInt(Dish::getCalories));
		System.out.println(r2);
	}

	/**
	 * 重载方法的Lambda匹配问题
	 */
	@Test
	public void test2() {
		doSomething((Task) () -> System.out.println());//此处重载的方法入参具有相同的函数描述符() -> void, 可以使用显式的类型转换来解决这个问题
	}

	private void doSomething(Runnable r) {
		r.run();
	}

	private void doSomething(Task t) {
		t.execute();
	}

	@FunctionalInterface
	interface Task {
		void execute();
	}

	/**
	 * 匿名内部类和Lambda表达式的区别
	 */
	@Test
	public void test1() {
		int a = 10;
		Runnable r1 = () -> {
			//int a = 1;//编译不通过
			System.out.println(a);
		};
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				int a = 3;
				System.out.println(a);
			}
		};
	}
}
