package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Message;

import client.ClientUI;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import client.logic.Discount;
import client.logic.Worker;

/**
 * this class represents the confirmation of a discount
 * 
 * @author danie
 * 
 *
 */
public class ConfirmDiscount implements Initializable {

	@FXML
	private Button mainMenuBtn;

	@FXML
	private Button btnReports;

	@FXML
	private Button btnConfirmDiscounts;

	@FXML
	private Button btnConfirmParemters;

	@FXML
	private Button btnChange;

	@FXML
	private Button logOut;
	@FXML
	private Label lblStatus;

	@FXML
	private TableView<Discount> tableView;

	@FXML
	private TableColumn<Discount, String> colnumber;

	@FXML
	private TableColumn<Discount, String> colParkName;

	@FXML
	private TableColumn<Discount, String> colDiscount;

	@FXML
	private TableColumn<Discount, String> colTo;

	@FXML
	private TableColumn<Discount, String> colFrom;

	@FXML
	private TableColumn<Discount, String> colStatus;

	@FXML
	private Button btnShow;

	@FXML
	private Button btnDeclineDiscount;

	@FXML
	private TextField txtDiscountNumber;

	@FXML
	private Label lblTime;

	@FXML
	private Label lblDate;
	/**
	 * discounts is the returned value after the server searched for data in sql
	 */
	public static String discounts;

	private Worker worker;

	/**
	 * this method initialize the table of discounts(number, park, discount, from,
	 * to, status) shows the current time show the current date
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.worker = loginPageController.worker;
		colnumber.setCellValueFactory(new PropertyValueFactory<Discount, String>("number"));
		colParkName.setCellValueFactory(new PropertyValueFactory<Discount, String>("park"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<Discount, String>("discount"));
		colFrom.setCellValueFactory(new PropertyValueFactory<Discount, String>("from"));
		colTo.setCellValueFactory(new PropertyValueFactory<Discount, String>("to"));
		colStatus.setCellValueFactory(new PropertyValueFactory<Discount, String>("status"));

		Show(new ActionEvent());

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();

			String curTime = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
					currentTime.getSecond());
			lblTime.setText(curTime);

		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();

		lblDate.setText(LocalDate.now().toString());

	}

	/**
	 * Switch the screen to confirm Discount screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmDiscounts(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmDiscount.fxml");
	}

	/**
	 * Switch the screen to confirm Parameters screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void ConfirmParemters(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/confirmParameters.fxml");

	}

	/**
	 * Switch the screen to Dep Report screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void CreateReportsAction(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepReport.fxml");

	}

	/**
	 * Switch the screen to DepartmentM anager Menu screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/DepartmentManagerMenu.fxml");

	}

	/**
	 * Switch the screen to login Page screen
	 * 
	 * @param event-the event that fired the method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * send a request for the server to search for discount in the sql create a
	 * table of discounts (number, park, discount, from, to, status)
	 * 
	 * @param event-the event that fired the method
	 */

	@FXML
	void Show(ActionEvent event) {
		discounts = "";
		final ObservableList<Discount> data = FXCollections.observableArrayList();
		Discount a;
		String[] temp;
		int i;

		MessageCS message = new MessageCS(MessageType.SHOW_DISCOUNTS, worker);
		ClientUI.chat.accept(message);
		if (!discounts.isEmpty()) {

			String[] discountRows = discounts.split("=");
			for (i = 0; i < discountRows.length; i++) {

				temp = discountRows[i].split(",");
				a = new Discount(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
				data.add(a);
			}
		}

		tableView.setItems(FXCollections.observableArrayList());

		tableView.setItems(data);

	}

	/**
	 * check if txtDiscountNumber is empty or contains not a number
	 * confirm discount by the discount number chosen
	 * use the function show
	 * @param event-the event that fired the method
	 */
	@FXML
	void Change(ActionEvent event) {
		if (txtDiscountNumber.getText().equals("")) {
			lblStatus.setText("Numebr field is empty");
			return;
		}
		
		if(!(txtDiscountNumber.getText().matches("[0-9]+")) || Integer.parseInt(txtDiscountNumber.getText()) < 1 ) {
			lblStatus.setText("Numebr value is not valid");
			return;
		}
		MessageCS message = new MessageCS(MessageType.CHANGE_DISCOUNT, txtDiscountNumber.getText());
		ClientUI.chat.accept(message);
		Show(new ActionEvent());

	}
	/**
	 * check if txtDiscountNumber is empty or contains not a number
	 * delete discount by the discount number chosen
	 * use the function show
	 * @param event-the event that fired the method
	 */
	@FXML
	void DeclineDiscount(ActionEvent event) {
		if (txtDiscountNumber.getText().equals("")) {
			lblStatus.setText("Numebr field is empty");
			return;
		}
		
		if(!(txtDiscountNumber.getText().matches("[0-9]+")) || Integer.parseInt(txtDiscountNumber.getText()) < 1 ) {
			lblStatus.setText("Numebr value is not valid");
			return;
		}
		MessageCS message = new MessageCS(MessageType.DELETE_DISCOUNT, txtDiscountNumber.getText());
		ClientUI.chat.accept(message);
		Show(new ActionEvent());

	}
}
