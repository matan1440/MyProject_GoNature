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
 * entrance controller class.
 * a page from which the entrance worker can access his other pages: adding a visitor, adding a random
 * visitor, exit a visitor.
 * contains park amount variables, each time a visitor enters or exit the park, the variables are updated  
 * 
 * @version 1.0
 * @author tomer
 *
 */
public class EntranceController implements Initializable{

	
    @FXML
    private Label lblG;

    @FXML
    private Label lblName;

    @FXML
    private Label lblParkName;
	
    @FXML
    private Label lblTime;

    @FXML
    private Label lblDate;
	
	
    @FXML
    private Button logOut;

    @FXML
    private Button mainMenuBtn;

    @FXML
    private Button btnCapacity;

    @FXML
    private Button btnOccasionalVisit;

    @FXML
    private Label lblnumVis;
    
    @FXML
    private Button btnAddVisitor;
    
    @FXML
    private Button btnExit;

	private Worker worker;
	
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
	 *
	 *initialize the page with worker info like name and park he belongs to, number of current visitors in that park.
	 *show a date and clock showing time
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;
		
		MessageCS message0 = new MessageCS(MessageType.Get_Park_Amount, worker.getParkName());
		ClientUI.chat.accept(message0);
		
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        
			if(worker.getParkName().equals("A"))
				lblnumVis.setText(Number_Of_Visitors_A+"");
			
			if(worker.getParkName().equals("B"))
				lblnumVis.setText(Number_Of_Visitors_B+"");
			
			if(worker.getParkName().equals("C"))
				lblnumVis.setText(Number_Of_Visitors_C+"");
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
		
	    Timeline clock1 = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        LocalTime currentTime = LocalTime.now();

	        String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
	        lblTime.setText(curTime);
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock1.setCycleCount(Animation.INDEFINITE);
	    clock1.play();

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
		
	    
	}


	/**
	 * switch screen to entrance menu
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
    void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

    }
    
    /**
     * switch screen to login page
	 * 
	 * @param event - the event that caused the method
     */
    @FXML
    void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

    }

    /**
     * switch screen to add random visitor page
	 * 
	 * @param event - the event that caused the method
     */
    @FXML
    void clickAddOccasional(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/AddOccasional.fxml");
    }

    
    /**
     * switch screen to add visitor page
	 * 
	 * @param event - the event that caused the method
     */
    @FXML
    void clickAddVisitor(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/AddVisitor.fxml");
    }
    
    /**
     * switch screen to exit visit page
	 * 
	 * @param event - the event that caused the method
     */
    @FXML
    void clickExitVisit(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/ExitVisit.fxml");
    }

}
