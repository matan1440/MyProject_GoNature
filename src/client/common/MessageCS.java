package client.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import client.logic.*;

@SuppressWarnings("serial")

/**
 * This Class contains the data for the messages between the client and the
 * server The Class is mainly for storeing the request data (client->server) and
 * the answer from the server (server->client)
 * 
 * @author Omer Matan Daniel Tania Tomer
 *
 */
public class MessageCS implements Serializable {

	/**
	 * Worker object
	 */
	public Worker Worker;
	/**
	 * String object
	 */
	public String string;

	/**
	 * num
	 */
	public int num;

	/**
	 * num2
	 */
	public int num2;

	/**
	 * Subscriber object
	 */
	public Subscriber subscriber;

	/**
	 * Order object
	 */
	public Order order;

	/**
	 * String object
	 */
	public String string1;
	/**
	 * String object
	 */
	public String string2;
	/**
	 * String object
	 */
	public String string3;

	/**
	 * boolean object
	 */
	public boolean check;

	/**
	 * boolean object
	 */
	public boolean conn_check;

	/**
	 * ArrayList<DateOrder object
	 */

	public ArrayList<DateOrder> orderDateslist = new ArrayList<DateOrder>();

	/**
	 * ArrayList<DateOrder object
	 */
	public ArrayList<Order> myOrders = new ArrayList<Order>();

	/**
	 * ArrayList<DateOrder object
	 */
	public LocalDate date;

	/**
	 * enum MessageType - all of the cases are enums Cases: Login, Waiting List ,
	 * Order , Access Control , Service Park/Dept. Manager
	 * 
	 * @author Omer Matan Daniel Tania Tomer
	 *
	 */
	public enum MessageType {

		/********************************************************
		 * Omer Matan
		 ******************************************/

		LOGIN_CHECK, LOG_OUT, LOGIN_WORKER, LOGIN_VISITOR, ERROR_LOGIN, LOGIN_SUBSCRIBER_WITH_ORDER, LOGIN_NEW_VISITOR,
		LOGIN_OWNER_ORDER, LOGIN_SUBSCRIBER_WITHOUT_ORDER, Create_Order, CheckPlace, CheckOtherDates, Enter_WAITINGLIST,
		CancelOrder, ThereIsPlace, CheckTopWaitingListAndPlace, ChangeStatusOrder, MyOrders, ArrivelConfirm,
		CheckDiscountForOrder, PayNow, CheckIfPaidNow, CardReaderCheckEntrance, CardReaderEnterToPark,
		CardReaderCheckCapicity, CardReaderCheckVisitId, CardReaderExitFromPark, GetVisitorType, CheckIfConfirmArrivel,

		/********************************************************
		 * Omer Matan
		 ******************************************/

		/********************************************************
		 * Tomer
		 ******************************************/

		CREATE_SUBSCRIBER, SEARCH_ORDER, Enter_to_park, CHECK_AMOUNT, ACCSESS_CHECK_ID, ENTER_PARK_NO_ORDER,
		CHECK_VISIT_ID, VISITOR_EXIT_PARK, UPDATE_NUM_OF_VISITORS, Get_Park_Amount, Get_Subscriber,

		/********************************************************
		 * Tomer
		 ******************************************/

		/********************************************************
		 * Dani Tanya
		 ******************************************/

		UPDATE_MAX_VISITOR, UPDATE_VISITING_TIME, MAX_ORDERS, UPDATE_MAX_ORDERS, REQUEST_SENT, UPDATE_DISCOUNT,
		DISCOUNT_UPDATED, REQUEST_MAX_VISITORS, SHOW_VALUES, REQUEST_PARAMETERS, CHANGE_MAX_ORDERS, CHANGE_MAX_VISITOR,
		CHANGE_VISITNG_TIME, ACTION_SUCCESSFULL, SHOW_DISCOUNTS, SHOW, CHANGE_DISCOUNT, DELETE_DISCOUNT,
		SHOW_CANCEL_REPORT, REPORT_VISITORS, REPORT_INCOME, REPORT_USAGE, REPORT_VISITING, REPORT_STAYING,
		Get_Park_Params;

		/********************************************************
		 * Dani Tanya
		 ******************************************/

	}

	/**
	 * messageType object
	 */
	public MessageType messageType;

	public MessageCS(MessageType messageType) // Empty constractor
	{
		this.messageType = messageType;
	}

	/**
	 * MessageCS Constructor (messageType + worker)
	 * 
	 * @param messageType - messageType
	 * @param worker      - worker
	 */
	public MessageCS(MessageType messageType, Worker worker) // constractor worker1
	{
		this.messageType = messageType;
		this.Worker = worker;
	}

