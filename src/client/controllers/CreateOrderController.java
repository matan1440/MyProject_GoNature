package client.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import client.common.*;
import client.common.MessageCS.MessageType;
import client.logic.DateOrder;
import client.logic.Order;
import client.logic.Subscriber;
import javafx.scene.control.DatePicker;

import client.ClientUI;

/**
 * This Class is for CreateOrderController, the class contains methods for: switching scenes, making order, entering waitinglist,
 * showing other dates and time in case the order time is not available
 * @author Omer Matan
 *
 */
public class CreateOrderController implements Initializable {

	@FXML
	private Button btnCreateOrderMenu;

	private Subscriber subscriber;

	private Order order;

	private String id_visitor;

	/**
	 * number of the order
	 */
	public static int numberOrder;

	/**
	 * boolean object for checing if place is available
	 */
	public static boolean checkPlace;
	
	/**
	 * VisitorType - WITH ORDER OR NEW ONE
	 */

	public static boolean VisitorType;
	
	/**
	 * ArrayList of open dates
	 */

	public static ArrayList<DateOrder> dates_list;

	/**
	 * flag
	 */
	private int flag = 0;

	@FXML
	private Button logOut;

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnCancelOrder;

	@FXML
	private Button btnViewOrdersTable;

	@FXML
	private ComboBox<String> comboPark;

	@FXML
	private DatePicker DatePicker;

	@FXML
	private ComboBox<String> comboTime;

	@FXML
	private ComboBox<String> comboAmount;

	@FXML
	private TextField txtVisitors;

	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnEnterWatingList;

	@FXML
	private Label msgError;

	@FXML
	private Button btnViewDates;

	@FXML
	private Button btnCreateOrder;

	@FXML
	private TableView<DateOrder> openDates;

	@FXML
	private TableColumn<DateOrder, String> dates;

	/**
	 * data of the table
	 */
	public ObservableList<DateOrder> data = FXCollections.observableArrayList();

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

	public static double btn_click = 0;

	/**
	 * initialize subscriber info and id_visitor, getting the visitor type from the server , setting the order data - in case of
	 * subsc. / guide
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.subscriber = loginPageController.subscriber;
		this.id_visitor = loginPageController.id_visitor;

		MessageCS message = new MessageCS(MessageType.GetVisitorType, "", new Order(this.id_visitor));
		ClientUI.chat.accept(message);

		dates.setCellValueFactory(new PropertyValueFactory<DateOrder, String>("date"));

		if (!VisitorType) // Hide button
			btnViewOrdersTable.setVisible(false);

		if (flag == 0) {
			ObservableList<String> options = FXCollections.observableArrayList("A", "B", "C");
			comboPark.setItems(options);
			comboPark.getSelectionModel().selectFirst();

			ObservableList<String> Timeoptions = FXCollections.observableArrayList("09:00", "10:00", "11:00", "12:00",
					"13:00", "14:00", "15:00", "16:00", "17:00", "18:00","19:00","20:00","21:00");
			comboTime.setItems(Timeoptions);
			comboTime.getSelectionModel().selectFirst();

			DatePicker.setValue(NOW_LOCAL_DATE());

			if (subscriber != null) // Insert details of subscriber
			{
				if (subscriber.getType().equals("Family")) {
					updateComboAmount(1, subscriber.getSubscriber_amount());
				}
				if (subscriber.getType().equals("Guide")) {
					updateComboAmount(2, 15);

				}

				txtEmail.setText(subscriber.getEmail());
			}

			if (subscriber == null) { // If is new visitor
				updateComboAmount(1, 50);
			}
		}
	}

	/**
	 * Switch the screen to main menu .
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		if (!VisitorType) {
			ScreenController.switchScenes("/client/boundary/MenuNewVisitor.fxml");
		} else {
			ScreenController.switchScenes("/client/boundary/MenuVisitorWithOrder.fxml");
		}

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
	 * Switch the screen to create order page .
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void clickCreateOrderMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/CreateOrder.fxml");
	}

	/**
	 * Switch the screen to View Orders Table .
	 * 
	 * @param event - the event that fired the method .
	 */
	@FXML
	void btnViewOrdersTable(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ViewMyOrders.fxml");

	}

