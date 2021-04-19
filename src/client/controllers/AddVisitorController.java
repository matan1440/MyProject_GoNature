package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
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
import client.logic.Order;
import client.logic.Worker;

/**
 * add visitor class, handles search order ID and letting visitors with orders
 * enter the park
 * 
 * @author tomer omer
 * @version 1.0
 */
public class AddVisitorController implements Initializable {

	@FXML
	private Label lblnumVis;

	@FXML
	private Label lblG;

	@FXML
	private Label lblName;

	@FXML
	private Label lblDate1;

	@FXML
	private Label lblTime1;

	@FXML
	private Button logOut;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnOccasionalVisit;

	@FXML
	private Button btnAddVisitor;

	@FXML
	private Button btnExit;

	@FXML
	private TextField txtOrderID;

	@FXML
	private Button btnSearch;

	@FXML
	private Label lblOrderID;

	@FXML
	private Label lblParkName;

	@FXML
	private Label lblDate;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblNumOfVistors;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblOwnerID;

	@FXML
	private Label lblType;

	@FXML
	private Label lblBefore;

	@FXML
	private Label lblSumBefore;

	@FXML
	private Label lblDiscount;

	@FXML
	private Label lblSumDiscount;

	@FXML
	private Label lblPrice;

	@FXML
	private Label lblSum;

	@FXML
	private Button btnEnterTopark;

	@FXML
	private Label lblAmount;

	@FXML
	private ComboBox<String> comboAmount;

	/**
	 * Order of add visitor , that order of the visitor
	 */
	public static Order order;
	private Worker worker;
	
	/**
	 * stringOfmsg - the msg that returned from the server
	 */
	public static String stringOfmsg;
	/**
	 * visit ID 
	 */
	
	public static int visitID;
	
	/**
	 * this is current_number_of_visitors in the park of the worker 
	 */
	public static int current_number_of_visitors = 0;

	/**
	 * switch screen to worker entrance menu
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

	}

	/**
	 * switch screen to login page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * switch screen to add random visitor page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickAddOccasional(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/AddOccasional.fxml");
	}

	/**
	 * switch screen to add visitor page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickAddVisitor(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/AddVisitor.fxml");
	}

	/**
	 * switch screen to exit visit page
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickExitVisit(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ExitVisit.fxml");
	}

	/**
	 * clicking on enter park button, sends message to server with order object and
	 * price string, updates the visit entry on database and number of visitors in
	 * park, sends message to server with current number of visitors in park,
	 * updating amount variable
	 * 
	 * @param event - the event that caused the method
	 */
	@FXML
	void clickEnterToPark(ActionEvent event) {

		order.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));
		MessageCS message = new MessageCS(MessageType.Enter_to_park, lblSum.getText(), order);
		ClientUI.chat.accept(message);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText("You Entered The Park Susscfuly");
		alert.setContentText("Your Visit Id : " + visitID + " \nRemeber it For The Exit");
		alert.showAndWait();

		MessageCS message0 = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, current_number_of_visitors,
				worker.getParkName());
		ClientUI.chat.accept(message0);

		ScreenController.switchScenes("/client/boundary/AddVisitor.fxml");

	}

	/**
	 * validates order id text field.
	 * sends message to server with worker info and order id
	 * returns form server order object input onto order variable
	 * validates order details and pulls price from order information 
	 * to print receipt 
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickSearchID(ActionEvent event) {

		if (InputTestFunctions.ValidateNumber(txtOrderID.getText())) {

			lblAmount.setVisible(false);
			comboAmount.setVisible(false);
			comboAmount.getItems().clear();
			comboAmount.getSelectionModel().clearSelection();

			System.out.println("1");
			String str = txtOrderID.getText();
			int numID = Integer.parseInt(str);
			System.out.println("" + numID);

			MessageCS message = new MessageCS(MessageType.SEARCH_ORDER, worker, numID);
			ClientUI.chat.accept(message);

			if (stringOfmsg.equals("not found order")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Sorry");
				alert.setHeaderText("order id is not valid");
				alert.setContentText("You Can't Enter :-| ");
				alert.showAndWait();
			} else {

				// check if the order is from the same date + hours are good//

				LocalDate datenow = LocalDate.now();
				LocalTime timenow = LocalTime.now();
				LocalTime orderTime = LocalTime.parse(order.getTime());
				boolean checkIfOrderTimeBefore = orderTime.isAfter(timenow.minusMinutes(30));
				boolean checkIfOrderTimeAfter = orderTime.isBefore(timenow.plusMinutes(30));
				boolean checkIfOrderDateIsToday = order.getDate().equals(datenow.toString());

				if (checkIfOrderTimeBefore && checkIfOrderTimeAfter && checkIfOrderDateIsToday) {

					lblAmount.setVisible(true);
					comboAmount.setVisible(true);
					updateComboAmount(1, order.getNumberOfVisitors());

					lblOrderID.setText("" + order.getOrderId());
					lblParkName.setText(order.getParkName());
					lblDate.setText(order.getDate());
					lblTime.setText(order.getTime());
					lblNumOfVistors.setText("" + order.getNumberOfVisitors());
					lblEmail.setText(order.getEmail());
					lblOwnerID.setText("" + order.getOwnerId());
					lblType.setText(order.getTypeOfOrder());

					setSumLabel();

					btnEnterTopark.setVisible(true);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Sorry");
					alert.setHeaderText("Sorry - It's not your visit time");
					alert.setContentText("You Can't Enter :-| ");
					alert.showAndWait();
				}
			}

		}
	}

	/**
	 *initialize page with worker info, time and date
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();

			String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
					currentTime.getSecond());
			lblTime1.setText(curTime);

		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();

		lblDate1.setText(LocalDate.now().toString());

		MessageCS message0 = new MessageCS(MessageType.Get_Park_Amount, worker.getParkName());
		ClientUI.chat.accept(message0);

		Timeline clock1 = new Timeline(new KeyFrame(Duration.ZERO, e -> {

			if (worker.getParkName().equals("A"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_A + "");

			if (worker.getParkName().equals("B"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_B + "");

			if (worker.getParkName().equals("C"))
				lblnumVis.setText(EntranceController.Number_Of_Visitors_C + "");

		}), new KeyFrame(Duration.seconds(1)));
		clock1.setCycleCount(Animation.INDEFINITE);
		clock1.play();

	}

	/**
	 * search id returns amount number of visitors from order, allows park worker to enter
	 * how many visitors actually arrived ( no more than what order specified)
	 * 
	 * @param start - minimum amount of visitors
	 * @param end - maximun amount of visitors (no more than amount in order details)
	 */
	public void updateComboAmount(int start, int end) {
		ArrayList<String> mylist = new ArrayList<String>();
		for (int i = start; i <= end; i++) {
			mylist.add(i + "");
		}
		ObservableList<String> options = FXCollections.observableArrayList(mylist);
		comboAmount.setItems(options);
		comboAmount.getSelectionModel().selectLast();
	}

	/**
	 * from order details gets prices visitors received upon making the order
	 * input into labels
	 */
	private void setSumLabel() {
		lblSumBefore.setText(order.getFull_price());
		lblSumDiscount.setText(order.getDiscount());
		lblSum.setText(order.getTotal_price());

	}
}
