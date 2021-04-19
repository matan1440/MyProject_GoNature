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
import client.common.SendMail;

/**
 * This Class is for orders reminders for all of the orders for tommrow
 * The Class contians methods for starting the thread , setting connection to the db and the main method of the order reminder + 
 * email sending - message of orders reminder
 * @author Omer
 *
 */

public class ThreadOrderReminder {

	/**
	 * instance of the class
	 */
	private static ThreadOrderReminder instance = new ThreadOrderReminder();

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
	private ThreadOrderReminder() {
	}

	/**
	 * get the instance of the class
	 */
	public static ThreadOrderReminder getInstance() {
		return instance;
	}

	/**
	 * the method initialize the timer and set the task (order reminder) which needs to be performed in each interval
	 */
	public void start() {
		try {
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					OrderAlert();
				}
			};
			stmt = conn.createStatement();
			timer = new Timer(true);
			// this is task that start after we do start to server //
			// this task runs every day (every 24 hours)//
			timer.scheduleAtFixedRate(timerTask, 0, 24 * 60 * 60 * 1000);
		} catch (Exception e) {

		}
	}

	/**
	 *the method send arrival confirmation email for all of the orders for tomorrow .
	 *the method sets the visibility of arrival confirmation button in the view order details page
	 */
	
	private void OrderAlert() {
		try {
			LocalDate tomorrow = LocalDate.now().plusDays(1);
			query = "SELECT * FROM orders WHERE date = '" + tomorrow + "' and status = \"not arrived\" " + ";";
			rset = stmt.executeQuery(query);
			while (rset.next()) {
				SendMail.sendMailReminder((rset.getString("email")));
			}

			query = "UPDATE orders SET confirmed  = \"show\" WHERE date = '" + tomorrow
					+ "' and status = \"not arrived\" " + ";";
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
		String dateInString = "09-01-2021 09:00:00";
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
		ThreadOrderReminder.conn = conn;
	}
}
