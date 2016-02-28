package parallel.java;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		
		String[] data = new String[] { "Apple", "Orange", "Cherry", "Banana", "Perry", "Fig" };
		
		// Execute the task on each data
		System.out.println("---------- ForEach");
		Parallel.ForEach(
				data, // data to fetch
				2,    // number on threads in the pool
				(item) -> { // task to execute
					sleepRandom(1000);
					done(Thread.currentThread());
					System.out.println(Thread.currentThread().getName() + " : " + item);
				}
		);
		stats();
		
		// Run task from 0 to 9
		System.out.println("---------- For (fixed)");
		Parallel.For(0, 10, (index) -> {
			sleepRandom(1000);
			done(Thread.currentThread());
			System.out.println(Thread.currentThread().getName() + " : " + index);
		});
		stats();
		
		// Run task from 0 to 9
		System.out.println("---------- For (dynamic)");
		Parallel.For(
			0, // Valeur initiale
			(index) -> { return index < 10; }, // Condition
			(index) -> { return index + 1; }, // Post-traitement
			(index) -> {
				sleepRandom(1000);
				done(Thread.currentThread());
				System.out.println(Thread.currentThread().getName() + " : " + index);
			}
		);
		stats();
		
	}

	private static void stats() {
		double c = counters.values().stream().mapToInt(Integer::intValue).sum();
		counters.forEach((thread, i) -> {
			System.out.print(String.format("  %.0f%%", i / c * 100d));
		});
		System.out.println();
		counters.clear();
	}

	private static Map<Thread, Integer> counters = new HashMap<>();
	
	private static void done(Thread thread) {
		int c = counters.containsKey(thread) ? counters.get(thread) : 0;
		c++;
		counters.put(thread, c);
	}

	private static void sleepRandom(long maxDuration) {
		try {
			Thread.sleep((long)(Math.random() * maxDuration));
		}
		catch (InterruptedException e) { }
	}

}
