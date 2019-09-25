package com.java8.action.utils;

import java.util.stream.LongStream;

/**
 * @author li huaichuan
 * @date 19-9-24
 */
public class ParallelStream {

    public static long sequentialSum(long n) {
        return LongStream.iterate(1, i -> i + 1)
                .limit(n)
                .sum();
//        return LongStream.rangeClosed(1, n).reduce(0, Long::sum);//4毫秒
    }

    public static long iterativeSum(long n) {
        long sum = 0;
        for (long i = 1; i < n + 1; i++) {
            sum += i;
        }
        return sum;
    }

    public static long parallelSum(long n) {
        return LongStream.iterate(1, i -> i + 1)
                .limit(n)
                .parallel()
                .sum();
//        return LongStream.rangeClosed(1, n).parallel().reduce(0, Long::sum);//2毫秒
    }
}
