package com.java8.action;

import com.java8.action.entity.Car;
import com.java8.action.entity.Insurance;
import com.java8.action.entity.Person;
import org.junit.Test;

import java.util.Optional;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/28
 */
public class ChapterTest10 {

	@Test
	public void test4() {

	}

	/**
	 * 组合两个Optional对象的用法
	 */
	@Test
	public void test3() {
		Optional<Insurance> insurance1 = find(Optional.of(new Person()), Optional.empty());
		System.out.println(insurance1);//result: Optional.empty
		Optional<Insurance> insurance2 = find(Optional.of(new Person()), Optional.of(new Car()));
		System.out.println(insurance2);//result: Optional[Insurance(name=null)]
	}

	public Optional<Insurance> find(Optional<Person> person, Optional<Car> car) {
		return person.flatMap(p -> car.map(c -> find(p, c)));
	}

	private Insurance find(Person p, Car c) {
		return new Insurance();
	}

	/**
	 * flatMap和map用法
	 */
	@Test
	public void test2() {
		Optional<Person> person = Optional.ofNullable(new Person());
		String name = person.flatMap(Person::getCar).flatMap(Car::getInsurance).map(Insurance::getName).orElse("哪吒之魔童降世");
		System.out.println(name); //result: 哪吒之魔童降世
	}

	/**
	 * 创建optional对象
	 */
	@Test
	public void test1() {
		//声明一个空的optional
		Optional<Car> c1 = Optional.empty();
		//根据一个非空的值创建optional, 如果传入的值是null，会抛出空指针异常
		Optional<Car> c2 = Optional.of(new Car());
		//可接受null的Optional
		Optional<Object> c3 = Optional.ofNullable(null);
	}
}
