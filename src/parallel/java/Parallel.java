package parallel.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Parallel {
	
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	/**
     * Executes a foreach loop in which iterations may run in parallel.
     * 
     * @param elements Array of data
     * @param operation Operation to perform on each data
     */
    
    
    public static <T> void ForEach(T[] elements, ParameterizedRunnable<T> operation) {
    	ForEach(elements, NUM_CORES * 2, operation);
    }

    public static <T> void ForEach(T[] elements, Function<T, ?> operation) {
    	ForEach(Arrays.asList(elements), operation);
    }
    
    public static <T> void ForEach(Collection<T> elements, ParameterizedRunnable<T> operation) {
    	Functions.execute(Functions.createCallables(elements.iterator(), operation), NUM_CORES * 2);
    }
    
    public static <T> void For(T initializationValue, Function<T, Boolean> condition, Function<T, T> afterThought, ParameterizedRunnable<T> operation) {
    	execute(Functions.createCallables(initializationValue, condition, afterThought, operation));
    }
    
    public static <T> void ForEach(T[] elements, int corePoolSize, ParameterizedRunnable<T> operation) {
    	ForEach(Arrays.asList(elements), corePoolSize, (element) -> {
    		operation.run(element);
    		return null;
    	});
    }
    
    public static <T> void ForEach(Iterable<T> elements, Function<T, ?> operation) {
    	ForEach(elements, NUM_CORES * 2, operation);
    }
    
    public static <T> void ForEach(final Iterable<T> elements, int corePoolSize, final Function<T, ?> operation) {
    	Functions.execute(Functions.createCallables(elements, operation), corePoolSize);
    }

    protected static void execute(Collection<Callable<Void>> tasks) {
    	Functions.execute(tasks, NUM_CORES * 2);
    }
    

    
    /**
     * Executes a for loop in which iterations may run in parallel.
     * 
     * @param fromInclusive Start index, inclusive
     * @param toExclusive End index, exclusive
     * @param operation Operation to perform on each value
     */
    public static <T> void For(int fromInclusive, int toExclusive, ParameterizedRunnable<Integer> operation) {
    	Iterable<Integer> iterator = new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					private int cFrom = fromInclusive;
					private int cTo = toExclusive;
					public boolean hasNext() {
						return cFrom < cTo;
					}
					public Integer next() {
						return cFrom++;
					}
				};
			}
		};
		ForEach(iterator, (element) -> {
    		operation.run(element);
    		return null;
    	});
    }

	public static <T> void Chunk(final Collection<T> elements, int chunkSize, final Function<T, ?> operation) {
		//elements.sp
		
	}
    
}