package com.java8.action;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/28
 */
public class ChapterTest9 {

	/**
	 * 默认方法带来的菱形继承问题
	 */
	@Test
	public void test4() {
		new I().hello();//result: heelo from E
	}

	class I implements G, H { }
	interface G extends E { }
	interface H extends E { }

	/**
	 * 默认方法带来的多继承问题2
	 */
	@Test
	public void test3() {
		new F().hello(); //result: heelo from E
	}

	interface E {
		default void hello() {
			System.out.println("heelo from E");
		}
	}

	class F implements A, E {
		public void hello() {
			//这里接口A和E不再具有继承关系,需显式的选择调用接口E或A中的方法,否则无法通过编译
			E.super.hello();
		}
	}

	/**
	 * 默认方法带来的多继承问题1
	 */
	@Test
	public void test2() {
		new C().hello();//result: hello from D
	}

	interface A {
		default void hello() {
			System.out.println("heelo from A");
		}
	}

	interface B extends A {
		default void hello() {
			System.out.println("heelo from B");
		}
	}

	class D implements A{
		public void hello() {
			System.out.println("hello from D");
		}
	}

	class C extends D implements A, B{
	}

	/**
	 * Java8中的新功能: 接口中可以声明默认方法和静态方法.
	 */
	@Test
	public void test1() {
		List<Integer> list = Arrays.asList(3, 5, 6, 7, 1);
		list.sort(Comparator.naturalOrder());
		System.out.println(list);
	}
}
