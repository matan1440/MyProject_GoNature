package client.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import client.logic.Order;
import client.logic.Subscriber;

/**
 * This class is the controller of View Order Details Screen - it contains
 * methods for handling order - Cancel order , confirm arrivel , confirm order
 * from waiting list , paynow button for guide .
 * 
 * @version 1.0
 * @author Omer Matan
 */

public class ViewOrderDetailsController implements Initializable {

	@FXML
	private Button btnCreateOrder;

	/**
	 * Subscriber object for the order
	 */
	private Subscriber subscriber;

	/**
	 * Order object - the order of which the details belong to
	 */
	public static Order order;

	/**
	 * id_visitor - the id_visitor of the order in which the details belong to
	 */
	private String id_visitor;

	@FXML
	private Button logOut;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnViewOrdersTable;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnCancelOrder;

	@FXML
	private Button btnConfirmOrder;

	@FXML
	private Button btnArriveConfirm;

	@FXML
	private Button btnpaynow;

	@FXML
	private Label labOrderID;

	@FXML
	private Label labPrakName;

	@FXML
	private Label labDate;

	@FXML
	private Label labTime;

	@FXML
	private Label labAmount;

	@FXML
	private Label labEmail;

	@FXML
	private Label labOwnerOrder;

	@FXML
	private Label labType;

	@FXML
	private Label labStatus;

	@FXML
	private Label labprice;

	/**
	 * Check if Arrivel Confirmation button should be visible
	 */
	public static boolean bool;

	/**
	 * Check if Confirm Order From Waiting List button should be visible
	 */
	public static boolean bool2;

	/**
	 * Check if Pay Now button for guide should be visible
	 */
	public static double btn_click = 0;

	/**
	 * Price of the order after guide made payment
	 */
	public static String price_after_pay_now;

	/**
	 * initialize the order details , the full price of the order , the traveler
	 * info . initialize the visibily status of the buttons initialize the discount
	 * for the order the method checks if the pay now / waiting list / arrivel
	 * confirm /cancel order buttons should be visible , by doing request to the
	 * server for the realvent data
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.subscriber = loginPageController.subscriber;
		this.id_visitor = loginPageController.id_visitor;
		price_after_pay_now = "";

		// Update of the labels
		labOrderID.setText(Integer.toString(order.getOrderId()));
		labPrakName.setText(order.getParkName());
		labDate.setText(order.getDate());
		labTime.setText(order.getTime());
		labAmount.setText(Integer.toString(order.getNumberOfVisitors()));
		labEmail.setText(order.getEmail());
		labOwnerOrder.setText(Integer.toString(order.getOwnerId()));
		labType.setText(order.getTypeOfOrder());
		labStatus.setText(order.getStatus());

		String string = order.getPaynow();
		String[] parts = string.split(",");
		String part3 = parts[2]; // 004
		String result = part3 + " ¤";
		labprice.setText(result);

		MessageCS message = new MessageCS(MessageType.CheckTopWaitingListAndPlace, "Check Top Waiting List And Place",
				order);
		ClientUI.chat.accept(message);

		if (bool2)
			btnConfirmOrder.setVisible(true);

		MessageCS message2 = new MessageCS(MessageType.CheckIfConfirmArrivel, "Check Confirm Visit", order);

		ClientUI.chat.accept(message2);

		if (bool)
			btnArriveConfirm.setVisible(true);

		if (btn_click == 1) {
			btnpaynow.setVisible(true);
			btn_click = 0;
		} else {
			btnpaynow.setVisible(false);
		}

		if (order.getStatus().equals("not arrived") || order.getStatus().equals("Waiting")) {
			btnCancelOrder.setVisible(true);
		} else {
			btnCancelOrder.setVisible(false);
		}

	}

	/**
	 * Switch the screen to main menu .
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/MenuVisitorWithOrder.fxml");

	}

	/**
	 * Log out from the system - return the travler to login page
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * Switch the screen to my orders screen .
	 * 
	 * @param event - the event that fired the method .
	 */

	@FXML
	void btnViewOrdersTable(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ViewMyOrders.fxml");

	}

	/**
	 * Switch the screen to create order page .
	 * 
	 * @param event - the event that fired the method .
	 */

	@FXML
	void clickCreateOrder(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/CreateOrder.fxml");
	}

	/**
	 * Switch the screen to my orders screen .
	 * 
	 * @param event - the event that fired the method .
	 */

	@FXML
	void clickbtnBack(MouseEvent event) {
		ScreenController.switchScenes("/client/boundary/ViewMyOrders.fxml");
	}

