package parallel.java;

public class Test {

	public static void main(String[] args) {
		
		String[] data = new String[] { "Apple", "Orange", "Cherry", "Banana" };
		
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
		
	}

}
