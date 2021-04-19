package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import client.logic.Worker;

/**
 * This class is the controller of exit visit Screen - it contains
 * methods for handling searching and checking for a visit id, and existing a visitor from the park,
 * updating park visitors amount and the visit information in database. 
 * 
 * @version 1.0
 * @author tomer
 *
 */
public class ExitVisitController implements Initializable{
	
    @FXML
    private Label lblnumVis;
	
    @FXML
    private Label lblG;

    @FXML
    private Label lblName;

	
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
    private Button btnAddVisitor;
    
    @FXML
    private Button btnExit;

    @FXML
    private TextField txtVisitID;

    @FXML
    private Button btnExitVisitor;

    @FXML
    private Button btnCheckVisitID;

	private Worker worker;
	
	/**
	 * current number of visitors in the park
	 */
	public static int current_number_of_visitors = 0;
	
	/**
	 * check if the visitor can exit the park
	 */
    public static boolean visitID_yes_no;

	/**
	 * switches screen to entrance worker menu.
	 * 
	 * @param event - the event that caused the method 
	 */
	@FXML
    void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

    }
    
    /**
	 * switches screen to login page.
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

    }

    /**
	 * switches screen to add occasional visitor page (add random visitor).
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickAddOccasional(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/AddOccasional.fxml");
    }

    
    /**
     * switches screen to add visitor page.
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickAddVisitor(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/AddVisitor.fxml");
    }
    
    /**
     * switches screen to exit visit page.
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickExitVisit(ActionEvent event) {
    	ScreenController.switchScenes("/client/boundary/ExitVisit.fxml");
    }
    
    /**
     * check id button,this method sends message to server checking if visit id is valid to this park and time
     * and opens the exit button if it is.
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickCheckVisitID(ActionEvent event) {
    	
    	if (InputTestFunctions.ValidateNumber(txtVisitID.getText())) {
    	
    		MessageCS message = new MessageCS(MessageType.CHECK_VISIT_ID,txtVisitID.getText());
    		ClientUI.chat.accept(message);
		
    		if(visitID_yes_no) {
    			btnExitVisitor.setDisable(false);
    		
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Done");
    			alert.setHeaderText("You can exit");
    			alert.setContentText("The visitor can now exit");
    			alert.showAndWait();
    		}
    		else {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("Sorry");
    			alert.setHeaderText("The visit id is incorrect");
    			alert.setContentText(" park workers will check your id ");
				alert.showAndWait();
    		}
		
    	}
		
    }
    /**
     * this method sends message to server with visit id and update in database
     * visitor exited the park.
     * send another message with new current amount of visitors in the park to update
     * the variables which workers pages get number of visitors from
     * 
     * @param event - the event that caused the method 
     */
    @FXML
    void clickExitVisitor(ActionEvent event) {
    	MessageCS message = new MessageCS(MessageType.VISITOR_EXIT_PARK,txtVisitID.getText());
		ClientUI.chat.accept(message);
		
		MessageCS message0 = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, current_number_of_visitors, worker.getParkName());
		ClientUI.chat.accept(message0);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText("Visitor existed");
		alert.setContentText("We hope you enjoyed your visit (:");
		alert.showAndWait();
    }


    /**
     *initialize the page, sets clock showing time and date, show worker name and park he belongs to,
     *show current amount of visitors in the park worker belongs to  
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
	    
		lblDate.setText(LocalDate.now().toString());

		
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

}
