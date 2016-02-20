# Java - Task Parallel Library

Equivalent of Task Parallel Library from C# in Java. Compatible with lambda expressions and collections.

```java
// Execute the task on each data
Parallel.ForEach(data,
	(item) -> {
		System.out.println(Thread.currentThread().getName() + " : " + item);
	}
);
// Run task from 0 to 9
Parallel.For(0, 10, (index) -> {
	System.out.println(Thread.currentThread().getName() + " : " + index);
});
```