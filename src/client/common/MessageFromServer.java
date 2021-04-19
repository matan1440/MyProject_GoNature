package client.common;

import client.common.InputTestFunctions;
import client.common.MessageCS;
import client.controllers.AddOccasionalController;
import client.controllers.AddVisitorController;
import client.controllers.CardReaderEntranceController;
import client.controllers.CardReaderExitController;
import client.controllers.ConfirmDiscount;
import client.controllers.ConfirmParametersController;
import client.controllers.CreateOrderController;
import client.controllers.CreateSubController;
import client.controllers.DepReportCanncellationController;
import client.controllers.DepReportStayingController;
import client.controllers.DepReportVisitingController;
import client.controllers.DepartmentManagerManuController;
import client.controllers.EntranceController;
import client.controllers.ExitVisitController;
import client.controllers.ParkManagerManuController;
import client.controllers.ParkReportIncomeController;
import client.controllers.ParkReportUsageController;
import client.controllers.ParkReportVisitorsController;
import client.controllers.UpdateDiscountController;
import client.controllers.ViewMyOrdersController;
import client.controllers.ViewOrderDetailsController;
import client.controllers.loginPageController;
import client.controllers.updateParametersController;

/**
 * this class handles messages the server sends back to the client.
 * based on the type of message the program sends in the server, we update values
 * in the controllers in the client 
 * @author matan daniel tanya tomer omer
 *
 */
public class MessageFromServer {
	@SuppressWarnings({ "incomplete-switch" })
	
