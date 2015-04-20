package queuesSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Shop {

	private int currentArrivalTime = 0;
	private int cashRegNo;
	private CashRegister[] registers;
	private Timer timer;
	private int minArrivalTime;
	private int maxArrivalTime;
	private int minServingTime;
	private int maxServingTime;
	private Date startDate, endDate, currentTime;
	public GUI window;
	private int totalNoOfCustomers;
	private int customerNumber;
	private Customer randomCustomer;
	private Random randomNo;
	private BlockingQueue<Customer> customersQueue;
	private int cashRegClosed = -1;
	private int totalWaitingTime, totalServiceTime;
	private int averageWaitingTime, averageServiceTime;

	public Shop(int cashRegNo, int minArrivalTime, int maxArrivalTime,
			int minServingTime, int maxServingTime, Date startDate,
			Date endDate, GUI window) {
		this.cashRegNo = cashRegNo;
		this.minArrivalTime = minArrivalTime;
		this.maxArrivalTime = maxArrivalTime;
		this.minServingTime = minServingTime;
		this.maxServingTime = maxServingTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.window = window;
		// This is used to manage time and all the actions
		timer = new Timer(1000, new TickListener());
		customerNumber = 0;
		customersQueue = new LinkedBlockingQueue<Customer>();
		totalNoOfCustomers = 0;
		totalServiceTime = 0;
		randomNo = new Random();
		newShop();

	}

	private void newShop() {

		// -----------Open X registers---------
		registers = new CashRegister[this.cashRegNo];

		for (int i = 0; i < cashRegNo; i++) {
			registers[i] = new CashRegister(i, this.window);

			registers[i].runQueue();
		}
		// ------------------------------------

		// -----------Set seconds&milliseconds = 0---------
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startDate = cal.getTime();

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endDate);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		endDate = cal2.getTime();
		// -------------------------------------------------

		currentTime = startDate;

		if (startDate.before(endDate)) {
			timer.start();
		} else {
			JOptionPane.showMessageDialog(null,
					"Start date should be before end date!");
		}
	}

	public int getCashRegWithMinimumWaitingTime() {
		int minimumWaitingTime = 1000;// registers[].getTotalWaitingTime();
		int position = 0;
		for (int i = 0; i < cashRegNo; i++) {
			if (i != cashRegClosed) {
				if (registers[i].getTotalWaitingTime() < minimumWaitingTime) {
					minimumWaitingTime = registers[i].getTotalWaitingTime();
					position = i;
				}
			}
		}
		return position;
	}

	public Customer addNewCustomer() {
		int randomServiceTime = randomNo.nextInt(maxServingTime
				- minServingTime)
				+ minServingTime;
		++customerNumber;
		randomCustomer = new Customer(customerNumber, currentArrivalTime,
				randomServiceTime, 0);

		totalServiceTime += randomServiceTime;

		return randomCustomer;
	}

	public void closeCashRegister(int i) {
		cashRegClosed = i;

		customersQueue = registers[i].getCustomers();

		registers[i].stopQueue();
		window.setEmptyString(i);
		window.print("Closed", i, 0);

		for (Customer customer : customersQueue) {

			int randomRegister = getCashRegWithMinimumWaitingTime();
			System.out.println(randomRegister);
			if (randomRegister != i) {
				registers[randomRegister].addCustomer(customer);
				window.setLogText("Customer number " + customer.getID()
						+ " has moved to register number "
						+ (randomRegister + 1) + "\n");

				window.print("\nC. " + customer.getID() + " has arrived\n",
						randomRegister, 0);

			}

		}
	}

	public int getAverageWaitingTime() {
		totalWaitingTime = 0;
		for (int i = 0; i < cashRegNo; i++) {
			totalWaitingTime += registers[i].getTotalWaitingTime();
		}
		averageWaitingTime = totalWaitingTime / totalNoOfCustomers;
		return averageWaitingTime;
	}

	public int getAverageServiceTime() {
		averageServiceTime = totalServiceTime / totalNoOfCustomers;
		return averageServiceTime;
	}

	public class TickListener implements ActionListener {

		private int i = 0;
		private int minPosition;

		/*
		 * private Date auxSPH[]; private Date auxEPH[]; private int counter[];
		 * private int noOfHours = 0; private int hourNo = 0;
		 */
		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {

			/*
			 * noOfHours = endDate.getHours() - startDate.getHours()-1; auxSPH =
			 * new Date[noOfHours]; auxEPH = new Date[noOfHours]; counter = new
			 * int[noOfHours];
			 * 
			 * for(int j=0;j<noOfHours;j++){ auxSPH[j] = new Date(); auxEPH[j] =
			 * new Date(); auxSPH[j].setHours(startDate.getHours()+j);
			 * auxEPH[j].setHours(startDate.getHours()+j+1);
			 * auxSPH[j].setMinutes(startDate.getMinutes());
			 * auxEPH[j].setMinutes(startDate.getMinutes()); }
			 */

			i++;

			int timeBetweenCustomers = randomNo.nextInt(maxArrivalTime
					- minArrivalTime)
					+ minArrivalTime;
			currentArrivalTime += timeBetweenCustomers;
			if (currentTime.compareTo(endDate) == 0
					|| currentTime.after(endDate)) {

				timer.stop();
			} else {

				minPosition = getCashRegWithMinimumWaitingTime();

				registers[minPosition].addCustomer(addNewCustomer());

				/*
				 * if(auxSPH[hourNo].getHours() < currentTime.getHours() &&
				 * currentTime.getHours()< auxEPH[hourNo].getHours()){
				 * counter[hourNo]++; }else{ hourNo++; }
				 * 
				 * int maxNoOfCustomers=0; int hour=0;
				 * 
				 * for(int j=0;j<hourNo;j++){ if (maxNoOfCustomers<counter[j]){
				 * maxNoOfCustomers=counter[j]; hour=j; } }
				 */

				// System.out.println("The peak hour was between "+auxSPH[hour]+" and "+
				// auxEPH[hour]+" : "+maxNoOfCustomers+" customers.");

				System.out.println("registrul nr: " + (minPosition + 1)
						+ "   client: " + i + " " + currentTime);

				window.print("\nC. " + i + " has arrived\n", minPosition, 0);

				window.setLogText("\nCustomer number " + i + " has arrived at "
						+ currentTime.toString() + " at register: "
						+ (minPosition + 1) + "\n");

				currentTime.setMinutes(currentTime.getMinutes()
						+ timeBetweenCustomers);
			}
			totalNoOfCustomers = i;
		}

	}

}
