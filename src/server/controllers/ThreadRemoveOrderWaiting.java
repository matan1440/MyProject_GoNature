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


/**
 * This Class is for removing orders from waiting list in case the time has passed
 * The Class contians methods for starting the thread , setting connection to the db and the main method of the deleting orders from waiting list 
 * @author Omer
 *
 */

public class ThreadRemoveOrderWaiting {

	/**
	 * instance of the class
	 */
	private static ThreadRemoveOrderWaiting instance = new ThreadRemoveOrderWaiting();

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
	private ThreadRemoveOrderWaiting() {
		
	}

	/**
	 * get the instance of the class
	 */
	public static ThreadRemoveOrderWaiting getInstance() {
		return instance;
	}

	/**
	 * the method initialize the timer and set the task (remove orders from waitinglist) which needs to be performed in each interval
	 */
	public void start() {
		try {
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					RemoveOrders();
				}
			};
			stmt = conn.createStatement();
			timer = new Timer(true);
			// this is task that start two hours after we do start to server //
			// this task runs every day (every 24 hours)//
			timer.schedule(timerTask, getStartTime() , 24 * 60 * 60 * 1000);
		} catch (Exception e) {

		}
	}

	/**
	 *the method remove orders from waitinglist in case the order time has passed 
	 *the method remove orders from orders in case the order time has passed 
	 */
	
	private void RemoveOrders() {
		try {
			LocalDate yesterday = LocalDate.now().minusDays(1);			
			query = "delete from waitinglist where date = " + "'" + yesterday +"';";
			stmt.executeUpdate(query);

			query = "delete from orders where date = " + "'" + yesterday +"'" + " and status =" +"'" + "Waiting" + "';";
			stmt.executeUpdate(query);
			}
		catch (Exception e) {

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
		ThreadRemoveOrderWaiting.conn = conn;
	}
}