	/**
	 * MessageCS Constructor (messageType + num)
	 * 
	 * @param messageType - messageType
	 * @param num         - num
	 */
	public MessageCS(MessageType messageType, Worker worker, int num) // constractor worker1
	{
		this.messageType = messageType;
		this.Worker = worker;
		this.num = num;
	}

	/**
	 * MessageCS Constructor (messageType + string object)
	 * 
	 * @param messageType - messageType
	 * @param str         - string object
	 */
	public MessageCS(MessageType messageType, String str) // constractor str
	{
		this.messageType = messageType;
		this.string = str;
	}

	/**
	 * MessageCS Constructor (messageType + string object + Subscriber object)
	 * 
	 * @param messageType - messageType
	 * @param str         - string object
	 * @param sub         - Subscriber object
	 */
	public MessageCS(MessageType messageType, String str, Subscriber sub) // constractor str
	{
		this.messageType = messageType;
		this.string = str;
		this.subscriber = sub;
	}

	/**
	 * MessageCS Constructor (messageType + Subscriber object)
	 * 
	 * @param messageType - messageType
	 * @param v           - Subscriber object
	 */

	public MessageCS(MessageType messageType, Subscriber v) // constractor subscriber
	{
		this.messageType = messageType;
		this.subscriber = v;
	}

	/**
	 * MessageCS Constructor (messageType + Subscriber object + Worker,strings and
	 * numbers)
	 * 
	 * @param messageType - messageType
	 * @param v           - Subscriber object
	 * @param worker      - Worker object
	 * @param num         - num
	 * @param str1        - string
	 * @param str2        - string
	 */

	public MessageCS(MessageType messageType, Subscriber v, Worker worker, int num, String str1, String str2) // constractor
																												// subscriber
	{
		this.messageType = messageType;
		this.subscriber = v;
		this.Worker = worker;
		this.string1 = str1;
		this.string2 = str2;
		this.num = num;
	}

	/**
	 * MessageCS Constructor (messageType + num)
	 * 
	 * @param messageType - messageType
	 * @param num         - num
	 */

	public MessageCS(MessageType messageType, int num) // constractor int
	{
		this.messageType = messageType;
		this.num = num;
	}

	/**
	 * MessageCS Constructor (messageType + num + num2)
	 * 
	 * @param messageType - messageType
	 * @param num         - num
	 * @param num2        - num2
	 */
	public MessageCS(MessageType messageType, int num, int num2) // constractor int
	{
		this.messageType = messageType;
		this.num = num;
		this.num2 = num2;
	}

	/**
	 * MessageCS Constructor (messageType + num + num2 + str)
	 * 
	 * @param messageType - messageType
	 * @param num         - num
	 * @param num2        - num2
	 * @param str         - str
	 */
	public MessageCS(MessageType messageType, int num, int num2, String str) // constractor int
	{
		this.messageType = messageType;
		this.num = num;
		this.num2 = num2;
		this.string = str;
	}

	/**
	 * MessageCS Constructor (messageType + num + str)
	 * 
	 * @param messageType - messageType
	 * @param num         - num
	 * @param str         - str
	 */
	public MessageCS(MessageType messageType, int num, String str) // constractor int
	{
		this.messageType = messageType;
		this.num = num;
		this.string = str;
	}

	/**
	 * MessageCS Constructor (messageType + String + Subscriber + Order)
	 * 
	 * @param messageType - messageType
	 * @param str         - string
	 * @param v           - Subscriber object
	 * @param order       - Order object
	 */

	public MessageCS(MessageType messageType, String str, Subscriber v, Order order) // constractor subscriber + order
	{
		this.messageType = messageType;
		this.string = str;
		this.subscriber = v;
		this.order = order;
	}

	/**
	 * MessageCS Constructor (messageType + String + Order)
	 * 
	 * @param messageType - messageType
	 * @param str         - string
	 * @param order       - Order object
	 */
	public MessageCS(MessageType messageType, String string, Order order) // constractor str + order
	{
		this.messageType = messageType;
		this.string = string;
		this.order = order;
	}

	/**
	 * MessageCS Constructor (messageType + boolean)
	 * 
	 * @param messageType - messageType
	 * @param check       - boolean object
	 */
	public MessageCS(MessageType messageType, boolean check) // constractor boolean
	{
		this.messageType = messageType;
		this.check = check;
	}

	/**
	 * MessageCS Constructor (messageType + boolean)
	 * 
	 * @param messageType - messageType
	 * @param check       - boolean object
	 * @param order       - Order object
	 */
	public MessageCS(MessageType messageType, boolean check, Order order) // constractor str + order
	{
		this.messageType = messageType;
		this.check = check;
		this.order = order;
	}

	/**
	 * 
	 * @return String
	 */
	public String getString() {
		return string;
	}

	/**
	 * set string
	 * 
	 * @param string
	 */

	public void setString(String string) {
		this.string = string;
	}

