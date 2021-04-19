package client.controllers;

import java.net.URL;
import java.time.LocalDate;

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
import java.time.LocalTime;

/**
 * this class represents the menu of reports for department manager
 * 
 * @author danie
 *
 */
public class DepReportController implements Initializable {

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
	private Button btnCancellationReport;

	@FXML
	private Button btnVisitsReport;
	@FXML
	private Button btnStayingReport;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;

	/**
	 * this method shows in the department report controller the current time ,and
	 * current date
	 * 
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
	 * Switch the screen to Dep Report Cancellation screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void CancellationReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReportCancellation.fxml");
	}

	/**
	 * Switch the screen to Dep Report Visiting screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void VisitsReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReportVisiting.fxml");
	}

	/**
	 * Switch the screen to Dep Report Staying screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void StayingReport(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReportStaying.fxml");

	}

}
