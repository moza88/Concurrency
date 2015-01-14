
public class SynchronizeKeyword {
	private int count = 0;
	
	public static void main(String[] args) {
		SynchronizeKeyword synch = new SynchronizeKeyword();
		synch.doWork();
	}
	
	//Makes sure that every object calls the instrinsic log (mutex) at a time
	//It will only allow one thread at a time use the count variable
	//Allows each thread see it's current state
	public synchronized void increment()  {
		count++;
	}
	
	public void doWork() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				
			for (int i = 0; i <1000; i++) {
				count++;
			}
		}
			
		});
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				
			for (int i = 0; i <1000; i++) {
				count++;
			}
		}
			
		});
		
		t1.start();
		t2.start();
		
		//Join allows the threads to complete their work
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Count = " + count);
		
	}
}
