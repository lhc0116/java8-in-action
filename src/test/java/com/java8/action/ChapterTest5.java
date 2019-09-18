package com.java8.action;

import com.java8.action.entity.Dish;
import com.java8.action.entity.Trader;
import com.java8.action.entity.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.dsig.TransformService;
import java.util.*;
import java.util.stream.Collectors;

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
