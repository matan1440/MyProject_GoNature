package server.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import client.common.MessageCS;
import client.common.SendMail;
import client.common.MessageCS.MessageType;

import client.logic.DateOrder;
import client.logic.Order;
import client.logic.Subscriber;
import client.logic.Worker;
import ocsf.server.ConnectionToClient;
import server.common.EchoServer;
import server.database.mysqlConnection;

/**
 * This Class is the main controller of the server , the class contains methods
 * for handling the request from the client, processing the data, and sending
 * the data back to the relevant client / all clients .
 * 
 * @author Omer Matan Daniel Tania Tomer
 *
 */
public class ServerController {
	mysqlConnection mysqlConnection = EchoServer.getDBClass();
	private MessageCS message;
	String query;
	Statement stmt;
	ResultSet rset;

	@SuppressWarnings("incomplete-switch")

	/**
	 * this is main method of the server - that method handle the processing of the
	 * data and sending the data back to the user
	 * 
	 * @param msg    - the msg for the server, contains the request data.
	 * @param client - the client that made the request to the server
	 * @param conn   - the conn to the db
	 * 
	 *               The cases are for all of logins , handling waiting list, making
	 *               order, getting other dates , cancel order, confirm arrivel ,
	 *               making order from waiting list, all of the access control cases
	 *               (card reader and worker) , the service cases of registration of
	 *               guide/ subscriber all of the cases of park manager - reports ,
	 *               discounts , parameters all of the cases of dep. manager -
	 *               reports , discounts , parameters
	 * 
	 */
	public void messageReceivedFromClient(Object msg, ConnectionToClient client, Connection conn) {
		message = (MessageCS) msg;

		try {
			stmt = conn.createStatement();
			switch (message.messageType) {
			case LOGIN_CHECK: {
				String type = "";
				String isConnected = "";
				int count = 0;

				if (message.conn_check == true) {
					type = "worker";
				} else {
					type = "traveler";
				}
				query = "SELECT COUNT(user_id) FROM users";
				query += " WHERE ID  =\"";
				query += message.string;
				query += "\" and type = \"";
				query += type;
				query += "\";";
				rset = stmt.executeQuery(query);

				while (rset.next()) {
					count = rset.getInt(1);
				}

				if (count == 0) {
					query = "INSERT INTO users(ID, type, connected) VALUES (\"";
					query += message.string;
					query += "\",\"";
					query += type;
					query += "\",\"";
					query += "1";
					query += "\");";
					stmt.executeUpdate(query);

					message = new MessageCS(MessageType.LOGIN_CHECK, GetID(message.string, type),
							false);
					client.sendToClient(message);

				} else {

					query = "SELECT connected FROM users";
					query += " WHERE ID  =\"";
					query += message.string;
					query += "\" and type = \"";
					query += type;
					query += "\";";
					rset = stmt.executeQuery(query);

					while (rset.next()) {
						isConnected = rset.getString(1);
					}

					query = "UPDATE users SET connected = \"";
					query += "1";
					query += "\" WHERE ID = \"";
					query += message.string;
					query += "\" and type = \"";
					query += type;
					query += "\";";
					stmt.executeUpdate(query);
					if (isConnected.equals("0")) {
						message = new MessageCS(MessageType.LOGIN_CHECK,
								GetID(message.string, type), false);
						client.sendToClient(message);
					} else {
						message = new MessageCS(MessageType.LOGIN_CHECK,
								GetID(message.string, type), true);
						client.sendToClient(message);
					}
				}
			}
				break;
			case LOG_OUT: {

				query = "delete from users where user_id = \"";
				query += message.num;
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message1 = new MessageCS(MessageType.LOG_OUT, message.num);
				client.sendToClient(message1);
			}
				break;

			case LOGIN_WORKER:
				// check if the worker id equals to worker id in the table //
				query = "SELECT * FROM workers WHERE ID = '" + message.Worker.getID() + "'" + ";";
				rset = stmt.executeQuery(query);

				// check if we don't found a worker with this id//
				if (!rset.next()) {
					message = new MessageCS(MessageType.ERROR_LOGIN, "The id / username not exists");
					client.sendToClient(message);
					break;
				}
				// we found worker with this id //
				else {
					// success case - we found a worker with this id and password !//
					if (rset.getString("Password").equals(message.Worker.getPassword())) {
						Worker worker = new Worker();
						worker.setEmail(rset.getString("email"));
						worker.setFirstName(rset.getString("firstname"));
						worker.setLastName(rset.getString("lastname"));
						worker.setJob(rset.getString("job"));
						worker.setParkName(rset.getString("parkName"));
						worker.setPhone(rset.getInt("numberphone"));
						worker.setID(rset.getString("ID"));
						worker.setPassword(rset.getString("password"));
						message = new MessageCS(MessageType.LOGIN_WORKER, worker);
						client.sendToClient(message);
					} else {
						// fail case - we found a worker with this id but his password is wrong !//
						message = new MessageCS(MessageType.ERROR_LOGIN, "Wrong Password - Try Again");
						client.sendToClient(message);
					}
				}
				break;
			case LOGIN_VISITOR:

				// if is subscriber:
				query = "SELECT * FROM subscriber WHERE subscriber_id = '" + message.subscriber.getSubscriber_id() + "'"
						+ ";";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {// 1

					// if is not subscriber -> if is owner:
					query = "SELECT * FROM orders WHERE ownerId = '" + message.subscriber.getSubscriber_id() + "'"
							+ ";";
					rset = stmt.executeQuery(query);

					if (!rset.next()) {// 2
						// Is new visitor
						message = new MessageCS(MessageType.LOGIN_NEW_VISITOR, "This is new visitor");
						client.sendToClient(message);
						break;

					} else {
						// 2//Is owner of order:
						Order order = new Order();
						order.setOrderId(rset.getInt("orderId"));
						order.setParkName(rset.getString("parkName"));
						order.setDate(rset.getString("date"));
						order.setTime(rset.getString("time"));
						order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
						order.setEmail(rset.getString("email"));
						order.setOwnerId(rset.getInt("ownerId"));
						order.setTypeOfOrder(rset.getString("typeOfOrder"));
						order.setStatus(rset.getString("status"));

						message = new MessageCS(MessageType.LOGIN_OWNER_ORDER, "This is owner of order",
								order);
						client.sendToClient(message);
						break;

					}
				}
				// 1//Is Subscriber:
				else {
					// If is subscriber and owner of order?
					Subscriber subscriber = new Subscriber();
					subscriber.setSubscriber_id(rset.getInt("subscriber_id"));
					subscriber.setFirstname(rset.getString("firstname"));
					subscriber.setLastname(rset.getString("lastname"));
					subscriber.setEmail(rset.getString("email"));
					subscriber.setSubscriber_amount(rset.getInt("subscriber_amount"));
					subscriber.setType(rset.getString("type"));

					query = "SELECT * FROM orders WHERE ownerId = '" + message.subscriber.getSubscriber_id() + "'"
							+ ";";
					rset = stmt.executeQuery(query);

					if (!rset.next()) { // is subscriber without order
						message = new MessageCS(MessageType.LOGIN_SUBSCRIBER_WITHOUT_ORDER,
								"This is subsriber without order", subscriber);
						client.sendToClient(message);
						break;
					} else {
						// This subscriber with order:
						Order order = new Order();

						order.setOrderId(rset.getInt("orderId"));
						order.setParkName(rset.getString("parkName"));
						order.setDate(rset.getString("date"));
						order.setTime(rset.getString("time"));
						order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
						order.setEmail(rset.getString("email"));
						order.setOwnerId(rset.getInt("ownerId"));
						order.setTypeOfOrder(rset.getString("typeOfOrder"));
						order.setStatus(rset.getString("status"));

						message = new MessageCS(MessageType.LOGIN_SUBSCRIBER_WITH_ORDER,
								"This is subsriber with order", subscriber, order);
						client.sendToClient(message);
						break;
					}
				}

				/********************************************************
				 * Dani Tanya
				 ******************************************/

			case UPDATE_MAX_VISITOR:
				String maxVisitors = "Max Visitors";
				query = "UPDATE requestparameter SET variable = \"";
				query += message.string;
				query += "\" WHERE parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and type = \"";
				query += maxVisitors;
				query += "\";";

				stmt.executeUpdate(query);

				MessageCS message0 = new MessageCS(MessageType.REQUEST_SENT, "Request sent");
				client.sendToClient(message0);
				break;

			case UPDATE_VISITING_TIME:
				String visitingTime = "Visiting Time";
				query = "UPDATE requestparameter SET variable = \"";
				query += message.string;
				query += "\" WHERE parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and type = \"";
				query += visitingTime;
				query += "\";";
				MessageCS message1 = new MessageCS(MessageType.REQUEST_SENT, "Request sent");
				stmt.executeUpdate(query);
				client.sendToClient(message1);
				break;

			case UPDATE_MAX_ORDERS:
				String maxOrders = "Max Orders";
				query = "UPDATE requestparameter SET variable = \"";
				query += message.string;
				query += "\" WHERE parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and type = \"";
				query += maxOrders;
				query += "\";";
				stmt.executeUpdate(query);
				MessageCS message2 = new MessageCS(MessageType.REQUEST_SENT, "Request sent");
				client.sendToClient(message2);
				break;

			case UPDATE_DISCOUNT:

				MessageCS message3;
				query = "select * from discount where parkName = \"";
				query += message.Worker.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				int flag = 0;
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				Date From = sdformat.parse(message.string1);
				Date To = sdformat.parse(message.string2);
				Date FromSql;
				Date ToSql;
				while (rset.next()) {
					FromSql = sdformat.parse(rset.getString(4));
					ToSql = sdformat.parse(rset.getString(5));
					if ((FromSql.before(From) && ToSql.after(From)) || From.equals(FromSql) || From.equals(ToSql))
						flag = 1;

					if ((FromSql.before(To) && ToSql.after(To)) || To.equals(FromSql) || To.equals(ToSql))
						flag = 1;

					if ((From.before(FromSql) && From.before(ToSql)) && (To.after(FromSql) && To.after(ToSql)))
						flag = 1;

				}

				if (flag == 0) {
					int max = 0;

					query = "SELECT MAX(number) FROM discount;";
					rset = stmt.executeQuery(query);
					if (rset.next()) {
						max = rset.getInt(1) + 1;
					} else {
						max = 0;
					}

					query = "INSERT INTO discount VALUES (\"";
					query += max;
					query += "\",\"";
					query += message.Worker.getParkName();
					query += "\",\"";
					query += message.string;
					query += "\",\"";
					query += message.string1;// from
					query += "\",\"";
					query += message.string2;// to
					query += "\",\"";
					query += "waiting";
					query += "\");";
					stmt.executeUpdate(query);

					message3 = new MessageCS(MessageType.DISCOUNT_UPDATED,
							"Discount request sent and wating for update");
					client.sendToClient(message3);
				} else {

					message3 = new MessageCS(MessageType.DISCOUNT_UPDATED, "Discount Time already exixsts");
					client.sendToClient(message3);
				}
				break;

			case REQUEST_PARAMETERS:
				String send = "";

				query = "select variable  from requestparameter WHERE parkName = \"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				while (rset.next())
					send += rset.getString(1) + ",";

				query = "select * from park WHERE parkName = \"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				rset.next();
				send += rset.getString(2) + ",";
				send += rset.getString(3) + ",";
				send += rset.getString(4) + ",";

				MessageCS message4 = new MessageCS(MessageType.SHOW_VALUES, send);
				client.sendToClient(message4);

				break;

			case CHANGE_MAX_ORDERS:
				MessageCS message5 = new MessageCS(MessageType.ACTION_SUCCESSFULL, "Action Successfull");
				if (message.string1.equals("Yes")) {
					// update max orders in park to max orders in requestparameter
					query = "UPDATE park SET maxOrders  = \"";
					query += message.string;
					query += "\" WHERE parkName = \"";
					query += message.string2;
					query += "\";";
					stmt.executeUpdate(query);

					client.sendToClient(message5);
				}
				// update max orders in requestparameter to null
				String maxOrders1 = "Max Orders";
				String empty = "";
				query = "UPDATE requestparameter SET variable  = \"";
				query += empty;
				query += "\" WHERE parkName = \"";
				query += message.string2;
				query += "\" and type = \"";
				query += maxOrders1;
				query += "\";";
				stmt.executeUpdate(query);
				client.sendToClient(message5);

				break;

			case CHANGE_MAX_VISITOR:
				MessageCS message6 = new MessageCS(MessageType.ACTION_SUCCESSFULL, "Action Successfull");
				if (message.string1.equals("Yes")) {
					// update max orders in park to max orders in requestparameter
					query = "UPDATE park SET maxVisitor  = \"";
					query += message.string;
					query += "\" WHERE parkName = \"";
					query += message.string2;
					query += "\";";
					stmt.executeUpdate(query);

					Date curr = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

					query = "delete from maxvisitorschange where park = \"";
					query += message.string2;
					query += "\" and date = \"";
					query += formatter.format(curr);
					query += "\";";
					stmt.executeUpdate(query);

					query = "insert into maxvisitorschange (park, date, maxvisitors) values (\"";
					query += message.string2;
					query += "\",\"";
					query += formatter.format(curr);
					query += "\",\"";
					query += message.string;
					query += "\");";
					stmt.executeUpdate(query);
				}
				// update max orders in requestparameter to null
				String maxVisitor = "Max Visitors";
				String empty1 = "";
				query = "UPDATE requestparameter SET variable  = \"";
				query += empty1;
				query += "\" WHERE parkName = \"";
				query += message.string2;
				query += "\" and type = \"";
				query += maxVisitor;
				query += "\";";
				stmt.executeUpdate(query);

				client.sendToClient(message6);

				break;

			case CHANGE_VISITNG_TIME:
				MessageCS message7 = new MessageCS(MessageType.ACTION_SUCCESSFULL, "Action Successfull");
				if (message.string1.equals("Yes")) {
					// update max orders in park to max orders in requestparameter
					query = "UPDATE park SET maxVisitTime  = \"";
					query += message.string;
					query += "\" WHERE parkName = \"";
					query += message.string2;
					query += "\";";
					stmt.executeUpdate(query);

					client.sendToClient(message7);
				}
				// update max orders in requestparameter to null
				String maxVisitTime = "Visiting Time";
				String empty2 = "";
				query = "UPDATE requestparameter SET variable  = \"";
				query += empty2;
				query += "\" WHERE parkName = \"";
				query += message.string2;
				query += "\" and type = \"";
				query += maxVisitTime;
				query += "\";";
				stmt.executeUpdate(query);
				client.sendToClient(message7);

				break;

			case SHOW_DISCOUNTS:

				String str = "";
				query = "select * from discount";
				rset = stmt.executeQuery(query);
				while (rset.next()) {
					str += rset.getString(1) + "," + rset.getString(2) + "," + rset.getString(3) + ","
							+ rset.getString(4) + "," + rset.getString(5) + "," + rset.getString(6);
					str += "=";
				}

				MessageCS message8 = new MessageCS(MessageType.SHOW, str);
				client.sendToClient(message8);
				break;

			case CHANGE_DISCOUNT:
				String s1 = "Approved";
				query = "UPDATE discount SET status  = \"";
				query += s1;
				query += "\" WHERE number = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message9 = new MessageCS(MessageType.CHANGE_DISCOUNT, "Changed");
				client.sendToClient(message9);

				break;

			case DELETE_DISCOUNT:
				// DELETE FROM somelog WHERE user = 'jcole'
				query = "delete from discount where number = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);
				MessageCS message10 = new MessageCS(MessageType.DELETE_DISCOUNT, "Deleted");
				client.sendToClient(message10);
				break;

			case SHOW_CANCEL_REPORT:
				String s3 = "";
				query = "select * from orders where status = \"Canceled\" and parkName = \"";
				query += message.string;
				query += "\" and date >= \"";
				query += message.string1;
				query += "\" and date <= \"";
				query += message.string2;
				query += "\";";

				rset = stmt.executeQuery(query);

				while (rset.next()) {
					s3 += rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(9);
					s3 += "=";

				}

				query = "select * from orders where status = \"not arrived\" and parkName = \"";
				query += message.string;
				query += "\";";

				rset = stmt.executeQuery(query);

				while (rset.next()) {
					s3 += rset.getString(3) + "," + rset.getString(5) + "," + rset.getString(9);
					s3 += "=";

				}
				MessageCS message11 = new MessageCS(MessageType.SHOW_CANCEL_REPORT, s3);
				client.sendToClient(message11);

				break;

			case REPORT_VISITORS:
				String ans = "";
				query = "select sum(amount) from visits where type = \"Single\"";
				query += " and parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and date >= \"";
				query += message.string;
				query += "\" and date <= \"";
				query += message.string1;
				query += "\";";
				rset = stmt.executeQuery(query);
				rset.next();
				ans += rset.getString(1) + ",";

				query = "select sum(amount) from visits where type = \"Guide\"";
				query += " and parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and date >= \"";
				query += message.string;
				query += "\" and date <= \"";
				query += message.string1;
				query += "\";";
				rset = stmt.executeQuery(query);
				rset.next();
				ans += rset.getString(1) + ",";

				query = "select sum(amount) from visits where type = \"Family\"";
				query += " and parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and date >= \"";
				query += message.string;
				query += "\" and date <= \"";
				query += message.string1;
				query += "\";";
				rset = stmt.executeQuery(query);
				rset.next();
				ans += rset.getString(1);

				MessageCS message12 = new MessageCS(MessageType.REPORT_VISITORS, ans);
				client.sendToClient(message12);

				break;

			case REPORT_INCOME:
				String s4 = "";
				query = "select * from visits where parkName = \"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				while (rset.next()) {
					s4 += rset.getString(5) + "," + rset.getString(9);
					s4 += "=";

				}
				MessageCS message13 = new MessageCS(MessageType.REPORT_INCOME, s4);
				client.sendToClient(message13);

				break;

			case REPORT_USAGE:
				String s5 = "";
				query = "select * from visits where parkName = \"";
				query += message.Worker.getParkName();
				query += "\" and date = \"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				while (rset.next()) {
					s5 += rset.getString(4) + "," + rset.getString(6) + "," + rset.getString(7);
					s5 += "=";

				}
				query = "select maxvisitors from maxvisitorschange where date <= \"";
				query += message.string;
				query += "\" and park = \"";
				query += message.Worker.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				String max = "";
				while (rset.next()) {
					max = rset.getString(1);
				}

				MessageCS message14 = new MessageCS(MessageType.REPORT_USAGE, s5, max);
				client.sendToClient(message14);
				break;

			case REPORT_VISITING:
				String s6 = "";
				query = "select * from visits where parkName = \"";
				query += message.string2;
				query += "\" and date >= \"";
				query += message.string;
				query += "\" and date <= \"";
				query += message.string1;
				query += "\";";

				rset = stmt.executeQuery(query);

				while (rset.next()) {
					s6 += rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(6);
					s6 += "=";

				}
				message = new MessageCS(MessageType.REPORT_VISITING, s6);
				client.sendToClient(message);
				break;

			case REPORT_STAYING:
				String s7 = "";
				query = "select * from visits where parkName = \"";
				query += message.string2;
				query += "\" and date >= \"";
				query += message.string;
				query += "\" and date <= \"";
				query += message.string1;
				query += "\";";
				rset = stmt.executeQuery(query);
				while (rset.next()) {
					s7 += rset.getString(3) + "," + rset.getString(4) + "," + rset.getString(8);
					s7 += "=";

				}
				MessageCS message16 = new MessageCS(MessageType.REPORT_STAYING, s7);
				client.sendToClient(message16);
				break;

			/********************************************************
			 * Dani Tanya
			 ******************************************/

			/********************************************************
			 * Omer Matan
			 ******************************************/

			case CheckPlace: {
				// maybe we need to change it , its wrong! 24/12/2020//
				boolean check = checkIfDateIsOpen(message.order, message.order.getDate(), message.order.getTime());
				MessageCS message = new MessageCS(MessageType.CheckPlace, check);
				client.sendToClient(message);
			}
				break;

			case CheckOtherDates: {
				// maybe we need to change it , its wrong! 24/12/2020//
				ArrayList<DateOrder> orderDateslist = new ArrayList<DateOrder>();
				try {
					LocalDate startDate = message.date.plusDays(1);
					LocalDate endDate = message.date.plusDays(9);
					for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
						for (int i = 9; i < 19; i++) {
							String date_str = date.toString();
							String time_str = String.format("%02d:%02d", i, 0);
							if (checkIfDateIsOpen(message.order, date_str, time_str)) {
								String val = date_str + " " + time_str;
								DateOrder date_order = new DateOrder(val);
								orderDateslist.add(date_order);
							}
						}
					}
					if (orderDateslist.size() == 0) {
						DateOrder date_order = new DateOrder("No Available Dates ");
						DateOrder date_order2 = new DateOrder("In 2 Weeks Ahead ");
						DateOrder date_order3 = new DateOrder("Come Again Next Year ");
						orderDateslist.add(date_order);
						orderDateslist.add(date_order2);
						orderDateslist.add(date_order3);

					}
				} catch (Exception e) {
					System.out.println(e);
				}
				MessageCS message = new MessageCS(MessageType.CheckOtherDates, orderDateslist);
				client.sendToClient(message);
			}
				break;

