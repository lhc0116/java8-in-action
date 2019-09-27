package com.java8.action;

import com.java8.action.entity.Dish;
import com.java8.action.entity.Dish.Type;
import com.java8.action.entity.Trader;
import com.java8.action.entity.Transaction;
import jdk.internal.util.xml.impl.ReaderUTF16;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

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

    @Test
    public void test4() {

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
