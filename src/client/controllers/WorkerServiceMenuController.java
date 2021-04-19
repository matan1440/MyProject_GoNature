package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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
import client.logic.Worker;

/**
 * This class is the controller of Worker Service Menu Screen.
 * 
 * @version 1.0
 * @author tomer
 *
 */
public class WorkerServiceMenuController implements Initializable {

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
	private Button btnCreateSubscriber;

	private Worker worker;


	/**
	 * initialize the service menu details , what worker is looged in, what park he belongs to.
	 * initialize the visibily status of the buttons, initialize the a clock showing time and date.
	 * 
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
			lblName.setLayoutX(475.0);
		}
		
		if(LocalTime.parse("17:00").isAfter(LocalTime.now()) &&  LocalTime.parse("12:00").isBefore(LocalTime.now()) ) {
			lblG.setText("Good Afternoon");
			lblName.setLayoutX(505.0);
		}
		
		if(LocalTime.parse("17:00").isBefore(LocalTime.now()) ) {
			lblName.setLayoutX(465.0);
			lblG.setText("Good Evening");
		}
		
		if(LocalTime.parse("06:00").isAfter(LocalTime.now()) ) {
			lblName.setLayoutX(465.0);
			lblG.setText("Good Evening");
		}
		
		lblName.setText(worker.getFirstName());
		lblDate.setText(LocalDate.now().toString());
		lblParkName.setText(worker.getParkName());
	}


    /**
     * Switch the screen to main menu of service worker.
	 * 
	 * @param event - the event that fired the method .
     */
    @FXML
    void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerServiceMenu.fxml");

    }
    
    /**
     * Switch the screen to create subscriber page .
	 * 
	 * @param event - the event that fired the method .
     */
    @FXML
    void clickCreateSubscriber(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/CreateSub.fxml");

    }
    
    /**
     * Switch the screen to login page .
	 * 
	 * @param event - the event that fired the method .
     */
    @FXML
    void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

    }


}
