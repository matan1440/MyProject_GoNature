package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.util.Duration;

/**
 * This class is represents the Park Report Controller that allows to choose
 * which report create: Usage Report/Income Report/Visitors Report
 * 
 * @author tania & daniel
 *
 */

public class ParkReportController implements Initializable {

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

	@FXML
	private Button btnVisitorsReport;

	@FXML
	private Button btnIncomeReport;

	@FXML
	private Button btnUsageReport;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;



	/**
	 * this method shows in the department report controller the current time ,and
	 * current date
	 * 
	 * @param location - location
	 * @param resources - resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
	 * Switch the screen to Park Report
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReport.fxml");
	}

	/**
	 * Switch the screen to Update Discount
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void UpdateDiscountAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/UpdateDiscount.fxml");

	}

	/**
	 * Switch the screen to Update Parameters
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void updateParametersAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/updateParameters.fxml");

	}

	/**
	 * Switch the screen to Park Manager Menu
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkManagerMenu.fxml");

	}

	/**
	 * Switch the screen to Login Page
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * Switch the screen to ParkReportVisitors screen
	 * 
	 * @param event-the event that fired the method
	 */

	@FXML
	void VisitorsReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReportVisitors.fxml");
	}

	/**
	 * Switch the screen to Park Report Income screen
	 * 
	 * @param event-the event that fired the method
	 */

	@FXML
	void IncomeReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReportIncome.fxml");
	}

	/**
	 * Switch the screen to Park Report Usage screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void btnUsageReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReportUsage.fxml");
	}

}
