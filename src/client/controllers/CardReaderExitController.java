package client.controllers;

import java.net.URL;

import java.util.ResourceBundle;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import client.common.*;
import client.common.MessageCS.MessageType;

import client.ClientUI;

/**
 * this class handles visitors existing using card reader, uses scan card and click exit
 * 
 * @version 1.0
 * @author tomer omer
 *
 */
public class CardReaderExitController implements Initializable {

	@FXML
	private Label lblOwnerID;

	@FXML
	private TextField txtOwnerID;

	@FXML
	private Button scan;

	@FXML
	private Button clear;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnExit;

	/**
	 * id check - check if the visitor can exit
	 */
	public static boolean idCheck;

	/**
	 * current number of visitors in the park
	 */

	public static int current_number_of_visitors = 0;
	
	/**
	 * visit id of the visitor
	 */

	public static int visit_id;

	/**
	 * park name object for updating the park amounts of visitors
	 */
	public static String Park_name;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * clear text field
	 * 
	 * @param event- event that caused method
	 */
	@FXML
	void ClearInfo(ActionEvent event) {
		txtOwnerID.clear();
	}

	/**
	 * scans id and validates,
	 * exit visitors from park and updates database
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void ScanCard(ActionEvent event) {
		if (InputTestFunctions.ValidateNumber(txtOwnerID.getText())) {
			if (InputTestFunctions.ValidateSizeID(txtOwnerID.getText())) {
				MessageCS message = new MessageCS(MessageType.CardReaderCheckVisitId, getOwnerID());
				ClientUI.chat.accept(message);
				if (idCheck == true) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Visit Id Found");
					alert.setHeaderText("You Can Exit The Park");
					alert.setContentText(null);
					alert.showAndWait();
					scan.setVisible(false);
					clear.setVisible(false);
					lblOwnerID.setVisible(false);
					txtOwnerID.setVisible(false);
					btnExit.setVisible(true);
				} else {
					btnExit.setVisible(false);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Sorry - Visit Id Not Found / Visit Is Over");
					alert.setContentText(null);
					alert.showAndWait();
				}
			}
		}
	}

	/**
	 * sends server message, updates visitors exit park in database
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickExit(ActionEvent event) {
		MessageCS message = new MessageCS(MessageType.CardReaderExitFromPark, visit_id + "");
		ClientUI.chat.accept(message);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText("You Exit The Park Suessfuly");
		alert.setContentText("Goodbye");
		alert.showAndWait();

		MessageCS message0 = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, current_number_of_visitors, Park_name);
		ClientUI.chat.accept(message0);

		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderMainMenu.fxml");
	}

	/**
	 * switch screen to Card Reader Main Menu
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickBack(ActionEvent event) {
		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderMainMenu.fxml");
	}

	public String getOwnerID() {
		return txtOwnerID.getText();
	}

}
