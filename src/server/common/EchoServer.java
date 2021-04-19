// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server.common;

import java.sql.Connection;

import server.controllers.ServerPortFrameController;
import server.controllers.ThreadOrderCancel;
import server.controllers.ThreadOrderReminder;
import server.controllers.ThreadRemoveOrderWaiting;
import server.database.mysqlConnection;
import ocsf.server.*;
import server.controllers.ServerController;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 **/

public class EchoServer extends AbstractServer {
	private ServerController server;
	private Connection conn;

	/**
	 * mysqlConnection var
	 */
	private static mysqlConnection DB;

	/**
	 * ServerPortFrameController var
	 */
	private static ServerPortFrameController ServerController;

	/**
	 * Waiting List Time Mins for each person
	 */
	public static Double WaitingListTimeMins;
	
	/**
	 * Confirm Visit Time Mins for each person
	 */
	public static Double ConfirmVisitTimeMins;

	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * set the server format frame parm
	 * 
	 * @param serverPortFrameController
	 */
	public static void setServerPortFrameController(ServerPortFrameController serverPortFrameController) {
		EchoServer.ServerController = serverPortFrameController;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		server = new ServerController();
		server.messageReceivedFromClient(msg, client, conn);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		DB = new mysqlConnection();
		conn = DB.connectToDB();

		ThreadOrderReminder.setConn(conn);
		ThreadOrderCancel.setConn(conn);
		ThreadRemoveOrderWaiting.setConn(conn);

		ThreadOrderReminder.getInstance().start();
		ThreadOrderCancel.getInstance().start();
		ThreadRemoveOrderWaiting.getInstance().start();

		// add label - db connected //
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * 
	 * @return db class
	 */
	public static mysqlConnection getDBClass() {
		return DB;
	}
	
	public ServerController getServer() {
		return server;
	}
	
	public void set_mysqlConnection(mysqlConnection DB) {
		EchoServer.DB = DB;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
