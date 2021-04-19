package client;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import client.common.ClientController;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import client.controllers.CardReaderMainMenuController;
import client.controllers.loginPageController;

/**
 *In this class we initialize the GUI
 * @author Matan
 *
 */

public class ClientUI extends Application {
	
	/**
	 * ClientController - chat client
	 */
	public static ClientController chat; // only one instance
	
	/**
	 * the primary Stage
	 */
	public static Stage primaryStage;
	
	/**
	 * the card reader stage
	 */
	public static Stage CardReaderStage;
	
	/**
	 * FXMLLoader of the main stage
	 */
	public static FXMLLoader loader;
	
	/**
	 * FXMLLoader of the card reader stage
	 */
	public static FXMLLoader loaderCard;
	
	/**
	 * lanuch ui
	 * @param args args
	 * @throws Exception err
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main
	
	/**
	 * In this function we present the login page and the card reader page. 
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		chat = new ClientController("localhost", 5555);
	
		loginPageController aFrame = new loginPageController();
		aFrame.start(primaryStage);
		ClientUI.primaryStage = primaryStage;
		ClientUI.primaryStage.setResizable(false);
		ClientUI.primaryStage.show();

		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						MessageCS message = new MessageCS(MessageType.LOG_OUT, loginPageController.ConnectedUser);
						ClientUI.chat.accept(message);
					}
				});
			}
		});
		
		 CardReaderMainMenuController CardReaderControllerMain = new CardReaderMainMenuController();
		 ClientUI.CardReaderStage = new Stage();
		 CardReaderControllerMain.start(CardReaderStage);
		 ClientUI.CardReaderStage.setTitle("Card Reader");
		 ClientUI.CardReaderStage.show();
	}
}
