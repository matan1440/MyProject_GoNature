package client.controllers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.ParseException;
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
 * This class is represents an income report of the park. after getting two
 * dates: "From" and "To" the income that summed up is: of the park that the
 * worker is belongs to and between the dates: "From" and "To"
 * 
 * @author tania  daniel
 *
 */

public class ParkReportIncomeController implements Initializable {

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
	private Button btnShow;

	@FXML
	private Label lblPark;

	@FXML
	private DatePicker dateFrom;

	@FXML
	private DatePicker dateTo;

	@FXML
	private Label lblIncome;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;
	/**
	 * ans is helper variable for function "split"
	 */

	public static String ans;

	private Worker worker;

	FileChooser fileChooser = new FileChooser();

	/**
	 * this method shows in the department report controller the current time ,and
	 * current date
	 * 
	 * @param location - location
	 * @param resources - resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;
		lblPark.setText(worker.getParkName());

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
		dateFrom.setValue(firstOfLastMonth);
		dateTo.setValue(endOfLastMonth);
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
	 * This method is sum up the income of the specific park between given dates
	 *  and save the report that created as a txt file
	 * 
	 * @param event -the event that fired the method throws ParseException
	 */

	@FXML
	void Show(ActionEvent event) throws ParseException {
		ans = "";
		double incomeInt = 0;

		if (dateFrom.getValue() != null && dateTo.getValue() != null) {
			MessageCS message = new MessageCS(MessageType.REPORT_INCOME, worker.getParkName());
			ClientUI.chat.accept(message);
			if (!ans.equals("")) {
				String[] income = ans.split("=");
				String[] temp;

				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				Date From = sdformat.parse(dateFrom.getValue().toString());
				Date To = sdformat.parse(dateTo.getValue().toString());

				for (String i : income) {
					temp = i.split(",");
					Date date = sdformat.parse(temp[0]);

					if ((date.after(From) || From.compareTo(date) == 0) && (date.before(To) || To.compareTo(date) == 0))
						incomeInt += Double.parseDouble(temp[1]); // Convert From String -> Double , Instead Of String
																	// -> Int
				}
			}
			lblIncome.setText(String.valueOf(incomeInt) + "  NIS");
			// ------------------------------------
			String strRep = "";

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());

			strRep += "Created: " + formatter.format(date) + "\n";
			strRep += "Income report\n";
			strRep += "Park name: " + worker.getParkName() + "\n";
			strRep += "From: " + dateFrom.getValue() + "\n";
			strRep += "To: " + dateTo.getValue() + "\n";
			strRep += "Income: " + incomeInt + "  NIS \n";

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
