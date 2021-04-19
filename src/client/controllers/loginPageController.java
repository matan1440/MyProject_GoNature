package client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ClientUI;
import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import client.logic.Subscriber;
import client.logic.Order;
import client.logic.Worker;

/**
 * This class responsible to all function of login page.
 * 
 * @version 1.0
 * @author Matan
 *
 */
public class loginPageController implements Initializable {

	@FXML
	private Button btnQuestionMark;

	@FXML
	private Rectangle formQM;

	@FXML
	private Label lblQM;

	@FXML
	private TextField txtIdentification;

	@FXML
	private PasswordField ptxtPassword;

	@FXML
	private Button btnEnter;

	@FXML
	private Button btnCardReaderEnter;

	@FXML
	private Button btnCardReaderExit;

	@FXML
	private CheckBox CbtnWorker;

	@FXML
	private CheckBox CbtnVisitor;

	@FXML
	private Label remarks_login;

	@FXML
	private Label passlabel;

	@FXML
	private Label usernameid;

	@FXML
	private Button btnQuickEnter;

	/**
	 * Error_Msg in case the login has failed
	 */
	public static String Error_Msg;

	/**
	 * returned msg from the server (login status)
	 */
	public static String Msg;

	/**
	 * worker object , we store in the object what we get from the server
	 */

	public static Worker worker;

	/**
	 * Subscriber object for checking Subscriber info
	 */

	public static Subscriber subscriber;

	/**
	 * Order object for checking if the visitor has orders
	 */

	public static Order order;

	/**
	 * the id of the visitor
	 */
	public static String id_visitor;

	/**
	 * is the worker / visitor / travler logged on
	 */
	public static boolean Connected = false;

	/**
	 * the ConnectedUser id , when the user log out , we send this id to the server
	 */

	public static int ConnectedUser;

	private boolean isPressed = true;

	private boolean QuickEnterWindow = true;

	/**
	 * This function present the login page. This function is responsible for
	 * compiling the page layout file - FXML.
	 * 
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/client/boundary/loginPage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/client/boundary/Design.css");
		primaryStage.setTitle("GoNature");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * This function is enabled in click on Enter button. The function divides the
	 * different cases according to the user who wants to log in.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void EnterMainMenu(ActionEvent event) throws Exception {
		Error_Msg = null;
		Msg = null;
		worker = null;
		subscriber = null;
		order = null;
		id_visitor = null;
		Connected = false;
		ConnectedUser = 0;

		if (CbtnWorker.isSelected() == true) { // If worker try enter

			String id = getID();
			String password = getPassword();
			Error_Msg = null;

			MessageCS message = new MessageCS(MessageType.LOGIN_WORKER, new Worker(id, password));
			ClientUI.chat.accept(message);

			// check if we don't found a worker with this id //
			if (Error_Msg != null) {
				remarks_login.setText(Error_Msg);
			} else {
				MessageCS message_check_login = new MessageCS(MessageType.LOGIN_CHECK, getID(),
						CbtnWorker.isSelected());
				ClientUI.chat.accept(message_check_login);
				if (Connected == false) {
					if (worker.getJob().equals("Entrance")) {
						ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

					}
					if (worker.getJob().equals("Service")) {
						ScreenController.switchScenes("/client/boundary/WorkerServiceMenu.fxml");

					}
					if (worker.getJob().equals("Park manager")) {
						ScreenController.switchScenes("/client/boundary/ParkManagerMenu.fxml");

					}
					if (worker.getJob().equals("Department manager")) {
						ScreenController.switchScenes("/client/boundary/DepartmentManagerMenu.fxml");

					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Login Error");
					alert.setHeaderText("You Are Already Logged In");
					alert.setContentText("");
					alert.show();
				}

			}
		}

		if (CbtnVisitor.isSelected() == true) { // If visitor try enter
			Msg = null;
			String id = getID(); // get from txtfeild

			if (ValidateSizeID(id) && ValidateNumber(id)) {

				MessageCS message = new MessageCS(MessageType.LOGIN_VISITOR, new Subscriber(Integer.parseInt(id)));
				ClientUI.chat.accept(message);

				MessageCS message_check_login = new MessageCS(MessageType.LOGIN_CHECK, getID(),
						CbtnWorker.isSelected());
				ClientUI.chat.accept(message_check_login);

				if (Connected == false) {
					if (Msg.equals("This is new visitor")) {
						id_visitor = id;
						ScreenController.switchScenes("/client/boundary/MenuNewVisitor.fxml");
					}

					if (Msg.equals("This is owner of order")) {
						id_visitor = id;
						ScreenController.switchScenes("/client/boundary/MenuVisitorWithOrder.fxml");
					}

					if (Msg.equals("This is subsriber without order")) {
						id_visitor = id;// Insert of subscriber happened in MessageFromServer
						ScreenController.switchScenes("/client/boundary/MenuNewVisitor.fxml");
					}

					if (Msg.equals("This is subsriber with order")) {
						id_visitor = id;// Insert of subscriber happened in MessageFromServer
						ScreenController.switchScenes("/client/boundary/MenuVisitorWithOrder.fxml");
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Login Error");
					alert.setHeaderText("You Are Already Logged In");
					alert.setContentText("");
					alert.show();
				}

			} // End of if
		}

	}

	/**
	 * This function is called when a user selects this box visitor
	 * 
	 * @param event
	 */
	@FXML
	void OnActionCBtnVitiror(ActionEvent event) {
		btnQuestionMark.setVisible(true);

		if (CbtnWorker.isSelected() == false && CbtnVisitor.isSelected() == false)
			CbtnVisitor.setSelected(true);

		ptxtPassword.setDisable(true);
		if (CbtnWorker.isSelected() == true) {
			CbtnWorker.setSelected(false);
		}

		ptxtPassword.setVisible(false);
		txtIdentification.clear();
		ptxtPassword.clear();
		passlabel.setOpacity(0);
		usernameid.setText("ID:");
		usernameid.setLayoutX(359.0);
	}

