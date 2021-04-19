package client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * this class responsible for settings of scenes also uses switchscenes method
 * to change between different scenes
 * 
 * @author omer
 *
 */
public class ScreenController implements Initializable {

	/**
	 * switch between different scenes
	 * 
	 * @param fxmlFile - string containing the path toward an fxml file in client
	 *                 boundary
	 */
	public static void switchScenes(String fxmlFile) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Parent MainParent;
				try {
					if (fxmlFile.equals("/client/boundary/loginPage.fxml")) {
						MessageCS message = new MessageCS(MessageType.LOG_OUT, loginPageController.ConnectedUser);
						ClientUI.chat.accept(message);
					}

					double width = ClientUI.primaryStage.getWidth();
					double height = ClientUI.primaryStage.getHeight();

					ClientUI.loader = new FXMLLoader(getClass().getResource(fxmlFile));

					MainParent = (Parent) ClientUI.loader.load();
					Scene scene = new Scene(MainParent);
					scene.getStylesheets().add("/client/boundary/Design.css");

					ScreenController.setScene(ClientUI.primaryStage, scene, width, height);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * switches scenes for card reader (card reader has different separate window
	 * for itself, with different settings that appears alongside the go nature
	 * system)
	 * 
	 * @param fxmlFile
	 */
	public static void switchCardReaderScenes(String fxmlFile) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Parent MainParent;
				try {
					ClientUI.loaderCard = new FXMLLoader(getClass().getResource(fxmlFile));
					MainParent = (Parent) ClientUI.loaderCard.load();
					Scene scene = new Scene(MainParent);
					scene.getStylesheets().add("/client/boundary/Design.css");
					ClientUI.CardReaderStage.setScene(scene);
					ClientUI.CardReaderStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * set scene parameters
	 * 
	 * @param stage  - the stage object
	 * @param scene  - the scene object
	 * @param width  - double, width of scene (window)
	 * @param height - double, height of scene (window)
	 */
	private static void setScene(Stage stage, Scene scene, double width, double height) {
		stage.setScene(scene);
		stage.setWidth(width);
		stage.setHeight(height);
		stage.show();
	}

	/**
	 * initialize window
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}