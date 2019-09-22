package com.java8.action.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/22 17:24
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	/**
	 * 创建一个空的结果容器,供数据收集使用
	 * @return
	 */
	@Override
	public Supplier<List<T>> supplier() {
		return ArrayList::new;
	}

	/**
	 * 将元素添加到结果容器
	 * @return
	 */
	@Override
	public BiConsumer<List<T>, T> accumulator() {
		return List::add;
	}

	/**
	 * 此方法定义了在使用并行流时,从各个子流进行归约所得的结果容器要如何合并在一起
	 * @return
	 */
	@Override
	public BinaryOperator<List<T>> combiner() {
		return (left, right) -> {
			left.addAll(right);
			return left;
		};
	}

	/**
	 * 对结果容器做最终类型转换
	 * @return
	 */
	@Override
	public Function<List<T>, List<T>> finisher() {
		return Function.identity();
	}

	/**
	 * 定义收集器的一些行为特征,比如无序归约、并行归约、最终类型转换finisher()返回的函数是一个恒等函数
	 * @return
	 */
	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
	}
}
