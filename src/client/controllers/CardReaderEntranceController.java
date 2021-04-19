package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import client.common.*;
import client.common.MessageCS.MessageType;
import client.logic.Order;
import client.logic.Subscriber;


import client.ClientUI;

/**
 * this class handles visitors entering park using card reader scan card and enter
 * 
 * @version 1.0
 * @author tomer omer
 *
 */
public class CardReaderEntranceController implements Initializable {

	@FXML
	private Label lblID;

	@FXML
	private TextField txtID;

	@FXML
	private Label lblPark;

	@FXML
	private ComboBox<String> comboPark;

	@FXML
	private Label lblorder;

	@FXML
	private ComboBox<String> comboOrders;

	@FXML
	private Label lblAmount;

	@FXML
	private ComboBox<String> comboAmount;

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
	private Button btnCapicity;

	@FXML
	private Button btnEnter;

	@FXML
	private Button scan;

	@FXML
	private Button clear;

	@FXML
	private Button btnBack;

	private String type;

	/**
	 * Subscriber info for card reader
	 */
	public static Subscriber subscriber;
	
	/**
	 * ArrayList of orders of the visitor
	 */

	public static ArrayList<Order> myorders;

	
	/**
	 * selected Order object of the visitor
	 */
	public static Order selectedOrder;

	
	/**
	 * visit object
	 */
	public static Order visit;


	/**
	 * parkPrice
	 */
	public static int parkPrice = 15;

	/**
	 * discount
	 */
	public static double discount = 0;

	/**
	 * totalprice
	 */
	public static double totalprice = 0;

	/**
	 * price_discount
	 */
	public static double price_discount = 0;
	
	/**
	 * current number of viisotrs in the park
	 */
	public static int currentNoOrderVisitor = 0;
	
	/**
	 * the current Capacity of visitors without order
	 */

	public static int NoOrderCapacity = 0;

	/**
	 * payment for server message
	 */
	public static String Payment = "";
	
	/**
	 * hasOrder - check if the visitor is with order/not with order
	 */
	public static String hasOrder = "";
	
	/**
	 * order id for the server
	 */

	public static String orderId = "";
	
	/**
	 * current number of visitors in the park
	 */

	public static int current_number_of_visitors = 0;
	
	/**
	 * visit id of the visitor
	 */

	public static int visit_id;

	private ChangeListener<String> ChangeListener = null;

	/**
	 *initialize page with park options
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> options = FXCollections.observableArrayList("A", "B", "C");
		comboPark.setItems(options);
		comboPark.getSelectionModel().selectFirst();
		ChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeOrder();
			}
		};
		hasOrder = "";
		orderId = "";
		Payment = "";
	}

	/**
	 * clear text field
	 * @param event - event caused method
	 */
	@FXML
	void ClearInfo(ActionEvent event) {
		txtID.clear();
	}

	/**
	 * check if order is vaild or if random visit if enough place in park
	 * @param event - event caused method
	 */
	@FXML
	void ScanCard(ActionEvent event) {

		if (InputTestFunctions.ValidateNumber(txtID.getText())) {
			if (InputTestFunctions.ValidateSizeID(txtID.getText())) {
				resetButtons();
				resetVars();
				String park = getParkName();
				Subscriber subscriber_check = new Subscriber(Integer.parseInt(getID()));
				MessageCS message = new MessageCS(MessageType.CardReaderCheckEntrance, park, subscriber_check);
				ClientUI.chat.accept(message);

				discount = discount / 100.0;

				if (myorders.size() > 0) {
					updateOrders();
					lblorder.setVisible(true);
					comboOrders.setVisible(true);
					lblAmount.setLayoutY(lblorder.getLayoutY() + 50.0);
					comboAmount.setLayoutY(comboOrders.getLayoutY() + 50.0);
					selectedOrder = myorders.get(comboOrders.getSelectionModel().getSelectedIndex());
					type = selectedOrder.getTypeOfOrder();
					updateComboAmount(1, selectedOrder.getNumberOfVisitors());
					comboOrders.valueProperty().addListener(ChangeListener);
					btnCapicity.setVisible(true);
					setSumLabel(1);
				} else {
					lblorder.setVisible(false);
					comboOrders.setVisible(false);
					lblAmount.setLayoutY(lblorder.getLayoutY());
					comboAmount.setLayoutY(comboOrders.getLayoutY());

					if (subscriber == null) {
						updateComboAmount(1, 50);
						type = "Single";
					} else {
						type = subscriber.getType();
						if (subscriber.getType().equals("Family")) {
							updateComboAmount(1, subscriber.getSubscriber_amount());
						}
						if (subscriber.getType().equals("Guide")) {
							updateComboAmount(1, 15);
						}
					}
					btnCapicity.setVisible(true);
				}
				lblAmount.setVisible(true);
				comboAmount.setVisible(true);
			}
		}
	}

