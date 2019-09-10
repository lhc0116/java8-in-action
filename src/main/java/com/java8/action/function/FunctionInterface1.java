package com.java8.action.function;

/**
 * @author li huaichuan
 * @date 19-9-10
 */
@FunctionalInterface
public interface FunctionInterface1<O, T, K, R> {

    R apply(O o, T t, K k);

}
