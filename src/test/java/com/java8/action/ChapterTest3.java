package com.java8.action;

import com.java8.action.entity.Apple;
import com.java8.action.entity.Dish;
import com.java8.action.entity.Trader;
import com.java8.action.entity.Transaction;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChapterTest3 {

    static List<Apple> inventory;
    static List<Dish> menu;

    static {
        inventory = new ArrayList<>();
        inventory.add(new Apple("red", 94));
        inventory.add(new Apple("green", 100));
        inventory.add(new Apple("yellow", 65));
        inventory.add(new Apple("blue", 19));
        inventory.add(new Apple("green", 65));
        inventory.add(new Apple("blue", 94));

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
    }

    /**
     * 函数的组合用法
     */
    @Test
    public void test15() {
        Function<String, Integer> f = i -> Integer.valueOf(i);//方法引用写法: Integer::valueOf
        Function<Integer, Apple> g = weight -> new Apple(weight); //构造函数引用写法: Apple::new
        Function<String, Apple> h = f.andThen(g); // andThen()相当于数学上的 g(f(x)) 函数
        Apple apple = h.apply("99"); //result: Apple(color=null, weight=99)

        Function<Apple, String> y = Apple::getColor;
        Function<Apple, Integer> z = f.compose(y); // compose()相当于数学上的 f(y(x)) 函数
        Integer result = z.apply(new Apple("red", 78));//会报 java.lang.NumberFormatException: For input string: "red" 异常
    }

    /**
     * 谓词的组合用法
     * and和or方法是按照在表达式链中的位置，从左到右确定优先级的，如a.or(b).and(c).or(d) 可以看成 ((a || b) && c) || d
     */
    @Test
    public void test14() {
        Predicate<Apple> p1 = apple -> "green".equals(apple.getColor());
        final Predicate<Apple> negate = p1.negate(); //非
        System.out.println(negate.test(new Apple("green", 98)));// result: false

        final Predicate<Apple> and = p1.and(apple -> apple.getWeight() > 150);//与
        System.out.println(and.test(new Apple("green", 140)));//result: false

        final Predicate<Apple> or = p1.or(apple -> apple.getWeight() > 150);//或
        System.out.println(or.test(new Apple("blue", 170)));//result: true
    }

    /**
     * 比较器组合的用法
     */
    @Test
    public void test13() {
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());//苹果按重量倒序排序
        System.out.println(inventory);
        //苹果按重量倒序排序，当苹果重量相同时，按颜色升序排序
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
        System.out.println(inventory);
    }

    /**
     * 假设需要对一个List集合进行不同规则的排序，我们可以有多种实现方式
     * <P>下面这段代码是经过重构后的最终代码，它经历了 使用函数式接口的实例--》使用匿名类--》使用Lambda表达式--》使用方法引用 的逐步简化过程</P>
     */
    @Test
    public void test12() {
        inventory.sort(Comparator.comparing(Apple::getWeight));
    }

    /**
     * 关于构造函数的 Lambda表达式---》方法引用 之间的转换
     */
    @Test
    public void test11() {
        //1.无参构造
        Supplier<Apple> c1 = () -> new Apple();
        Supplier<Apple> c2 = Apple::new;
        Apple a1 = c2.get();

        //有参构造
        BiFunction<String, Integer, Apple> f1 = (color, weight) -> new Apple(color, weight);//Lambda表达式
        BiFunction<String, Integer, Apple> f2 = Apple::new;//构造函数引用
        Apple a2 = f2.apply("red", 10);
    }

    /**
     * 重构
     * Lambda表达式-----》等价的方法引用
     */
    @Test
    public void test10() {
        Consumer<String> c1 = i -> this.run(i);
        //上面的Lambda表达式可以简写成下面的方法引用，符合方法引用的第三类方式, this引用即所谓的外部对象
        Consumer<String> c2 = this::run;
    }

    public void run(String s) {
    }

    /**
     * 第一次测试方法引用
     */
    @Test
    public void test9() {
        List<String> list = Arrays.asList("a", "b", "A", "B");
        list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        //上面这个Lambda表达式转变成更简洁的方法引用
        list.sort(String::compareToIgnoreCase);
    }

    String s1 = "";
    static String s2 = "";

    /**
     * Lambda表达式可以没有限制的在其主体中引用实例变量和静态变量，但如果是局部变量，则必须显式的声明为final或只能被赋值一次，才能在Lambda主体中被引用
     */
    @Test
    public void test8() {
        String str = "ceshi";
        //str = "瑟瑟发抖";
        new Thread(() -> System.out.println(str)).start();
        s1 = "实例变量";
        s2 = "静态变量";
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
