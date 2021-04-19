package client.controllers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import client.logic.Worker;
/**
 * This class is represents the sum of the 3 type of the visitors                       
 * in the park between the dates: "From" and "To"
 * @author tania daniel
 *
 */
public class ParkReportVisitorsController implements Initializable {

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
	private Label lblSingle;

	@FXML
	private Label lblSub;

	@FXML
	private Label lblGroup;

	@FXML
	private Button btnShow;

	@FXML
	private DatePicker datePickerFrom;

	@FXML
	private DatePicker datePickerTo;
	@FXML
	private Label lblstatus;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;                                                                          
	/**
	 * ans is help string for the function "split"
	 */

	public static String ans;

	private Worker worker;
	FileChooser fileChooser = new FileChooser();
/**
 *  this method shows in the department report controller the current time ,and current date
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

		LocalDate today = LocalDate.now();
		LocalDate firstOfThisMonth = today.withDayOfMonth(1);
		LocalDate firstOfLastMonth = firstOfThisMonth.minusMonths(1);
		LocalDate endOfLastMonth = firstOfThisMonth.minusDays(1);
		datePickerFrom.setValue(firstOfLastMonth);
		datePickerTo.setValue(endOfLastMonth);
		lblDate.setText(LocalDate.now().toString());

	}
	/**
     * Switch the screen to Park Report
     * @param event-the event that fired the method
     */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkReport.fxml");
	}
	/**
     * Switch the screen to Update Discount
     * @param event-the event that fired the method
     */
	@FXML
	void UpdateDiscountAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/UpdateDiscount.fxml");

	}
	/**
     * Switch the screen to Update Parameters
     * @param event-the event that fired the method
     */
	@FXML
	void updateParametersAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/updateParameters.fxml");

	}
	/**
     * Switch the screen to Park Manager Menu screen
     * @param event-the event that fired the method
     */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkManagerMenu.fxml");

	}
	/**
     * Switch the screen to Login Page screen
     * @param event-the event that fired the method
     */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}
	/**                                                                                   &&&&&&&&&&&
	 * This function is sum up and show the amount of the 3 types of visitors:
	 * single/subscriber/group
	 * in the given park that worker is works in
	 * and allows to create txt file with the data report
	 * @param event
	 */

	@FXML
	void Show(ActionEvent event) {
		ans = "";
		String[] temp;
		if (datePickerFrom.getValue() == null || datePickerTo.getValue() == null) {
			lblstatus.setText("Wrong date");
		}

		else {
			MessageCS message = new MessageCS(MessageType.REPORT_VISITORS, datePickerFrom.getValue().toString(),
					datePickerTo.getValue().toString(), worker);
			ClientUI.chat.accept(message);
			if (!(ans.equals(""))) {
				temp = ans.split(",");
				if (temp[0].equals("null"))
					temp[0] = "0";
				lblSingle.setText(temp[0]);
				if (temp[1].equals("null"))
					temp[1] = "0";
				lblGroup.setText(temp[1]);
				if (temp[2].equals("null"))
					temp[2] = "0";
				lblSub.setText(temp[2]);

				String strRep = "";

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
				Date date = new Date(System.currentTimeMillis());

				strRep += "Created: " + formatter.format(date) + "\n";
				strRep += "Visitors report\n";
				strRep += "Park name: " + worker.getParkName() + "\n";
				strRep += "From: " + datePickerFrom.getValue() + "\n";
				strRep += "To: " + datePickerTo.getValue() + "\n";
				strRep += "Single: " + temp[0] + "\n";
				strRep += "Subscriber: " + temp[2] + "\n";
				strRep += "Organized Group : " + temp[1];

				fileChooser.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
				File file = fileChooser.showSaveDialog(ClientUI.primaryStage);
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(strRep);
					fileWriter.close();
				} catch (Exception e) {

				}

			}
		}

	}

}
