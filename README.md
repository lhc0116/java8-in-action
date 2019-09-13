### chapter 1
  - 函数式编程的两个核心思想：将方法和Lambda作为一等值,以及在没有可变共享状态时,函数或方法可以有效、安全的并行执行。
### chapter 2
  - 行为参数化，就是一个方法接受多个不同的行为作为参数，并在内部使用他们，完成不同行为的能力。
  - 在java8之前，要将一段代码作为参数传递给方法，可以使用匿名类的方式来减少代码的冗余。
### chapter 3
  - Lambda表达式的基本语法是:   (参数列表) -> 主体
    - (parameters) -> expression
    - (parameters) -> {statements;}
  - 函数式接口就是一个`有且仅有一个抽象方法`，但是可以有多个非抽象方法(静态方法和default关键字修饰的默认方法)的接口。
    > 如果接口中声明的是java.lang.Object类中的 public 方法，那么这些方法就不算做是函数式接口的抽象方法。因为任何一个实现该接口的类都会有Object类中公共方法的默认实现。
  - Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例(具体的说，是函数式接口的一个具体实现的实例)。
  - Lambda表达式可以被赋给一个变量，也可以作为参数传递给一个接受函数式接口作为入参的方法。
  - @FunctionalInterface 注解用于标注接口会被设计成一个函数式接口，虽然他不是必须的，但是推荐使用，这样会在编译期检查使用 @FunctionalInterface 接口是否是一个函数式接口。
  - 关于Java8新引入的几个常用的泛型函数式接口 Predicate、Consumer、Function
  - 如果一个Lambda的主体是一个表达式，它就和一个返回 void 的函数描述符(即函数式接口的抽象方法签名, 例如 `(T, U) -> R`)兼容。下面这个语句是合法的，虽然Lambda主体返回的是List<String>，而不是Consumer上下文要求的 void。
  ```java
     Consumer<String> c = s -> Arrays.asList(s);
  ```
  - Lambda表达式可以没有限制的在其主体中引用实例变量和静态变量，但如果是局部变量，则必须显式的声明为final或只能被赋值一次，才能在Lambda主体中被引用。
  - 方法引用主要有三类
  - 指向静态方法的方法引用，例如 `s -> String.valueOf(s)` 可简写成 `String::valueOf`
  - 指向任意类型的实例方法的方法引用，例如 `(String s) -> s.length()` 可简写成 `String::length` (简单的说，就是你在引用一个对象的方法，而这个对象本身是Lambda的一个入参)
  - 指向Lambda表达式外部的已经存在的对象的实例方法的方法引用，下面的示例很好的展示了如何将 Lambda 重构成对应的方法引用
  ```java
        @Test
        public void test10() {
            Consumer<String> c1 = i -> this.run(i);
            //上面的Lambda表达式可以简写成下面的方法引用，符合方法引用的第三类方式, this引用即所谓的外部对象
            Consumer<String> c2 = this::run;
        }
        
        public void run(String s) {}
  ```
  