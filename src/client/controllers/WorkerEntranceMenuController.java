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
 * This class is the controller of Worker Entrance Menu Screen.
 * 
 * @version 1.0
 * @author Tomer
 */
public class WorkerEntranceMenuController implements Initializable{

    @FXML
    private Label lblnumVis;
	
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
	private Button btnAccessControl;

	private Worker worker;


	/**
	 * initialize the entrance worker menu details, which worker is logged in, what park he works in,
	 * a clock showing time and a date.
	 * initialize current amount of visitors in the park the worker belongs to in the labels by sending message
	 * to server with worker park information and receiving amount on park currently.
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
		
		Timeline clock1 = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        
			if(worker.getParkName().equals("A"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_A+"");
			
			if(worker.getParkName().equals("B"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_B+"");
			
			if(worker.getParkName().equals("C"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_C+"");
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock1.setCycleCount(Animation.INDEFINITE);
	    clock1.play();
		
	}

    /**
     * switches screen to entrance main menu. 
     * 
     * @param event - the event that fired the method .
     */
    @FXML
    void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

    }
    
    /**
     * switches the screen to the entrance page
     * 
     * @param event - the event that fired the method .
     */
    @FXML
    void clickAccessControl(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/Entrance.fxml");

    }

    /**
     * switches the screen to the login page
     * 
     * @param event - the event that fired the method .
     */
    @FXML
    void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

    }


}