	/**
	 * this method handles messages the server sends back to the client.
	 * based on the type of message the program sends in the server, we update values
	 * in the controllers in the client 
	 * @author matan daniel tanya tomer omer
	 *
	 */
	public static void HandleMessage(Object msg) {
		MessageCS message = (MessageCS) msg;
		switch (message.messageType) {
		
		/******************************************************** Omer Matan  ******************************************/
		
		case LOGIN_CHECK:
			loginPageController.Connected = message.conn_check;
			loginPageController.ConnectedUser = message.num;
			break;
	
		case LOG_OUT:
			loginPageController.Connected = false;
			loginPageController.ConnectedUser = 0;
	
			
		case ERROR_LOGIN:
			loginPageController.Error_Msg = message.getString();
			break;

		case LOGIN_WORKER:
			loginPageController.worker = message.Worker;
			break;

		case LOGIN_NEW_VISITOR:
			loginPageController.Msg = message.string;
			break;

		case LOGIN_OWNER_ORDER:
			loginPageController.Msg = message.string;
			loginPageController.order = message.order;
			break;

		case LOGIN_SUBSCRIBER_WITHOUT_ORDER:
			loginPageController.Msg = message.string;
			loginPageController.subscriber = message.subscriber;
			break;

		case LOGIN_SUBSCRIBER_WITH_ORDER:
			loginPageController.Msg = message.string;
			loginPageController.subscriber = message.subscriber;
			loginPageController.order = message.order;
			break;

		
		case Create_Order:
			CreateOrderController.numberOrder = message.num;
			break;
		
		case CheckPlace:
			CreateOrderController.checkPlace = message.check;
		
		case CheckOtherDates:
			CreateOrderController.dates_list = message.orderDateslist;
			break;
			
		case ThereIsPlace:
			ViewOrderDetailsController.bool = message.check;
			break;
		
		case PayNow:
			ViewOrderDetailsController.price_after_pay_now = message.string;
			break;
		case CheckTopWaitingListAndPlace:
			ViewOrderDetailsController.bool2 = message.check;
			break;
		case CheckIfConfirmArrivel:
			ViewOrderDetailsController.bool = message.check;

		case MyOrders:
			ViewMyOrdersController.myorders = message.myOrders;
			break;
		case CheckDiscountForOrder:
			CreateOrderController.discount = Double.parseDouble(message.string);
			break;

		case CardReaderCheckEntrance:
			CardReaderEntranceController.subscriber = message.subscriber;
			CardReaderEntranceController.myorders = message.myOrders;
			CardReaderEntranceController.discount = Double.parseDouble(message.string);
			break;
		case CardReaderCheckCapicity:
			CardReaderEntranceController.NoOrderCapacity = message.num;
			CardReaderEntranceController.currentNoOrderVisitor = message.num2;
			break;
		case CardReaderEnterToPark:
			CardReaderEntranceController.visit_id = message.num;
			CardReaderEntranceController.current_number_of_visitors = message.num2;
			break;
			
		case CardReaderCheckVisitId:
			CardReaderExitController.idCheck = message.conn_check;
			CardReaderExitController.visit_id = message.num;
			break;
			
		case CardReaderExitFromPark:
			CardReaderExitController.current_number_of_visitors = message.num;
			CardReaderExitController.Park_name = message.string;
			break;
		case GetVisitorType:
			CreateOrderController.VisitorType = message.check;
			
		/******************************************************** Omer Matan  ******************************************/

		/******************************************************** Tomer  ******************************************/

		case CREATE_SUBSCRIBER:
			CreateSubController.sub_id = message.num;
			break;
			
		case SEARCH_ORDER:
			AddVisitorController.order = message.order;
			AddVisitorController.stringOfmsg = message.string;
			break;
			
		case Enter_to_park:
			AddVisitorController.visitID=message.num;
			AddVisitorController.current_number_of_visitors = message.num2;
			break;
		
		case UPDATE_NUM_OF_VISITORS:
			if(message.string.equals("A")) {
				EntranceController.Number_Of_Visitors_A = message.num;
				ParkManagerManuController.Number_Of_Visitors_A = message.num;
				DepartmentManagerManuController.Number_Of_Visitors_A = message.num;
			}
			else if (message.string.equals("B")) {
				EntranceController.Number_Of_Visitors_B = message.num;
				ParkManagerManuController.Number_Of_Visitors_B = message.num;
				DepartmentManagerManuController.Number_Of_Visitors_B = message.num;
			}
			else if (message.string.equals("C")) {
				EntranceController.Number_Of_Visitors_C = message.num; 
				ParkManagerManuController.Number_Of_Visitors_C = message.num;
				DepartmentManagerManuController.Number_Of_Visitors_C = message.num;
			}
			break;
			
		case CHECK_AMOUNT:
			AddOccasionalController.spaceavialble=message.check;
			break;
			
		case ACCSESS_CHECK_ID:
			AddOccasionalController.subscriber=message.subscriber;
			AddOccasionalController.discount= Double.parseDouble(message.string);
			break;
			
		case ENTER_PARK_NO_ORDER:
			AddOccasionalController.visitID = message.num;
			AddOccasionalController.current_number_of_visitors =message.num2;
			break;
			
		case CHECK_VISIT_ID:
			ExitVisitController.visitID_yes_no=message.check;
			break;
			
		case VISITOR_EXIT_PARK:
			ExitVisitController.current_number_of_visitors = message.num;
			break;
		
		case Get_Park_Params:
			ParkManagerManuController.maxVistor = message.num;
			ParkManagerManuController.maxOrders = message.num2;
			ParkManagerManuController.visittime = Double.parseDouble(message.string);
			break;
		
		case Get_Subscriber:
			InputTestFunctions.CheckSubscriber = message.check;
			break;

			
			
		/******************************************************** Tomer  ******************************************/

		
		/******************************************************** Dani Tanya ******************************************/
	
		case REQUEST_SENT:
			updateParametersController.requestMessage = message.getString();
			break;
			
		case DISCOUNT_UPDATED:
			UpdateDiscountController.updateDiscount = message.getString();
			break;
			
		case SHOW_VALUES:
			ConfirmParametersController.requestPar = message.getString();
			break;
			
		case ACTION_SUCCESSFULL:
			ConfirmParametersController.requestPar = message.getString();
			break;
			
		case SHOW:
			ConfirmDiscount.discounts = message.getString();
			break;
			
		case CHANGE_DISCOUNT:	
			ConfirmDiscount.discounts = message.getString();
			break;
			
		case DELETE_DISCOUNT:
			ConfirmDiscount.discounts = message.getString();
			break;
			
		case SHOW_CANCEL_REPORT:
			DepReportCanncellationController.ans = message.getString();
			break;
			
		case REPORT_VISITORS:
			ParkReportVisitorsController.ans = message.getString();
			break;
			
		case REPORT_INCOME:
			ParkReportIncomeController.ans = message.getString();
			break;
			
		case REPORT_USAGE:
			ParkReportUsageController.ans = message.getString();
			ParkReportUsageController.dateMaxVisitors = Integer.parseInt(message.string1);
			break;
		
		case REPORT_VISITING:
			DepReportVisitingController.ans = message.getString();
			break;
			
		case REPORT_STAYING:
			DepReportStayingController.ans = message.getString();
			break;
			
		
		/******************************************************** Dani Tanya ******************************************/

	
		}
	}
}