	/**
	 * sets combo box options of how many visitors allowed
	 * 
	 * @param start- minimum amount of visitors according to order details
	 * @param end- maximun amount of visitors according to order details
	 */
	public void updateComboAmount(int start, int end) {
		ArrayList<String> mylist = new ArrayList<String>();
		for (int i = start; i <= end; i++) {
			mylist.add(i + "");
		}
		ObservableList<String> options = FXCollections.observableArrayList(mylist);
		comboAmount.setItems(options);
		comboAmount.getSelectionModel().selectFirst();
	}

	/**
	 * sets if there is visitor with order in combobox number of visitors
	 */
	public void updateOrders() {
		ArrayList<String> mylist = new ArrayList<String>();
		for (int i = 0; i < myorders.size(); i++) {
			mylist.add(myorders.get(i).getOrderId() + "");
		}
		ObservableList<String> options = FXCollections.observableArrayList(mylist);
		comboOrders.setItems(options);
		comboOrders.getSelectionModel().selectFirst();
	}

	public String getID() {
		return txtID.getText();
	}

	public String getParkName() {
		return comboPark.getValue().toString();
	}

	/**
	 * calculate entry price
	 * @param event - event caused method
	 */
	@FXML
	void calcPrice(ActionEvent event) {
		if (comboAmount.getItems().isEmpty() == false) {
			setSumLabel(Integer.parseInt(comboAmount.getValue()));
		}
		btnCapicity.setVisible(true);
	}

	/**
	 * switch screen to of visitors according to order details
	 * @param event - event caused method 
	 */
	@FXML
	void clickBack(ActionEvent event) {
		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderMainMenu.fxml");
	}

	@FXML
	void resetWindow(ActionEvent event) {
		resetButtons();
	}

	/**
	 * reset variables
	 */
	private void resetVars() {
		subscriber = null;
		myorders = null;
		selectedOrder = null;
		parkPrice = 15;
		discount = 0;
		totalprice = 0;
		price_discount = 0;
		currentNoOrderVisitor = 0;
		NoOrderCapacity = 0;
		hasOrder = "";
		orderId = "";
		Payment = "";

	}

	/**
	 * reset buttons
	 */
	private void resetButtons() {
		lblorder.setVisible(false);
		comboOrders.setVisible(false);
		comboOrders.valueProperty().removeListener(ChangeListener);
		comboOrders.getSelectionModel().clearSelection();
		comboOrders.getItems().clear();

		lblAmount.setVisible(false);
		comboAmount.setVisible(false);
		comboAmount.getItems().clear();
		comboAmount.getSelectionModel().clearSelection();

		lblBefore.setVisible(false);
		lblSumBefore.setVisible(false);
		lblDiscount.setVisible(false);
		lblSumDiscount.setVisible(false);
		lblPrice.setVisible(false);
		lblSum.setVisible(false);
		btnCapicity.setVisible(false);

	}

	/**
	 * sets order details to price and amount
	 */
	private void changeOrder() {
		selectedOrder = myorders.get(comboOrders.getSelectionModel().getSelectedIndex());
		updateComboAmount(1, selectedOrder.getNumberOfVisitors());
		type = selectedOrder.getTypeOfOrder();
		setSumLabel(1);
	}

	/**
	 * sets price according to what is in order datails
	 * @param amount- amount of visitors
	 */
	private void setSumLabel(int amount) {
		parkPrice = 15;
		totalprice = 0;
		price_discount = 0;
		if (myorders.size() > 0) {
            
			lblSumBefore.setText(selectedOrder.getFull_price());
			lblSumDiscount.setText(selectedOrder.getDiscount());
			lblSum.setText(selectedOrder.getTotal_price());
			
			
		} else {

			// we check here order for: single only
			if (subscriber == null) {
				price_discount += parkPrice * discount * amount;
				totalprice = (parkPrice * amount) - price_discount;
			} else {
				// we check here order for: family + subscribuion
				if (subscriber.getType().equals("Family")) {
					price_discount = parkPrice * 0.20 * amount;
					price_discount += parkPrice * discount * amount;
					totalprice = (parkPrice * amount) - price_discount;
				}

				// we check here order for: guide only
				if (subscriber.getType().equals("Guide")) {

					price_discount = parkPrice * 0.10 * (amount);
					price_discount += parkPrice * discount * (amount);
					totalprice = parkPrice * (amount) - price_discount;
				}
			}
			String full_price = String.format("%.2f", totalprice+price_discount);
			String discount = String.format("%.2f", price_discount);
			String total_price = String.format("%.2f", totalprice);
			lblSumBefore.setText(full_price);
			lblSumDiscount.setText(discount);
			lblSum.setText(total_price);

		}

	}

