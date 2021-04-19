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
import javafx.scene.control.Label;
import javafx.util.Duration;
import client.logic.Worker;
/**
 * this class  represents the Park Department Menu:                       
 * Department manager can choose: confirm Parameters/Create Reports/confirm Discount
 * @author tania & daniel
 *
 */
public class DepartmentManagerManuController  implements Initializable {

	@FXML
	private Button btnConfirmParemters;

	@FXML
	private Button btnReports;

	@FXML
	private Button btnConfirmDiscounts;
	@FXML
	private Button mainMenuBtn;
	
    @FXML
    private Label lblTime;
    
    @FXML
    private Label lblG;
    
    @FXML
    private Label lblDate;	
	   
    @FXML
    private Label lblName;

	@FXML
	private Button logOut;
	
	@FXML
	private Label lblParkName;
	
	private Worker worker;
	
    @FXML
    private Label lblnumVisA;

    @FXML
    private Label lblnumVisB;

    @FXML
    private Label lblnumVisC;
    /**
     * Number_Of_Visitors_A is the number of the visitors in the park A
     */
    public static int Number_Of_Visitors_A=0;
    /**
     * Number_Of_Visitors_B is the number of the visitors in the park B
     */
	public static int Number_Of_Visitors_B=0;
	 /**
     * Number_Of_Visitors_C is the number of the visitors in the park C
     */
	public static int Number_Of_Visitors_C=0;

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
	/**
	 * this method shows in the department menu controller the current time ,Good Morning"/Good Afternoon/Good Evening depends on the time
	 * and the amount of visitors in each park
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;
		
	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        LocalTime currentTime = LocalTime.now();

	        String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
	        lblTime.setText(curTime);
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	    lblName.setText(worker.getFirstName());
	    lblDate.setText(LocalDate.now().toString());
	    
	    
		if(LocalTime.parse("12:00").isAfter(LocalTime.now()) &&  LocalTime.parse("06:00").isBefore(LocalTime.now()) ) {
			lblG.setText("Good Morning");
			lblName.setLayoutX(470.0);
		}
		
		if(LocalTime.parse("17:00").isAfter(LocalTime.now()) &&  LocalTime.parse("12:00").isBefore(LocalTime.now()) ) {
			lblG.setText("Good Afternoon");
			lblName.setLayoutX(497.0);
		}
		
		if(LocalTime.parse("17:00").isBefore(LocalTime.now()) ) {
			lblName.setLayoutX(460.0);
			lblG.setText("Good Evening");
		}
		
		if(LocalTime.parse("06:00").isAfter(LocalTime.now()) ) {
			lblName.setLayoutX(460.0);
			lblG.setText("Good Evening");
		}
		
		MessageCS messageA = new MessageCS(MessageType.Get_Park_Amount, "A");
		ClientUI.chat.accept(messageA);
		
		MessageCS messageB = new MessageCS(MessageType.Get_Park_Amount, "B");
		ClientUI.chat.accept(messageB);
		
		MessageCS messageC = new MessageCS(MessageType.Get_Park_Amount, "C");
		ClientUI.chat.accept(messageC);
		
		Timeline clock2 = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
				lblnumVisA.setText(Number_Of_Visitors_A+"");

				lblnumVisB.setText(Number_Of_Visitors_B+"");
			
				lblnumVisC.setText(Number_Of_Visitors_C+"");
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock2.setCycleCount(Animation.INDEFINITE);
	    clock2.play();
		
	}

}
