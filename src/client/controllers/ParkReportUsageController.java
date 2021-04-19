package client.controllers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import client.logic.ReportUsage;
import client.logic.Worker;
/**                                                                               
 * This class is represent a Park Usage Report
 * it shows in the table in the given date
 * the percentage of the use of the specific park (from maximum that allowed on this date).
 * @author tania daniel
 *
 */
public class ParkReportUsageController implements Initializable {

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
	private TableView<ReportUsage> tableView;

	@FXML
	private TableColumn<ReportUsage, String> colTime;

	@FXML
	private TableColumn<ReportUsage, Integer> colVisitorsAmount;

	@FXML
	private TableColumn<ReportUsage, String> colMaxVisitors;

	@FXML
	private TableColumn<ReportUsage, String> colPrecent;

	@FXML
	private Button btnShow;

	@FXML
	private DatePicker dateUsageDay;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;

	int[] openHours = new int[13];
	
	/**                                                                                      
	 * dateMaxVisitors is the date that we want to check on the maximum visitors
	 */

	public static int dateMaxVisitors;
	
	/**
	 * ans is the help string for function "split"
	 */
	public static String ans;

	private Worker worker;

	FileChooser fileChooser = new FileChooser();

	/**
	 *  this method shows in the department report controller the current time ,and current date
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;

		colTime.setCellValueFactory(new PropertyValueFactory<ReportUsage, String>("time"));
		colVisitorsAmount.setCellValueFactory(new PropertyValueFactory<ReportUsage, Integer>("amount"));
		colMaxVisitors.setCellValueFactory(new PropertyValueFactory<ReportUsage, String>("maxVisitors"));
		colPrecent.setCellValueFactory(new PropertyValueFactory<ReportUsage, String>("precent"));

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
     * Switch the screen to Park Report  screen
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
     * Switch the screen to Park Manager Menu
     * @param event-the event that fired the method
     */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ParkManagerMenu.fxml");

	}
	/**
     * Switch the screen to Login Page
     * @param event-the event that fired the method
     */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}
	/**                                                                           
	 * This function is calculate the percentage of the amount of the visitors in the given park 
	 * that the worker is working  in and on the given date 
	 * the data is shown by hours in the table
	 * also it create txt file with the  created report.
	 * @param event
	 * @throws ParseException
	 */

	@FXML
	void Show(ActionEvent event) throws ParseException {
		ReportUsage usage;
		final ObservableList<ReportUsage> data = FXCollections.observableArrayList();

		for (int i = 0; i < openHours.length; i++)
			openHours[i] = 0;
		ans = "";
		ArrayList<String> allDate = new ArrayList<String>();

		if (dateUsageDay.getValue() != null) {
			
			
			MessageCS message = new MessageCS(MessageType.REPORT_USAGE, dateUsageDay.getValue().toString(), worker);
			ClientUI.chat.accept(message);

			if (!(ans.equals(""))) {
				String[] temp;
				String[] Rows;
				// ArrayList<String []> data = new ArrayList<>();
				int amount = 0;
				int i;
				int start = 0;
				int end = 0;

				Rows = ans.split("=");

				for (String R : Rows) {
					temp = R.split(",");
					for (String j : temp) {
						allDate.add(j);
					}
				}

				for (i = 0; i < allDate.size(); i++) {
					amount = Integer.parseInt(allDate.get(i));
					i++;
					start = Integer.parseInt(allDate.get(i).substring(0, 2));
					i++;
					end = Integer.parseInt(allDate.get(i).substring(0, 2));
					if(Integer.parseInt(allDate.get(i).substring(3, 5)) > 0 || Integer.parseInt(allDate.get(i).substring(6, 8)) > 0 )
						end += 1;
					
					for (; start < end; start++)
						openHours[start - 9] += amount;
				}
				for (i = 9; i < 22; i++) {
					if (i < 10)
						usage = new ReportUsage("0" + i + ":00 - "+ (i+1) + ":00", openHours[i - 9], String.valueOf(dateMaxVisitors), String.format("%.2f",(((float)(openHours[i - 9])/dateMaxVisitors)* 100 )) );

					else
						usage = new ReportUsage(i + ":00 - "+ (i+1)  + ":00", openHours[i - 9], String.valueOf(dateMaxVisitors), String.format("%.2f",(((float)(openHours[i - 9])/dateMaxVisitors)* 100 )));

					data.add(usage);
				}

			} 
			else {
 
				for (int i = 9; i < 22; i++) {
					if (i < 10)
						usage = new ReportUsage("0" + i + ":00 - "+ (i+1) + ":00", openHours[i - 9], String.valueOf(dateMaxVisitors), String.valueOf(((float)(openHours[i - 9])/dateMaxVisitors)* 100 ));

					else
						usage = new ReportUsage(i + ":00 - "+ (i+1)  + ":00", openHours[i - 9], String.valueOf(dateMaxVisitors), String.valueOf(((float)(openHours[i - 9])/dateMaxVisitors)* 100 ));

					data.add(usage);

				}
			}
			
			tableView.setItems(FXCollections.observableArrayList());
			tableView.setItems(data);
			
			String strRep ="";

			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());
			
			strRep +=  "Created: "+formatter.format(date) + "\n" ;
			strRep += "Usage report\n";
			strRep += "Park name: " + worker.getParkName() +"\n";
			strRep += "Date: " + dateUsageDay.getValue() +"\n";
			strRep += "Time\t\tVisitors Amount\t\tMax Visitors\t\t%\n";
			strRep +="-----------------------------------------------------------------------------------------\n";
			for (int i = 9; i<22;i++) {
				String t = ""+i;
				if(i<10)
					t = "0"+i;
				strRep += t + ":00 - "+ (i+1) + ":00"+ "\t\t" + openHours[i - 9]+ "     \t\t   "+ dateMaxVisitors+ "\t\t      "+ String.format("%.2f",(((float)(openHours[i - 9])/dateMaxVisitors)* 100 ))+"\n" ; 
				strRep +="-----------------------------------------------------------------------------------------\n";
				
			}
			

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