	/**
	 *  check if order is valid or if occasional visitor if enough place in park
	 *  sends message to server with order id or visitors amount
	 * 
	 * @param event - event caused method
	 */
	@FXML
	void checkAccess(ActionEvent event) {
		currentNoOrderVisitor = 0;
		NoOrderCapacity = 0;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Visit");
		alert.setHeaderText("Confirm Your Visit Please");
		alert.setContentText("Are You Sure You Want To Enter To The Park?");

		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().getText() == "Yes") {
			if (myorders.size() > 0) {
				LocalTime timenow = LocalTime.now();
				LocalTime orderTime = LocalTime.parse(selectedOrder.getTime());
				boolean checkIfOrderBefore = orderTime.isAfter(timenow.minusMinutes(30));
				boolean checkIfOrderAfter = orderTime.isBefore(timenow.plusMinutes(30));

				if (checkIfOrderBefore && checkIfOrderAfter) {

					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Access Control Passed");
					alert.setHeaderText("Access Control Passed Sussfuly ");
					alert.setContentText("You Can Enter To The Park");
					alert.showAndWait();
					btnCapicity.setVisible(false);
					lblorder.setVisible(false);
					comboOrders.setVisible(false);

					lblAmount.setVisible(false);
					comboAmount.setVisible(false);
					
				
					btnCapicity.setVisible(false);
					comboPark.setVisible(false);
					txtID.setVisible(false);
					scan.setVisible(false);
					clear.setVisible(false);
					lblID.setVisible(false);
					lblPark.setVisible(false);
					btnEnter.setVisible(true);
					lblBefore.setVisible(true);
					lblSumBefore.setVisible(true);
					lblDiscount.setVisible(true);
					lblSumDiscount.setVisible(true);
					lblPrice.setVisible(true);
					lblSum.setVisible(true);

				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Sorry");
					alert.setHeaderText("Sorry - It's not your visit time");
					alert.setContentText("You Can't Enter :-| ");
					alert.showAndWait();
				}

			} else {
				Order order = new Order();
				order.setParkName(getParkName());
				order.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));

				MessageCS message = new MessageCS(MessageType.CardReaderCheckCapicity, "", order);
				ClientUI.chat.accept(message);
				if ((currentNoOrderVisitor + order.getNumberOfVisitors()) > NoOrderCapacity) {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Sorry");
					alert.setHeaderText("Sorry - The Park Is Now Full!");
					alert.setContentText("You Can't Enter :-| ");
					alert.showAndWait();
				} else {
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Access Control Passed");
					alert.setHeaderText("Access Control Passed Sussfuly ");
					alert.setContentText("You Can Enter To The Park");
					alert.showAndWait();
					btnCapicity.setVisible(false);
					lblorder.setVisible(false);
					comboOrders.setVisible(false);

					lblAmount.setVisible(false);
					comboAmount.setVisible(false);

	
					btnCapicity.setVisible(false);
					comboPark.setVisible(false);
					txtID.setVisible(false);
					scan.setVisible(false);
					clear.setVisible(false);
					lblID.setVisible(false);
					lblPark.setVisible(false);
					
					lblBefore.setVisible(true);
					lblSumBefore.setVisible(true);
					lblDiscount.setVisible(true);
					lblSumDiscount.setVisible(true);
					lblPrice.setVisible(true);
					lblSum.setVisible(true);
					btnEnter.setVisible(true);

				}
			}
		} else {
			// ... user chose CANCEL or closed the dialog
		}
		
	
		
		
	}
	/**
	 * enters visitors to park and updates in database
	 * sends message to server with order details and current number of visitors
	 * 
	 * @param event - event caused method
	 */
	@FXML
	void clickEnter(ActionEvent event) {

		visit = new Order();
		// here we save the visit time//
		visit.setParkName(getParkName());
		visit.setTypeOfOrder(type);
		visit.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));
		visit.setDate(LocalDate.now().toString());
		visit.setOwnerId(Integer.parseInt(getID()));

		LocalTime timenow = LocalTime.now();
		String time = String.format("%02d:%02d:%02d", timenow.getHour(), timenow.getMinute(), 0);
		visit.setTime(time);
		Payment = this.lblSum.getText();
		if (myorders.size() > 0) {
			hasOrder = "yes";
			orderId = "" + selectedOrder.getOrderId();
		} else {
			hasOrder = "no";
			orderId = "no";
		}

		MessageCS message = new MessageCS(MessageType.CardReaderEnterToPark, visit, Payment, hasOrder, orderId);
		ClientUI.chat.accept(message);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText("You Entered The Park Susscfuly");
		alert.showAndWait();

		MessageCS message0 = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, current_number_of_visitors,
				visit.getParkName());
		ClientUI.chat.accept(message0);

		ScreenController.switchCardReaderScenes("/client/boundary/CardReaderMainMenu.fxml");

	}

}
