package client.logic;

import java.io.Serializable;

/**
 * This class represent the structure of the Usage Report and its getters and setters
 *
 */
public class ReportUsage implements Serializable {
	private String time;
	private int amount;
	private String maxVisitors;
	private String precent;
	/**
	 * constructor for ReportUsage
	 * @param time
	 * @param amount
	 * @param maxVisitors
	 * @param precent
	 */
	public ReportUsage(String time, int amount, String maxVisitors, String precent) {
		super();
		this.time = time;
		this.amount = amount;
		this.maxVisitors = maxVisitors;
		this.precent = precent;
	}
/**
 * @return time -  get  and return specific hour for usage report
 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time- set the time that we choose for the usage report
	 */

	public void setTime(String time) {
		this.time = time;
	}
/**
 * 
 * @return- amount-get the amount of the visitors and return it
 */
	public int getAmount() {
		return amount;
	}
/**
 * 
 * @param amount- set the new amount of visitors that we send
 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
/**
 * @return maxVisitors-get and return maximum of the visitors  in the park
 */
	public String getMaxVisitors() {
		return maxVisitors;
	}
/**
 * @param maxVisitors -set new maximum of the visitors that we send
 */
	public void setMaxVisitors(String maxVisitors) {
		this.maxVisitors = maxVisitors;
	}
/**
 * @return precent-return the percentage that we want to get
 */
	public String getPrecent() {
		return precent;
	}
/**
 * @param precent-set new perentage that we want to send for report
 */
	public void setPrecent(String precent) {
		this.precent = precent;
	}
	
	
	
}
