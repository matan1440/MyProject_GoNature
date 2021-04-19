package client.common;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import client.controllers.CreateOrderController;
import client.logic.Order;
import client.logic.Subscriber;

/**
 * This class summary all the message that the system need send depending on the
 * appropriate summons.
 * 
 * @version 1.0
 * @author Matan
 *
 */
public class SendMail {

///////////////////////////////////////////////////////////////////////////////////////////////////////////	sendMailConfirme

	/**
	 * This function send mail depending on the order details that the user insert
	 * in create order.
	 * 
	 * @param recepient
	 * @param order
	 * @throws MessagingException
	 */
	public static void sendMailConfirme(String recepient, Order order) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

// Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

// Prepare email message
		Message message = prepareMessageConfirme(session, myAccountEmail, recepient, order);

		new Thread(() -> {
			try {
// Send mail
				Transport.send(message);

			} catch (MessagingException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}

	/**
	 * In this function we create the message that need send to the user.
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 * @param order
	 */
	private static Message prepareMessageConfirme(Session session, String myAccountEmail, String recepient,
			Order order) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Order Confirmation");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'> Order Confirmation from GoNature parks</h1>"
							+ "<body>"

							+ " <p style='text-align:left;'> Order ID" + " :" + order.getOrderId()
							+ " <p style='text-align:left;'> Park" + " :" + order.getParkName()
							+ " <p style='text-align:left;'> Date" + " :" + order.getDate()
							+ "  <p style='text-align:left;'> Time" + " :" + order.getTime()
							+ "  <p style='text-align:left;'> Amount" + " :" + order.getNumberOfVisitors()
							+ "  <p style='text-align:left;'> Email" + " :" + order.getEmail()
							+ "  <p style='text-align:left;'> Owner ID" + " :" + order.getOwnerId()
							+ "  <p style='text-align:left;'> Type order" + " :" + order.getTypeOfOrder()
							+ "  <p style='text-align:left;'> Status order" + " :" + order.getStatus()

							+ " <h2> <p style='text-align:left;'> Total price </h2>" + "<p style='text-align:left;'>"
							+ order.getTotal_price()

							+ "<p style='text-align:left; font-weight: bold; '>  Please make sure you arrive at the park with the amount of people you mentioned at the time *"

							+ "<h2><p style='text-align:left; color:#66cdaa;'>  Hope you enjoy, see you in the park </h2>"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////// sendMailReminder

	/**
	 * This function send reminder message about the visit one day before.
	 * 
	 * @param recepient
	 * @throws MessagingException
	 */
	public static void sendMailReminder(String recepient) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

// Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

// Prepare email message
		Message message = prepareMessage(session, myAccountEmail, recepient);

		new Thread(() -> {
			try {
// Send mail
				Transport.send(message);

			} catch (MessagingException e) {
// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}

	/**
	 * In this function we prepare reminder message that need send to user.
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 */
	private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Reminder from GoNature");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'> Order tomorrow </h1>" + "<body>"

							+ " <p style='text-align:left;'> "
							+ "We wanted to remind you that a visit to the park is planned for tomorrow"

							+ " <h2> <p style='text-align:left; style='text-align:left; color:red;'>Please log in to confirm arrival</h2>"

							+ "<p style='text-align:left; font-weight: bold; '>  If you do not confirm within two hours, your order will be automatically canceled *"

							+ "<h2><p style='text-align:left; color:#66cdaa;'>  Hope to see you tomorrow </h2>"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////	sendMailNewSubscriber

	/**
	 * This function send mail with details of new subscriber.
	 * 
	 * @param recepient
	 * @param Sub
	 * @param sub_id
	 * @throws MessagingException
	 */
	public static void sendMailNewSubscriber(String recepient, Subscriber Sub, int sub_id) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

//Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

//Prepare email message
		Message message = prepareMessage(session, myAccountEmail, recepient, Sub, sub_id);

		new Thread(() -> {
			try {
//Send mail
				Transport.send(message);

			} catch (MessagingException e) {
//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}

	/**
	 * In this function we prepare message with all details of subscriber.
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 * @param sub
	 * @param sub_id
	 */
	private static Message prepareMessage(Session session, String myAccountEmail, String recepient, Subscriber sub,
			int sub_id) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("GoNatuer Subscriber");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'>Welcome to the family GoNature</h1>"
							+ "<body>"

							+ " <p style='text-align:left;'> First name" + " :" + sub.getFirstname()
							+ " <p style='text-align:left;'> Last name" + " :" + sub.getLastname()
							+ " <p style='text-align:left;'> Email" + " :" + sub.getEmail()
							+ "  <p style='text-align:left;'> Amount" + " :" + sub.getSubscriber_amount()
							+ "  <p style='text-align:left;'> Type" + " :" + sub.getType()
							+ "  <p style='text-align:left;'> Subscriber ID" + " :" + sub_id

							+ "<h2><p style='text-align:left; color:#66cdaa;'>  Hope you enjoy, see you in the parks </h2>"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////	sendMailWaitingList1 - enter into waiting list

	/**
	 * This function send mail confirmation of entry to the waiting list
	 * 
	 * @param recepient
	 * @param order
	 * @throws MessagingException
	 */
	public static void sendMailWaitingList1(String recepient, Order order) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

//Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

//Prepare email message
		Message message = prepareMessageWaitingList1(session, myAccountEmail, recepient, order);

		new Thread(() -> {
			try {
//Send mail
				Transport.send(message);

			} catch (MessagingException e) {
//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}

	/**
	 * In this function we create the message that need send to the user.
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 * @param order
	 */
	private static Message prepareMessageWaitingList1(Session session, String myAccountEmail, String recepient,
			Order order) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Enter to waitinglist");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'> Enter to waiting list from GoNature parks</h1>"
							+ "<body>"

							+ " <p style='text-align:left;'> Order ID" + " :" + order.getOrderId()
							+ " <p style='text-align:left;'> Park" + " :" + order.getParkName()
							+ " <p style='text-align:left;'> Date" + " :" + order.getDate()
							+ "  <p style='text-align:left;'> Time" + " :" + order.getTime()
							+ "  <p style='text-align:left;'> Amount" + " :" + order.getNumberOfVisitors()
							+ "  <p style='text-align:left;'> Email" + " :" + order.getEmail()
							+ "  <p style='text-align:left;'> Owner ID" + " :" + order.getOwnerId()
							+ "  <p style='text-align:left;'> Type order" + " :" + order.getTypeOfOrder()
							+ "  <p style='text-align:left;'> Status order" + " :" + order.getStatus()

//+ " <h2> <p style='text-align:left;'> Total price </h2>"
//+"<p style='text-align:left;'>" + ViewOrderDetailsController.totalprice

							+ "<p style='text-align:left; font-weight: bold; '>  We will send you a notice when space is available *"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////	sendMailWaitingList2  - Free place in the list

	/**
	 * This function send mail about available space vacated
	 * 
	 * @param recepient
	 * @throws MessagingException
	 */
	public static void sendMailWaitingList2(String recepient) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

//Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

//Prepare email message
		Message message = prepareMessageWaitingList2(session, myAccountEmail, recepient);

		new Thread(() -> {
			try {
//Send mail
				Transport.send(message);

			} catch (MessagingException e) {
//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}

	/**
	 * This function prepare message about available space vacated in park.
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 */
	private static Message prepareMessageWaitingList2(Session session, String myAccountEmail, String recepient) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Place available");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'> Free space on the waiting list </h1>"
							+ "<body>"

							+ "<p style='text-align:left; font-weight: bold; text-size: 24; '> A space is available in the park, You have 1 hour to confirm your order"

							+ "<h2><p style='text-align:left; color:#66cdaa;'> ! Hope you enjoy, see you in the park </h2>"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////	sendMailWaitingList2  - Free place in the list

	/**
	 * This function send mail about order which cancneled - if the travler not confirmed his order
	 * 
	 * @param recepient
	 * @throws MessagingException
	 */
	public static void sendMailCancelNotConfirmed(String recepient) throws MessagingException {

		Properties properties = new Properties();
		String myAccountEmail = "gonature012@gmail.com";
		String password = "GoNature123";

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		properties.setProperty("mail.smtp.user", myAccountEmail);
		properties.setProperty("mail.smtp.password", password);

//Create a session with account credentials
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});

//Prepare email message
		Message message = prepareMessageCancelNotConfirmed(session, myAccountEmail, recepient);

		new Thread(() -> {
			try {
//Send mail
				Transport.send(message);

			} catch (MessagingException e) {
//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		System.out.println("Message sent successfully");
	}


	/**
	 * This function prepare message about order cancellation (the traveler didn't confirmed his order in 2 hours).
	 * 
	 * @param session
	 * @param myAccountEmail
	 * @param recepient
	 */
	
	private static Message prepareMessageCancelNotConfirmed(Session session, String myAccountEmail, String recepient) {
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Order Canceled");

			String htmlCode =

					"<html lang='en'>" +

							"<h1> <p style='text-align:left; color:#66cdaa;'> Your Order Has Been Canceled </h1>"
							+ "<body>"

							+ "<p style='text-align:left; font-weight: bold; text-size: 24; '> Unfornately , You Had Enough Time To Confirm Your Order - Order Has Been Canceled"

							+ "</body>";

			message.setContent(htmlCode, "text/html");

			return message;
		} catch (Exception ex) {
			Logger.getLogger(CreateOrderController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

}
