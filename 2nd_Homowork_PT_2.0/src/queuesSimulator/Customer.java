package queuesSimulator;

public class Customer {

	private int customerID;
	private int servingTime;
	private int arrivalTime;
	private int waitingTime;

	public Customer(int customerNumber, int arrivalTime, int servingTime,
			int waitingTime) {
		this.customerID = customerNumber;
		this.arrivalTime = arrivalTime;
		this.servingTime = servingTime;
		this.waitingTime = waitingTime;
	}

	public int getID() {
		return customerID;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setServingTime(int newServingTime) {
		servingTime = newServingTime;
	}

	public int getServingTime() {
		return servingTime;
	}

	public void setWaitingTime(int newWaitingTime) {
		waitingTime = newWaitingTime;
	}

	public int getWaitingTime() {
		return this.waitingTime;
	}

}
