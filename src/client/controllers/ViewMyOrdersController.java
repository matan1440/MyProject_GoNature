package client.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import client.ClientUI;

import client.common.MessageCS;
import client.common.MessageCS.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import client.logic.Order;

/**
 * This class is the controller of View My Orders Screen - the controller loads the orders of the Traveler 
 * into the tableview , A request is being sent to the server for the realvent data
 * @author Omer
 *
 */
public class ViewMyOrdersController implements Initializable {

    @FXML
    private Button logOut;

    @FXML
    private Button mainMenuBtn;

    @FXML
    private Button btnCreateOrder;

    @FXML
    private Button btnViewOrderDetails;

    @FXML
    private TableView<Order> myOrdersTable;

    @FXML
    private TableColumn<Order, Integer> idCol;

    @FXML
    private TableColumn<Order, String> parkCol;

    @FXML
    private TableColumn<Order, String> dateCol;

    @FXML
    private TableColumn<Order, String> timeCol;

    @FXML
    private TableColumn<Order, Integer> amountCol;

    @FXML
    private TableColumn<Order, String> emailCol;

    @FXML
    private TableColumn<Order, Integer> ownerCol;

    @FXML
    private TableColumn<Order, String> typeCol;

    @FXML
    private TableColumn<Order, String> statusCol;
    
    @FXML
    private Label lblOwnerID;
    
    @FXML
    private Button viewOrder;
    
    /**
     * visitor id
     */
    private String id_visitor;
    
    /**
     * ArrayList of the visitor orders
     */
	public static ArrayList<Order> myorders;
	
	/**
	 * the order object of the selected row in the table
	 */
	public static Order selectedOrder;

	/**
	 * ObservableList of Order of the data
	 */
	public ObservableList<Order> data = FXCollections.observableArrayList();

	


	/**
	  * initialize the table columns of the orders table , the traveler
	 * id , sending request to the server for the realvent data , when the request is returend from the server
	 * the controller update the tableview .
	 * An EventHandler is being attached to any of the table rows - when clicked, the controller open the order deteils
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.id_visitor = loginPageController.id_visitor;
		selectedOrder = null;
		idCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
		parkCol.setCellValueFactory(new PropertyValueFactory<Order, String>("parkName"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
		timeCol.setCellValueFactory(new PropertyValueFactory<Order, String>("time"));
		amountCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("numberOfVisitors"));
		emailCol.setCellValueFactory(new PropertyValueFactory<Order, String>("email"));
		typeCol.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		statusCol.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
		
		myOrdersTable.getItems().clear();
		MessageCS message = new MessageCS(MessageType.MyOrders, id_visitor);
		ClientUI.chat.accept(message);
		data.addAll(myorders);
		myOrdersTable.setItems(data);
		lblOwnerID.setText(id_visitor);
		
		myOrdersTable.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	showOrder();                  
		        }
		    }
		});
		
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
     * Calls showOrder method , showOrder loads the order info into ViewOrderDetails controller.
     * @param event - the event that fired the method .
     */
    @FXML
    void viewOrder(MouseEvent event) {
    	showOrder();
    }
  
    /**
     * the method calls ViewOrderDetailsController setOrder method - loads the selected order object into the controller 
     * if there is a selection of a row - the method change the screen to ViewOrderDetails
     * In case of no selection of a row - ERROR alert pops. 
     */
    private void showOrder() {
    	selectedOrder = myOrdersTable.getSelectionModel().getSelectedItem();
		ViewOrderDetailsController.setOrder(selectedOrder);
		if (selectedOrder != null) {
			ScreenController.switchScenes("/client/boundary/ViewOrderDetails.fxml");	
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not Selected Order");
			alert.setHeaderText(null);
			alert.setContentText("Please Choose An Order");
			alert.showAndWait();
			
		}
    }
}
