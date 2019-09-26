package com.java8.action;

import com.java8.action.entity.WordCounter;
import com.java8.action.spliterator.WordCounterSpliterator;
import com.java8.action.utils.ParallelStream;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author li huaichuan
 * @date 19-9-22
 */
public class ChapterTest7 {

    @Test
    public void test2() {
        String str = " Nel mezzo del cammin di nostra vita mi ritrovai in una selva oscura ché la dritta via era smarrita ";
        System.out.println(this.countIteratively(str));

        Stream<Character> characterStream = IntStream.range(0, str.length()).mapToObj(str::charAt);
        WordCounter count = characterStream.parallel().reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
        System.out.println(count.getCount());//这里使用并行流导致结果出错，因为他可能在拆分子流的时候将一个单词拆分成两个单词
        WordCounter counter = StreamSupport.stream(new WordCounterSpliterator(str), true).parallel().reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
        System.out.println(counter.getCount());//使用自定义的Spliterator，只有在遍历到单词之间的空格时才会去拆分成多个子流
    }

    private int countIteratively(String str) {
        int count = 0;
        boolean lastSpace = true;
        for (char c : str.toCharArray()) {
            if(Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if(lastSpace) {
                    count++;
                }
                lastSpace = false;
            }
        }
        return count;
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
