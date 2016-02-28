package parallel.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class Parallel {
	
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

    /**
     * Executes a foreach loop in which iterations may run in parallel.
     * 
     * @param elements Array of data
     * @param operation Operation to perform on each data
     */
    public static <T> void ForEach(T[] elements, Function<T, ?> operation) {
    	ForEach(Arrays.asList(elements), operation);
    }
    
    public static <T> void For(T initializationValue, Function<T, Boolean> condition, Function<T, T> afterThought, ParameterizedRunnable<T> operation) {
    	execute(createCallables(initializationValue, condition, afterThought, operation));
    }

	/**
     * Executes a foreach loop in which iterations may run in parallel.
     * 
     * @param elements Array of data
     * @param operation Operation to perform on each data
     */
    public static <T> void ForEach(T[] elements, ParameterizedRunnable<T> operation) {
    	ForEach(elements, NUM_CORES * 2, operation);
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
        execute(createCallables(elements, operation), corePoolSize);
    }

    protected static void execute(Collection<Callable<Void>> tasks) {
    	execute(tasks, NUM_CORES * 2);
    }
    
    protected static void execute(Collection<Callable<Void>> tasks, int corePoolSize) {
		try {
        	
        	// Create pool
        	ExecutorService forPool = Executors.newFixedThreadPool(corePoolSize, new ThreadFactory() {
        		int i = 0;
				public Thread newThread(Runnable r) {
					return new Thread(r, "Parallel(" + i++ + ")");
				}
			});
        	
            // invokeAll blocks for us until all submitted tasks in the call complete
            forPool.invokeAll(tasks);
            
            // On arrête le pool
            forPool.shutdown();
            
        }
        catch (InterruptedException e) {
            return;
        }
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

	/**
	 * Utility method to generate a list of callable operations from the list of given items.
	 * This method will generate as much function call that there are elements in the elements list.
	 * 
	 * @param elements
	 * @param operation
	 * @return the generated list
	 */
    protected static <T> Collection<Callable<Void>> createCallables(final Iterable<T> elements, final Function<T, ?> operation) {
        List<Callable<Void>> callables = new LinkedList<>();
        for (final T elem : elements) {
            callables.add(() -> {
            	operation.apply(elem);
            	return null;
            });
        }
        return callables;
    }
    
    private static <T> Collection<Callable<Void>> createCallables(T initializationValue, Function<T, Boolean> condition, Function<T, T> afterThought, ParameterizedRunnable<T> operation) {
    	List<Callable<Void>> callables = new LinkedList<>();
    	// Condition
    	while (condition.apply(initializationValue)) {
    		// Current value
    		final T var = initializationValue;
    		// Create job
    		callables.add(() -> {
    			operation.run(var);
    			return null;
    		});
    		// Post-condition
    		initializationValue = afterThought.apply(initializationValue);
    	}
		return callables;
	}

	public static <T> void Chunk(final Collection<T> elements, int chunkSize, final Function<T, ?> operation) {
		//elements.sp
		
	}
    
}