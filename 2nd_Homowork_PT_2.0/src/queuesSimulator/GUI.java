package queuesSimulator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SpinnerDateModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI {

	private JFrame frame;
	private JTextField queuesNo;
	private JTextField minArrivingT;
	private JTextField maxArrivingT;
	private JLabel lblMinimumArrivingTime;
	private JLabel lblMaximumArrivingTime;
	private JLabel lblMinimumServingTime;
	private JLabel lblMaximumServintTime;
	private JTextField minServingT;
	private JTextField maxServingT;
	private JSpinner spinnerStartInterval;
	private JButton btnStartSimulation;
	private JButton btnStopSimulation;
	private Date startDate, endDate;

	private static GUI window;
	private String text;
	private String textOutput[];

	private int xJLabel = 25;
	private int yJLabel = 170;
	private int heightJLabel = 20;

	private int x = 25;
	private int y = 200;
	private int width = 90;
	private int height = 350;
	private int space = 10;
	private JScrollPane scrollPane[];
	private JTextArea textArea[];
	private JLabel jLabel[];

	private Shop myShop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		text = "";

		frame = new JFrame();
		frame.setTitle("Queue simulation");
		frame.setBounds(100, 100, 900, 600);
		frame.getContentPane().setEnabled(true);
		frame.setResizable(true);
		frame.getContentPane().setLayout(null);

		queuesNo = new JTextField();
		queuesNo.setBounds(102, 15, 50, 25);
		frame.getContentPane().add(queuesNo);
		queuesNo.setColumns(10);

		JLabel lblQueuesNo = new JLabel("Queues No.");
		lblQueuesNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQueuesNo.setBounds(0, 15, 80, 25);
		frame.getContentPane().add(lblQueuesNo);

		minArrivingT = new JTextField();
		minArrivingT.setColumns(10);
		minArrivingT.setBounds(428, 15, 50, 25);
		frame.getContentPane().add(minArrivingT);

		maxArrivingT = new JTextField();
		maxArrivingT.setColumns(10);
		maxArrivingT.setBounds(428, 50, 50, 25);
		frame.getContentPane().add(maxArrivingT);

		lblMinimumArrivingTime = new JLabel(
				"Minimum arriving time between customers");
		lblMinimumArrivingTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblMinimumArrivingTime.setBounds(175, 15, 270, 25);
		frame.getContentPane().add(lblMinimumArrivingTime);

		lblMaximumArrivingTime = new JLabel(
				"Maximum arriving time between customers");
		lblMaximumArrivingTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblMaximumArrivingTime.setBounds(175, 50, 270, 25);
		frame.getContentPane().add(lblMaximumArrivingTime);

		lblMinimumServingTime = new JLabel("Minimum serving time");
		lblMinimumServingTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblMinimumServingTime.setBounds(525, 15, 137, 25);
		frame.getContentPane().add(lblMinimumServingTime);

		lblMaximumServintTime = new JLabel("Maximum servint time");
		lblMaximumServintTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblMaximumServintTime.setBounds(525, 50, 137, 25);
		frame.getContentPane().add(lblMaximumServintTime);

		minServingT = new JTextField();
		minServingT.setColumns(10);
		minServingT.setBounds(665, 15, 50, 25);
		frame.getContentPane().add(minServingT);

		maxServingT = new JTextField();
		maxServingT.setColumns(10);
		maxServingT.setBounds(665, 50, 50, 25);
		frame.getContentPane().add(maxServingT);

		// -----------------------------------------------------------------------------
		startDate = new Date();
		spinnerStartInterval = new JSpinner(new SpinnerDateModel(startDate,
				null, null, Calendar.HOUR_OF_DAY));
		spinnerStartInterval.setEditor(new JSpinner.DateEditor(
				spinnerStartInterval, "HH:mm - yyyy.MM.dd"));

		spinnerStartInterval.setBounds(749, 15, 125, 25);
		frame.getContentPane().add(spinnerStartInterval);
		// -----------------------------------------------------------------------------

		// -----------------------------------------------------------------------------
		endDate = new Date();
		JSpinner spinnerEndInterval = new JSpinner(new SpinnerDateModel(
				endDate, null, null, Calendar.HOUR_OF_DAY));
		spinnerEndInterval.setEditor(new JSpinner.DateEditor(
				spinnerEndInterval, "HH:mm - yyyy.MM.dd"));
		spinnerEndInterval.setBounds(749, 50, 125, 25);
		frame.getContentPane().add(spinnerEndInterval);
		// -----------------------------------------------------------------------------

		btnStartSimulation = new JButton("Start simulation");
		btnStartSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createTextAreas(Integer.parseInt(queuesNo.getText()));
				myShop = new Shop(Integer.parseInt(queuesNo.getText()), Integer
						.parseInt(minArrivingT.getText()), Integer
						.parseInt(maxArrivingT.getText()), Integer
						.parseInt(minServingT.getText()), Integer
						.parseInt(maxServingT.getText()),
						(Date) spinnerStartInterval.getModel().getValue(),
						(Date) spinnerEndInterval.getModel().getValue(), window);
			}
		});
		btnStartSimulation.setBounds(49, 107, 200, 50);
		frame.getContentPane().add(btnStartSimulation);

		btnStopSimulation = new JButton("Stop simulation");
		btnStopSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// average waiting time
				System.out.println("The average waiting time: "
						+ myShop.getAverageWaitingTime() + " minutes.");

				window.setLogText("The average waiting time for this store is: "
						+ myShop.getAverageWaitingTime() + " minutes.");
				// ----------

				// average service time
				System.out.println("The average service time: "
						+ myShop.getAverageServiceTime() + " minutes.");

				window.setLogText("The average service time for this store is: "
						+ myShop.getAverageServiceTime() + " minutes.");
				// ----------
				Logger logger = Logger.getLogger("MyLog");
				FileHandler fh;

				try {

					// This block configure the logger with handler and
					// formatter
					fh = new FileHandler(
							"E:/Eclipse Workspace/2nd_Homowork_PT_2.0/src/myLog.log");
					logger.setUseParentHandlers(false);
					logger.addHandler(fh);

					SimpleFormatter formatter = new SimpleFormatter();
					fh.setFormatter(formatter);

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// the following statement is used to log any messages
				logger.info(text);
				System.out.println(text);

				System.exit(0);
			}
		});
		btnStopSimulation.setBounds(620, 107, 200, 50);
		frame.getContentPane().add(btnStopSimulation);

		JButton btnCloseRandomQueue = new JButton("Close random queue");
		btnCloseRandomQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				myShop.closeCashRegister((int) (Math.random() * Integer
						.parseInt(queuesNo.getText())));
			}
		});
		btnCloseRandomQueue.setBounds(360, 115, 160, 35);
		frame.getContentPane().add(btnCloseRandomQueue);

		JLabel lblCristianMariusMuresan = new JLabel(
				"Designed by Cristian Marius Muresan");
		lblCristianMariusMuresan.setBounds(650, 542, 220, 25);
		frame.getContentPane().add(lblCristianMariusMuresan);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void createTextAreas(int howMany) {

		jLabel = new JLabel[howMany];
		scrollPane = new JScrollPane[howMany];
		textArea = new JTextArea[howMany];
		textOutput = new String[howMany];

		for (int i = 0; i < howMany; i++) {
			textOutput[i] = "";

			jLabel[i] = new JLabel();
			jLabel[i].setBounds(xJLabel + (space + width) * i, yJLabel, width,
					heightJLabel);
			jLabel[i].setText("Register" + (i + 1));
			frame.getContentPane().add(jLabel[i]);

			frame.repaint();

			scrollPane[i] = new JScrollPane();
			scrollPane[i].setBounds(x + (space + width) * i, y, width, height);
			frame.getContentPane().add(scrollPane[i]);

			textArea[i] = new JTextArea();
			textArea[i].setLineWrap(true);
			textArea[i].setColumns(100);
			scrollPane[i].setViewportView(textArea[i]);
		}
	}

	public void print(String newOutput, int queueNo, int customerID) {

		if (("C. " + customerID + " left\n").equals(newOutput)) {
			textOutput[queueNo] = textOutput[queueNo].replaceFirst("\n", "");
			textOutput[queueNo] = textOutput[queueNo].replace("C. "
					+ customerID + " has arrived\n", "");
		} else {
			textOutput[queueNo] += newOutput;

		}

		textArea[queueNo].setText(textOutput[queueNo]);

	}

	public void setLogText(String newText) {
		text += "\n";
		text += newText;
	}

	public void setEmptyString(int i) {
		textOutput[i] = " ";
	}
}
