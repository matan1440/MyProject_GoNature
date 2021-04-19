package server.controllers;




import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.common.EchoServer;
import server.ServerUI;
import client.common.ChatClient;

/**
 * this class is for servert port frame controller
 * @author Omer
 *
 */
public class ServerPortFrameController {

	String temp = "";

	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnDone = null;
	@FXML
	private Label lbllist;

	@FXML
	private Label txtip;

	@FXML
	private Label txthost;

	@FXML
	private Label txtStatus;

	@FXML
	private TextField portxt;

	@FXML
	private TextField WaitingListTime;

	@FXML
	private TextField ConfirmVisitTime;

	ObservableList<String> list;

	/**
	 * get port data
	 * @return
	 */
	private String getport() {
		return portxt.getText();
	}

	/**
	 * the method start the server + sets the server parms according to the user selection
	 * @param event that fired the method
	 * @throws Exception in case of fail
	 */
	public void Done(ActionEvent event) throws Exception {
		String p;
		p = getport();
		if (p.trim().isEmpty() || WaitingListTime.getText().isEmpty() || ConfirmVisitTime.getText().isEmpty()) {
			System.out.println("You must enter values - Port , WaitingListTime , ConfirmVisitTime");
		} else {
			try {
				EchoServer.WaitingListTimeMins = Double.parseDouble(WaitingListTime.getText());
				EchoServer.ConfirmVisitTimeMins = Double.parseDouble(ConfirmVisitTime.getText());
				ServerUI.runServer(p);
				EchoServer.setServerPortFrameController(this);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Connected");
				alert.setHeaderText("Server Connected And Running");
				alert.setContentText("");
				alert.showAndWait();
				
				
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Please Enter Valid Numbers");
				alert.setContentText("");
				alert.showAndWait();
			}

		}
	}

	/**
	 * start the main stage of the server
	 * @param primaryStage - stage to start
	 * @throws Exception - in case of fail
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/server/boundary/ServerPort.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/server/boundary/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	/**
	 * the method close the server and close all of the timers of the threads
	 * @param event - the event that fired the method
	 * @throws Exception - in case of fail
	 */

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Server Tool");
		System.exit(0);
	}
}