	/**
	 * MessageCS Constructor (messageType + string + worker)
	 * 
	 * @param messageType - messageType
	 * @param string      - string
	 * @param worker      - worker
	 */
	public MessageCS(MessageType messageType, String string, Worker worker) // update visitors
	{
		this.messageType = messageType;
		this.string = string;
		this.Worker = worker;
	}

	/**
	 * MessageCS Constructor (messageType + strings + worker)
	 * 
	 * @param messageType - messageType
	 * @param newDiscount - string
	 * @param localDate   - string
	 * @param localDate2  - string
	 * @param worker      - Worker object
	 */
	public MessageCS(MessageType messageType, String newDiscount, String localDate, String localDate2, Worker worker) {
		this.messageType = messageType;
		string = newDiscount;
		this.Worker = worker;
		string1 = localDate;
		string2 = localDate2;

	}

	/**
	 * MessageCS Constructor (messageType + strings)
	 * 
	 * @param messageType - messageType
	 * @param newDiscount - string
	 * @param localDate   - string
	 * @param localDate2  - string
	 */
	public MessageCS(MessageType messageType, String newDiscount, String localDate, String localDate2) {
		this.messageType = messageType;
		string = newDiscount;
		string1 = localDate;
		string2 = localDate2;
	}

	/**
	 * MessageCS Constructor (messageType + strings + worker)
	 * 
	 * @param messageType - messageType
	 * @param text        - string
	 * @param string4     - string
	 * @param worker      - Worker object
	 */

	public MessageCS(MessageType messageType, String text, String string4, Worker worker) {
		this.messageType = messageType;
		this.Worker = worker;
		string = text;
		string1 = string4;

	}

	/**
	 * MessageCS Constructor (messageType + string + ArrayList<Order>)
	 * 
	 * @param messageType - messageType
	 * @param string      - string
	 * @param myOrders    - ArrayList<Order>
	 */
	public MessageCS(MessageType messageType, String string, ArrayList<Order> myOrders) {
		this.messageType = messageType;
		this.string = string;
		this.myOrders = myOrders;
	}

	/**
	 * MessageCS Constructor (messageType + string + ArrayList<Order> + Subscriber)
	 * 
	 * @param messageType - messageType
	 * @param string      - string
	 * @param myOrders    - ArrayList<Order>
	 * @param subscriber  - Subscriber
	 */
	public MessageCS(MessageType messageType, String string, ArrayList<Order> myOrders, Subscriber subscriber) {
		this.messageType = messageType;
		this.string = string;
		this.myOrders = myOrders;
		this.subscriber = subscriber;
	}

	/**
	 * MessageCS Constructor (messageType + ArrayList<Order> orderDateslist)
	 * 
	 * @param messageType
	 * @param orderDateslist
	 */
	public MessageCS(MessageType messageType, ArrayList<DateOrder> orderDateslist) {
		this.messageType = messageType;
		this.orderDateslist = orderDateslist;
	}

	/**
	 * MessageCS Constructor (messageType +LocalDate + Order)
	 * 
	 * @param messageType - messageType
	 * @param date        - date
	 * @param order       - Order object
	 */
	public MessageCS(MessageType messageType, LocalDate date, Order order) {
		this.messageType = messageType;
		this.order = order;
		this.date = date;
	}

	/**
	 * MessageCS Constructor (messageType +Order + Strings)
	 * 
	 * @param messageType - messageType
	 * @param visit - visit
	 * @param payment - payment
	 * @param hasOrder - hasorder
	 * @param orderId - id
	 */

	public MessageCS(MessageType messageType, Order visit, String payment, String hasOrder, String orderId) {
		this.messageType = messageType;
		this.order = visit;
		this.string = payment;
		this.string1 = hasOrder;
		this.string2 = orderId;
	}

	/**
	 * MessageCS Constructor (messageType + String + boolean)
	 * @param messageType - messageType
	 * @param id - id
	 * @param conn_check - boolean
	 */
	public MessageCS(MessageType messageType, String id, boolean conn_check) {
		this.messageType = messageType;
		this.string = id;
		this.conn_check = conn_check;
	}
	/**
	 * MessageCS Constructor (messageType + int + boolean)
	 * @param messageType - messageType
	 * @param id - id
	 * @param conn_check - boolean
	 */
	
	public MessageCS(MessageType messageType, int id, boolean conn_check) {
		this.messageType = messageType;
		this.num = id;
		this.conn_check = conn_check;
	}

	/**
	 * MessageCS Constructor (messageType + strings)
	 * @param reportVisiting - MessageType
	 * @param string4 - string
	 * @param value - string
	 */
	public MessageCS(MessageType reportVisiting, String string4, String value) {
		this.messageType = reportVisiting;
		this.string = string4;
		this.string1 = value;

	}

}
