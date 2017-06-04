# Java 8

[![codebeat badge](https://codebeat.co/badges/d2a74038-4494-414a-a76a-aa226bb3f416)](https://codebeat.co/projects/github-com-narenkmanoharan-java-cheatsheet-master)

* [Default Methods for Interfaces](#default-methods-for-interfaces)
* [Lambda expressions](#lambda-expressions)
* [Functional Interfaces](#functional-interfaces)
* [Method and Constructor References](#method-and-constructor-references)
* [Lambda Scopes](#lambda-scopes)
  * [Accessing local variables](#accessing-local-variables)
  * [Accessing fields and static variables](#accessing-fields-and-static-variables)
  * [Accessing Default Interface Methods](#accessing-default-interface-methods)
* [Built-in Functional Interfaces](#built-in-functional-interfaces)
  * [Predicates](#predicates)
  * [Functions](#functions)
  * [Suppliers](#suppliers)
  * [Consumers](#consumers)
  * [Comparators](#comparators)
* [Optionals](#optionals)
* [Streams](#streams)
  * [Filter](#filter)
  * [Sorted](#sorted)
  * [Map](#map)
  * [Match](#match)
  * [Count](#count)
  * [Reduce](#reduce)
* [Parallel Streams](#parallel-streams)
  * [Sequential Sort](#sequential-sort)
  * [Parallel Sort](#parallel-sort)
* [Maps](#maps)
* [Date API](#date-api)
  * [Clock](#clock)
  * [Timezones](#timezones)
  * [LocalTime](#localtime)
  * [LocalDate](#localdate)
  * [LocalDateTime](#localdatetime)
* [Annotations](#annotations)
* [Where to go from here?](#where-to-go-from-here)

## Default Methods for Interfaces

Java 8 enables us to add non-abstract method implementations to interfaces by utilizing the `default` keyword. This feature is also known as [virtual extension methods](http://stackoverflow.com/a/24102730). 

Here is our first example:

```java
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
```

Besides the abstract method `calculate` the interface `Formula` also defines the default method `sqrt`. Concrete classes only have to implement the abstract method `calculate`. The default method `sqrt` can be used out of the box.

```java
Formula formula = new Formula() {
    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
};

formula.calculate(100);     // 100.0
formula.sqrt(16);           // 4.0
```

The formula is implemented as an anonymous object. The code is quite verbose: 6 lines of code for such a simple calculation of `sqrt(a * 100)`. As we'll see in the next section, there's a much nicer way of implementing single method objects in Java 8.


## Lambda expressions

Let's start with a simple example of how to sort a list of strings in prior versions of Java:

```java
List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return b.compareTo(a);
    }
});
```

The static utility method `Collections.sort` accepts a list and a comparator in order to sort the elements of the given list. You often find yourself creating anonymous comparators and pass them to the sort method.

Instead of creating anonymous objects all day long, Java 8 comes with a much shorter syntax, **lambda expressions**:

```java
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

As you can see the code is much shorter and easier to read. But it gets even shorter:

```java
Collections.sort(names, (String a, String b) -> b.compareTo(a));
```

For one line method bodies you can skip both the braces `{}` and the `return` keyword. But it gets even shorter:

```java
names.sort((a, b) -> b.compareTo(a));
```

List now has a `sort` method. Also the java compiler is aware of the parameter types so you can skip them as well. Let's dive deeper into how lambda expressions can be used in the wild.


## Functional Interfaces

How does lambda expressions fit into Java's type system? Each lambda corresponds to a given type, specified by an interface. A so called _functional interface_ must contain **exactly one abstract method** declaration. Each lambda expression of that type will be matched to this abstract method. Since default methods are not abstract you're free to add default methods to your functional interface.

We can use arbitrary interfaces as lambda expressions as long as the interface only contains one abstract method. To ensure that your interface meet the requirements, you should add the `@FunctionalInterface` annotation. The compiler is aware of this annotation and throws a compiler error as soon as you try to add a second abstract method declaration to the interface.

Example:

```java
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}
```

```java
Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);    // 123
```

Keep in mind that the code is also valid if the `@FunctionalInterface` annotation would be omitted.


## Method and Constructor References

The above example code can be further simplified by utilizing static method references:

```java
Converter<String, Integer> converter = Integer::valueOf;
Integer converted = converter.convert("123");
System.out.println(converted);   // 123
```

Java 8 enables you to pass references of methods or constructors via the `::` keyword. The above example shows how to reference a static method. But we can also reference object methods:

```java
class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
```

```java
Something something = new Something();
Converter<String, String> converter = something::startsWith;
String converted = converter.convert("Java");
System.out.println(converted);    // "J"
```

Let's see how the `::` keyword works for constructors. First we define an example class with different constructors:

```java
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

Next we specify a person factory interface to be used for creating new persons:

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

Instead of implementing the factory manually, we glue everything together via constructor references:

```java
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
```

We create a reference to the Person constructor via `Person::new`. The Java compiler automatically chooses the right constructor by matching the signature of `PersonFactory.create`.

## Lambda Scopes

Accessing outer scope variables from lambda expressions is very similar to anonymous objects. You can access final variables from the local outer scope as well as instance fields and static variables.

### Accessing local variables

We can read final local variables from the outer scope of lambda expressions:

```java
final int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

But different to anonymous objects the variable `num` does not have to be declared final. This code is also valid:

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

However `num` must be implicitly final for the code to compile. The following code does **not** compile:

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

Writing to `num` from within the lambda expression is also prohibited.

### Accessing fields and static variables

In contrast to local variables, we have both read and write access to instance fields and static variables from within lambda expressions. This behaviour is well known from anonymous objects.

```java
class Lambda4 {
    static int outerStaticNum;
    int outerNum;

    void testScopes() {
        Converter<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            return String.valueOf(from);
        };

        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }
}
```

### Accessing Default Interface Methods

Remember the formula example from the first section? Interface `Formula` defines a default method `sqrt` which can be accessed from each formula instance including anonymous objects. This does not work with lambda expressions.

Default methods **cannot** be accessed from within lambda expressions. The following code does not compile:

```java
Formula formula = (a) -> sqrt(a * 100);
```


## Built-in Functional Interfaces

The JDK 1.8 API contains many built-in functional interfaces. Some of them are well known from older versions of Java like `Comparator` or `Runnable`. Those existing interfaces are extended to enable Lambda support via the `@FunctionalInterface` annotation.

But the Java 8 API is also full of new functional interfaces to make your life easier. Some of those new interfaces are well known from the [Google Guava](https://code.google.com/p/guava-libraries/) library. Even if you're familiar with this library you should keep a close eye on how those interfaces are extended by some useful method extensions.

### Predicates

Predicates are boolean-valued functions of one argument. The interface contains various default methods for composing predicates to complex logical terms (and, or, negate)

```java
Predicate<String> predicate = (s) -> s.length() > 0;

predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();
```

### Functions

Functions accept one argument and produce a result. Default methods can be used to chain multiple functions together (compose, andThen).

```java
Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);

backToString.apply("123");     // "123"
```

### Suppliers

Suppliers produce a result of a given generic type. Unlike Functions, Suppliers don't accept arguments.

```java
Supplier<Person> personSupplier = Person::new;
personSupplier.get();   // new Person
```

### Consumers

Consumers represent operations to be performed on a single input argument.

```java
Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
greeter.accept(new Person("Luke", "Skywalker"));
```

### Comparators

Comparators are well known from older versions of Java. Java 8 adds various default methods to the interface.

```java
Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");

comparator.compare(p1, p2);             // > 0
comparator.reversed().compare(p1, p2);  // < 0
```

## Optionals

Optionals are not functional interfaces, but nifty utilities to prevent `NullPointerException`. It's an important concept for the next section, so let's have a quick look at how Optionals work.

Optional is a simple container for a value which may be null or non-null. Think of a method which may return a non-null result but sometimes return nothing. Instead of returning `null` you return an `Optional` in Java 8.

```java
Optional<String> optional = Optional.of("bam");

optional.isPresent();           // true
optional.get();                 // "bam"
optional.orElse("fallback");    // "bam"

optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

## Streams

A `java.util.Stream` represents a sequence of elements on which one or more operations can be performed. Stream operations are either _intermediate_ or _terminal_. While terminal operations return a result of a certain type, intermediate operations return the stream itself so you can chain multiple method calls in a row. Streams are created on a source, e.g. a `java.util.Collection` like lists or sets (maps are not supported). Stream operations can either be executed sequentially or parallely.

> Streams are extremely powerful, so I wrote a separate [Java 8 Streams Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/). You should also check out [Stream.js](https://github.com/winterbe/streamjs), a JavaScript port of the Java 8 Streams API.

Let's first look how sequential streams work. First we create a sample source in form of a list of strings:

```java
List<String> stringCollection = new ArrayList<>();
stringCollection.add("ddd2");
stringCollection.add("aaa2");
stringCollection.add("bbb1");
stringCollection.add("aaa1");
stringCollection.add("bbb3");
stringCollection.add("ccc");
stringCollection.add("bbb2");
stringCollection.add("ddd1");
```

Collections in Java 8 are extended so you can simply create streams either by calling `Collection.stream()` or `Collection.parallelStream()`. The following sections explain the most common stream operations.

### Filter

Filter accepts a predicate to filter all elements of the stream. This operation is _intermediate_ which enables us to call another stream operation (`forEach`) on the result. ForEach accepts a consumer to be executed for each element in the filtered stream. ForEach is a terminal operation. It's `void`, so we cannot call another stream operation.

```java
stringCollection
    .stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa2", "aaa1"
```

### Sorted

Sorted is an _intermediate_ operation which returns a sorted view of the stream. The elements are sorted in natural order unless you pass a custom `Comparator`.

```java
stringCollection
    .stream()
    .sorted()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa1", "aaa2"
```

Keep in mind that `sorted` does only create a sorted view of the stream without manipulating the ordering of the backed collection. The ordering of `stringCollection` is untouched:

```java
System.out.println(stringCollection);
// ddd2, aaa2, bbb1, aaa1, bbb3, ccc, bbb2, ddd1
```

### Map

The _intermediate_ operation `map` converts each element into another object via the given function. The following example converts each string into an upper-cased string. But you can also use `map` to transform each object into another type. The generic type of the resulting stream depends on the generic type of the function you pass to `map`.

```java
stringCollection
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))
    .forEach(System.out::println);

// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
```

### Match

Various matching operations can be used to check whether a certain predicate matches the stream. All of those operations are _terminal_ and return a boolean result.

```java
boolean anyStartsWithA =
    stringCollection
        .stream()
        .anyMatch((s) -> s.startsWith("a"));

System.out.println(anyStartsWithA);      // true

boolean allStartsWithA =
    stringCollection
        .stream()
        .allMatch((s) -> s.startsWith("a"));

System.out.println(allStartsWithA);      // false

boolean noneStartsWithZ =
    stringCollection
        .stream()
        .noneMatch((s) -> s.startsWith("z"));

System.out.println(noneStartsWithZ);      // true
```

#### Count

Count is a _terminal_ operation returning the number of elements in the stream as a `long`.

```java
long startsWithB =
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("b"))
        .count();

System.out.println(startsWithB);    // 3
```


### Reduce

This _terminal_ operation performs a reduction on the elements of the stream with the given function. The result is an `Optional` holding the reduced value.

```java
Optional<String> reduced =
    stringCollection
        .stream()
        .sorted()
        .reduce((s1, s2) -> s1 + "#" + s2);

reduced.ifPresent(System.out::println);
// "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
```

## Parallel Streams

As mentioned above streams can be either sequential or parallel. Operations on sequential streams are performed on a single thread while operations on parallel streams are performed concurrently on multiple threads.

The following example demonstrates how easy it is to increase the performance by using parallel streams.

First we create a large list of unique elements:

```java
int max = 1000000;
List<String> values = new ArrayList<>(max);
for (int i = 0; i < max; i++) {
    UUID uuid = UUID.randomUUID();
    values.add(uuid.toString());
}
```

Now we measure the time it takes to sort a stream of this collection.

### Sequential Sort

```java
long t0 = System.nanoTime();

long count = values.stream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));

// sequential sort took: 899 ms
```

### Parallel Sort

```java
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));

// parallel sort took: 472 ms
```

As you can see both code snippets are almost identical but the parallel sort is roughly 50% faster. All you have to do is change `stream()` to `parallelStream()`.

## Maps

As already mentioned maps do not directly support streams. There's no `stream()` method available on the `Map` interface itself, however you can create specialized streams upon the keys, values or entries of a map via `map.keySet().stream()`, `map.values().stream()` and `map.entrySet().stream()`. 

Furthermore maps support various new and useful methods for doing common tasks.

```java
Map<Integer, String> map = new HashMap<>();

for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((id, val) -> System.out.println(val));
```

The above code should be self-explaining: `putIfAbsent` prevents us from writing additional if null checks; `forEach` accepts a consumer to perform operations for each value of the map.

This example shows how to compute code on the map by utilizing functions:

```java
map.computeIfPresent(3, (num, val) -> val + num);
map.get(3);             // val33

map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);     // false

map.computeIfAbsent(23, num -> "val" + num);
map.containsKey(23);    // true

map.computeIfAbsent(3, num -> "bam");
map.get(3);             // val33
```

Next, we learn how to remove entries for a given key, only if it's currently mapped to a given value:

```java
map.remove(3, "val3");
map.get(3);             // val33

map.remove(3, "val33");
map.get(3);             // null
```

Another helpful method:

```java
map.getOrDefault(42, "not found");  // not found
```

Merging entries of a map is quite easy:

```java
map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9

map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9concat
```

Merge either put the key/value into the map if no entry for the key exists, or the merging function will be called to change the existing value.


## Date API

Java 8 contains a brand new date and time API under the package `java.time`. The new Date API is comparable with the [Joda-Time](http://www.joda.org/joda-time/) library, however it's [not the same](http://blog.joda.org/2009/11/why-jsr-310-isn-joda-time_4941.html). The following examples cover the most important parts of this new API.

### Clock

Clock provides access to the current date and time. Clocks are aware of a timezone and may be used instead of `System.currentTimeMillis()` to retrieve the current time in milliseconds since Unix EPOCH. Such an instantaneous point on the time-line is also represented by the class `Instant`. Instants can be used to create legacy `java.util.Date` objects.

```java
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();

Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```

### Timezones

Timezones are represented by a `ZoneId`. They can easily be accessed via static factory methods. Timezones define the offsets which are important to convert between instants and local dates and times.

```java
System.out.println(ZoneId.getAvailableZoneIds());
// prints all available timezone ids

ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
System.out.println(zone1.getRules());
System.out.println(zone2.getRules());

// ZoneRules[currentStandardOffset=+01:00]
// ZoneRules[currentStandardOffset=-03:00]
```

### LocalTime

LocalTime represents a time without a timezone, e.g. 10pm or 17:30:15. The following example creates two local times for the timezones defined above. Then we compare both times and calculate the difference in hours and minutes between both times.

```java
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);

System.out.println(now1.isBefore(now2));  // false

long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);       // -3
System.out.println(minutesBetween);     // -239
```

LocalTime comes with various factory methods to simplify the creation of new instances, including parsing of time strings.

```java
LocalTime late = LocalTime.of(23, 59, 59);
System.out.println(late);       // 23:59:59

DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.GERMAN);

LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
System.out.println(leetTime);   // 13:37
```

### LocalDate

LocalDate represents a distinct date, e.g. 2014-03-11. It's immutable and works exactly analog to LocalTime. The sample demonstrates how to calculate new dates by adding or subtracting days, months or years. Keep in mind that each manipulation returns a new instance.

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
LocalDate yesterday = tomorrow.minusDays(2);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);    // FRIDAY
```

Parsing a LocalDate from a string is just as simple as parsing a LocalTime:

```java
DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
System.out.println(xmas);   // 2014-12-24
```

### LocalDateTime

LocalDateTime represents a date-time. It combines date and time as seen in the above sections into one instance. `LocalDateTime` is immutable and works similar to LocalTime and LocalDate. We can utilize methods for retrieving certain fields from a date-time:

```java
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);      // WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);          // DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);    // 1439
```

With the additional information of a timezone it can be converted to an instant. Instants can easily be converted to legacy dates of type `java.util.Date`.

```java
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();

Date legacyDate = Date.from(instant);
System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
```

Formatting date-times works just like formatting dates or times. Instead of using pre-defined formats we can create formatters from custom patterns.

```java
DateTimeFormatter formatter =
    DateTimeFormatter
        .ofPattern("MMM dd, yyyy - HH:mm");

LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
String string = formatter.format(parsed);
System.out.println(string);     // Nov 03, 2014 - 07:13
```

Unlike `java.text.NumberFormat` the new `DateTimeFormatter` is immutable and **thread-safe**.

For details on the pattern syntax read [here](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).


## Annotations

Annotations in Java 8 are repeatable. Let's dive directly into an example to figure that out.

First, we define a wrapper annotation which holds an array of the actual annotations:

```java
@interface Hints {
    Hint[] value();
}

@Repeatable(Hints.class)
@interface Hint {
    String value();
}
```
Java 8 enables us to use multiple annotations of the same type by declaring the annotation `@Repeatable`.

### Variant 1: Using the container annotation (old school)

```java
@Hints({@Hint("hint1"), @Hint("hint2")})
class Person {}
```

### Variant 2: Using repeatable annotations (new school)

```java
@Hint("hint1")
@Hint("hint2")
class Person {}
```

Using variant 2 the java compiler implicitly sets up the `@Hints` annotation under the hood. That's important for reading annotation information via reflection.

```java
Hint hint = Person.class.getAnnotation(Hint.class);
System.out.println(hint);                   // null

Hints hints1 = Person.class.getAnnotation(Hints.class);
System.out.println(hints1.value().length);  // 2

Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
System.out.println(hints2.length);          // 2
```

Although we never declared the `@Hints` annotation on the `Person` class, it's still readable via `getAnnotation(Hints.class)`. However, the more convenient method is `getAnnotationsByType` which grants direct access to all annotated `@Hint` annotations.


Furthermore the usage of annotations in Java 8 is expanded to two new targets:

```java
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface MyAnnotation {}
```

## Where to go from here?

 If you want to learn more about all the new classes and features of the JDK 8 API, check out my [JDK8 API Explorer](http://winterbe.com/projects/java8-explorer/). It helps you figuring out all the new classes and hidden gems of JDK 8, like `Arrays.parallelSort`, `StampedLock` and `CompletableFuture` - just to name a few.

- [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
- [Java 8 Concurrency Tutorial: Threads and Executors](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)
- [Java 8 Concurrency Tutorial: Synchronization and Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)
- [Java 8 Concurrency Tutorial: Atomic Variables and ConcurrentMap](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)
- [Java 8 API by Example: Strings, Numbers, Math and Files](http://winterbe.com/posts/2015/03/25/java8-examples-string-number-math-files/)
- [Avoid Null Checks in Java 8](http://winterbe.com/posts/2015/03/15/avoid-null-checks-in-java/)
- [Fixing Java 8 Stream Gotchas with IntelliJ IDEA](http://winterbe.com/posts/2015/03/05/fixing-java-8-stream-gotchas-with-intellij-idea/)


# RxJava 2.x

## Contents 
 
   - [Flowable, Single and Observable](#flowable)
   - [Simple Operators](#simple-operators)
   - [Merging Streams](#merging-streams)
   - [Hot Publishers](#hot-publishers)
   - [Schedulers](#schedulers)
   - [FlatMap Operator](#flatmap-operator)
   - [Error Handling](#error-handling)
   - [Backpressure](#backpressure)
   - [Articles and books](#articles)

## Reactive Streams
Reactive Streams is a programming concept for handling asynchronous 
data streams in a non-blocking manner while providing backpressure to stream publishers.
It has evolved into a [specification](https://github.com/reactive-streams/reactive-streams-jvm) that is based on the concept of **Publisher&lt;T&gt;** and **Subscriber&lt;T&gt;**.
A **Publisher** is the source of events **T** in the stream, and a **Subscriber** is the consumer for those events.
A **Subscriber** subscribes to a **Publisher** by invoking a "factory method" in the Publisher that will push
the stream items **&lt;T&gt;** starting a new **Subscription**:

```java
public interface Publisher<T> {
    public void subscribe(Subscriber<? super T> s);
}
```

When the Subscriber is ready to start handling events, it signals this via a **request** to that **Subscription**
 
```java
public interface Subscription {
    public void request(long n); //request n items
    public void cancel();
}
```

Upon receiving this signal, the Publisher begins to invoke **Subscriber::onNext(T)** for each event **T**. 
This continues until either completion of the stream (**Subscriber::onComplete()**) 
or an error occurs during processing (**Subscriber::onError(Throwable)**).

```java
public interface Subscriber<T> {
    //signals to the Publisher to start sending events
    public void onSubscribe(Subscription s);     
    
    public void onNext(T t);
    public void onError(Throwable t);
    public void onComplete();
}
```

## Flowable and Observable
RxJava provides more types of event publishers: 
   - **Flowable** Publisher that emits 0..N elements, and then completes successfully or with an error
   - **Observable** like Flowables but without a backpressure strategy. They were introduced in RxJava 1.x
   
   - **Single** a specialized emitter that completes with a value successfully either an error.(doesn't have onComplete callback, instead onSuccess(val))
   - **Maybe** a specialized emitter that can complete with / without a value or complete with an error.
   - **Completable** a specialized emitter that just signals if it completed successfully or with an error.

Code is available at [Part01CreateFlowable.java](https://github.com/balamaci/rxjava-playground/blob/master/src/test/java/com/balamaci/rx/Part01CreateFlowable.java)

### Simple operators to create Streams
```java
Flowable<Integer> flowable = Flowable.just(1, 5, 10);
Flowable<Integer> flowable = Flowable.range(1, 10);
Flowable<String> flowable = Flowable.fromArray(new String[] {"red", "green", "blue"});
Flowable<String> flowable = Flowable.fromIterable(List.of("red", "green", "blue"));
```


### Flowable from Future

```java
CompletableFuture<String> completableFuture = CompletableFuture
            .supplyAsync(() -> { //starts a background thread the ForkJoin common pool
                    log.info("CompletableFuture work starts");  
                    Helpers.sleepMillis(100);
                    return "red";
            });

Single<String> observable = Single.from(completableFuture);
single.subscribe(val -> log.info("Stream completed successfully : {}", val));
```


### Creating your own stream

We can use **Flowable.create(...)** to implement the emissions of events by calling **onNext(val)**, **onComplete()**, **onError(throwable)**

When subscribing to the Observable / Flowable with flowable.subscribe(...) the lambda code inside **create(...)** gets executed.
Flowable.subscribe(...) can take 3 handlers for each type of event - onNext, onError and onCompleted.

When using **Observable.create(...)** you need to be aware of [backpressure](#backpressure) and that Observables created with 'create' are not BackPressure aware

```java 
Observable<Integer> stream = Observable.create(subscriber -> {
    log.info("Started emitting");

    log.info("Emitting 1st");
    subscriber.onNext(1);

    log.info("Emitting 2nd");
    subscriber.onNext(2);

    subscriber.onComplete();
});

//Flowable version same Observable but with a BackpressureStrategy
//that will be discussed separately.
Flowable<Integer> stream = Flowable.create(subscriber -> {
    log.info("Started emitting");

    log.info("Emitting 1st");
    subscriber.onNext(1);

    log.info("Emitting 2nd");
    subscriber.onNext(2);

    subscriber.onComplete();
}, BackpressureStrategy.MISSING);

stream.subscribe(
       val -> log.info("Subscriber received: {}", val),
       err -> log.error("Subscriber received error", err),
       () -> log.info("Subscriber got Completed event")
);
```

### Streams are lazy 
Streams are lazy meaning that the code inside create() doesn't get executed without subscribing to the stream.
So event if we sleep for a long time inside create() method(to simulate a costly operation),
without subscribing to this Observable, the code is not executed and the method returns immediately.

```java
public void observablesAreLazy() {
    Observable<Integer> observable = Observable.create(subscriber -> {
        log.info("Started emitting but sleeping for 5 secs"); //this is not executed
        Helpers.sleepMillis(5000);
        subscriber.onNext(1);
    });
    log.info("Finished"); 
}
===========
[main] - Finished
```

### Multiple subscriptions to the same Observable / Flowable 
When subscribing to an Observable/Flowable, the create() method gets executed for each Subscriber, the events 
inside **create(..)** are re-emitted to each subscriber independently. 

So every subscriber will get the same events and will not lose any events - this behavior is named **'cold observable'**
See [Hot Publishers](#hot-publisher) to understand sharing a subscription and multicasting events.
 
```java
Observable<Integer> observable = Observable.create(subscriber -> {
   log.info("Started emitting");

   log.info("Emitting 1st event");
   subscriber.onNext(1);

   log.info("Emitting 2nd event");
   subscriber.onNext(2);

   subscriber.onComplete();
});

log.info("Subscribing 1st subscriber");
observable.subscribe(val -> log.info("First Subscriber received: {}", val));

log.info("=======================");

log.info("Subscribing 2nd subscriber");
observable.subscribe(val -> log.info("Second Subscriber received: {}", val));
```

will output

```
[main] - Subscribing 1st subscriber
[main] - Started emitting
[main] - Emitting 1st event
[main] - First Subscriber received: 1
[main] - Emitting 2nd event
[main] - First Subscriber received: 2
[main] - =======================
[main] - Subscribing 2nd subscriber
[main] - Started emitting
[main] - Emitting 1st event
[main] - Second Subscriber received: 1
[main] - Emitting 2nd event
[main] - Second Subscriber received: 2
```

## Observable / Flowable lifecycle

### Operators
Between the source Observable / Flowable and the Subscriber there can be a wide range of operators and RxJava provides 
lots of operators to chose from. Probably you are already familiar with functional operations like **filter** and **map**. 
so let's use them as example:

```java
Flowable<Integer> stream = Flowable.create(subscriber -> {
        subscriber.onNext(1);
        subscriber.onNext(2);
        ....
        subscriber.onComplete();
    }, BackpressureStrategy.MISSING);
    .filter(val -> val < 10)
    .map(val -> val * 10)
    .subscribe(val -> log.info("Received: {}", val));
```

When we call _Flowable.create()_ you might think that we're calling onNext(..), onComplete(..) on the Subscriber at the end of the chain, 
not the operators between them.

This is not true because **the operators themselves are decorators for their source** wrapping it with the operator behavior 
like an onion's layers. 
When we call **.subscribe()** at the end of the chain, **Subscription propagates through the layers back to the source,
each operator subscribing itself to it's wrapped source Observable / Flowable and so on to the original source, 
triggering it to start producing/emitting items**.

**Flowable.create** calls **---&gt; filterOperator.onNext(val)** which if val &gt; 10 calls **---&gt; 
mapOperator.onNext(val)** does val = val * 10 and calls **---&gt; subscriber.onNext(val)**. 

[Found](https://tomstechnicalblog.blogspot.ro/2015_10_01_archive.html) a nice analogy with a team of house movers, with every mover doing it's thing before passing it to the next in line 
until it reaches the final subscriber.

![Movers](https://1.bp.blogspot.com/-1RuGVz4-U9Q/VjT0AsfiiUI/AAAAAAAAAKQ/xWQaOwNtS7o/s1600/animation_2.gif) 
 
### Canceling subscription
Inside the create() method, we can check is there are still active subscribers to our Flowable/Observable.

There are operators that also unsubscribe from the stream so the source knows to stop producing events.  
It's a way to prevent to do extra work(like for ex. querying a datasource for entries) if no one is listening
In the following example we'd expect to have an infinite stream, but because we stop if there are no active subscribers, we stop producing events.   

**take(limit)** is a simple operator. It's role is to count the number of events and then unsubscribes from it's source 
once it received the specified amount and calls onComplete() to it's subscriber.

```java
Observable<Integer> observable = Observable.create(subscriber -> {

    int i = 1;
    while(true) {
        if(subscriber.isDisposed()) {
             break;
        }

        subscriber.onNext(i++);
        
        //registering a callback when the downstream subscriber unsubscribes
        subscriber.setCancellable(() -> log.info("Subscription canceled"));
    }
});

observable
    .take(5) //unsubscribes after the 5th event
    .subscribe(val -> log.info("Subscriber received: {}", val),
               err -> log.error("Subscriber received error", err),
               () -> log.info("Subscriber got Completed event") //The Complete event 
               //is triggered by 'take()' operator

==================
[main] - Subscriber received: *1*
[main] - Subscriber received: *2*
[main] - Subscriber received: *3*
[main] - Subscriber received: *4*
[main] - Subscriber received: *5*
[main] - Subscriber got Completed event
[main] - Subscription canceled
```


## Simple Operators
Code is available at [Part02SimpleOperators.java](https://github.com/balamaci/rxjava-playground/blob/master/src/test/java/com/balamaci/rx/Part02SimpleOperators.java)

### delay
Delay operator - the Thread.sleep of the reactive world, it's pausing each emission for a particular increment of time.

```java
CountDownLatch latch = new CountDownLatch(1);
Flowable.range(0, 2)
        .doOnNext(val -> log.info("Emitted {}", val))
        .delay(5, TimeUnit.SECONDS)
        .subscribe(tick -> log.info("Tick {}", tick),
                   (ex) -> log.info("Error emitted"),
                   () -> {
                          log.info("Completed");
                          latch.countDown();
                   });
latch.await();

==============
14:27:44 [main] - Starting
14:27:45 [main] - Emitted 0
14:27:45 [main] - Emitted 1
14:27:50 [RxComputationThreadPool-1] - Tick 0
14:27:50 [RxComputationThreadPool-1] - Tick 1
14:27:50 [RxComputationThreadPool-1] - Completed
```

The **.delay()**, **.interval()** operators uses a [Scheduler](#schedulers) by default which is why we see it executing
on a different thread _RxComputationThreadPool-1_ which actually means it's running the operators and the subscribe operations 
on another thread and so the test method will terminate before we see the text from the log unless we wait for the completion of the stream.


### interval
Periodically emits a number starting from 0 and then increasing the value on each emission.

```java
log.info("Starting");
Flowable.interval(5, TimeUnit.SECONDS)
       .take(4)
       .subscribe(tick -> log.info("Subscriber received {}", tick),
                  (ex) -> log.info("Error emitted"),
                  () -> log.info("Subscriber got Completed event"));

==========
12:17:56 [main] - Starting
12:18:01 [RxComputationThreadPool-1] - Subscriber received: 0
12:18:06 [RxComputationThreadPool-1] - Subscriber received: 1
12:18:11 [RxComputationThreadPool-1] - Subscriber received: 2
12:18:16 [RxComputationThreadPool-1] - Subscriber received: 3
12:18:21 [RxComputationThreadPool-1] - Subscriber received: 4
12:18:21 [RxComputationThreadPool-1] - Subscriber got Completed event
```



### scan
Takes an **initial value** and a **function(accumulator, currentValue)**. It goes through the events
sequence and combines the current event value with the previous result(accumulator) emitting downstream the function's
result for each event(the initial value is used for the first event)

```java
Flowable<Integer> numbers = 
                Flowable.just(3, 5, -2, 9)
                    .scan(0, (totalSoFar, currentValue) -> {
                               log.info("TotalSoFar={}, currentValue={}", 
                                            totalSoFar, currentValue);
                               return totalSoFar + currentValue;
                    });

=============
16:09:17 [main] - Subscriber received: 0
16:09:17 [main] - TotalSoFar=0, currentValue=3
16:09:17 [main] - Subscriber received: 3
16:09:17 [main] - TotalSoFar=3, currentValue=5
16:09:17 [main] - Subscriber received: 8
16:09:17 [main] - TotalSoFar=8, currentValue=-2
16:09:17 [main] - Subscriber received: 6
16:09:17 [main] - TotalSoFar=6, currentValue=9
16:09:17 [main] - Subscriber received: 15
16:09:17 [main] - Subscriber got Completed event
```

### reduce
reduce operator acts like the scan operator but it only passes downstream the final result 
(doesn't pass the intermediate results downstream) so the subscriber receives just one event

```java
Flowable<Integer> numbers = Flowable.just(3, 5, -2, 9)
                            .reduce(0, (totalSoFar, val) -> {
                                         log.info("totalSoFar={}, emitted={}",
                                                        totalSoFar, val);
                                         return totalSoFar + val;
                            });
                            
=============                            
17:08:29 [main] - totalSoFar=0, emitted=3
17:08:29 [main] - totalSoFar=3, emitted=5
17:08:29 [main] - totalSoFar=8, emitted=-2
17:08:29 [main] - totalSoFar=6, emitted=9
17:08:29 [main] - Subscriber received: 15
17:08:29 [main] - Subscriber got Completed event
```

### collect
collect operator acts similar to the _reduce_ operator, but while the _reduce_ operator uses a reduce function
which returns a value, the _collect_ operator takes a container supplier and a function which doesn't return
anything(a consumer). The mutable container is passed for every event and thus you get a chance to modify it
in this collect consumer function.

```java
Flowable<List<Integer>> numbers = Flowable.just(3, 5, -2, 9)
                                        .collect(ArrayList::new, (container, value) -> {
                                            log.info("Adding {} to container", value);
                                            container.add(value);
                                            //notice we don't need to return anything
                                        });
=========
17:40:18 [main] - Adding 3 to container
17:40:18 [main] - Adding 5 to container
17:40:18 [main] - Adding -2 to container
17:40:18 [main] - Adding 9 to container
17:40:18 [main] - Subscriber received: [3, 5, -2, 9]
17:40:18 [main] - Subscriber got Completed event
```

because the usecase to store to a List container is so common, there is a **.toList()** operator that is just a collector adding to a List. 

### defer
An easy way to switch from a blocking method to a reactive Single/Flowable is to use **.defer(() -> blockingOp())**.

Simply using **Flowable.just(blockingOp())** would still block, as Java needs to resolve the parameter when invoking
**Flux.just(param)** method, so _blockingOp()_ method would still be invoked(and block).

```java
//NOT OK
Flowable<String> flowableBlocked = Flowable.just((blockingOp())); //blocks on this line
```
    
In order to get around this problem, we can use **Flowable.defer(() -> blockingOp())** and wrap the _blockingOp()_ call inside a lambda which 
will be invoked lazy **at subscribe time**.

```java
Flowable<String> stream = Flowable.defer(() -> Flowable.just(blockingOperation())); 
stream.subscribe(val -> log.info("Val " + val)); //only now the code inside defer() is executed
```


## Merging Streams
Operators for working with multiple streams
Code at [Part03MergingStreams.java](https://github.com/balamaci/rxjava-playground/blob/master/src/test/java/com/balamaci/rx/Part03MergingStreams.java)

### zip
Zip operator operates sort of like a zipper in the sense that it takes an event from one stream and waits
for an event from another other stream. Once an event for the other stream arrives, it uses the zip function
to merge the two events.

This is an useful scenario when for example you want to make requests to remote services in parallel and
wait for both responses before continuing. It also takes a function which will produce the combined result
of the zipped streams once each has emitted a value.

![Zip](https://raw.githubusercontent.com/reactor/projectreactor.io/master/src/main/static/assets/img/marble/zip.png)

Zip operator besides the streams to zip, also takes as parameter a function which will produce the 
combined result of the zipped streams once each stream emitted it's value

```java
Single<Boolean> isUserBlockedStream = 
                    Single.fromFuture(CompletableFuture.supplyAsync(() -> {
                            Helpers.sleepMillis(200);
                            return Boolean.FALSE;
                    }));

Single<Integer> userCreditScoreStream = 
                    Single.fromFuture(CompletableFuture.supplyAsync(() -> {
                            Helpers.sleepMillis(2300);
                            return 5;
                    }));

Single<Pair<Boolean, Integer>> userCheckStream = Single.zip(isUserBlockedStream, userCreditScoreStream, 
                      (blocked, creditScore) -> new Pair<Boolean, Integer>(blocked, creditScore));

userCheckStream.subscribe(pair -> log.info("Received " + pair));
```

Even if the 'isUserBlockedStream' finishes after 200ms, 'userCreditScoreStream' is slow at 2.3secs, 
the 'zip' method applies the combining function(new Pair(x,y)) after it received both values and passes it 
to the subscriber.


Another good example of 'zip' is to slow down a stream by another basically **implementing a periodic emitter of events**:

```java  
Flowable<String> colors = Flowable.just("red", "green", "blue");
Flowable<Long> timer = Flowable.interval(2, TimeUnit.SECONDS);

Flowable<String> periodicEmitter = Flowable.zip(colors, timer, (key, val) -> key);
```

Since the zip operator needs a pair of events, the slow stream will work like a timer by periodically emitting 
with zip setting the pace of emissions downstream every 2 seconds.

**Zip is not limited to just two streams**, it can merge 2,3,4,.. streams and wait for groups of 2,3,4 'pairs' of 
events which it combines with the zip function and sends downstream.

### merge
Merge operator combines one or more stream and passes events downstream as soon as they appear.
![merge](https://raw.githubusercontent.com/reactor/projectreactor.io/master/src/main/static/assets/img/marble/merge.png)

```
Flowable<String> colors = periodicEmitter("red", "green", "blue", 2, TimeUnit.SECONDS);

Flowable<Long> numbers = Flowable.interval(1, TimeUnit.SECONDS)
                .take(5);
Flowable flowable = Flowable.merge(colors, numbers);                

============
21:32:15 - Subscriber received: 0
21:32:16 - Subscriber received: red
21:32:16 - Subscriber received: 1
21:32:17 - Subscriber received: 2
21:32:18 - Subscriber received: green
21:32:18 - Subscriber received: 3
21:32:19 - Subscriber received: 4
21:32:20 - Subscriber received: blue
```

### concat
Concat operator appends another streams at the end of another
![concat](https://raw.githubusercontent.com/reactor/projectreactor.io/master/src/main/static/assets/img/marble/concat.png)

```java
Flowable<String> colors = periodicEmitter("red", "green", "blue", 2, TimeUnit.SECONDS);

Flowable<Long> numbers = Flowable.interval(1, TimeUnit.SECONDS)
                .take(4);

Flowable events = Flowable.concat(colors, numbers);

==========
22:48:23 - Subscriber received: red
22:48:25 - Subscriber received: green
22:48:27 - Subscriber received: blue
22:48:28 - Subscriber received: 0
22:48:29 - Subscriber received: 1
22:48:30 - Subscriber received: 2
22:48:31 - Subscriber received: 3
```

Even if the 'numbers' streams should start early, the 'colors' stream emits fully its events
before we see any 'numbers'.
This is because 'numbers' stream is actually subscribed only after the 'colors' complete.
Should the second stream be a 'hot' emitter, its events would be lost until the first one finishes
and the seconds stream is subscribed.

## Hot Publishers
We've seen that with 'cold publishers', whenever a subscriber subscribes, each subscriber will get
it's version of emitted values independently, the exact set of data indifferently when they subscribe.
But cold publishers only produce data when the subscribers subscribes, however there are cases where 
the events happen independently from the consumers regardless if someone is 
listening or not and we don't have control to request more. So you could say we have 'cold publishers' for pull
scenarios and 'hot publishers' which push.

### Subjects
Subjects are one way to handle hot observables. Subjects keep reference to their subscribers and allow 'multicasting' 
an event to them.

```java
for (Disposable<T> s : subscribers.get()) {
    s.onNext(t);
}
```

Subjects besides being traditional Observables you can use the same operators and subscribe to them,
are also an **Observer**(interface like **Subscriber** from [reactive-streams](#reactive-streams), implementing the 3 methods **onNext, onError, onComplete**), 
meaning you can invoke subject.onNext(value) from different parts in the code,
which means that you publish events which the Subject will pass on to their subscribers.

```java
Subject<Integer> subject = ReplaySubject.create()
                     .map(...);
                     .subscribe(); //

...
subject.onNext(val);
...
subject.onNext(val2);
```
remember for 
```java
Observable.create(subscriber -> {
      subscriber.onNext(val);
})
```

### ReplaySubject
ReplaySubject keeps a buffer of events that it 'replays' to each new subscriber, first he receives a batch of missed 
and only later events in real-time.

```java
Subject<Integer> subject = ReplaySubject.createWithSize(50);

log.info("Pushing 0");
subject.onNext(0);
log.info("Pushing 1");
subject.onNext(1);

log.info("Subscribing 1st");
subject.subscribe(val -> log.info("Subscriber1 received {}", val), 
                            logError(), logComplete());

log.info("Pushing 2");
subject.onNext(2);

log.info("Subscribing 2nd");
subject.subscribe(val -> log.info("Subscriber2 received {}", val), 
                            logError(), logComplete());

log.info("Pushing 3");
subject.onNext(3);

subject.onComplete();

==================
[main] - Pushing 0
[main] - Pushing 1
[main] - Subscribing 1st
[main] - Subscriber1 received 0
[main] - Subscriber1 received 1
[main] - Pushing 2
[main] - Subscriber1 received 2
[main] - Subscribing 2nd
[main] - Subscriber2 received 0
[main] - Subscriber2 received 1
[main] - Subscriber2 received 2
[main] - Pushing 3
[main] - Subscriber1 received 3
[main] - Subscriber2 received 3
[main] - Subscriber got Completed event
[main] - Subscriber got Completed event
```

### ConnectableObservable / ConnectableFlowable and resource sharing
There are cases when we want to share a single subscription between subscribers, meaning while the code that executes
on subscribing should be executed once, the events should be published to all subscribers.     

For ex. when we want to share a connection between multiple Observables / Flowables. 
Using a plain Observable would just reexecute the code inside _.create()_ and opening / closing a new connection for each 
new subscriber when it subscribes / cancels its subscription.

**ConnectableObservable** are a special kind of **Observable**. No matter how many Subscribers subscribe to ConnectableObservable, 
it opens just one subscription to the Observable from which it was created.

Anyone who subscribes to **ConnectableObservable** is placed in a set of Subscribers(it doesn't trigger
the _.create()_ code a normal Observable would when .subscribe() is called). A **.connect()** method is available for ConnectableObservable.
**As long as connect() is not called, these Subscribers are put on hold, they never directly subscribe to upstream Observable**

```java
ConnectableObservable<Integer> connectableObservable = 
                                  Observable.<Integer>create(subscriber -> {
        log.info("Inside create()");

     /* A JMS connection listener example
         Just an example of a costly operation that is better to be shared **/

     /* Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(true, AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(orders);
        consumer.setMessageListener(subscriber::onNext); */

        subscriber.setCancellable(() -> log.info("Subscription cancelled"));

        log.info("Emitting 1");
        subscriber.onNext(1);

        log.info("Emitting 2");
        subscriber.onNext(2);

        subscriber.onComplete();
}).publish();

connectableObservable
       .take(1)
       .subscribe((val) -> log.info("Subscriber1 received: {}", val), 
                    logError(), logComplete());

connectableObservable
       .subscribe((val) -> log.info("Subscriber2 received: {}", val), 
                    logError(), logComplete());

log.info("Now connecting to the ConnectableObservable");
connectableObservable.connect();

===================

```

### share() operator 
Another operator of the ConnectableObservable **.refCount()** allows to do away with having to manually call **.connect()**,
instead it invokes the .create() code when the first Subscriber subscribes while sharing this single subscription with subsequent Subscribers.
This means that **.refCount()** basically keeps a count of references of it's subscribers and subscribes to upstream Observable
(executes the code inside .create() just for the first subscriber), but multicasts the same event to each active subscriber. 
When the last subscriber unsubscribes, the ref counter goes from 1 to 0 and triggers any unsubscribe callback associated.   
If another Subscriber subscribes after that, counter goes from 0 to 1 and the process starts over again. 

```java
ConnectableObservable<Integer> connectableStream = Observable.<Integer>create(subscriber -> {
   log.info("Inside create()");
   
   //Simulated MessageListener emits periodically every 500 milliseconds
   ResourceConnectionHandler resourceConnectionHandler = new ResourceConnectionHandler() {
        @Override
        public void onMessage(Integer message) {
             log.info("Emitting {}", message);
             subscriber.onNext(message);
        }
   };
   resourceConnectionHandler.openConnection();

   //when the last subscriber unsubscribes it will invoke disconnect on the resourceConnectionHandler
   subscriber.setCancellable(resourceConnectionHandler::disconnect);
}).publish(); 

//publish().refCount() have been joined together in the .share() operator
Observable<Integer> observable = connectableObservable.refCount();

CountDownLatch latch = new CountDownLatch(2);
connectableStream
      .take(5)
      .subscribe((val) -> log.info("Subscriber1 received: {}", val), 
                    logError(), logComplete(latch));

Helpers.sleepMillis(1000);

log.info("Subscribing 2nd");
//we're not seing the code inside .create() reexecuted
connectableStream
      .take(2)
      .subscribe((val) -> log.info("Subscriber2 received: {}", val), 
                    logError(), logComplete(latch));

//waiting for the streams to complete
Helpers.wait(latch);

//subscribing another after previous Subscribers unsubscribed
latch = new CountDownLatch(1);
log.info("Subscribing 3rd");
observable
     .take(1)
     .subscribe((val) -> log.info("Subscriber3 received: {}", val), logError(), logComplete(latch));


private abstract class ResourceConnectionHandler {

   ScheduledExecutorService scheduledExecutorService;

   private int counter;

   public void openConnection() {
      log.info("**Opening connection");

      scheduledExecutorService = periodicEventEmitter(() -> {
            counter ++;
            onMessage(counter);
      }, 500, TimeUnit.MILLISECONDS);
   }

   public abstract void onMessage(Integer message);

   public void disconnect() {
      log.info("**Shutting down connection");
      scheduledExecutorService.shutdown();
   }
}

===============
14:55:23 [main] INFO BaseTestObservables - Inside create()
14:55:23 [main] INFO BaseTestObservables - **Opening connection
14:55:23 [pool-1-thread-1] INFO BaseTestObservables - Emitting 1
14:55:23 [pool-1-thread-1] INFO BaseTestObservables - Subscriber1 received: 1
14:55:24 [pool-1-thread-1] INFO BaseTestObservables - Emitting 2
14:55:24 [pool-1-thread-1] INFO BaseTestObservables - Subscriber1 received: 2
14:55:24 [pool-1-thread-1] INFO BaseTestObservables - Emitting 3
14:55:24 [pool-1-thread-1] INFO BaseTestObservables - Subscriber1 received: 3
14:55:24 [main] INFO BaseTestObservables - Subscribing 2nd
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Emitting 4
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber1 received: 4
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber2 received: 4
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Emitting 5
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber1 received: 5
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber got Completed event
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber2 received: 5
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - **Shutting down connection
14:55:25 [pool-1-thread-1] INFO BaseTestObservables - Subscriber got Completed event
14:55:25 [main] INFO BaseTestObservables - Subscribing 3rd
14:55:25 [main] INFO BaseTestObservables - Inside create()
14:55:25 [main] INFO BaseTestObservables - **Opening connection
14:55:25 [pool-2-thread-1] INFO BaseTestObservables - Emitting 1
14:55:25 [pool-2-thread-1] INFO BaseTestObservables - Subscriber3 received: 1
14:55:25 [pool-2-thread-1] INFO BaseTestObservables - **Shutting down connection
14:55:25 [pool-2-thread-1] INFO BaseTestObservables - Subscriber got Completed event
```
The **share()** operator of Observable / Flowable is an operator which basically does **publish().refCount()**. 

## Schedulers
RxJava provides some high level concepts for concurrent execution, like ExecutorService we're not dealing
with the low level constructs like creating the Threads ourselves. Instead we're using a **Scheduler** which create
Workers who are responsible for scheduling and running code. By default RxJava will not introduce concurrency 
and will run the operations on the subscription thread.

There are two methods through which we can introduce Schedulers into our chain of operations:

   - **subscribeOn** allows to specify which Scheduler invokes the code contained in the lambda code for Observable.create()
   - **observeOn** allows control to which Scheduler executes the code in the downstream operators

RxJava provides some general use Schedulers:
 
  - **Schedulers.computation()** - to be used for CPU intensive tasks. A threadpool. Should not be used for tasks involving blocking IO.
  - **Schedulers.io()** - to be used for IO bound tasks  
  - **Schedulers.from(Executor)** - custom ExecutorService
  - **Schedulers.newThread()** - always creates a new thread when a worker is needed. Since it's not thread pooled and 
  always creates a new thread instead of reusing one, this scheduler is not very useful 
 
Although we said by default RxJava doesn't introduce concurrency, lots of operators involve waiting like **delay**,
**interval**, **zip** need to run on a Scheduler, otherwise they would just block the subscribing thread. 
By default **Schedulers.computation()** is used, but the Scheduler can be passed as a parameter.


## Flatmap operator
The flatMap operator is so important and has so many different uses it deserves it's own category to explain it.
Code at [Part06FlatMapOperator.java](https://github.com/balamaci/rxjava-playground/blob/master/src/test/java/com/balamaci/rx/Part06FlatMapOperator.java)

I like to think of it as a sort of **fork-join** operation because what flatMap does is it takes individual stream items
and maps each of them to an Observable(so it creates new Streams from each object) and then 'flattens' the events from 
these Streams back as coming from a single stream.

Why this looks like fork-join because for each element you can fork some jobs that keeps emitting results,
and these results are emitted back as elements to the subscribers downstream

**Rules of thumb** to consider before getting comfortable with flatMap: 
   
   - When you have an 'item' **T** and a method **T -&gt; Flowable&lt;X&gt;**, you need flatMap. Most common example is when you want 
   to make a remote call that returns an Observable / Flowable . For ex if you have a stream of customerIds, and downstream you
    want to work with actual Customer objects:    
   
   - When you have Observable&lt;Observable&lt;T&gt;&gt;(aka stream of streams) you probably need flatMap. Because flatMap means you are subscribing
   to each substream.

We use a simulated remote call that might return asynchronous as many events as the length of the color string

```java
private Flowable<String> simulateRemoteOperation(String color) {
        return Flowable.<String>create(subscriber -> {
            for (int i = 0; i < color.length(); i++) {
                subscriber.onNext(color + i);
                Helpers.sleepMillis(200);
            }

            subscriber.onComplete();
        }, BackpressureStrategy.MISSING);
    }
```

If we have a stream of color names:

```java
Flowable<String> colors = Flowable.just("orange", "red", "green")
```

to invoke the remote operation: 

```java
Flowable<String> colors = Flowable.just("orange", "red", "green")
         .flatMap(colorName -> simulatedRemoteOperation(colorName));

colors.subscribe(val -> log.info("Subscriber received: {}", val));         

====
16:44:15 [Thread-0]- Subscriber received: orange0
16:44:15 [Thread-2]- Subscriber received: green0
16:44:15 [Thread-1]- Subscriber received: red0
16:44:15 [Thread-0]- Subscriber received: orange1
16:44:15 [Thread-2]- Subscriber received: green1
16:44:15 [Thread-1]- Subscriber received: red1
16:44:15 [Thread-0]- Subscriber received: orange2
16:44:15 [Thread-2]- Subscriber received: green2
16:44:15 [Thread-1]- Subscriber received: red2
16:44:15 [Thread-0]- Subscriber received: orange3
16:44:15 [Thread-2]- Subscriber received: green3
16:44:16 [Thread-0]- Subscriber received: orange4
16:44:16 [Thread-2]- Subscriber received: green4
16:44:16 [Thread-0]- Subscriber received: orange5
```

Notice how the results are coming intertwined. This is because flatMap actually subscribes to it's inner Observables 
returned from 'simulateRemoteOperation'. You can specify the **concurrency level of flatMap** as a parameter. Meaning 
you can say how many of the substreams should be subscribed "concurrently" - after **onComplete** is triggered on the substreams,
a new substream is subscribed-.

By setting the concurrency to **1** we don't subscribe to other substreams until the current one finishes:

```
Observable<String> colors = Observable.just("orange", "red", "green")
                     .flatMap(val -> simulateRemoteOperation(val), 1); //

```

Notice now there is a sequence from each color before the next one appears

```
17:15:24 [Thread-0]- Subscriber received: orange0
17:15:24 [Thread-0]- Subscriber received: orange1
17:15:25 [Thread-0]- Subscriber received: orange2
17:15:25 [Thread-0]- Subscriber received: orange3
17:15:25 [Thread-0]- Subscriber received: orange4
17:15:25 [Thread-0]- Subscriber received: orange5
17:15:25 [Thread-1]- Subscriber received: red0
17:15:26 [Thread-1]- Subscriber received: red1
17:15:26 [Thread-1]- Subscriber received: red2
17:15:26 [Thread-2]- Subscriber received: green0
17:15:26 [Thread-2]- Subscriber received: green1
17:15:26 [Thread-2]- Subscriber received: green2
17:15:27 [Thread-2]- Subscriber received: green3
17:15:27 [Thread-2]- Subscriber received: green4
```

There is actually an operator which is basically this **flatMap with 1 concurrency called concatMap**.


Inside the flatMap we can operate on the substream with the same stream operators

```java
Observable<Pair<String, Integer>> colorsCounted = colors
    .flatMap(colorName -> {
               Observable<Long> timer = Observable.interval(2, TimeUnit.SECONDS);

               return simulateRemoteOperation(colorName) // <- Still a stream
                              .zipWith(timer, (val, timerVal) -> val)
                              .count()
                              .map(counter -> new Pair<>(colorName, counter));
               }
    );
```

## Error handling
Code at [Part08ErrorHandling.java](https://github.com/balamaci/rxjava-playground/blob/master/src/test/java/com/balamaci/rx/Part08ErrorHandling.java)

Exceptions are for exceptional situations.
The Observable contract specifies that exceptions are terminal operations. 
That means in case an error reaches the Subscriber, after invoking the 'onError' handler, it also unsubscribes:

```java
Observable<String> colors = Observable.just("green", "blue", "red", "yellow")
       .map(color -> {
              if ("red".equals(color)) {
                        throw new RuntimeException("Encountered red");
              }
              return color + "*";
       })
       .map(val -> val + "XXX");

colors.subscribe(
         val -> log.info("Subscriber received: {}", val),
         exception -> log.error("Subscriber received error '{}'", exception.getMessage()),
         () -> log.info("Subscriber completed")
);
```

returns:
```
23:30:17 [main] INFO - Subscriber received: green*XXX
23:30:17 [main] INFO - Subscriber received: blue*XXX
23:30:17 [main] ERROR - Subscriber received error 'Encountered red'
```
After the map() operator encounters an error it unsubscribes(cancels the subscription) from upstream 
- therefore 'yellow' is not even emitted-. The error travels downstream and triggers the error handler in the subscriber.


There are operators to deal with error flow control:
 
### onErrorReturn

The 'onErrorReturn' operator replaces an exception with a value:

```java
Flowable<Integer> numbers = Flowable.just("1", "3", "a", "4", "5", "c")
                            .map(Integer::parseInt) 
                            .onErrorReturn(0);      
subscribeWithLog(numbers);
=====
Subscriber received: 1
Subscriber received: 3
Subscriber received: 0
Subscriber got Completed event
```

Notice though how it didn't prevent map() operator from unsubscribing from the Flowable, but it did 
trigger the normal onNext callback instead of onError in the subscriber.


Let's introduce a more realcase scenario of a simulated remote request that might fail 

```java
private Observable<String> simulateRemoteOperation(String color) {
    return Observable.<String>create(subscriber -> {
         if ("red".equals(color)) {
              log.info("Emitting RuntimeException for {}", color);
              throw new RuntimeException("Color red raises exception");
         }
         if ("black".equals(color)) {
              log.info("Emitting IllegalArgumentException for {}", color);
              throw new IllegalArgumentException("Black is not a color");
         }

         String value = "**" + color + "**";

         log.info("Emitting {}", value);
         subscriber.onNext(value);
         subscriber.onCompleted();
    });
}

Flowable<String> colors = Flowable.just("green", "blue", "red", "white", "blue")
                .flatMap(color -> simulateRemoteOperation(color))
                .onErrorReturn(throwable -> "-blank-");
                
subscribeWithLog(colors);

============

22:15:51 [main] INFO - Emitting **green**
22:15:51 [main] INFO - Subscriber received: **green**
22:15:51 [main] INFO - Emitting **blue**
22:15:51 [main] INFO - Subscriber received: **blue**
22:15:51 [main] INFO - Emitting RuntimeException for red
22:15:51 [main] INFO - Subscriber received: -blank-
22:15:51 [main] INFO - Subscriber got Completed event
```
flatMap encounters an error when it subscribes to 'red' substreams and thus still unsubscribe from 'colors' 
stream and the remaining colors are not longer emitted


```java
Flowable<String> colors = Flowable.just("green", "blue", "red", "white", "blue")
                .flatMap(color -> simulateRemoteOperation(color)
                                    .onErrorReturn(throwable -> "-blank-")
                );
```
onErrorReturn() is applied to the flatMap substream and thus translates the exception to a value and so flatMap 
continues on with the other colors after red

returns:
```
22:15:51 [main] INFO - Emitting **green**
22:15:51 [main] INFO - Subscriber received: **green**
22:15:51 [main] INFO - Emitting **blue**
22:15:51 [main] INFO - Subscriber received: **blue**
22:15:51 [main] INFO - Emitting RuntimeException for red
22:15:51 [main] INFO - Subscriber received: -blank-
22:15:51 [main] INFO - Emitting **white**
22:15:51 [main] INFO - Subscriber received: **white**
22:15:51 [main] INFO - Emitting **blue**
22:15:51 [main] INFO - Subscriber received: **blue**
22:15:51 [main] INFO - Subscriber got Completed event
```

### onErrorResumeNext
onErrorResumeNext() returns a stream instead of an exception, useful for example to invoke a fallback 
method that returns also a stream

```java
Observable<String> colors = Observable.just("green", "blue", "red", "white", "blue")
     .flatMap(color -> simulateRemoteOperation(color)
                        .onErrorResumeNext(th -> {
                            if (th instanceof IllegalArgumentException) {
                                return Observable.error(new RuntimeException("Fatal, wrong arguments"));
                            }
                            return fallbackRemoteOperation();
                        })
     );

private Observable<String> fallbackRemoteOperation() {
        return Observable.just("blank");
}
```

## Retrying

### timeout()
Timeout operator raises exception when there are no events incoming before it's predecessor in the specified time limit.

### retry()
**retry()** - resubscribes in case of exception to the Observable

```java
Flowable<String> colors = Flowable.just("red", "blue", "green", "yellow")
       .concatMap(color -> delayedByLengthEmitter(TimeUnit.SECONDS, color) 
                             //if there are no events flowing in the timeframe   
                             .timeout(6, TimeUnit.SECONDS)  
                             .retry(2)
                             .onErrorResumeNext(Observable.just("blank"))
       );

subscribeWithLog(colors.toBlocking());
```

returns
```
12:40:16 [main] INFO - Received red delaying for 3 
12:40:19 [main] INFO - Subscriber received: red
12:40:19 [RxComputationScheduler-2] INFO - Received blue delaying for 4 
12:40:23 [main] INFO - Subscriber received: blue
12:40:23 [RxComputationScheduler-4] INFO - Received green delaying for 5 
12:40:28 [main] INFO - Subscriber received: green
12:40:28 [RxComputationScheduler-6] INFO - Received yellow delaying for 6 
12:40:34 [RxComputationScheduler-7] INFO - Received yellow delaying for 6 
12:40:40 [RxComputationScheduler-1] INFO - Received yellow delaying for 6 
12:40:46 [main] INFO - Subscriber received: blank
12:40:46 [main] INFO - Subscriber got Completed event
```

When you want to retry considering the thrown exception type:

```java
Observable<String> colors = Observable.just("blue", "red", "black", "yellow")
         .flatMap(colorName -> simulateRemoteOperation(colorName)
                .retry((retryAttempt, exception) -> {
                           if (exception instanceof IllegalArgumentException) {
                               log.error("{} encountered non retry exception ", colorName);
                               return false;
                           }
                           log.info("Retry attempt {} for {}", retryAttempt, colorName);
                           return retryAttempt <= 2;
                })
                .onErrorResumeNext(Observable.just("generic color"))
         );
```

```
13:21:37 [main] INFO - Emitting **blue**
13:21:37 [main] INFO - Emitting RuntimeException for red
13:21:37 [main] INFO - Retry attempt 1 for red
13:21:37 [main] INFO - Emitting RuntimeException for red
13:21:37 [main] INFO - Retry attempt 2 for red
13:21:37 [main] INFO - Emitting RuntimeException for red
13:21:37 [main] INFO - Retry attempt 3 for red
13:21:37 [main] INFO - Emitting IllegalArgumentException for black
13:21:37 [main] ERROR - black encountered non retry exception 
13:21:37 [main] INFO - Emitting **yellow**
13:21:37 [main] INFO - Subscriber received: **blue**
13:21:37 [main] INFO - Subscriber received: generic color
13:21:37 [main] INFO - Subscriber received: generic color
13:21:37 [main] INFO - Subscriber received: **yellow**
13:21:37 [main] INFO - Subscriber got Completed event
```

### retryWhen
A more complex retry logic like implementing a backoff strategy in case of exception
This can be obtained with **retryWhen**(exceptionObservable -> Observable)

retryWhen resubscribes when an event from an Observable is emitted. It receives as parameter an exception stream
     
we zip the exceptionsStream with a .range() stream to obtain the number of retries,
however we want to wait a little before retrying so in the zip function we return a delayed event - .timer()

The delay also needs to be subscribed to be effected so we also flatMap

```java
Observable<String> colors = Observable.just("blue", "green", "red", "black", "yellow");

colors.flatMap(colorName -> 
                   simulateRemoteOperation(colorName)
                      .retryWhen(exceptionStream -> exceptionStream
                                    .zipWith(Observable.range(1, 3), (exc, attempts) -> {
                                        //don't retry for IllegalArgumentException
                                        if(exc instanceof IllegalArgumentException) {
                                             return Observable.error(exc);
                                        }

                                        if(attempts < 3) {
                                             return Observable.timer(2 * attempts, TimeUnit.SECONDS);
                                        }
                                        return Observable.error(exc);
                                    })
                                    .flatMap(val -> val)
                      )
                      .onErrorResumeNext(Observable.just("generic color")
                   )
            );
```

```
15:20:23 [main] INFO - Emitting **blue**
15:20:23 [main] INFO - Emitting **green**
15:20:23 [main] INFO - Emitting RuntimeException for red
15:20:23 [main] INFO - Emitting IllegalArgumentException for black
15:20:23 [main] INFO - Emitting **yellow**
15:20:23 [main] INFO - Subscriber received: **blue**
15:20:23 [main] INFO - Subscriber received: **green**
15:20:23 [main] INFO - Subscriber received: generic color
15:20:23 [main] INFO - Subscriber received: **yellow**
15:20:25 [RxComputationScheduler-1] INFO - Emitting RuntimeException for red
15:20:29 [RxComputationScheduler-2] INFO - Emitting RuntimeException for red
15:20:29 [main] INFO - Subscriber received: generic color
15:20:29 [main] INFO - Subscriber got Completed event
```

**retryWhen vs repeatWhen** 
With similar names it worth noting the difference.
 
   - repeat() resubscribes when it receives onCompleted().
   - retry() resubscribes when it receives onError().
   
Example using repeatWhen() to implement periodic polling
```java
remoteOperation.repeatWhen(completed -> completed
                                     .delay(2, TimeUnit.SECONDS))                                                       
```
   
## Backpressure

It can be the case of a slow consumer that cannot keep up with the producer that is producing too many events
that the subscriber cannot process. 

Backpressure relates to a feedback mechanism through which the subscriber can signal to the producer how much data 
it can consume and so to produce only that amount.

The [reactive-streams](https://github.com/reactive-streams/reactive-streams-jvm) section above we saw that besides the 
**onNext, onError** and **onComplete** handlers, the Subscriber
has an **onSubscribe(Subscription)**, Subscription through which it can signal upstream it's ready to receive a number 
of items and after it processes the items request another batch.


```java
public interface Subscriber<T> {
    //signals to the Publisher to start sending events
    public void onSubscribe(Subscription s);     
    
    public void onNext(T t);
    public void onError(Throwable t);
    public void onComplete();
}
```

The methods exposed by **Subscription** through which the subscriber comunicates with the upstream:

```java
public interface Subscription {
    public void request(long n); //request n items
    public void cancel();
}
```

So in theory the Subscriber can prevent being overloaded by requesting an initial number of items. The Publisher would
send those items downstream and not produce any more, until the Subscriber would request more. We say in theory because
until now we did not see a custom **onSubscribe(Subscription)** request being implemented. This is because if not specified explicitly,
there is a default implementation which requests of **Long.MAX_VALUE** which basically means "send all you have".

Neither did we see the code in the producer that takes consideration of the number of items requested by the subscriber. 

```java
Flowable.create(subscriber -> {
      log.info("Started emitting");

      for(int i=0; i < 300; i++) {
           if(subscriber.isCanceled()) {
              return;
           }
           log.info("Emitting {}", i);
           subscriber.next(i);
      }

      subscriber.complete();
}, BackpressureStrategy.BUFFER); //BackpressureStrategy will be explained further bellow
```
Looks like it's not possible to slow down production based on request(as there is no reference to the requested items),
we can at most stop production if the subscriber canceled subscription. 

This can be done if we extend Flowable so we can pass our custom Subscription type to the downstream subscriber:

```java
private class CustomRangeFlowable extends Flowable<Integer> {

        private int startFrom;
        private int count;

        CustomRangeFlowable(int startFrom, int count) {
            this.startFrom = startFrom;
            this.count = count;
        }

        @Override
        public void subscribeActual(Subscriber<? super Integer> subscriber) {
            subscriber.onSubscribe(new CustomRangeSubscription(startFrom, count, subscriber));
        }

        class CustomRangeSubscription implements Subscription {

            volatile boolean cancelled;
            boolean completed = false;
            
            private int count;
            private int currentCount;
            private int startFrom;

            private Subscriber<? super Integer> actualSubscriber;

            CustomRangeSubscription(int startFrom, int count, Subscriber<? super Integer> actualSubscriber) {
                this.count = count;
                this.startFrom = startFrom;
                this.actualSubscriber = actualSubscriber;
            }

            @Override
            public void request(long items) {
                log.info("Downstream requests {} items", items);
                for(int i=0; i < items; i++) {
                    if(cancelled || completed) {
                        return;
                    }

                    if(currentCount == count) {
                        completed = true;
                        if(cancelled) {
                            return;
                        }

                        actualSubscriber.onComplete();
                        return;
                    }

                    int emitVal = startFrom + currentCount;
                    currentCount++;
                    actualSubscriber.onNext(emitVal);
                }
            }

            @Override
            public void cancel() {
                cancelled = true;
            }
        }
    }
```   
Now lets see how we can custom control how many items we request from upstream, to simulate an initial big request, 
and then a request for other smaller batches of items as soon as the subscriber finishes and is ready for another batch.
  
```java
Flowable<Integer> flowable = new CustomRangeFlowable(5, 10);

flowable.subscribe(new Subscriber<Integer>() {

       private Subscription subscription;
       private int backlogItems;

       private final int BATCH = 2;
       private final int INITIAL_REQ = 5;

       @Override
       public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                backlogItems = INITIAL_REQ;

                log.info("Initial request {}", backlogItems);
                subscription.request(backlogItems);
            }

            @Override
            public void onNext(Integer val) {
                log.info("Subscriber received {}", val);
                backlogItems --;

                if(backlogItems == 0) {
                    backlogItems = BATCH;
                    subscription.request(BATCH);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("Subscriber encountered error");
            }

            @Override
            public void onComplete() {
                log.info("Subscriber completed");
            }
        });
=====================
Initial request 5
Downstream requests 5 items
Subscriber received 5
Subscriber received 6
Subscriber received 7
Subscriber received 8
Subscriber received 9
Downstream requests 2 items
Subscriber received 10
Subscriber received 11
Downstream requests 2 items
Subscriber received 12
Subscriber received 13
Downstream requests 2 items
Subscriber received 14
Subscriber completed        
```  

Returning to the _Flowable.create()_ example since it's not taking any account of the requested 
items by the subscriber, does it mean it might overwhelm a slow Subscriber? 

```java
private Flowable<Integer> createFlowable(int items,
                     BackpressureStrategy backpressureStrategy) {

return Flowable.create(subscriber -> {
        log.info("Started emitting");

        for (int i = 0; i < items; i++) {
            if(subscriber.isCancelled()) {
                 return;
            }
                
            log.info("Emitting {}", i);
            subscriber.onNext(i);
        }

        subscriber.onComplete();
}, backpressureStrategy); //can be BackpressureStrategy.DROP, BUFFER, LATEST,..
```
This is where the 2nd parameter _BackpressureStrategy_ comes in that allows you to specify what to do 
in the case.
 
   - BackpressureStrategy.BUFFER buffer in memory the events that overflow. Of course is we don't drop over some threshold, it might lead to OufOfMemory. 
   - BackpressureStrategy.DROP just drop the overflowing events
   - BackpressureStrategy.LATEST keep only recent event and discards previous unconsumed events.
   - BackpressureStrategy.ERROR we get an error in the subscriber immediately  
   - BackpressureStrategy.MISSING means we don't care about backpressure(we let one of the downstream operators
   onBackpressureXXX handle it -explained further down-)

  
Still what does it mean to 'overwhelm' the subscriber? 
It means to emit more items than requested by downstream subscriber.
But we said that by default the subscriber requests Long.MAX_VALUE since the code 
**flowable.subscribe(onNext(), onError, onComplete)** uses a default **onSubscribe**:
```
(subscription) -> subscription.request(Long.MAX_VALUE);
```

so unless we override it like in our custom Subscriber above, it means it would never overflow. But between the Publisher and the Subscriber you'd have a series of operators. 
When we subscribe, a Subscriber travels up through all operators to the original Publisher and some operators override 
the requested items upstream. One such operator is **observeOn**() which makes it's own request to the upstream Publisher(256 by default),
but can take a parameter to specify the request size.

```
Flowable<Integer> flowable = createFlowable(5, BackpressureStrategy.DROP)
                .observeOn(Schedulers.io(), false, 3);
flowable.subscribe((val) -> {
                               log.info("Subscriber received: {}", val);
                               Helpers.sleepMillis(millis);
                           }, logError(), logComplete());
======
[main] - Started emitting
[main] - Emitting 0
[main] - Emitting 1
[main] - Emitting 2
[main] - Emitting 3
[main] - Emitting 4
[RxCachedThreadScheduler-1] - Subscriber received: 0
[RxCachedThreadScheduler-1] - Subscriber received: 1
[RxCachedThreadScheduler-1] - Subscriber received: 2
[RxCachedThreadScheduler-1] - Subscriber got Completed event  
```
This is expected, as the subscription travels upstream through the operators to the source Flowable, while initially
the Subscriber requesting Long.MAX_VALUE from the upstream operator **observeOn**, which in turn subscribes to the source and it requests just 3 items from the source instead.
Since we used **BackpressureStrategy.DROP** all the items emitted outside the expected 3, get discarded and thus never reach our subscriber.

You may wonder what would have happened if we didn't use **observeOn**. We had to use it if we wanted to be able
to produce faster than the subscriber(it wasn't just to show a limited request operator), because we'd need a 
separate thread to produce events faster than the subscriber processes them.   

Also you can transform an Observable to Flowable by specifying a BackpressureStrategy, otherwise Observables 
just throw exception on overflowing(same as using BackpressureStrategy.DROP in Flowable.create()).
```
Flowable flowable = observable.toFlowable(BackpressureStrategy.DROP)
```
so can a hot Publisher be converted to a Flowable:
```
PublishSubject<Integer> subject = PublishSubject.create();

Flowable<Integer> flowable = subject
                .toFlowable(BackpressureStrategy.DROP)
```

There are also specialized operators to handle backpressure the onBackpressureXXX operators: **onBackpressureBuffer**,
**onBackpressureDrop**, **onBackpressureLatest**

These operators request Long.MAX_VALUE(unbounded amount) from upstream and then take it upon themselves to manage the 
requests from downstream. 
In the case of _onBackpressureBuffer_ it adds in an internal queue and send downstream the events as requested,
_onBackpressureDrop_ just discards events that are received from upstream more than requested from downstream, 
_onBackpressureLatest_ also drops emitted events excluding the last emitted event(most recent).  

```java
Flowable<Integer> flowable = createFlowable(10, BackpressureStrategy.MISSING)
                .onBackpressureBuffer(5, () -> log.info("Buffer has overflown"));

flowable = flowable
                .observeOn(Schedulers.io(), false, 3);
subscribeWithSlowSubscriber(flowable);                

=====                
[main] - Started emitting
[main] - Emitting 0
[main] - Emitting 1
[RxCachedThreadScheduler-1] - Subscriber received: 0
[main] - Emitting 2
[main] - Emitting 3
[main] - Emitting 4
[main] - Emitting 5
[main] - Emitting 6
[main] - Emitting 7
[main] - Emitting 8
[main] - Emitting 9
[main] - Buffer has overflown
[RxCachedThreadScheduler-1] ERROR - Subscriber received error 'Buffer is full'                
```

We create the Flowable with _BackpressureStrategy.MISSING_ saying we don't care about backpressure
but let one of the onBackpressureXXX operators handle it.
Notice however 



Chaining together multiple onBackpressureXXX operators doesn't actually make sense
Using something like

```java
Flowable<Integer> flowable = createFlowable(10, BackpressureStrategy.MISSING)
                 .onBackpressureBuffer(5)
                 .onBackpressureDrop((val) -> log.info("Dropping {}", val))
flowable = flowable
                .observeOn(Schedulers.io(), false, 3);
                 
subscribeWithSlowSubscriber(flowable);
```

is not behaving as probably you'd expected - buffer 5 values, and then dropping overflowing events-.
Because _onBackpressureDrop_ subscribes to the previous _onBackpressureBuffer_ operator
signaling it's requesting **Long.MAX_VALUE**(unbounded amount) from it. 
Thus **onBackpressureBuffer** will never feel its subscriber is overwhelmed and never "trigger", meaning that the last 
onBackpressureXXX operator overrides the previous one if they are chained.

Of course for implementing an event dropping strategy after a full buffer, there is the special overrided
version of **onBackpressureBuffer** that takes a **BackpressureOverflowStrategy**.

```java
Flowable<Integer> flowable = createFlowable(10, BackpressureStrategy.MISSING)
                .onBackpressureBuffer(5, () -> log.info("Buffer has overflown"),
                                            BackpressureOverflowStrategy.DROP_OLDEST);

flowable = flowable
                .observeOn(Schedulers.io(), false, 3);

subscribeWithSlowSubscriber(flowable);

===============
[main] - Started emitting
[main] - Emitting 0
[main] - Emitting 1
[RxCachedThreadScheduler-1] - Subscriber received: 0
[main] - Emitting 2
[main] - Emitting 3
[main] - Emitting 4
[main] - Emitting 5
[main] - Emitting 6
[main] - Emitting 7
[main] - Emitting 8
[main] - Buffer has overflown
[main] - Emitting 9
[main] - Buffer has overflown
[RxCachedThreadScheduler-1] - Subscriber received: 1
[RxCachedThreadScheduler-1] - Subscriber received: 2
[RxCachedThreadScheduler-1] - Subscriber received: 5
[RxCachedThreadScheduler-1] - Subscriber received: 6
[RxCachedThreadScheduler-1] - Subscriber received: 7
[RxCachedThreadScheduler-1] - Subscriber received: 8
[RxCachedThreadScheduler-1] - Subscriber received: 9
[RxCachedThreadScheduler-1] - Subscriber got Completed event
```

onBackpressureXXX operators can be added whenever necessary and it's not limited to cold publishers and we can use them 
on hot publishers also.

## Articles and books for further reading
[Reactive Programming with RxJava](http://shop.oreilly.com/product/0636920042228.do)

  










