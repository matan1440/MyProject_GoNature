package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import client.logic.Worker;

/**
 * This class is represent the update parameters of the given park by choose new
 * MaxVisitors/MaxOrders/VisitingTime parameters and send them for confirmation
 * to Department Manager
 * 
 * @author tania daniel
 *
 */
public class updateParametersController implements Initializable {

	@FXML
	private Label lblParkName;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;

	@FXML
	private TextField txtMaxVisitors;

	@FXML
	private TextField txtVisitingTime;

	@FXML
	private Button btnSendRequest;

	@FXML
	private TextField txtMaxOrders;

	@FXML
	private CheckBox chkMaxVisitor;

	@FXML
	private CheckBox chkVisitingTime;

	@FXML
	private CheckBox chkMaxOrders;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnUpdateParemters;

	@FXML
	private Button btnReports;

	@FXML
	private Button btnUpdateDiscount;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button logOut;

	/**
	 * requestMessage is the string that label use- if we succeded to sent request
	 * it shows
	 */
	public static String requestMessage;

	/**
	 * The Park Manager Object (Park Info is stored in)
	 */
	private Worker worker;

	/**
	 * This function is allowd park manager send new parameters of
	 * MaxOrder/MaxVisitors/VisitingTime request to Department Manager for
	 * confirmation
	 * 
	 * @param event-event that fired the method
	 */
	@FXML
	void SendRequest(ActionEvent event) {
		requestMessage = null;
		lblStatus.setText("");

		// add if parameter is right
		if (chkMaxVisitor.isSelected()) {// check what is selected
			if (txtMaxVisitors.getText().equals(""))
				lblStatus.setText(" Maximum visitors is empty");
			else {
				if (!(txtMaxVisitors.getText().matches("[0-9]+")) || Integer.parseInt(txtMaxVisitors.getText()) < 1) {
					lblStatus.setText(" Maximum visitors vlaue not valid");
				} else {
					MessageCS message = new MessageCS(MessageType.UPDATE_MAX_VISITOR, txtMaxVisitors.getText(), worker);// send
																														// max
																														// visitors
																														// request
					ClientUI.chat.accept(message);
				}
			}
		}

		if (chkVisitingTime.isSelected()) {// check what is selected
			if (txtVisitingTime.getText().equals(""))
				lblStatus.setText(" Visiting time is empty");
			else {
				if (!(txtVisitingTime.getText().matches("[0-9]+")) || Integer.parseInt(txtVisitingTime.getText()) < 1) {
					lblStatus.setText(" Visiting time vlaue not valid");
				} else {
					MessageCS message = new MessageCS(MessageType.UPDATE_VISITING_TIME, txtVisitingTime.getText(),
							worker);// send VISITING_TIME request
					ClientUI.chat.accept(message);
				}
			}
		}

		if (chkMaxOrders.isSelected()) {// check what is selected
			if (txtMaxOrders.getText().equals(""))
				lblStatus.setText(" Max orders is empty");
			else {
				if (!(txtMaxOrders.getText().matches("[0-9]+")) || Integer.parseInt(txtMaxOrders.getText()) < 1) {
					lblStatus.setText(" Max orders vlaue not valid");
				} else {
					MessageCS message = new MessageCS(MessageType.UPDATE_MAX_ORDERS, txtMaxOrders.getText(), worker);// send
																														// MAX_ORDERS
																														// request
					ClientUI.chat.accept(message);
				}
			}
		}

		if (requestMessage != null) // change label if changed in sql
			lblStatus.setText(requestMessage);
	}

	/**
	 * this method shows in the department report controller the current time ,and
	 * current date
	 * 
	 * @param location location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();

			String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
					currentTime.getSecond());
			lblTime.setText(curTime);

		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();

		lblDate.setText(LocalDate.now().toString());
		lblParkName.setText(worker.getParkName());
		txtMaxOrders.setEditable(false);
		txtMaxVisitors.setEditable(false);
		txtVisitingTime.setEditable(false);
	}

	/**
	 * When turning on/off the max orders checkbox, the method set  Editable/Selected Status
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void chkMaxOrdersAction(ActionEvent event) {// turn on/off check box
		if (chkMaxOrders.isSelected()) {
			chkMaxOrders.setSelected(true);
			txtMaxOrders.setEditable(true);
		}

		else {
			chkMaxOrders.setSelected(false);
			txtMaxOrders.setEditable(false);
			txtMaxOrders.clear();
		}
	}

	/**
	 * When turning on/off the max visitor checkbox, the method set  Editable/Selected Status
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void chkMaxVisitorAction(ActionEvent event) {
		if (chkMaxVisitor.isSelected()) {
			chkMaxVisitor.setSelected(true);
			txtMaxVisitors.setEditable(true);
		} else {
			chkMaxVisitor.setSelected(false);
			txtMaxVisitors.setEditable(false);
			txtMaxVisitors.clear();
		}
	}

	/**
	 * When turning on/off the visiting time checkbox, the method set  Editable/Selected Status
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void chkVisitingTimeAction(ActionEvent event) {
		if (chkVisitingTime.isSelected()) {
			chkVisitingTime.setSelected(true);
			txtVisitingTime.setEditable(true);
		} else {
			chkVisitingTime.setSelected(false);
			txtVisitingTime.setEditable(false);
			txtVisitingTime.clear();
		}
	}

	/**
	 * Switch the screen to Park Report screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReport.fxml");
	}

	/**
	 * Switch the screen to Update Discount screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void UpdateDiscountAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/UpdateDiscount.fxml");

	}

	/**
	 * Switch the screen to Update Parameters screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void updateParametersAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/updateParameters.fxml");

	}

	/**
	 * Switch the screen to Park Manager Menu screen
	 * 
	 * @param event-the event that fired the method
	 */

	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkManagerMenu.fxml");

	}

	/**
	 * Switch the screen to Login Page screen
	 * 
	 * @param event-the event that fired the method
	 */

	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

}
