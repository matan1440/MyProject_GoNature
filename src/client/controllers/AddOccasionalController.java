package client.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import client.ClientUI;
import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import client.logic.Subscriber;
import client.logic.Worker;

/**
 * this class handles adding a random visitor, search for place availability,
 * enters visitor and updates in data base, calculates price based on visitor type 
 * 
 * 
 * @version 1.0
 * @author tomer omer
 *
 */
public class AddOccasionalController implements Initializable {

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
	private Button btnCheckParkAmount;

	@FXML
	private Button btnOccasionalVisit;

	@FXML
	private Button btnAddVisitor;

	@FXML
	private Button btnExit;

	@FXML
	private TextField txtOrderID;

	@FXML
	private Label lblCheckID;

	@FXML
	private Button btnSearch;

	@FXML
	private Label lblCheckAmount;

	@FXML
	private TextField txtAmount;

	@FXML
	private Button btnCheckCapacity;

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
	private Button btnEnterPark;

	private Worker worker;
	
	/**
	 * Subscriber info for the controller
	 */
	public static Subscriber subscriber;
	
	/**
	 * boolean check if spaceavialble
	 */
	public static boolean spaceavialble;
	
	/**
	 * visit ID 
	 */
	
	public static int visitID;
	
	/**
	 * this is current_number_of_visitors in the park of the worker 
	 */
	public static int current_number_of_visitors = 0;


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
	 *initialize page with worker name and park belong to, number of visitors in park,
	 *time and date
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
	 * switch screen to Worker Entrance Menu
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickMainMenu(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/WorkerEntranceMenu.fxml");

	}

	/**
	 * switch screen to login page
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickLogOut(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/loginPage.fxml");

	}

	/**
	 * switch screen to add random visit
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickAddOccasional(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/AddOccasional.fxml");
	}

	/**
	 * switch screen to add visitor
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickAddVisitor(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/AddVisitor.fxml");
	}

	/**
	 * switch screen to exit visit
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickExitVisit(ActionEvent event) {
		ScreenController.switchScenes("/client/boundary/ExitVisit.fxml");
	}

	/**
	 * check if there is enough place for occasional visitors in park based on amount in txtamount
	 * sends message to server with worker info and amount number
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickOnCheckCapacity(ActionEvent event) {

		if (InputTestFunctions.ValidateNumber(txtAmount.getText())) {

			MessageCS message = new MessageCS(MessageType.CHECK_AMOUNT, worker, Integer.parseInt(txtAmount.getText()));
			ClientUI.chat.accept(message);

			if (spaceavialble) {
				txtOrderID.setDisable(false);
				btnSearch.setDisable(false);
				txtAmount.setDisable(true);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Sorry");
				alert.setHeaderText("There Is No Place Availble");
				alert.setContentText("You Can't Enter :-| ");
				alert.showAndWait();
			}

		}
	}

	/**
	 * validates id number, checks if visitor is subscriber or not, calculates price accordingly
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickSearchID(ActionEvent event) {

		if (InputTestFunctions.ValidateNumber(txtOrderID.getText())) {
			if (InputTestFunctions.ValidateSizeID(txtOrderID.getText())) {
				MessageCS message = new MessageCS(MessageType.ACCSESS_CHECK_ID, worker,
						Integer.parseInt(txtOrderID.getText()));
				ClientUI.chat.accept(message);

				discount = discount / 100.0;

				setSumLabel(Integer.parseInt(txtAmount.getText()));

				btnEnterPark.setDisable(false);
			}
		}

	}

	/**
	 * calculates price based on visitor type and amount of visitors entering 
	 * and updates labels 
	 * 
	 * @param amount - amount of visitors from txtamount text field
	 */
	private void setSumLabel(int amount) {
		parkPrice = 15;
		totalprice = 0;
		price_discount = 0;

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
		lblSumBefore.setText(String.valueOf(totalprice + price_discount));
		lblSumDiscount.setText(String.valueOf(price_discount));
		lblSum.setText(String.valueOf(totalprice));

	}

	/**
	 * sends message to server with worker and visitor info, updates visit into database,
	 * updates amount of visitors in park in amount variable by sending message to server with 
	 * current amount
	 * 
	 * @param event - event that caused method
	 */
	@FXML
	void clickEnterPark(ActionEvent event) {

		MessageCS message = new MessageCS(MessageType.ENTER_PARK_NO_ORDER, subscriber, worker,
				Integer.parseInt(txtAmount.getText()), lblSum.getText(), txtOrderID.getText());
		ClientUI.chat.accept(message);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText("You Entered The Park Susscfuly");
		alert.setContentText("Your Visit Id : " + visitID + " \nFor The Exit");
		alert.showAndWait();

		MessageCS message0 = new MessageCS(MessageType.UPDATE_NUM_OF_VISITORS, current_number_of_visitors,
				worker.getParkName());
		ClientUI.chat.accept(message0);

		ScreenController.switchScenes("/client/boundary/AddOccasional.fxml");
	}

}
