package com.java8.action;

import com.java8.action.entity.Dish;
import com.java8.action.entity.Dish.CaloricLevel;
import com.java8.action.entity.Dish.Type;
import com.java8.action.entity.Trader;
import com.java8.action.entity.Transaction;
import org.junit.Test;

import java.util.*;

import static com.java8.action.entity.Dish.CaloricLevel.*;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/21 17:04
 */
public class ChapterTest6 {

	static List<Dish> menu;
	static List<Transaction> transactions;

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
	 * 流的分组和分区操作
	 */
	@Test
	public void test2() {
		//单级分组
		Map<Type, List<Dish>> map1 = menu.stream().collect(groupingBy(Dish::getType));
		//多级分组 result: {FISH={NORMAL=[salmon], DIET=[prawns]}, OTHER={NORMAL=[french fries, pizza], DIET=[rice, season fruit]}, MEAT={NORMAL=[chicken], FAT=[pork, beef]}}
		Map<Type, Map<CaloricLevel, List<Dish>>> map2 = menu.stream().collect(groupingBy(Dish::getType, groupingBy(dish -> {
			if (dish.getCalories() < 400) return DIET;
			else if (dish.getCalories() < 700) return NORMAL;
			else return FAT;
		})));
		//菜单中每种类型的菜肴的数量
		Map<Type, Long> map3 = menu.stream().collect(groupingBy(Dish::getType, counting()));//result: {FISH=2, OTHER=4, MEAT=3}
		//菜单中每种类型热量最高的菜肴
		Map<Type, Optional<Dish>> map4 = menu.stream().collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));//result:{FISH=Optional[salmon], OTHER=Optional[pizza], MEAT=Optional[pork]}
		//上面分组操作后的Optional<Dish>是一定有值的,所以这个Optional包装没什么意义,可以通过下面这方式把Dish直接提取出来
		Map<Type, Dish> map5 = menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));//result:{FISH=Optional[salmon], OTHER=Optional[pizza], MEAT=Optional[pork]}

	}

	/**
	 * 规约和汇总操作
	 */
	@Test
	public void test1() {
		Long count = menu.stream().collect(counting());//菜单里有多少种菜
		Optional<Dish> optionalDish = menu.stream().collect(maxBy(comparingInt(Dish::getCalories)));//菜单里热量最高的菜
		menu.stream().collect(summingInt(Dish::getCalories));//菜单列表的总热量
		menu.stream().collect(averagingInt(Dish::getCalories));//菜单列表的热量平均值
		IntSummaryStatistics intSummaryStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));//一次迭代,统计出菜单列表元素个数, 菜肴热量最大值、最小值、平均值、总和
		System.out.println(intSummaryStatistics.toString()); //result: IntSummaryStatistics{count=9, sum=4200, min=120, average=466.666667, max=800}

		String names = menu.stream().map(Dish::getName).collect(joining(","));//连接字符串
		menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));//菜单列表的总热量
	}
}
