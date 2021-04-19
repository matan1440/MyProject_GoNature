package client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * this class handles the card reader main menu choosing exit or entry pages
 * 
 * @author tomer omer
 *
 */
public class CardReaderMainMenuController implements Initializable {

	@FXML
	private Button btnCardReaderEnter;

	@FXML
	private Button btnCardReaderExit;

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client/boundary/CardReaderMainMenu.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/client/boundary/Design.css");
		primaryStage.setTitle("GoNature");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * switch screen to Card Reader Entrance
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void ClickOnImageEntery(MouseEvent event) {
		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderEntrance.fxml");
	}

	/**
	 * switch screen to Card Reader exit
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void ClickOnImageExit(MouseEvent event) {
		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderExit.fxml");

	}

}