	/**
	 * update the order labels and info from a given order
	 * 
	 * @param order to set the labels for
	 */
	public void updateOrder(Order order) {
		Platform.runLater(() -> {
			labOrderID.setText(Integer.toString(order.getOrderId()));
			labPrakName.setText(order.getParkName());
			labDate.setText(order.getDate());
			labTime.setText(order.getTime());
			labAmount.setText(Integer.toString(order.getNumberOfVisitors()));
			labEmail.setText(order.getEmail());
			labOwnerOrder.setText(Integer.toString(order.getOwnerId()));
			labType.setText(order.getTypeOfOrder());
			labStatus.setText(order.getStatus());
			labprice.setText(order.getPaynow());
		});
	}

	/**
	 * update the selected order from a given order , this method change the
	 * selected order
	 * 
	 * @param selectedOrder for viewOrderdeatiles page and labels
	 */
	public static void setOrder(Order selectedOrder) {
		Platform.runLater(() -> {
			order = selectedOrder;
		});
	}

	/**
	 * this method send CancelOrder message to the server , with the order object to
	 * be cancelled.
	 * 
	 * @param event - the event that fired the method
	 */
	@FXML
	void ClickCancelOrder(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancel Order");
		alert.setHeaderText("Cancel Order Confirmation");
		alert.setContentText("Are You Sure You Want To Cancel Your Order?");

		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().getText() == "Yes") {
			MessageCS message = new MessageCS(MessageType.CancelOrder, "Cancel order", order);
			ClientUI.chat.accept(message);

			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done");
			alert.setHeaderText("Order Cancelled Susscfuly");
			alert.setContentText("We Cancelled Your Order - Thank You ");
			alert.showAndWait();

			order.setStatus("Canceled");
			ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");
		}
	}

	/**
	 * this method is for button - Confirm order from waiting list . the method send
	 * message to the server with the given order (which order to change from
	 * waiting to not arrived this button will appear only when another travler
	 * canacled order and there is a place in the park
	 * 
	 * @param event - the event that fired the method
	 */
	@FXML
	void ClickConfirmOrder(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Order");
		alert.setHeaderText("Order Confirmation");
		alert.setContentText("Are You Sure You Want To Make Your Order?");
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get().getText() == "Yes") {

			MessageCS message = new MessageCS(MessageType.ChangeStatusOrder,
					"Change Status Order : wait -> not arrived", order);
			ClientUI.chat.accept(message);

			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done");
			alert.setHeaderText("Order Made Successfully");
			alert.setContentText("Thank You");
			alert.showAndWait();

			order.setStatus("not arrived");

		}
		ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");

	}

	/**
	 * this method is for button - arrivel confirmation , one day before order date
	 * the method send message to the server with the given order (which order to
	 * set confirm arrivel to be true in the db) this button will appear only one
	 * day before order date
	 * 
	 * @param event - the event that fired the method
	 */
	@FXML
	void ClickArriveConfirm(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Arrivel Confirmation");
		alert.setHeaderText("Arrivel Confirmation");
		alert.setContentText("Are You Sure You Want To Confirm Your Arrivel Tomorrow?");
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get().getText() == "Yes") {

			MessageCS message = new MessageCS(MessageType.ArrivelConfirm, "Confirm Arrivel", order);
			ClientUI.chat.accept(message);
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done");
			alert.setHeaderText("Thank You");
			alert.setContentText("We Received Your Confirmation , Enjoy Your Visit");
			alert.showAndWait();
		}
		ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");
	}

	/**
	 * this method is for button - click pay now for guide the method send message
	 * to the server with the given order (which order to set guide pay now to be
	 * true in the db) this button will appear only for guide
	 * 
	 * @param event - the event that fired the method
	 */
	@FXML
	void clickPayNow(ActionEvent event) {
		price_after_pay_now = "";
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Pay");
		alert.setHeaderText("Pay Now");
		alert.setContentText("Are You Sure You Want To Pay Now - 12% Discount!");
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get().getText() == "Yes") {
			MessageCS message = new MessageCS(MessageType.PayNow, "Pay Now", order);
			ClientUI.chat.accept(message);
			alert.setTitle("Done");
			alert.setHeaderText("Thank You");
			alert.setContentText("We Recived Your Payment - Thank You , Enjoy ");
			alert.showAndWait();
			order.setPaynow(price_after_pay_now);
			String string = price_after_pay_now;
			String[] parts = string.split(",");
			order.setFull_price(parts[0]);
			order.setDiscount(parts[1]);
			order.setTotal_price(parts[2]);
			ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");
		}
	}

	/**
	 * set pay now option visible for guide
	 * 
	 * @param order show button visible
	 */
	public static void setPayNowButtonShow(Order order) {
		Platform.runLater(() -> {
			btn_click = 1;
		});
	}

}
