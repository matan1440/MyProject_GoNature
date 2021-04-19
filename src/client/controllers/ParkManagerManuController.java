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
 * this class is represents the Park Manager Menu:                       
 * park manager can choose: Update Parameters/Create Reports/Update Discount
 * @author tania & daniel
 *
 */

public class ParkManagerManuController implements Initializable  {

    @FXML
    private Button btnUpdateParemters;

    @FXML
    private Button btnReports;
    
    @FXML
    private Button btnUpdateDiscount;

    
    @FXML
    private Button mainMenuBtn;
    
    @FXML
    private Label lblG;

    @FXML
    private Button logOut;

    @FXML
    private Label lblTime;
    
    @FXML
    private Label lblName;
    
    @FXML
    private Label lblDate;
    
    @FXML
    private Label lblParkName;

    @FXML
    private Label lblnumVis;
    
    @FXML
    private Label lblmaxVistor;

    @FXML
    private Label lblmaxOrder;

    @FXML
    private Label lblvisittime;
    
    private Worker worker;                                                                            
    /**
     * Number_Of_Visitors_A is the number of the visitors in the park A
     */
    
	public static int Number_Of_Visitors_A=0;
    /**
     *Number_Of_Visitors_B is the number of the visitors in the park B
     */
	public static int Number_Of_Visitors_B=0;
	/**
	 *Number_Of_Visitors_C is the number of the visitors in the park C
	 */
	public static int Number_Of_Visitors_C=0;                                                        
	/**
     * maxVisitors is the maximum number of the  visitors in the park 
     */
    
	public static int maxVistor=0;
	/**
	 * * maxOrders is the maximum number of the orders in the park 
	 */
	public static int maxOrders=0;
	/**
	 *  visittime is the maximum duration of the visit in the park 
	 */
	public static double visittime=0;
	/**
	 *  this method shows in the department report controller the current time ,and current date
	 * @param location
	 * @param resources
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
		
		lblName.setText(worker.getFirstName());
		lblDate.setText(LocalDate.now().toString());
		lblParkName.setText(worker.getParkName());
		
		
		MessageCS message0 = new MessageCS(MessageType.Get_Park_Amount, worker.getParkName());
		ClientUI.chat.accept(message0);
		
		Timeline clock2 = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        
			if(worker.getParkName().equals("A"))
				lblnumVis.setText(Number_Of_Visitors_A+"");
			
			if(worker.getParkName().equals("B"))
				lblnumVis.setText(Number_Of_Visitors_B+"");
			
			if(worker.getParkName().equals("C"))
				lblnumVis.setText(Number_Of_Visitors_C+"");
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock2.setCycleCount(Animation.INDEFINITE);
	    clock2.play();
		
	    
		MessageCS message1 = new MessageCS(MessageType.Get_Park_Params, worker.getParkName());
		ClientUI.chat.accept(message1);
	    
		lblmaxVistor.setText(maxVistor + "");
		lblmaxOrder.setText(maxOrders + "");
		lblvisittime.setText(visittime + "");
	}

/**
 * Switch the screen to park Report
 * @param event-the event that fired the method
 */
    @FXML
    void CreateReportsAction(ActionEvent event) {
    	
    	ScreenController.switchScenes("/client/boundary/ParkReport.fxml");
    }
    
    /**
     * Switch the screen to Update Discount screen
     * @param event-the event that fired the method
     */
    @FXML
    void UpdateDiscountAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/UpdateDiscount.fxml");

    }
    /**
     * Switch the screen to Update Parameters screen
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

}
