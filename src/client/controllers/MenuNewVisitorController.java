package client.controllers;

import java.net.URL;
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
import client.logic.Subscriber;

/**
 * This Class is for MenuNewVisitorController 
 * The class contains methods for the menu Navigation + initialize the clock
 * @author Omer
 *
 */
public class MenuNewVisitorController implements Initializable {

	@FXML
	private Button btnCreateOrder;
	
	@FXML
	private Button logOut;

	@FXML
	private Button mainMenuBtn;
	
	
	/**
	 * subscriber object for the screen
	 */
	private Subscriber subscriber;

	/**
	 * id_visitor object for the screen
	 */
	private String id_visitor;
	
    @FXML
    private Label lblTime;


    /**
     * This method initialize the subscriber and id_visitor info objects
     * subscriber object - object of subscriber in case the visitor is subscriber
     * id_visitor - the id of the visitor
     * initialize the clock
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.subscriber = loginPageController.subscriber;
		this.id_visitor = loginPageController.id_visitor;
		
	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        LocalTime currentTime = LocalTime.now();

	        String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
	        lblTime.setText(curTime);
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();

		

		
	}

	/**
	 * Switch the screen to main menu .
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/MenuNewVisitor.fxml");

	}

	/**
	 * Log out from the system - return the travler to login page
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}
	
	/**
	 * Switch the screen to create order page .
	 * 
	 * @param event - the event that fired the method .
	 */
    @FXML
    void clickCreateOrder(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/CreateOrder.fxml");
    }


}
