package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Message;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import client.logic.Worker;

/**
 * this class represents the confirmation of a parameter (max visitors, max
 * orders, visiting time)
 * 
 * @author danie
 * 
 *
 */
public class ConfirmParametersController implements Initializable {
	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnReports;

	@FXML
	private Button btnConfirmDiscounts;

	@FXML
	private Button btnConfirmParemters;

	@FXML
	private Button logOut;

	@FXML
	private TextField txtCMaxVisitors;

	@FXML
	private TextField txtCVisitingTime;

	@FXML
	private TextField txtCMaxOrders;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnShowRequests;

	@FXML
	private TextField txtNMaxVisitors;

	@FXML
	private TextField txtNVisitingTime;

	@FXML
	private TextField txtNMaxOrders;

	@FXML
	private Button btnConfirmParameters;

	@FXML
	private Button btnDeclineParameters;

	@FXML
	private CheckBox chkMaxVisitors;

	@FXML
	private CheckBox chkVisitingTime;

	@FXML
	private CheckBox chkMaxOrders;

	@FXML
	private ComboBox<String> comBoxParkName;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;
	/**
	 * requestPar is the returned value after the server searched for data in sql
	 */
	public static String requestPar;

	private Worker worker;

	/**
	 * check if the checkbox of a parameter is checked and the text field is not
	 * empty ask the server to the change the paramter in the park in the sql show
	 * if action succsseful or show nothing in the label below
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmParameters(ActionEvent event) {
		requestPar = "";
		if (chkVisitingTime.isSelected())
			if (!(txtNVisitingTime.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_VISITNG_TIME, txtNVisitingTime.getText(), "Yes",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}

		if (chkMaxVisitors.isSelected())
			if (!(txtNMaxVisitors.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_MAX_VISITOR, txtNMaxVisitors.getText(), "Yes",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}

		if (chkMaxOrders.isSelected())
			if (!(txtNMaxOrders.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_MAX_ORDERS, txtNMaxOrders.getText(), "Yes",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}
		if (!(requestPar.equals("")))
			lblStatus.setText("Action Succsefull");
		else
			lblStatus.setText("");
		ShowRequests(new ActionEvent());

	}

	/**
	 * check if the checkbox of a parameter is checked and the text field is not
	 * empty ask the server to delete the request from the sql show if action
	 * succsseful or show nothing in the label below
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void DeclineParameters(ActionEvent event) {
		requestPar = "";
		if (chkVisitingTime.isSelected())
			if (!(txtNVisitingTime.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_VISITNG_TIME, txtNVisitingTime.getText(), "No",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}

		if (chkMaxVisitors.isSelected())
			if (!(txtNMaxVisitors.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_MAX_VISITOR, txtNMaxVisitors.getText(), "No",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}

		if (chkMaxOrders.isSelected())
			if (!(txtNMaxOrders.getText().equals(""))) {

				MessageCS message = new MessageCS(MessageType.CHANGE_MAX_ORDERS, txtNMaxOrders.getText(), "No",
						comBoxParkName.getValue());// send max visitors request
				ClientUI.chat.accept(message);
			}
		if (!(requestPar.equals("")))
			lblStatus.setText("Action Succsefull");
		else
			lblStatus.setText("");
		ShowRequests(new ActionEvent());

	}

	/**
	 * send a request for the server to search for a request to change a paramter of
	 * a specific park in the sql show the requested parameter to change
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ShowRequests(ActionEvent event) {
		requestPar = null;
		String[] par = null;
		MessageCS message = new MessageCS(MessageType.REQUEST_PARAMETERS, comBoxParkName.getValue());// send max
																										// visitors
																										// request
		System.out.println(comBoxParkName.getValue());
		ClientUI.chat.accept(message);
		if (requestPar != null) // change label if changed in sql
			par = requestPar.split(",");
		txtNMaxVisitors.setText(par[0]);
		txtNVisitingTime.setText(par[1]);
		txtNMaxOrders.setText(par[2]);
		txtCMaxVisitors.setText(par[3]);
		txtCVisitingTime.setText(par[5]);
		txtCMaxOrders.setText(par[4]);

	}

	/**
	 * initialize the comBoxParkName with park names show current time show current
	 * date
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;
		comBoxParkName.getItems().addAll("A", "B", "C");
		comBoxParkName.getSelectionModel().selectFirst();

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();

			String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
					currentTime.getSecond());
			lblTime.setText(curTime);

		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();

		lblDate.setText(LocalDate.now().toString());
	}

	/**
	 * Switch the screen to confirm Discount screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmDiscounts(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmDiscount.fxml");
	}

	/**
	 * Switch the screen to confirm Parameters screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmParemters(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmParameters.fxml");

	}

	/**
	 * Switch the screen to Dep Report screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReport.fxml");

	}

	/**
	 * Switch the screen to DepartmentM anager Menu screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepartmentManagerMenu.fxml");

	}

	/**
	 * Switch the screen to login Page screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * if chkMaxOrders is checked, uncheck it else check chkMaxOrders
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void MaxOrders(ActionEvent event) {
		if (chkMaxOrders.isSelected())
			chkMaxOrders.setSelected(true);
		else
			chkMaxOrders.setSelected(false);

	}

	/**
	 * if chkMaxVisitors is checked, uncheck it else check chkMaxVisitors
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void MaxVisitors(ActionEvent event) {
		if (chkMaxVisitors.isSelected())
			chkMaxVisitors.setSelected(true);
		else
			chkMaxVisitors.setSelected(false);
	}

	/**
	 * if chkVisitingTime is checked, uncheck it else check chkVisitingTime
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void VisitingTime(ActionEvent event) {
		if (chkVisitingTime.isSelected())
			chkVisitingTime.setSelected(true);
		else
			chkVisitingTime.setSelected(false);

	}

}
