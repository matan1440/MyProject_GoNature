package client.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ClientUI;
import client.common.MessageCS.MessageType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class is summary of functions that we need use in all controllers
 * in order to check validation input form the user in the system.
 * 
 * @version 1.0
 * @author Matan
 */

public class InputTestFunctions {

	/**
	 * Check Subscriber - Check if the user that made a login is Subscriber
	 */
	public static boolean CheckSubscriber = false;
	
	/**
	 * This function check that the user does not insert empty input to text field.
	 * @param str
	 * @return boolean 
	 */
	public static boolean ValidateEmptyString(String str) {

		Pattern p = Pattern.compile("[]+[ ]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("One of the fields is empty");
			alert.show();

			return false;
		}
	}

	
	/**
	 * This function check that the user insert only numbers to the text field.
	 * @param str
	 * @return boolean
	 */
	public static boolean ValidateNumber(String str) {

		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter Valid Number");
			alert.show();

			return false;
		}
	}

	
	/**
	 * This function check that the user insert only string (English letters) 
	 * to text field.
	 * @param str
	 * @return boolean
	 */
	public static boolean ValidateString(String str) {

		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter Valid text");
			alert.show();

			return false;
		}
	}
	

	/**
	 * This function doing the same as ValidateString function, 
	 * without show message popup alert.
	 * @param str
	 * @return boolean
	 */
	public static boolean ValidateStringNoAlert(String str) {

		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else 
			return false;
	}
	
	
	/**
	 * This function check if the user insert validate email 
	 * According to a suitable wording.
	 * @param str
	 * @return boolean
	 */
	public static boolean ValidateEmail(String str) {

		Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter Valid Email");
			alert.show();

			return false;
		}
	}
	

	/**
	 * This function check if the user insert only positive number to text field
	 * and displays an appropriate message if this is not positive number.
	 * @param num
	 * @return boolean
	 */
	public static boolean ValidatePositiveInteger(int num) {

		if (0 < num) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter Valid Number");
			alert.show();

			return false;
		}
	}

	
	/**
	 * This is function check if the date and the time is validate relative 
	 * to the present time. 
	 * @param date
	 * @param time
	 * @return boolean
	 */
	public static boolean ValidateDate(String date, String time) {

		try {

			if (LocalDate.now().isBefore(LocalDate.parse(date)) || (LocalDate.now().isEqual(LocalDate.parse(date))
					&& LocalTime.now().isBefore(LocalTime.parse(time)))) {
				return true;

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Validation error");
		alert.setHeaderText(null);
		alert.setContentText("Please enter Valid date");
		alert.show();

		return false;
	}

	
	/**
	 * This function check that the user insert ID or SubscriberID with validate length.
	 * @param str
	 * @return boolean
	 */
	public static boolean ValidateSizeID(String str) {

		if (str.length() == 5) {
			MessageCS message = new MessageCS(MessageType.Get_Subscriber, str);
			ClientUI.chat.accept(message);
			if(CheckSubscriber) {
				return true;
			}
		}
		
		if (str.length() == 9) {
			return true;

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter Valid ID	/ SubscriberID ");
			alert.show();

			return false;
		}

	}
}