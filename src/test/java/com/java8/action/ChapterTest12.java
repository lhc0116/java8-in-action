package com.java8.action;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
public class ChapterTest12 {

    @Test
    public void test2() {

    }

    /**
     * 使用静态工厂方法创建LocalDate和LocalTime,LocalDateTime, 以及日期、时间对象之间的转换。
     */
    @Test
    public void test() {
        LocalDate ld = LocalDate.of(2019, 10, 3);
        System.out.println(ld.getYear() + "\n" + ld.getMonth() + "\n" + ld.getDayOfMonth() + "\n" + ld.getDayOfWeek() + "\n" + ld.lengthOfMonth() + "\n" + ld.isLeapYear());
        LocalDate now = LocalDate.now();
        System.out.println(now.get(ChronoField.YEAR) + "\n" + now.get(ChronoField.MONTH_OF_YEAR) + "\n" + now.get(ChronoField.DAY_OF_MONTH));

        LocalTime lt = LocalTime.of(20, 44, 12);
        System.out.println(lt.getHour() + "\n" + lt.getMinute() + "\n" + lt.getSecond());

        LocalDate ld2 = LocalDate.parse("2019-10-05");//默认格式: yyyy-MM-dd
        System.out.println(ld2.toString());
        LocalTime lt2 = LocalTime.parse("20:42:12.828");//默认格式: HH:mm:ss.SSS
        System.out.println(lt2.toString());

        LocalDateTime ldt = LocalDateTime.of(2019, 10, 5, 21, 12, 10, 888).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        LocalDateTime ldt2 = LocalDateTime.of(ld2, lt2);
        LocalDateTime ldt3 = ld2.atTime(10, 10, 10);
        LocalDateTime ldt4 = ld2.atTime(lt2);
        LocalDateTime ldt5 = lt2.atDate(ld2);
        LocalDateTime ldt6 = LocalDateTime.parse("2019/10/05 20:20:20.888", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        LocalDate ld6 = ldt6.toLocalDate();
        LocalTime lt6 = ldt6.toLocalTime();
    }
}
