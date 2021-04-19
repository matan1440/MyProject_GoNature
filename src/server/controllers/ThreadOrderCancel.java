package server.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import server.common.EchoServer;
import client.common.SendMail;

/**
 * This Class is for orders cancellations when arrival confirmation has not been made
 * The Class contians methods for starting the thread , setting connection to the db and the main method of the order cancel + 
 * email sending - message of order cancel
 * @author Omer
 *
 */
public class ThreadOrderCancel {

	/**
	 * instance of the class
	 */
	private static ThreadOrderCancel instance = new ThreadOrderCancel();

	/**
	 * timer , we are using timer task .
	 */
	private static Timer timer;
	
	/**
	 * db conn
	 */
	private static Connection conn;
	
	/**
	 * date format
	 */
	private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss";
	String query;
	ResultSet rset;
	Statement stmt;

	/**
	 * empty constructor
	 */
	private ThreadOrderCancel() {
	}

	/**
	 * get the instance of the class
	 */
	public static ThreadOrderCancel getInstance() {
		return instance;
	}

	/**
	 * the method initialize the timer and set the task (order cancel) which needs to be performed in each interval
	 */
	public void start() {
		try {
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					CancelOrders();
				}
			};
			stmt = conn.createStatement();
			timer = new Timer(true);
			// this is task that start two hours after we do start to server //
			// this task runs every day (every 24 hours)//
			Double intervel = EchoServer.ConfirmVisitTimeMins * 60 * 1000;
			timer.scheduleAtFixedRate(timerTask, intervel.intValue(), 24 * 60 * 60 * 1000);
		} catch (Exception e) {

		}
	}

	/**
	 *the method cancel all of the orders for tomorrow in case the arrival confirmation has not been made.
	 *the method send email for the owners of the orders about the automatic cancellation
	 */
	private void CancelOrders() {
		try {
			LocalDate tomorrow = LocalDate.now().plusDays(1);
			query = "SELECT * FROM orders WHERE date = '" + tomorrow
					+ "' and status = \"not arrived\"  and confirmed = " + "'" + "show" + "'" + ";";
			rset = stmt.executeQuery(query);
			while (rset.next()) {
				SendMail.sendMailCancelNotConfirmed((rset.getString("email")));
			}
			
			query = "UPDATE orders SET confirmed = \"no\",status = \"Canceled\" WHERE date = '" + tomorrow
					+ "' and status = \"not arrived\"  and confirmed = " + "'" + "show" + "'" + ";";
			stmt.executeUpdate(query);
			
		} catch (Exception e) {
		}

	}

	/**
	 * 
	 * @return starting time of the main thread
	 * @throws ParseException in case the method not working
	 */
	public static Date getStartTime() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		String dateInString = "09-01-2021 11:00:00";
		Date date = formatter.parse(dateInString);
		return date;
	}

	/**
	 * closing the timer when the server shuts down
	 */
	public void close() {
		timer.cancel();
	}

	/**
	 * setting the conn to the db
	 * @param conn - which conn to set the parm of the class
	 */
	public static void setConn(Connection conn) {
		ThreadOrderCancel.conn = conn;
	}
}
