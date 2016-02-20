package parallel.java;

@FunctionalInterface
public interface ParameterizedRunnable<T> {

	public void run(T arg);
	
}