			case GetVisitorType: {
				query = "SELECT * FROM orders WHERE ownerId = '" + message.order.getOwnerId() + "'" + ";";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					MessageCS message = new MessageCS(MessageType.GetVisitorType, false);
					client.sendToClient(message);
				} else {
					MessageCS message = new MessageCS(MessageType.GetVisitorType, true);
					client.sendToClient(message);
				}
			}
				break;

			case Create_Order: {

				query = "INSERT INTO orders (parkName, date, time, numberOfVisitors, email, ownerId, typeOfOrder, status,confirmed,paynow) VALUES (\"";
				query += message.order.getParkName();
				query += "\",\"";
				query += message.order.getDate();
				query += "\",\"";
				query += message.order.getTime();
				query += "\",\"";
				query += message.order.getNumberOfVisitors();
				query += "\",\"";
				query += message.order.getEmail();
				query += "\",\"";
				query += message.order.getOwnerId();
				query += "\",\"";
				query += message.order.getTypeOfOrder();
				query += "\",\"";
				query += message.order.getStatus();
				query += "\",\"";
				query += "no"; // this no is for arrivel confirmation //
				query += "\",\"";
				query += message.order.getPaynow();
				query += "\");";

				stmt.executeUpdate(query);
				query = "SELECT MAX(orderId) FROM orders";
				rset = stmt.executeQuery(query);
				int orderId = 0;
				while (rset.next())
					orderId = rset.getInt(1);
				MessageCS message = new MessageCS(MessageType.Create_Order, orderId);
				client.sendToClient(message);
			}
				break;

			case Enter_WAITINGLIST: {

				query = "INSERT INTO waitinglist (orderId, parkName, date, time, numberOfVisitors, email, ownerId, typeOfOrder,show_b) VALUES (\"";
				query += message.order.getOrderId();
				query += "\",\"";
				query += message.order.getParkName();
				query += "\",\"";
				query += message.order.getDate();
				query += "\",\"";
				query += message.order.getTime();
				query += "\",\"";
				query += message.order.getNumberOfVisitors();
				query += "\",\"";
				query += message.order.getEmail();
				query += "\",\"";
				query += message.order.getOwnerId();
				query += "\",\"";
				query += message.order.getTypeOfOrder();
				query += "\",\"";
				query += "no";
				query += "\");";
				stmt.executeUpdate(query);
				MessageCS message = new MessageCS(MessageType.Enter_WAITINGLIST, "Enter waitinglist success!");
				client.sendToClient(message);

			}
				break;

			case CancelOrder: {
				String orderStatus = getOrderStatus(message.order);

				query = "UPDATE orders SET status  = \"";
				query += "Canceled";
				query += "\" WHERE orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);

				query = "delete from waitinglist where orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);

				if (orderStatus.equals("not arrived")) {
					new Thread(() -> {
						handleWaitingList(message.order);
					}).start();
				}

				MessageCS message = new MessageCS(MessageType.CancelOrder, "");
				client.sendToClient(message);
				break;

			}

			case CheckTopWaitingListAndPlace: {
				query = "select show_b from waitinglist where orderId = " + "'" + message.order.getOrderId() + "'"
						+ ";";
				rset = stmt.executeQuery(query);

				if (!rset.next()) {
					MessageCS message21 = new MessageCS(MessageType.CheckTopWaitingListAndPlace);
					client.sendToClient(message21);

				} else {
					String show = rset.getString("show_b");
					boolean bool = show.equals("yes") ? true : false;
					MessageCS message30 = new MessageCS(MessageType.CheckTopWaitingListAndPlace, bool);
					client.sendToClient(message30);
				}

				break;
			}

			case CheckIfConfirmArrivel: {
				query = "select confirmed from orders where orderId = " + "'" + message.order.getOrderId() + "'" + ";";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					MessageCS message21 = new MessageCS(MessageType.CheckIfConfirmArrivel, false);
					client.sendToClient(message21);
				} else {
					String val = rset.getString("confirmed");
					boolean bool = val.equals("show") ? true : false;
					MessageCS message30 = new MessageCS(MessageType.CheckIfConfirmArrivel, bool);
					client.sendToClient(message30);
				}
				break;
			}
			case ChangeStatusOrder: {
				query = "UPDATE orders SET status  = \"";
				query += "not arrived";
				query += "\" WHERE orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);

				query = "delete from waitinglist where orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);
				MessageCS message = new MessageCS(MessageType.ChangeStatusOrder, "The order from waitinglist Deleted");
				client.sendToClient(message);

				break;
			}

			case ArrivelConfirm: {
				query = "UPDATE orders SET confirmed  = \"";
				query += "yes";
				query += "\" WHERE orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);
				MessageCS message = new MessageCS(MessageType.ArrivelConfirm, "Arrivel Confirm");
				client.sendToClient(message);
				break;
			}

			case PayNow: {

				double full_price = 0;
				double price_discount = 0;
				double totalprice = 0;
				double parkPrice = 15.0;

				String string = message.order.getPaynow();
				String[] parts = string.split(",");
				full_price = Double.parseDouble(parts[0]);
				price_discount = Double.parseDouble(parts[1]);
				totalprice = Double.parseDouble(parts[2]);
				price_discount += parkPrice * 0.12 * (message.order.getNumberOfVisitors() - 1);
				totalprice = full_price - price_discount;

				String discount = String.format("%.2f", price_discount);
				String total_price = String.format("%.2f", totalprice);
				String val = parts[0] + "," + discount + "," + total_price;

				message.order.setPaynow(val);

				query = "UPDATE orders SET paynow  = \"";
				query += val;
				query += "\" WHERE orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);
				MessageCS message = new MessageCS(MessageType.PayNow, val);
				client.sendToClient(message);
				break;
			}

			case MyOrders: {
				ArrayList<Order> myOrders = new ArrayList<Order>();
				String OwnerID = message.string;
				query = "SELECT * FROM orders WHERE ownerId = '" + OwnerID + "'" + ";";
				rset = stmt.executeQuery(query);
				while (rset.next()) {
					Order order = new Order();
					order.setOrderId(rset.getInt("orderId"));
					order.setParkName(rset.getString("parkName"));
					order.setDate(rset.getString("date"));
					order.setTime(rset.getString("time"));
					order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
					order.setEmail(rset.getString("email"));
					order.setOwnerId(rset.getInt("ownerId"));
					order.setTypeOfOrder(rset.getString("typeOfOrder"));
					order.setStatus(rset.getString("status"));
					order.setPaynow(rset.getString("paynow"));
					String string = rset.getString("paynow");
					String[] parts = string.split(",");
					order.setFull_price(parts[0]);
					order.setDiscount(parts[1]);
					order.setTotal_price(parts[2]);
					myOrders.add(order);
				}

				MessageCS message = new MessageCS(MessageType.MyOrders, OwnerID, myOrders);
				client.sendToClient(message);
			}

				break;

			case CheckDiscountForOrder: {
				String discount = "0";
				query = "SELECT discount FROM discount WHERE " + "`" + "from" + "`" + " <= " + "'"
						+ message.order.getDate() + "'" + " and " + "`" + "to" + "`" + " >= " + "'"
						+ message.order.getDate() + "'" + " and parkName = " + "'" + message.order.getParkName() + "'"
						+ " and status = " + "'" + "Approved" + "'" + ";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					discount = rset.getString(1);
				}
				MessageCS message = new MessageCS(MessageType.CheckDiscountForOrder, discount);
				client.sendToClient(message);
			}
				break;

			case CardReaderCheckEntrance: {
				Subscriber subscriber = new Subscriber();
				ArrayList<Order> myOrders = new ArrayList<Order>();
				String discount = "0";
				Integer OwnerID = message.subscriber.getSubscriber_id();

				// check if its subscriber//
				query = "SELECT * FROM subscriber WHERE subscriber_id = '" + message.subscriber.getSubscriber_id() + "'"
						+ ";";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					subscriber = null;
				} else {
					subscriber.setSubscriber_id(rset.getInt("subscriber_id"));
					subscriber.setFirstname(rset.getString("firstname"));
					subscriber.setLastname(rset.getString("lastname"));
					subscriber.setEmail(rset.getString("email"));
					subscriber.setSubscriber_amount(rset.getInt("subscriber_amount"));
					subscriber.setType(rset.getString("type"));
				}

				// check orders for today + park//

				query = "SELECT * FROM orders WHERE ownerId = '" + OwnerID.toString() + "'" + " and parkName =" + "'"
						+ message.string + "'" + " and date =" + "'" + LocalDate.now() + "'"
						+ " and status = \"not arrived\" " + ";";

				rset = stmt.executeQuery(query);

				while (rset.next()) {
					Order order = new Order();
					order.setOrderId(rset.getInt("orderId"));
					order.setParkName(rset.getString("parkName"));
					order.setDate(rset.getString("date"));
					order.setTime(rset.getString("time"));
					order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
					order.setEmail(rset.getString("email"));
					order.setOwnerId(rset.getInt("ownerId"));
					order.setTypeOfOrder(rset.getString("typeOfOrder"));
					order.setStatus(rset.getString("status"));
					order.setPaynow(rset.getString("paynow"));
					String string = rset.getString("paynow");
					String[] parts = string.split(",");
					order.setFull_price(parts[0]);
					order.setDiscount(parts[1]);
					order.setTotal_price(parts[2]);
					myOrders.add(order);
				}

				// check the discount//

				query = "SELECT discount FROM discount WHERE " + "`" + "from" + "`" + " <= " + "'" + LocalDate.now()
						+ "'" + " and " + "`" + "to" + "`" + " >= " + "'" + LocalDate.now() + "'" + " and parkName = "
						+ "'" + message.string + "'" + " and status = " + "'" + "Approved" + "'" + ";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					discount = rset.getString(1);
				}

				MessageCS message = new MessageCS(MessageType.CardReaderCheckEntrance, discount, myOrders, subscriber);
				client.sendToClient(message);
			}
				break;

			case CardReaderCheckCapicity: {
				int NoOrderCapacity = 0;
				int NoOrderInPark = 0;

				query = "SELECT maxVisitor , maxOrders , currentNoOrderVisitor from park";
				query += " WHERE parkName =\"";
				query += message.order.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					NoOrderCapacity = rset.getInt(1) - rset.getInt(2);
					NoOrderInPark = rset.getInt(3);
				}
				MessageCS message = new MessageCS(MessageType.CardReaderCheckCapicity, NoOrderCapacity, NoOrderInPark);
				client.sendToClient(message);

			}
				break;

			case CardReaderEnterToPark: {
				query = "INSERT INTO visits (parkName, type, amount, date, enterTime,payment,hasOrder,orderID,ownerID) VALUES (\"";
				query += message.order.getParkName();
				query += "\",\"";
				query += message.order.getTypeOfOrder();
				query += "\",\"";
				query += message.order.getNumberOfVisitors();
				query += "\",\"";
				query += message.order.getDate();
				query += "\",\"";
				query += message.order.getTime();
				query += "\",\"";
				query += message.string;
				query += "\",\"";
				query += message.string1;
				query += "\",\"";
				query += message.string2;
				query += "\",\"";
				query += message.order.getOwnerId();
				query += "\");";

				stmt.executeUpdate(query);
				query = "SELECT MAX(id) FROM visits";
				rset = stmt.executeQuery(query);
				int id = 0;
				while (rset.next())
					id = rset.getInt(1);

				int NoOrderInPark = 0;

				// check if its visitor without order !!!!!!!!!! //
				if (message.string1.equals("no")) {

					query = "SELECT currentNoOrderVisitor from park";
					query += " WHERE parkName =\"";
					query += message.order.getParkName();
					query += "\";";
					rset = stmt.executeQuery(query);
					if (rset.next()) {
						NoOrderInPark = rset.getInt(1);
					}
					NoOrderInPark = message.order.getNumberOfVisitors() + NoOrderInPark;

					query = "UPDATE park SET currentNoOrderVisitor  = \"";
					query += NoOrderInPark;
					query += "\" WHERE parkName = \"";
					query += message.order.getParkName();
					query += "\";";
					stmt.executeUpdate(query);
				} else {
					query = "UPDATE orders SET status  = \"";
					query += "arrived";
					query += "\" WHERE orderId = \"";
					query += Integer.parseInt(message.string2); // this is order id = message.string2
					query += "\";";
					stmt.executeUpdate(query);

					query = "delete from waitinglist where orderId = \"";
					query += Integer.parseInt(message.string2);
					query += "\";";
					stmt.executeUpdate(query);

				}

				// update currentVisitorAmountInPark for all type of visitors //
				int currentVisitorAmountInPark = 0;
				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += message.order.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					currentVisitorAmountInPark = rset.getInt(1);
				}
				currentVisitorAmountInPark = message.order.getNumberOfVisitors() + currentVisitorAmountInPark;

				query = "UPDATE park SET currentVisitorAmountInPark  = \"";
				query += currentVisitorAmountInPark;
				query += "\" WHERE parkName = \"";
				query += message.order.getParkName();
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message = new MessageCS(MessageType.CardReaderEnterToPark, id, currentVisitorAmountInPark);
				client.sendToClient(message);
			}
				break;

			// this is check for exit with card reader //
			case CardReaderCheckVisitId: {

				query = "SELECT id,exitTime from visits";
				query += " WHERE ownerID = '";
				query += message.string;
				query += "' and isnull(exitTime) and date = '";
				query += LocalDate.now().toString();
				query += "' ;";
				rset = stmt.executeQuery(query);
				int visit_id = 0;
				if (!rset.next()) {
					MessageCS message = new MessageCS(MessageType.CardReaderCheckVisitId, visit_id, false);
					client.sendToClient(message);
				} else {
					visit_id = rset.getInt(1);
					if (rset.getString(2) != null) {
						if (rset.getString(2).isEmpty()) {
							MessageCS message = new MessageCS(MessageType.CardReaderCheckVisitId, visit_id, true);
							client.sendToClient(message);
						} else {
							MessageCS message = new MessageCS(MessageType.CardReaderCheckVisitId, visit_id, false);
							client.sendToClient(message);
						}
					}
					if (rset.getString(2) == null) {
						MessageCS message = new MessageCS(MessageType.CardReaderCheckVisitId, visit_id, true);
						client.sendToClient(message);
					}

				}
			}
				break;

			// this is exit with card reader //
			case CardReaderExitFromPark: {
				String enterTime = "";
				String exitTime = "";
				String visitTime = "";
				String hasOrder = "";
				String parkName = "";
				int amount = 0;
				query = "SELECT enterTime , amount , hasOrder , parkName from visits";
				query += " WHERE id =\"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					enterTime = rset.getString(1);
					amount = rset.getInt(2);
					hasOrder = rset.getString(3);
					parkName = rset.getString(4);
				}
				LocalTime timenow = LocalTime.now();
				exitTime = String.format("%02d:%02d:%02d", timenow.getHour(), timenow.getMinute(), 0);

				// update the exitTime time//
				query = "UPDATE visits SET exitTime  = \"";
				query += exitTime;
				query += "\" WHERE id = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);

				// calc the visitTime + update it//
				LocalTime enTime = LocalTime.parse(enterTime);
				LocalTime exTime = LocalTime.parse(exitTime);

				Duration d = Duration.between(enTime, exTime);

				long hours = d.toHours();
				int minutes = (int) (d.toMinutes() % 60);

				visitTime = String.format("%02d:%02d:%02d", hours, minutes, 0);

				query = "UPDATE visits SET visitTime  = \"";
				query += visitTime;
				query += "\" WHERE id = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);

				// update park visitors amount y//

				int NoOrderInPark = 0;
				int currentVisitorAmountInPark = 0;

				// update visitors without order capicitiy//
				if (hasOrder.equals("no")) {
					query = "SELECT currentNoOrderVisitor from park";
					query += " WHERE parkName =\"";
					query += parkName;
					query += "\";";
					rset = stmt.executeQuery(query);
					if (rset.next()) {
						NoOrderInPark = rset.getInt(1);
					}
					NoOrderInPark = NoOrderInPark - amount;

					query = "UPDATE park SET currentNoOrderVisitor  = \"";
					query += NoOrderInPark;
					query += "\" WHERE parkName = \"";
					query += parkName;
					query += "\";";
					stmt.executeUpdate(query);
				}

				// update all visitors//

				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += parkName;
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					currentVisitorAmountInPark = rset.getInt(1);
				}
				currentVisitorAmountInPark = currentVisitorAmountInPark - amount;

				query = "UPDATE park SET currentVisitorAmountInPark  = \"";
				query += currentVisitorAmountInPark;
				query += "\" WHERE parkName = \"";
				query += parkName;
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message = new MessageCS(MessageType.CardReaderExitFromPark, currentVisitorAmountInPark,
						parkName);
				client.sendToClient(message);

			}
				break;
			/********************************************************
			 * Omer Matan
			 ******************************************/

			/********************************************************
			 * Tomer
			 ******************************************/

			case CREATE_SUBSCRIBER: {
				query = "INSERT INTO subscriber (firstname, lastname, email, subscriber_amount, type) VALUES (\"";
				query += message.subscriber.getFirstname();
				query += "\",\"";
				query += message.subscriber.getLastname();
				query += "\",\"";
				query += message.subscriber.getEmail();
				query += "\",\"";
				query += message.subscriber.getSubscriber_amount();
				query += "\",\"";
				query += message.subscriber.getType();
				query += "\");";

				stmt.executeUpdate(query);
				query = "SELECT MAX(subscriber_id) FROM subscriber";
				rset = stmt.executeQuery(query);
				int subscriber_id = 0;
				while (rset.next())
					subscriber_id = rset.getInt(1);
				MessageCS message = new MessageCS(MessageType.CREATE_SUBSCRIBER, subscriber_id);
				client.sendToClient(message);
			}
				break;

			case SEARCH_ORDER: {

				int num1 = message.num;
				String discount;
				query = "SELECT * FROM orders WHERE orderId = " + num1 + " and status = \"not arrived\""
						+ "and parkName = '" + message.Worker.getParkName() + "';";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					Order order = new Order();

					MessageCS message = new MessageCS(MessageType.SEARCH_ORDER, "not found order", order);
					client.sendToClient(message);
				} else {
					Order order = new Order();
					order.setOrderId(rset.getInt("orderId"));
					order.setParkName(rset.getString("parkName"));
					order.setDate(rset.getString("date"));
					order.setTime(rset.getString("time"));
					order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
					order.setEmail(rset.getString("email"));
					order.setOwnerId(rset.getInt("ownerId"));
					order.setTypeOfOrder(rset.getString("typeOfOrder"));
					order.setPaynow(rset.getString("paynow"));
					String string = rset.getString("paynow");
					String[] parts = string.split(",");
					order.setFull_price(parts[0]);
					order.setDiscount(parts[1]);
					order.setTotal_price(parts[2]);
					order.setOwnerId(rset.getInt("ownerID"));
					query = "SELECT discount FROM discount WHERE " + "`" + "from" + "`" + " <= " + "'" + LocalDate.now()
							+ "'" + " and " + "`" + "to" + "`" + " >= " + "'" + LocalDate.now() + "'"
							+ " and parkName = " + "'" + order.getParkName() + "'" + " and status = " + "'" + "Approved"
							+ "'" + ";";
					rset = stmt.executeQuery(query);
					if (rset.next()) {
						discount = rset.getString(1);

						MessageCS message = new MessageCS(MessageType.SEARCH_ORDER, discount, order);
						client.sendToClient(message);
					}

					else {
						MessageCS message = new MessageCS(MessageType.SEARCH_ORDER, "0", order);
						client.sendToClient(message);
					}
				}

			}
				break;

			case Enter_to_park: {

				LocalTime timenow = LocalTime.now();
				String enterTime = String.format("%02d:%02d:%02d", timenow.getHour(), timenow.getMinute(), 0);

				query = "INSERT INTO visits (parkName, type, amount, date, enterTime,payment,hasOrder,orderID,ownerID) VALUES (\"";
				query += message.order.getParkName();
				query += "\",\"";
				query += message.order.getTypeOfOrder();
				query += "\",\"";
				query += message.order.getNumberOfVisitors();
				query += "\",\"";
				query += message.order.getDate();
				query += "\",\"";
				query += enterTime;
				query += "\",\"";
				query += message.string;
				query += "\",\"";
				query += "yes";
				query += "\",\"";
				query += message.order.getOrderId();
				query += "\",\"";
				query += message.order.getOwnerId();
				query += "\");";

				stmt.executeUpdate(query);
				query = "SELECT MAX(id) FROM visits";
				rset = stmt.executeQuery(query);
				int id = 0;
				while (rset.next())
					id = rset.getInt(1);

				query = "UPDATE orders SET status  = \"";
				query += "arrived";
				query += "\" WHERE orderId = \"";
				query += message.order.getOrderId(); // this is order id = message.string2
				query += "\";";
				stmt.executeUpdate(query);

				query = "delete from waitinglist where orderId = \"";
				query += message.order.getOrderId();
				query += "\";";
				stmt.executeUpdate(query);

				int currentVisitorAmountInPark = 0;
				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += message.order.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					currentVisitorAmountInPark = rset.getInt(1);
				}
				currentVisitorAmountInPark = message.order.getNumberOfVisitors() + currentVisitorAmountInPark;

				query = "UPDATE park SET currentVisitorAmountInPark  = \"";
				query += currentVisitorAmountInPark;
				query += "\" WHERE parkName = \"";
				query += message.order.getParkName();
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message = new MessageCS(MessageType.Enter_to_park, id, currentVisitorAmountInPark);
				client.sendToClient(message);

			}
				break;

			case CHECK_AMOUNT: {
				int NoOrderCapacity = 0;
				int NoOrderInPark = 0;
				int noOrderPlaceAvailble = 0;
				boolean bool;
				query = "SELECT maxVisitor , maxOrders , currentNoOrderVisitor from park";
				query += " WHERE parkName =\"";
				query += message.Worker.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					NoOrderCapacity = rset.getInt(1) - rset.getInt(2);
					NoOrderInPark = rset.getInt(3);
				}
				noOrderPlaceAvailble = NoOrderCapacity - NoOrderInPark;
				bool = noOrderPlaceAvailble >= message.num;

				MessageCS message = new MessageCS(MessageType.CHECK_AMOUNT, bool);
				client.sendToClient(message);
			}
				break;

			case ACCSESS_CHECK_ID: {
				Subscriber subscriber = new Subscriber();
				String discount = "0";
				Integer OwnerID = message.num;

				// check if its subscriber//
				query = "SELECT * FROM subscriber WHERE subscriber_id = '" + OwnerID + "'" + ";";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					subscriber = null;
				} else {
					subscriber.setSubscriber_id(rset.getInt("subscriber_id"));
					subscriber.setFirstname(rset.getString("firstname"));
					subscriber.setLastname(rset.getString("lastname"));
					subscriber.setEmail(rset.getString("email"));
					subscriber.setSubscriber_amount(rset.getInt("subscriber_amount"));
					subscriber.setType(rset.getString("type"));
				}

				query = "SELECT discount FROM discount WHERE " + "`" + "from" + "`" + " <= " + "'" + LocalDate.now()
						+ "'" + " and " + "`" + "to" + "`" + " >= " + "'" + LocalDate.now() + "'" + " and parkName = "
						+ "'" + message.Worker.getParkName() + "'" + " and status = " + "'" + "Approved" + "'" + ";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					discount = rset.getString(1);
				}

				MessageCS message = new MessageCS(MessageType.ACCSESS_CHECK_ID, discount, subscriber);
				client.sendToClient(message);

			}
				break;

			case ENTER_PARK_NO_ORDER: {

				LocalTime timenow = LocalTime.now();
				String enterTime = String.format("%02d:%02d:%02d", timenow.getHour(), timenow.getMinute(), 0);
				LocalDate date = java.time.LocalDate.now();
				int NoOrderInPark = 0;
				String type;
				if (message.subscriber == null)
					type = "Single";
				else
					type = message.subscriber.getType();

				query = "INSERT INTO visits (parkName, type, amount, date, enterTime,payment,hasOrder,orderID,ownerID) VALUES (\"";
				query += message.Worker.getParkName();
				query += "\",\"";
				query += type;
				query += "\",\"";
				query += message.num;
				query += "\",\"";
				query += date.toString();
				query += "\",\"";
				query += enterTime;
				query += "\",\"";
				query += message.string1;
				query += "\",\"";
				query += "no";
				query += "\",\"";
				query += "no";
				query += "\",\"";
				query += message.string2;
				query += "\");";

				stmt.executeUpdate(query);
				query = "SELECT MAX(id) FROM visits";
				rset = stmt.executeQuery(query);
				int id = 0;
				while (rset.next())
					id = rset.getInt(1);

				int currentVisitorAmountInPark = 0;
				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += message.Worker.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					currentVisitorAmountInPark = rset.getInt(1);
				}
				currentVisitorAmountInPark = message.num + currentVisitorAmountInPark;

				query = "UPDATE park SET currentVisitorAmountInPark  = \"";
				query += currentVisitorAmountInPark;
				query += "\" WHERE parkName = \"";
				query += message.Worker.getParkName();
				query += "\";";
				stmt.executeUpdate(query);

				query = "SELECT currentNoOrderVisitor from park";
				query += " WHERE parkName =\"";
				query += message.Worker.getParkName();
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					NoOrderInPark = rset.getInt(1);
				}
				NoOrderInPark = NoOrderInPark + message.num;

				query = "UPDATE park SET currentNoOrderVisitor  = \"";
				query += NoOrderInPark;
				query += "\" WHERE parkName = \"";
				query += message.Worker.getParkName();
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message = new MessageCS(MessageType.ENTER_PARK_NO_ORDER, id, currentVisitorAmountInPark);
				client.sendToClient(message);
			}
				break;

			case CHECK_VISIT_ID: {

				query = "SELECT id,exitTime from visits";
				query += " WHERE id = '";
				query += message.string;
				query += "' ;";
				rset = stmt.executeQuery(query);
				if (!rset.next()) {
					MessageCS message = new MessageCS(MessageType.CHECK_VISIT_ID, false);
					client.sendToClient(message);
				} else {
					if (rset.getString(2) != null) {
						if (rset.getString(2).isEmpty()) {
							MessageCS message = new MessageCS(MessageType.CHECK_VISIT_ID, true);
							client.sendToClient(message);
						} else {
							MessageCS message = new MessageCS(MessageType.CHECK_VISIT_ID, false);
							client.sendToClient(message);
						}
					}
					if (rset.getString(2) == null) {
						MessageCS message = new MessageCS(MessageType.CHECK_VISIT_ID, true);
						client.sendToClient(message);
					}

				}
			}
				break;

			case VISITOR_EXIT_PARK: {

				String enterTime = "";
				String exitTime = "";
				String visitTime = "";
				String hasOrder = "";
				String parkName = "";
				int amount = 0;
				query = "SELECT enterTime , amount , hasOrder , parkName from visits";
				query += " WHERE id =\"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					enterTime = rset.getString(1);
					amount = rset.getInt(2);
					hasOrder = rset.getString(3);
					parkName = rset.getString(4);
				}
				LocalTime timenow = LocalTime.now();
				exitTime = String.format("%02d:%02d:%02d", timenow.getHour(), timenow.getMinute(), 0);

				// update the exitTime time//
				query = "UPDATE visits SET exitTime  = \"";
				query += exitTime;
				query += "\" WHERE id = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);

				// calc the visitTime + update it//
				LocalTime enTime = LocalTime.parse(enterTime);
				LocalTime exTime = LocalTime.parse(exitTime);

				Duration d = Duration.between(enTime, exTime);

				long hours = d.toHours();
				int minutes = (int) (d.toMinutes() % 60);

				visitTime = String.format("%02d:%02d:%02d", hours, minutes, 0);

				query = "UPDATE visits SET visitTime  = \"";
				query += visitTime;
				query += "\" WHERE id = \"";
				query += message.string;
				query += "\";";
				stmt.executeUpdate(query);

				// update park visitors amount y//

				int NoOrderInPark = 0;
				int currentVisitorAmountInPark = 0;

				// update visitors without order capicitiy//
				if (hasOrder.equals("no")) {
					query = "SELECT currentNoOrderVisitor from park";
					query += " WHERE parkName =\"";
					query += parkName;
					query += "\";";
					rset = stmt.executeQuery(query);
					if (rset.next()) {
						NoOrderInPark = rset.getInt(1);
					}
					NoOrderInPark = NoOrderInPark - amount;

					query = "UPDATE park SET currentNoOrderVisitor  = \"";
					query += NoOrderInPark;
					query += "\" WHERE parkName = \"";
					query += parkName;
					query += "\";";
					stmt.executeUpdate(query);
				}

				// update all visitors//

				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += parkName;
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					currentVisitorAmountInPark = rset.getInt(1);
				}
				currentVisitorAmountInPark = currentVisitorAmountInPark - amount;

				query = "UPDATE park SET currentVisitorAmountInPark  = \"";
				query += currentVisitorAmountInPark;
				query += "\" WHERE parkName = \"";
				query += parkName;
				query += "\";";
				stmt.executeUpdate(query);

				MessageCS message = new MessageCS(MessageType.VISITOR_EXIT_PARK, currentVisitorAmountInPark);
				client.sendToClient(message);
			}
				break;

			case UPDATE_NUM_OF_VISITORS: {
				String park_name = message.string;
				int num_visitors = message.num;
				MessageCS message = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, num_visitors, park_name);
				server.ServerUI.sv.sendToAllClients(message);
			}
				break;

			case Get_Park_Amount: {
				String park_name = message.string;
				int num_visitors = 0;
				query = "SELECT currentVisitorAmountInPark from park";
				query += " WHERE parkName =\"";
				query += park_name;
				query += "\";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					num_visitors = rset.getInt(1);
				}
				MessageCS message = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, num_visitors, park_name);
				server.ServerUI.sv.sendToAllClients(message);
			}

			case Get_Park_Params: {
				query = "select maxVisitor,maxOrders,maxVisitTime from park WHERE parkName = \"";
				query += message.string;
				query += "\";";
				rset = stmt.executeQuery(query);
				rset.next();
				int max_Visitor = rset.getInt(1);
				int max_Orders = rset.getInt(2);
				String max_VisitTime = rset.getString(3);
				MessageCS message = new MessageCS(MessageType.Get_Park_Params, max_Visitor, max_Orders, max_VisitTime);
				client.sendToClient(message);
			}
				break;

			case Get_Subscriber: {
				query = "SELECT * FROM subscriber WHERE subscriber_id = '" + message.string + "'" + ";";
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					MessageCS message = new MessageCS(MessageType.Get_Subscriber, true);
					client.sendToClient(message);
				} else {
					MessageCS message = new MessageCS(MessageType.Get_Subscriber, false);
					client.sendToClient(message);
				}
			}
				break;

			/********************************************************
			 * Tomer
			 ******************************************/

			}// End of SWITCH

		} catch (

		Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * this method check if date and time is available (for order making)
	 * 
	 * @param order - the order which the user wants to make
	 * @param date  - the date of the order
	 * @param time  - the time of the order
	 * @return true/false - date and time available (true/false, true -available ,
	 *         false - not available )
	 */
	private boolean checkIfDateIsOpen(Order order, String date, String time) {

		boolean check = true;
		try {

			query = "SELECT maxOrders , maxVisitor , maxVisitTime from park";
			query += " WHERE parkName =\"";
			query += order.getParkName();
			query += "\";";
			rset = stmt.executeQuery(query);
			int orders_park = 0;
			String maxVisitTime = "";
			Double min_diff_d = 0.0;
			int min_diff_int = 0;
			while (rset.next()) {
				orders_park = rset.getInt(1);
				maxVisitTime = rset.getString(3);
				min_diff_d = (Double.parseDouble(maxVisitTime)) * 60.0;
				min_diff_int = min_diff_d.intValue();
			}

			LocalTime t = LocalTime.parse(time);
			String StartTime = t.minusMinutes(min_diff_int).toString();
			String EndTime = t.plusMinutes(min_diff_int).toString();

			query = "SELECT * from orders" + " WHERE date =" + "'" + date + "'" + " and parkName =" + "'"
					+ order.getParkName() + "'" + " and status = \"not arrived\" " + " and time > '" + StartTime + "'"
					+ " and time < '" + EndTime + "'" + ";";
			rset = stmt.executeQuery(query);

			ArrayList<Order> orders = getOrders(rset);

			check = checkPlaceForVisitTime(order, orders, time, min_diff_int, orders_park);
		} catch (Exception e) {
		}
		return check;
	}

	/**
	 * this method check if the park available in given starting time
	 * 
	 * @param order        - the order
	 * @param orders       - all of the overlapping orders
	 * @param StartTime    - starting time for the check
	 * @param maxVisitTime - Estimation of the visit time
	 * @param maxOrders    - the max orders parm (in the park)
	 * @return true/false - date and time available (true/false, true -available ,
	 *         false - not available )
	 */
	private boolean checkPlaceForVisitTime(Order order, ArrayList<Order> orders, String StartTime, int maxVisitTime,
			int maxOrders) {
		boolean bool = true;
		for (int i = 0; i < maxVisitTime; i++) {
			LocalTime t = LocalTime.parse(StartTime).plusMinutes(i);
			String sTime = t.minusMinutes(maxVisitTime).toString();
			bool = checkMinCapicity(order, orders, sTime, t.toString(), maxOrders);
			if (bool == false) {
				return false;
			}
		}
		return bool;

	}

	/**
	 * The method check for given min if the park is available
	 * 
	 * @param order     - the order
	 * @param orders    - all of the overlapping orders
	 * @param StartTime - starting time
	 * @param Time      - the min to check
	 * @param maxOrders - the max orders parm (in the park)
	 * @return true/false - date and time available (true/false, true -available ,
	 *         false - not available )
	 */
	private boolean checkMinCapicity(Order order, ArrayList<Order> orders, String StartTime, String Time,
			int maxOrders) {
		int sum = 0;
		LocalTime sTime = LocalTime.parse(StartTime);
		LocalTime eTime = LocalTime.parse(Time);
		for (Order cross_order : orders) {
			LocalTime orderTime = LocalTime.parse(cross_order.getTime());
			if (sTime.isBefore(orderTime) && (orderTime.isBefore(eTime) || orderTime.equals(eTime))) {
				sum = sum + cross_order.getNumberOfVisitors();
			}
		}
		return (order.getNumberOfVisitors() + sum) <= maxOrders;
	}

	/**
	 * the method return ArrayList<Order> of given ResultSet
	 * 
	 * @param rset ResultSet
	 * @return ArrayList<Order> of the given ResultSet
	 */
	private ArrayList<Order> getOrders(ResultSet rset) {
		ArrayList<Order> orders = new ArrayList<Order>();
		try {
			while (rset.next()) {
				Order order = new Order();
				order.setOrderId(rset.getInt("orderId"));
				order.setParkName(rset.getString("parkName"));
				order.setDate(rset.getString("date"));
				order.setTime(rset.getString("time"));
				order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
				order.setEmail(rset.getString("email"));
				order.setOwnerId(rset.getInt("ownerId"));
				order.setTypeOfOrder(rset.getString("typeOfOrder"));
				order.setStatus(rset.getString("status"));
				order.setPaynow(rset.getString("paynow"));
				String string = rset.getString("paynow");
				String[] parts = string.split(",");
				order.setFull_price(parts[0]);
				order.setDiscount(parts[1]);
				order.setTotal_price(parts[2]);

				orders.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orders;

	}

	/**
	 * the method return ArrayList<Order> of given ResultSet
	 * 
	 * @param rset ResultSet
	 * @return ArrayList<Order> of the given ResultSet the method is for orders in
	 *         waitinglist
	 */
	private ArrayList<Order> getWaitingListOrders(ResultSet rset) {
		ArrayList<Order> orders = new ArrayList<Order>();
		try {
			while (rset.next()) {
				Order order = new Order();
				order.setOrderId(rset.getInt("orderId"));
				order.setParkName(rset.getString("parkName"));
				order.setDate(rset.getString("date"));
				order.setTime(rset.getString("time"));
				order.setNumberOfVisitors(rset.getInt("numberOfVisitors"));
				order.setEmail(rset.getString("email"));
				order.setOwnerId(rset.getInt("ownerId"));
				order.setTypeOfOrder(rset.getString("typeOfOrder"));

				orders.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orders;

	}

	/**
	 * the method get user_id in users table - checking who is logged in
	 * 
	 * @param id   - which id to serach
	 * @param type of user
	 * @return user_id
	 */
	private int GetID(String id, String type) {
		int user_id = 0;
		try {
			query = "SELECT user_id FROM users";
			query += " WHERE ID  =\"";
			query += id;
			query += "\" and type = \"";
			query += type;
			query += "\";";
			rset = stmt.executeQuery(query);
			while (rset.next())
				user_id = rset.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_id;
	}

	/**
	 * the method get orderCheck and handle the waitinglist process - sending email
	 * for the people in waitinglist , handling the waitinglist process - making
	 * sure each person has X mins to confirm his order from waitinglist, and
	 * passing the turn to the next person in the waitinglist
	 * 
	 * @param orderCheck
	 */

	private void handleWaitingList(Order orderCheck) {
		boolean bool = false;
		int min_diff_int = 0;
		Double intervel = EchoServer.WaitingListTimeMins * 60 * 1000;
		try {
			LocalTime t = LocalTime.parse(orderCheck.getTime());
			min_diff_int = getMaxVisitTime(orderCheck);
			String StartTime = t.minusMinutes(min_diff_int).toString();
			String EndTime = t.plusMinutes(min_diff_int).toString();
			query = "select * from waitinglist where date = " + "'" + orderCheck.getDate() + "'" + "and parkName = "
					+ "'" + orderCheck.getParkName() + "'" + " and time > '" + StartTime + "'" + " and time < '"
					+ EndTime + "'" + ";";
			rset = stmt.executeQuery(query);
			ArrayList<Order> orders = getWaitingListOrders(rset);
			for (int i = 0; i < 1; i++) {
				bool = checkIfDateIsOpen(orders.get(i), orders.get(i).getDate(), orders.get(i).getTime());
				if (getOrderStatus(orders.get(i)).equals("Waiting") && bool) {
					SendMail.sendMailWaitingList2(orders.get(i).getEmail());
					setShowTrue(orders.get(i));
					Thread.sleep(intervel.intValue());
				}
			}

			for (int i = 1; i < orders.size(); i++) {
				bool = checkIfDateIsOpen(orders.get(i), orders.get(i).getDate(), orders.get(i).getTime());
				if (getOrderStatus(orders.get(i - 1)).equals("Waiting")) {
					setShowFalse(orders.get(i - 1));
				}
				if (getOrderStatus(orders.get(i)).equals("Waiting") && bool) {
					SendMail.sendMailWaitingList2(orders.get(i).getEmail());
					setShowTrue(orders.get(i));
					Thread.sleep(intervel.intValue());
				}
			}
			if (getOrderStatus(orders.get(orders.size() - 1)).equals("Waiting")) {
				setShowFalse(orders.get(orders.size() - 1));
			}
		} catch (Exception e) {

		}
	}

	/**
	 * the method get the order status of given order from the db
	 * 
	 * @param orderCheck - which order to check the status for
	 * @return - the status order in string
	 */
	private String getOrderStatus(Order orderCheck) {
		String res = "null";
		try {
			query = "select status from orders where orderId = " + orderCheck.getOrderId();
			rset = stmt.executeQuery(query);
			if (rset.next()) {
				res = rset.getString(1);
			}
		} catch (Exception e) {

		}
		return res;
	}

	/**
	 * the method set the waitinglist button visible for the user/travler
	 * 
	 * @param orderCheck - which order to set the button visibility for
	 */

	private void setShowTrue(Order orderCheck) {
		try {
			query = "UPDATE waitinglist SET show_b  = \"";
			query += "yes";
			query += "\" WHERE orderId = \"";
			query += orderCheck.getOrderId();
			query += "\";";
			stmt.executeUpdate(query);
		} catch (Exception e) {

		}
	}

	/**
	 * the method set the waitinglist button not visible for the user/travler
	 * 
	 * @param orderCheck - which order to set the button not visible for this method
	 *                   set the button visibility to false
	 */
	private void setShowFalse(Order orderCheck) {
		try {
			query = "UPDATE waitinglist SET show_b  = \"";
			query += "no";
			query += "\" WHERE orderId = \"";
			query += orderCheck.getOrderId();
			query += "\";";
			stmt.executeUpdate(query);
		} catch (Exception e) {

		}
	}

	/**
	 * get the est. visit time for given order
	 * 
	 * @param order - the order to check the visit time for
	 * @return est. visit time
	 */
	private int getMaxVisitTime(Order order) {
		String maxVisitTime = "";
		Double min_diff_d = 0.0;
		int min_diff_int = 0;
		try {
			query = "SELECT maxVisitTime from park";
			query += " WHERE parkName =\"";
			query += order.getParkName();
			query += "\";";
			rset = stmt.executeQuery(query);
			while (rset.next()) {
				maxVisitTime = rset.getString(1);
				min_diff_d = (Double.parseDouble(maxVisitTime)) * 60.0;
				min_diff_int = min_diff_d.intValue();
			}
		} catch (Exception err) {

		}
		return min_diff_int;
	}
	
	/**
	 * @return MessageCS
	 */
	public MessageCS getMessage() {
		return message;
	}

}
