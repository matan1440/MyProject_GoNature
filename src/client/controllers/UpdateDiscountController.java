package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import client.logic.Worker;

/**
 * This class is represents the Discounts update, park manager can choose to
 * enter between the dates: "From" and "To" and send to Department manager for
 * confermation and create txt file of the report.
 * 
 * @author tania daniel
 *
 */
public class UpdateDiscountController implements Initializable {

	@FXML
	private Label lblParkName;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;

	@FXML
	private TextField txtUpdateDiscount;

	@FXML
	private Label lblStatus;

	@FXML
	private DatePicker DateFrom;

	@FXML
	private DatePicker DataTo;

	@FXML
	private Text txtFromDiscount;

	@FXML
	private Text txtToDiscount;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnUpdateParemters1;

	@FXML
	private Button btnReports1;

	@FXML
	private Button btnUpdateDiscount1;

	@FXML
	private Button logOut;

	private Worker worker;
	/**
	 * updateDiscount is the the new discount that the park manager is choose to
	 * sent to request
	 */
	public static String updateDiscount;

	/**
	 * this method shows in the department report controller the current time ,and
	 * current date
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {// to use worker info
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
	}

	/**
	 * This function is sent to request the new discount that given by worker
	 * between the dates "From" and "To" also it allows to create txt file of the
	 * created report.
	 * 
	 * @param event
	 */
	@FXML
	void updateDiscount(ActionEvent event) {
		updateDiscount = null;// to check if update sent
		// send msg = UPDATE_DISCOUNT, discount, from , to , worker(for Park Name)
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(txtUpdateDiscount.getText());
		if (!(m.find() && m.group().equals(txtUpdateDiscount.getText()))) {
			lblStatus.setText("Wrong discount value");
			return;
		}
		if (Integer.parseInt(txtUpdateDiscount.getText()) < 0 || Integer.parseInt(txtUpdateDiscount.getText()) > 100) {
			lblStatus.setText("Wrong discount value");
			return;
		}

		if (DateFrom.getValue() == null || DataTo.getValue() == null || DateFrom.getValue().isAfter(DataTo.getValue()))
			lblStatus.setText("Wrong dates");
		else if (txtUpdateDiscount.getText().equals(""))
			lblStatus.setText("No  discount");

		else {
			MessageCS message = new MessageCS(MessageType.UPDATE_DISCOUNT, txtUpdateDiscount.getText(),
					DateFrom.getValue().toString(), DataTo.getValue().toString(), worker);

			ClientUI.chat.accept(message);

			if (updateDiscount != null) // if updated in sql change label
				lblStatus.setText(updateDiscount);
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
