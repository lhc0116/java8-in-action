package com.java8.action;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertTrue;

/**
 * @author li huaichuan
 * @date 19-10-1
 */
public class ChapterTest12 {

    @Test
    public void test4() {
        LocalDate ld = LocalDate.of(2019, 10, 7);
        //修改时间对象的属性值,返回一个新的对象
        LocalDate ld2 = ld.withDayOfYear(365);//2019-12-31
        LocalDate ld3 = ld.withDayOfMonth(18);//2019-10-18
        LocalDate ld4 = ld.with(ChronoField.MONTH_OF_YEAR, 8);//2019-08-07
        System.out.println();
    }

    /**
     * @see Duration
     * @see Period
     */
    @Test
    public void test3() {
        Duration d1 = Duration.between(LocalDateTime.of(2019, 10, 7, 15, 55, 55, 888), LocalDateTime.now());
        Duration d2 = Duration.between(LocalTime.of(17, 55, 10), LocalTime.now());
        Duration d3 = Duration.between(Instant.ofEpochMilli(1570544602000L), Instant.now());
        System.out.println(d3.toHours());
        //Duration对象用秒和纳秒来衡量时间的长短，所以入参不能使用LocalDate类型, 否则抛UnsupportedTemporalTypeException: Unsupported unit: Seconds
        //Duration.between(LocalDate.of(2019, 10, 7), LocalDate.now());

        //如果想要对多个时间对象进行日期运算，可以用Period
        Period p1 = Period.between(LocalDate.of(2018, 8, 30), LocalDate.now());
        System.out.println(p1.getYears() + "\t" + p1.getMonths() + "\t" + p1.getDays());
        //工厂方法介绍
        Duration threeMinutes = Duration.ofMinutes(3);
        threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }

    /**
     * 计算机角度的日期和时间格式 instant
     */
    @Test
    public void test2() {
        assertTrue(Instant.ofEpochSecond(3).getEpochSecond() == Instant.ofEpochSecond(2, 1_000_000_000).getEpochSecond());
        assertTrue(Instant.ofEpochSecond(4, -1_000_000_000).getEpochSecond() == Instant.ofEpochSecond(2, 1_000_000_000).getEpochSecond());
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