	/**
	 * 
	 * @return the current date
	 */
	public static final LocalDate NOW_LOCAL_DATE() {
		String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}

	/**
	 * set the combo amount data 
	 * @param start - start number
	 * @param end - end number
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
	 * When CreateOrder button is pressed , the client send message to the server to check if date is open, and making order +
	 * sending email for the order maker
	 * @param event - the event that fired the method
	 * @throws MessagingException - in fail
	 */
	/////////////////////////////////////////// Insert of order details
	@FXML
	void clickCreateOrder(ActionEvent event) throws MessagingException {

		order = new Order();

		order.setParkName(comboPark.getValue());
		order.setDate(DatePicker.getValue().toString());
		order.setTime(comboTime.getValue());
		order.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));
		order.setEmail(txtEmail.getText());
		order.setOwnerId(Integer.parseInt(id_visitor));

		if (InputTestFunctions.ValidateDate(DatePicker.getValue().toString(), comboTime.getValue())) {

			if (InputTestFunctions.ValidateEmail(txtEmail.getText())) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm Order");
				alert.setHeaderText("Order Confirmation");
				alert.setContentText("Are You Sure You Want To Make Your Order?");
				ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
				alert.getButtonTypes().setAll(okButton, noButton);

				Optional<ButtonType> result = alert.showAndWait();

				if (result.get().getText() == "Yes") {

					if (checkAvilablePlaceInPark(order)) {
						if (subscriber == null) {
							order.setTypeOfOrder("Single");
						} else {
							order.setTypeOfOrder(subscriber.getType());
						}
						order.setStatus("not arrived");
						setPrice(order);
						MessageCS message = new MessageCS(MessageType.Create_Order, "Create new order", order);
						ClientUI.chat.accept(message);
						order.setOrderId(numberOrder);
						btnViewOrdersTable.setVisible(true);
						ViewOrderDetailsController.setOrder(order);
						
						if(order.getTypeOfOrder().equals("Guide")) {
							ViewOrderDetailsController.setPayNowButtonShow(order);
						}

						SendMail.sendMailConfirme(order.getEmail(), order);

						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Done");
						alert.setHeaderText("Order Made Successfully");
						alert.setContentText("Thank You");
						alert.showAndWait();

						ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");
					}

					else {
						alert = new Alert(AlertType.WARNING);
						alert.setTitle("No Space Available");
						alert.setHeaderText(null);
						alert.setContentText("There is no space available in the park");
						alert.show();

						btnEnterWatingList.setVisible(true);
						btnViewDates.setVisible(true);
						openDates.setVisible(true);
					}
				}
			} // End if of ValidationEmail
		}

	}

	/**
	 * view the other dates , when click, the client send message to server to get all of dates in the coming week.
	 * @param event - that fired the method
	 */
	@FXML
	void viewOtherDates(ActionEvent event) {

		order = new Order();

		order.setParkName(comboPark.getValue());
		order.setDate(DatePicker.getValue().toString());
		order.setTime(comboTime.getValue());
		order.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));
		order.setEmail(txtEmail.getText());
		order.setOwnerId(Integer.parseInt(id_visitor));
		if (subscriber == null) {
			order.setTypeOfOrder("Single");
		} else {
			order.setTypeOfOrder(subscriber.getType());
		}
		order.setStatus("not arrived");

		openDates.getItems().clear();
		MessageCS message = new MessageCS(MessageType.CheckOtherDates, NOW_LOCAL_DATE(), order);
		ClientUI.chat.accept(message);
		data.addAll(dates_list);
		openDates.setItems(data);
		openDates.setStyle("-fx-alignment: CENTER;");
	}

	/**
	 * check if place is Avilable in the park , sending the message and return boolean
	 * @param order - which order to check
	 * @return bool data
	 */
	private boolean checkAvilablePlaceInPark(Order order) {
		MessageCS message = new MessageCS(MessageType.CheckPlace, "Check Place In Park", order);
		ClientUI.chat.accept(message);
		return checkPlace;
	}

	/**
	 * this is for entering waitinglist, when clicked, Enter To Waiting List is done
	 * the button is visible only when place is not avilable
	 * @param event - that fired the method
	 * @throws MessagingException - error
	 */
	@FXML
	void clickEnterWitingList(ActionEvent event) throws MessagingException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Waiting List");
		alert.setHeaderText("Waiting List Confirmation");
		alert.setContentText("Are You Sure You Want To Enter To Waiting List?");
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get().getText() == "Yes") {

			order = new Order();

			order.setParkName(comboPark.getValue());
			order.setDate(DatePicker.getValue().toString());
			order.setTime(comboTime.getValue());
			order.setNumberOfVisitors(Integer.parseInt(comboAmount.getValue()));
			order.setEmail(txtEmail.getText());
			order.setOwnerId(Integer.parseInt(id_visitor));

			if (subscriber == null) {
				order.setTypeOfOrder("Single");
			} else {
				order.setTypeOfOrder(subscriber.getType());
			}
			
			order.setStatus("Waiting");
			setPrice(order);
			MessageCS message = new MessageCS(MessageType.Create_Order, "Create new order with status Waiting", order);
			ClientUI.chat.accept(message);
			order.setOrderId(numberOrder);

			ViewOrderDetailsController.setOrder(order);
			btnViewOrdersTable.setVisible(true);

			MessageCS message2 = new MessageCS(MessageType.Enter_WAITINGLIST, "Create order in waitinglist", order);
			ClientUI.chat.accept(message2);		
			
			SendMail.sendMailWaitingList1(order.getEmail(), order);
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done");
			alert.setHeaderText("You Entered To Waiting List");
			alert.setContentText("We will inform you when a place will be vacated ");
			alert.showAndWait();
			
			ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");
		}

	}

	/**
	 * restWindow - set the buttons visibliy to false: enter waitinglist , view other dates and the table
	 * @param event that fired the method
	 */
	@FXML
	void restWindow(ActionEvent event) {
		btnEnterWatingList.setVisible(false);
		btnViewDates.setVisible(false);
		openDates.setVisible(false);
	}
	
	/**
	 * set the price for the order, getting the info of the discounts from the server
	 * @param order - which order to set 
	 */
	
	private void setPrice(Order order) {
		MessageCS message2 = new MessageCS(MessageType.CheckDiscountForOrder, "Check My Discount", order);
		ClientUI.chat.accept(message2);
		discount = 0;
		totalprice = 0;
		price_discount = 0;
		discount = discount / 100.0;
		if (order.getTypeOfOrder().equals("Family")) {
			price_discount = parkPrice * 0.15 * order.getNumberOfVisitors();
			price_discount += parkPrice * 0.20 * order.getNumberOfVisitors();
			price_discount += parkPrice * discount * order.getNumberOfVisitors();
			totalprice = (parkPrice * order.getNumberOfVisitors()) - price_discount;
		}

		if (order.getTypeOfOrder().equals("Single")) {
			price_discount = parkPrice * 0.15 * order.getNumberOfVisitors();
			price_discount += parkPrice * discount * order.getNumberOfVisitors();
			totalprice = (parkPrice * order.getNumberOfVisitors()) - price_discount;
		}

		if (order.getTypeOfOrder().equals("Guide")) {
			price_discount = parkPrice * 0.25 * (order.getNumberOfVisitors() - 1);
			price_discount += parkPrice * discount * (order.getNumberOfVisitors() - 1);
			totalprice = parkPrice * (order.getNumberOfVisitors() - 1) - price_discount;
		}
		
		String full_price = String.format("%.2f", totalprice+price_discount);
		String discount = String.format("%.2f", price_discount);
		String total_price = String.format("%.2f", totalprice);
		String val = full_price + "," + discount + "," + total_price;
		order.setPaynow(val);
		order.setFull_price(full_price);
		order.setDiscount(discount);
		order.setTotal_price(total_price);

	}
}
