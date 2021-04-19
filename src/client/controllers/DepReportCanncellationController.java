package client.controllers;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.ZoneId;
/**
 * this class represents the canncellation report for that the department manager creates
 * @author danie
 *
 */
public class DepReportCanncellationController implements Initializable {


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
	private ComboBox<String> comboxPark;

	@FXML
	private TextField txtCancelation;

	@FXML
	private TextField txtNotArrived;

    @FXML
    private Button btnShow;
    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;
    @FXML
    private Label lblStatus;
    
    @FXML
    private Label lblTime;
         
    
    @FXML
    private Label lblDate;	
    /**
     * ans is the returned value after the server searched for data in sql
     */
	public static String ans;



	
	/**
	 * check if date from is earlier or same as date to
	 * show the amount of not arrived in the range of date from-to
	 * show the amount canclled in the range of date from-to
	 * @param event-the event that fired the method
	 * @throws ParseException
	 */
	@FXML
	void Show(ActionEvent event) throws ParseException {
		txtCancelation.clear();
		txtNotArrived.clear();
		lblStatus.setText("");
		String[] temp;
		int i;
		int notArrived = 0;
		int canceled = 0;
		ans = "";
		if(datePickerFrom.getValue().isAfter(datePickerTo.getValue())) {
			lblStatus.setText("To is earlier than From");
			return;
		}
		MessageCS message = new MessageCS(MessageType.SHOW_CANCEL_REPORT, comboxPark.getValue(),datePickerFrom.getValue().toString(),datePickerTo.getValue().toString());

		ClientUI.chat.accept(message);

		if (!ans.equals("")) {
			ZoneId defaultZoneId = ZoneId.systemDefault();
			LocalDate curr = LocalDate.now();
			Date date = Date.from(curr.atStartOfDay(defaultZoneId).toInstant());
			String[] RepDepRows = ans.split("=");
			
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			for (i = 0; i < RepDepRows.length; i++) {
				temp = RepDepRows[i].split(",");
				Date date1 = sdformat.parse(temp[0]);

				if (date1.compareTo(date) < 0 &&  temp[2].equals("not arrived")) {
					notArrived += Integer.parseInt(temp[1]);
				}

				else if (temp[2].equals("Canceled")) {
					canceled += Integer.parseInt(temp[1]);;
				}
			}
		}
		txtNotArrived.setText(String.valueOf(notArrived));
		txtCancelation.setText(String.valueOf(canceled));
	}


	/**
	 * initialize comboxPark with park names
	 * show current time
	 * show current date
	 * set datePickerFrom to start of last month
	 * set datePickerTo to end of last month
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboxPark.getItems().addAll("A", "B", "C");
		comboxPark.getSelectionModel().selectFirst();
		
		  Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
		        LocalTime currentTime = LocalTime.now();

		        String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
		        lblTime.setText(curTime);
		        
		    }),
		         new KeyFrame(Duration.seconds(1))
		    );
		    clock.setCycleCount(Animation.INDEFINITE);
		    clock.play();
		
			LocalDate today = LocalDate.now();
			LocalDate firstOfThisMonth = today.withDayOfMonth( 1 );
			LocalDate firstOfLastMonth = firstOfThisMonth.minusMonths( 1 );
			LocalDate endOfLastMonth = firstOfThisMonth.minusDays( 1 );
			datePickerFrom.setValue(firstOfLastMonth);
			datePickerTo.setValue(endOfLastMonth);		
			lblDate.setText(LocalDate.now().toString());

	}
	/**
	 * Switch the screen to confirm Discount screen
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmDiscounts(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmDiscount.fxml");
	}
	/**
	 * Switch the screen to confirm Parameters screen
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmParemters(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmParameters.fxml");

	}
	/**
	 * Switch the screen to Dep Report screen
	 * @param event-the event that fired the method
	 */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReport.fxml");

	}
	/**
	 * Switch the screen to DepartmentM anager Menu screen
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepartmentManagerMenu.fxml");

	}
	/**
	 * Switch the screen to login Page screen
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

}
