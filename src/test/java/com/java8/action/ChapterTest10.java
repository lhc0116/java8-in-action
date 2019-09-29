package com.java8.action;

import com.java8.action.entity.Car;
import com.java8.action.entity.Insurance;
import com.java8.action.entity.Person;
import org.junit.Test;

import java.util.Optional;

/**
 * @author huaichuanli
 * @version 1.0
 * @date 2019/9/28
 */
public class ChapterTest10 {

    /**
     * 这时一个简单的栗子
     */
    @Test
    public void test6() {
        Integer res = Optional.of("-9").flatMap(this::str2Int).filter(i -> i > 0).orElse(0);
        System.out.println(res);
    }

    private Optional<Integer> str2Int(String str) {
        try {
            return Optional.of(Integer.valueOf(str));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    /**
     * filter测试
     */
    @Test
    public void test5() {
        System.out.println(getCarInsuranceName(Optional.empty(), 8));//命运之夜
        Person p = new Person();
        p.setAge(10);
        System.out.println(getCarInsuranceName(Optional.of(p), 8));//命运之夜
        Insurance insurance = new Insurance();
        insurance.setName("知否知否");
        Car car = new Car();
        car.setInsurance(insurance);
        p.setCar(car);
        System.out.println(getCarInsuranceName(Optional.of(p), 8));//知否知否
    }

    //找出年龄大于或者等于 minAge 参数的 Person 所对应的保险公司列表。
    public String getCarInsuranceName(Optional<Person> person, int minAge) {
        return person.filter(p -> p.getAge() >= minAge).flatMap(Person::getCar).flatMap(Car::getInsurance).map(Insurance::getName).orElse("命运之夜");
    }

    /**
     * filter方法接受一个谓词作为参数。如果 Optional 对象的值存在,并且它符合谓词的条件,filter 方法就返回其值;否则它就返回一个空的 Optional 对象
     */
    @Test
    public void test4() {
        Optional.of("攀登者").filter(i -> i.length() < 4).ifPresent(System.out::println);
        Optional.<String>empty().filter(i -> i.length() < 4).ifPresent(System.out::println);
    }

    /**
     * 组合两个Optional对象的用法
     */
    @Test
    public void test3() {
        Optional<Insurance> insurance1 = find(Optional.of(new Person()), Optional.empty());
        System.out.println(insurance1);//result: Optional.empty
        Optional<Insurance> insurance2 = find(Optional.of(new Person()), Optional.of(new Car()));
        System.out.println(insurance2);//result: Optional[Insurance(name=null)]
    }

    public Optional<Insurance> find(Optional<Person> person, Optional<Car> car) {
        return person.flatMap(p -> car.map(c -> find(p, c)));
    }

    private Insurance find(Person p, Car c) {
        return new Insurance();
    }

    /**
     * flatMap和map用法
     */
    @Test
    public void test2() {
        Optional<Person> person = Optional.ofNullable(new Person());
        String name = person.flatMap(Person::getCar).flatMap(Car::getInsurance).map(Insurance::getName).orElse("哪吒之魔童降世");
        System.out.println(name); //result: 哪吒之魔童降世
    }

    /**
     * 创建optional对象
     */
    @Test
    public void test1() {
        //声明一个空的optional
        Optional<Car> c1 = Optional.empty();
        //根据一个非空的值创建optional, 如果传入的值是null，会抛出空指针异常
        Optional<Car> c2 = Optional.of(new Car());
        //可接受null的Optional
        Optional<Object> c3 = Optional.ofNullable(null);
    }
}
