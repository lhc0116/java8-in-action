package com.java8.action;

import com.java8.action.utils.ParallelStream;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author li huaichuan
 * @date 19-9-22
 */
public class ChapterTest7 {

    @Test
    public void test2() {

    }

    /**
     * 测试流的性能
     */
    @Test
    public void test1() {
        long sec1 = this.measureSumPerf(ParallelStream::iterativeSum, 1000_0000);
        System.out.println(sec1);//4毫秒
        long sec2 = this.measureSumPerf(ParallelStream::sequentialSum, 1000_0000);
        System.out.println(sec2);//16毫秒
        //每次应用iterate()方法时都要依赖前一次应用的结果，因此无法有效的把流划分为多个小块来并行处理，这里把流标记成并行，实则给原本的顺序处理增加了额外的开销
        long sec3 = this.measureSumPerf(ParallelStream::parallelSum, 1000_0000);
        System.out.println(sec3);//241毫秒
    }

    public long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + sum);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }
}
