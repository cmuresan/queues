package queuesSimulator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CashRegister implements Runnable {

	private int regNo;
	private BlockingQueue<Customer> customersQueue;
	private int totalWaitingTime;
	private Thread queueThread;
	public static int simTime = 1000;
	public String customerTextOut;
	private GUI window;

	public CashRegister(int regNo, GUI window) {
		this.regNo = regNo;
		this.window = window;
		customersQueue = new LinkedBlockingQueue<Customer>();
		totalWaitingTime = 0;
	}
	
	public int getRegNo() {
		return regNo;
	}

	public int getTotalWaitingTime() {
		return totalWaitingTime;
	}

	public BlockingQueue<Customer> getCustomers() {
		return customersQueue;
	}

	public void addCustomer(Customer newCustomer) {
		totalWaitingTime += newCustomer.getServingTime();
		try {
			customersQueue.put(newCustomer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getWaitingTime(BlockingQueue<Customer> customersQueue2) {
		int waitingTime = 0;
		for (Customer customer : customersQueue2) {
			waitingTime += customer.getServingTime();
		}
		return waitingTime;
	}

	public void runQueue() {

		queueThread = new Thread(this);

		queueThread.start();

	}

	@SuppressWarnings("deprecation")
	public void stopQueue() {
		queueThread.stop();
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while (simTime > 0) {

			if (!customersQueue.isEmpty()) {
				try {
					customersQueue.element().setWaitingTime(
							getWaitingTime(customersQueue));

					totalWaitingTime += customersQueue.element()
							.getWaitingTime();

					this.queueThread.sleep(customersQueue.element()
							.getServingTime() * 1000);

					customerTextOut = "";

					customerTextOut = "C. " + customersQueue.element().getID()
							+ " left\n";

					window.setLogText("Customer number "
							+ customersQueue.element().getID()
							+ " has left the store.\n");

					window.print(customerTextOut, this.regNo, customersQueue
							.element().getID());

					customersQueue.remove();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				simTime--;
			}

		}

	}

}
