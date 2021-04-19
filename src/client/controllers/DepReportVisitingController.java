package client.controllers;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;

/**
 * this class represents the Visiting report for that the department manager
 * creates
 * 
 * @author danie
 *
 */
public class DepReportVisitingController implements Initializable {

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnReports;

	@FXML
	private Button btnConfirmDiscounts;

	@FXML
	private Button btnConfirmParemters;

	@FXML
	private Button btnChange;

	@FXML
	private Button logOut;

	@FXML
	private DatePicker datepickerFrom;

	@FXML
	private DatePicker datePickerTo;

	@FXML
	private Button btnShow;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;

	/**
	 * ans is the returned value after the server searched for data in sql
	 */
	public static String ans;

	@FXML
	private ComboBox<String> comboxPark;

	/**
	 * initialize comboxPark with park names show current time show current date set
	 * datePickerFrom to start of last month set datePickerTo to end of last month
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboxPark.getItems().addAll("A", "B", "C");
		comboxPark.getSelectionModel().selectFirst();

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
		datepickerFrom.setValue(firstOfLastMonth);
		datePickerTo.setValue(endOfLastMonth);
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
	 * create hoursSingle, hoursFamily, hoursGuide each represents the hours of
	 * staying for single, family, guide ask the server for the data of entrance
	 * time from sql in a rage of dates create a graph that represents the amount of
	 * people that entered in a specific a time in the park for single, family,
	 * guide
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void Show(ActionEvent event) {
		ans = "";
		int[] hoursSingle = new int[13];
		int[] hoursFamily = new int[13];
		int[] hoursGuide = new int[13];
		for (int i = 0; i < 13; i++) {
			hoursSingle[i] = 0;
			hoursFamily[i] = 0;
			hoursGuide[i] = 0;
		}
		LocalDate from = datepickerFrom.getValue();
		LocalDate to = datePickerTo.getValue();
		if (Check(from) && Check(to)) {
			String From = from.toString();
			String To = to.toString();

			if (CheckFromBeforeTo(From, To)) {

				MessageCS message = new MessageCS(MessageType.REPORT_VISITING, datepickerFrom.getValue().toString(),
						datePickerTo.getValue().toString(), comboxPark.getValue());
				ClientUI.chat.accept(message);

				System.out.println(ans);
				ArrayList<Object> result = getVisitingArray(ans);
				hoursSingle = (int[]) result.get(0);
				hoursFamily = (int[]) result.get(2);
				hoursGuide = (int[]) result.get(1);



				// -----------------------------------------------------------
				CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel("Time");

				NumberAxis yAxis = new NumberAxis();
				yAxis.setLabel("Visits");

				ArrayList<Data<String, Number>> SingleArray = new ArrayList<Data<String, Number>>();
				for (int i = 0; i < 13; i++) {
					if (i + 9 < 10)
						SingleArray.add(
								new Data<String, Number>("0" + (i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursSingle[i]));
					else
						SingleArray
								.add(new Data<String, Number>((i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursSingle[i]));

				}

				ArrayList<Data<String, Number>> FamilyArray = new ArrayList<Data<String, Number>>();
				for (int i = 0; i < 13; i++) {
					if (i + 9 < 10)
						FamilyArray.add(
								new Data<String, Number>("0" + (i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursFamily[i]));
					else
						FamilyArray
								.add(new Data<String, Number>((i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursFamily[i]));
				}

				ArrayList<Data<String, Number>> SubscriberArray = new ArrayList<Data<String, Number>>();
				for (int i = 0; i < 13; i++) {
					if (i + 9 < 10)
						SubscriberArray.add(
								new Data<String, Number>("0" + (i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursGuide[i]));
					else
						SubscriberArray
								.add(new Data<String, Number>((i + 9) + ":00-" + (i + 9 + 1) + ":00", hoursGuide[i]));
				}

				final BarChart<String, Number> barchart = new BarChart<String, Number>(xAxis, yAxis);

				XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
				dataSeries1.setName("Single");
				dataSeries1.getData().addAll(SingleArray);
				barchart.getData().add(dataSeries1);

				XYChart.Series<String, Number> dataSeries2 = new XYChart.Series<String, Number>();
				dataSeries2.setName("Subscribers");
				dataSeries2.getData().addAll(FamilyArray);
				barchart.getData().add(dataSeries2);

				XYChart.Series<String, Number> dataSeries3 = new XYChart.Series<String, Number>();
				dataSeries3.setName("Organized Group");
				dataSeries3.getData().addAll(SubscriberArray);
				barchart.getData().add(dataSeries3);

				final Stage dialog = new Stage();
				Scene dialogScene = new Scene(barchart, 1600, 900);
				dialog.setScene(dialogScene);
				dialog.setTitle("Visiting Time Graph");
				dialog.show();
			}
		}
	}

	public boolean Check(LocalDate date) {
		if (date != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean CheckFromBeforeTo(String from, String to) {
		LocalDate From = LocalDate.now();
		LocalDate To = LocalDate.now();
		try {
			From = LocalDate.parse(from);
			To = LocalDate.parse(to);
		} catch (Exception e) {

		}
		return (From.isBefore(To) || From.equals(To));
	}

	public ArrayList<Object> getVisitingArray(String ans) {
		int[] hoursSingle = new int[13];
		int[] hoursFamily = new int[13];
		int[] hoursGuide = new int[13];
		for (int i = 0; i < 13; i++) {
			hoursSingle[i] = 0;
			hoursFamily[i] = 0;
			hoursGuide[i] = 0;
		}
		if (!ans.equals("")) {
			String[] temp;
			String[] RepDepRows = ans.split("=");
			for (String row : RepDepRows) {
				temp = row.split(",");
				if (temp[0].equals("Single")) {
					hoursSingle[Integer.parseInt(temp[2].substring(0, 2)) - 9] += Integer.parseInt(temp[1]);
				}
				if (temp[0].equals("Guide")) {
					hoursGuide[Integer.parseInt(temp[2].substring(0, 2)) - 9] += Integer.parseInt(temp[1]);
				}
				if (temp[0].equals("Family")) {
					hoursFamily[Integer.parseInt(temp[2].substring(0, 2)) - 9] += Integer.parseInt(temp[1]);
				}
			}
		}
		ArrayList<Object> array = new ArrayList<Object>();
		array.add(hoursSingle);
		array.add(hoursGuide);
		array.add(hoursFamily);
		return array;
	}

}
