package server;

import javafx.application.Application;
import javafx.stage.Stage;

import server.controllers.ServerPortFrameController;
import server.common.EchoServer;

/**
 * this is the main ui of the server, starting the server
 * @author Omer
 *
 */
public class ServerUI extends Application {
	
	/**
	 * DEFAULT_PORT
	 */
	final public static int DEFAULT_PORT = 5555;

	private ServerPortFrameController ServerController;
	
	/**
	 * EchoServer var.
	 */
	public static EchoServer sv;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * start the main stage for the gui
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ServerController = new ServerPortFrameController(); // create StudentFrame
		ServerController.start(primaryStage);
	}

	/**
	 * start the server
	 * @param p - port 
	 */
	public static void runServer(String p) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		EchoServer sv = new EchoServer(port);
		ServerUI.sv = sv;
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}	
}
