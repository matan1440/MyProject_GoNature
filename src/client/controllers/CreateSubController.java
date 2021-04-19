package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import client.ClientUI;
import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.common.SendMail;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import client.logic.Subscriber;
import client.logic.Worker;

/**
 * create subscriber controller, handles creating a subscriber by service worker.
 * 
 * 
 * @version 1.0
 * @author tomer
 *
 */
public class CreateSubController implements Initializable {

	@FXML
	private Button logOut;
	
    @FXML
    private Label lblTime;

    @FXML
    private Label lblDate;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnCreateSubscriber;

	@FXML
	private Button btnCreate;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtAmount;

	@FXML
	private ComboBox<String> txtType;

	/**
	 * the subscriber id
	 */
	public static int sub_id;

	/**
	 * the service worker object
	 */
	public static Worker worker;


	/**
	 * switch to service worker menu
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerServiceMenu.fxml");

	}

	/**
	 * switch to create subscriber page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickCreateSubscriber(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/CreateSub.fxml");

	}

	/**
	 * switch to login page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * clicking on type combo box choosing family or guide
	 * if guide is chosen txtamount is set to 15 max
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void ClickType(ActionEvent event) {

		if ((txtType.getValue()).equals("Guide")) {
			txtAmount.setText("15");
			txtAmount.setDisable(true);
		}else {
			txtAmount.setDisable(false);
			txtAmount.clear();
		}
			
	}

	/**
	 * clicking on create button, validates all text fields.
	 * creates new subscriber with new inputed data from text fields 
	 * sends message to server with new subscriber object and adds him to database
	 * 
	 * @param event - event that caused method 
	 * @throws MessagingException
	 */
	@FXML
	void clickCreate(ActionEvent event) throws MessagingException {

		if (InputTestFunctions.ValidateStringNoAlert(txtFirstName.getText())) {

			if (InputTestFunctions.ValidateStringNoAlert(txtLastName.getText())) {

				if (InputTestFunctions.ValidateEmail(txtEmail.getText()))
					if (InputTestFunctions.ValidateNumber(txtAmount.getText())) {

						String firstName = getFirstName();
						String lastName = getLastName();
						String email = getEmail();
						int amount = Integer.parseInt(getAmount());
						Subscriber subscriber = new Subscriber(firstName, lastName, email, amount, txtType.getValue());

						MessageCS message = new MessageCS(MessageType.CREATE_SUBSCRIBER, subscriber);
						ClientUI.chat.accept(message);

						SendMail.sendMailNewSubscriber(subscriber.getEmail(), subscriber, sub_id);

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("registration successful");
						alert.setHeaderText(null);
						alert.setContentText("subscriber id : " + sub_id);
						alert.show();
						
						ScreenController.switchScenes("/client/boundary/CreateSub.fxml");

					}

			} else { // incorrect last name
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Validation error");
				alert.setHeaderText(null);
				alert.setContentText("Please enter a vaild last name");
				alert.show();
			}

		} else { // incorrect first name
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Validation error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a vaild first name");
			alert.show();
		}

	}

	/**
	 * @return text in txtFirstName text field
	 */
	private String getFirstName() {
		return txtFirstName.getText();
	}

	/**
	 * @return text in txtLastName text field
	 */
	private String getLastName() {
		return txtLastName.getText();
	}

	/**
	 * @return text in getEmail text field
	 */
	private String getEmail() {
		return txtEmail.getText();
	}

	/**
	 * @return text in getAmount text field
	 */
	private String getAmount() {
		return txtAmount.getText();
	}

	/**
	 *initialize page with worker name and park he belongs to
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
	    
		ObservableList<String> options = FXCollections.observableArrayList("Family", "Guide");
		txtType.setItems(options);
		txtType.getSelectionModel().selectFirst();

	}

}