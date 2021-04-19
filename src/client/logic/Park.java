package client.logic;

import java.util.ArrayList;

/**
 * In this class we save details about the parks in the system GoNatuer
 * @author Matan
 *
 */
public class Park {
	private String parkName;
	private int maxVisitor;
	private int maxOrders;
	private int currentVisitorAmountInPark;
	private String maxVisitTime;

	
	/**
	 * Contractor to insert details of parks 
	 * @param parkName
	 * @param maxVisitor
	 * @param maxOrders
	 * @param currentVisitorAmountInPark
	 * @param maxVisitTime
	 */
	public Park(String parkName, int maxVisitor, int maxOrders, int currentVisitorAmountInPark, String maxVisitTime) {
		super();
		this.parkName = parkName;
		this.maxVisitor = maxVisitor;
		this.maxOrders = maxOrders;
		this.currentVisitorAmountInPark = currentVisitorAmountInPark;
		this.maxVisitTime = maxVisitTime;
	}
	
	
	/**
	 * This function return the name of park
	 * @return String
	 */
	public String getParkName() {
		return parkName;
	}
	
	
	/**
	 * In this function insert name of park 
	 * @param parkName
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
	
	/**
	 * This function return the number of max visitor in park
	 * @return int
	 */
	public int getMaxVisitor() {
		return maxVisitor;
	}
	
	
	/**
	 * In this function insert number of max visitor of park
	 * @param maxVisitor
	 */
	public void setMaxVisitor(int maxVisitor) {
		this.maxVisitor = maxVisitor;
	}
	
	
	/**
	 * This function return the number of max orders of the park
	 * @return int
	 */
	public int getMaxOrders() {
		return maxOrders;
	}
	
	
	/**
	 * In this function insert the number of max orders of the park
	 * @param maxOrders
	 */
	public void setMaxOrders(int maxOrders) {
		this.maxOrders = maxOrders;
	}
	
	/**
	 * This function return the number of current visitor in park
	 * @return int
	 */
	public int getCurrentVisitorAmountInPark() {
		return currentVisitorAmountInPark;
	}
	
	/**
	 * In this function insert the number of current visitor in park
	 */
	public void setCurrentVisitorAmountInPark(int currentVisitorAmountInPark) {
		this.currentVisitorAmountInPark = currentVisitorAmountInPark;
	}
	
	
	/**
	 * This function return the number of max visit time.
	 * @return String
	 */
	public String getMaxVisitTime() {
		return maxVisitTime;
	}
	
	
	/**
	 * In this function insert the max visit time
	 * @param maxVisitTime
	 */
	public void setMaxVisitTime(String maxVisitTime) {
		this.maxVisitTime = maxVisitTime;
	}
	

	
	
}