	/**
	 * * This function is called when a user selects this box worker
	 * 
	 * @param event
	 */
	@FXML
	void OnActionCBtnWorker(ActionEvent event) {
		formQM.setVisible(false);
		lblQM.setVisible(false);
		btnQuestionMark.setVisible(false);

		if (CbtnWorker.isSelected() == false && CbtnVisitor.isSelected() == false)
			CbtnWorker.setSelected(true);

		ptxtPassword.setEditable(true);
		ptxtPassword.setDisable(false);
		if (CbtnVisitor.isSelected() == true) {
			CbtnVisitor.setSelected(false);
		}

		txtIdentification.clear();
		ptxtPassword.clear();
		ptxtPassword.setVisible(true);
		passlabel.setOpacity(1.0);
		usernameid.setText("Username:");
		passlabel.setVisible(true);
		passlabel.setText("Password:");
		usernameid.setLayoutX(274.0);

	}

	/**
	 * This function return the ID that the user insert.
	 * 
	 * @return String
	 */
	private String getID() {
		return txtIdentification.getText();
	}

	/**
	 * This function return the password that the user insert.
	 * 
	 * @return String
	 */
	private String getPassword() {
		return ptxtPassword.getText();
	}

	/**
	 * This function initializes the page data each time you access it
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		txtIdentification.setOnKeyPressed(new EventHandler<KeyEvent>()

		{
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					try {
						EnterMainMenu(new ActionEvent());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		ptxtPassword.setOnKeyPressed(new EventHandler<KeyEvent>()

		{
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					try {
						EnterMainMenu(new ActionEvent());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	@FXML
	void ClickQuestionMark(ActionEvent event) {
		if (isPressed) {
			formQM.setVisible(true);
			lblQM.setVisible(true);
			isPressed = false;
		} else {
			formQM.setVisible(false);
			lblQM.setVisible(false);
			isPressed = true;
		}

	}

	@FXML
	void ClickQuickEnter(ActionEvent event) {

		if (QuickEnterWindow) {
			QuickEnterWindow = false;

			CardReaderMainMenuController CardReaderControllerMain = new CardReaderMainMenuController();
			Stage CardReaderStage = new Stage();
			ClientUI.CardReaderStage = CardReaderStage;
			try {
				CardReaderControllerMain.start(CardReaderStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ClientUI.CardReaderStage.setTitle("Card Reader");
			ClientUI.CardReaderStage.setResizable(false);

			ClientUI.CardReaderStage.show();
			ClientUI.CardReaderStage.setOnHidden(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							QuickEnterWindow = true;
						}
					});
				}
			});

		}

	}

	public boolean ValidateSizeID(String str) {
		if (str.length() == 9) {
			return true;

		} else {
			return false;
		}

	}

	public boolean ValidateNumber(String str) {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(str);
		if (m.find() && m.group().equals(str)) {
			return true;

		} else {
			return false;
		}
	}
}
