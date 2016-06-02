package parallel.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class Functions {
	
	
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

    protected static <T> Collection<Callable<Void>> createCallables(final Iterator<T> elements, final ParameterizedRunnable<T> operation) {
        List<Callable<Void>> callables = new LinkedList<>();
        while (elements.hasNext()) {
            callables.add(() -> {
            	operation.run(elements.next());
            	return null;
            });
        }
        return callables;
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
    
    protected static <T> Collection<Callable<Void>> createCallables(T initializationValue, Function<T, Boolean> condition, Function<T, T> afterThought, ParameterizedRunnable<T> operation) {
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
    
}
