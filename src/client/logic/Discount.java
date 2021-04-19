package client.logic;

import java.io.Serializable;

/**
 * In this class we save details about the discount in the system GoNatuer
 * @author Matan
 *
 */
public class Discount implements Serializable {
	private String number;
	private String park;
	private String discount;
	private String from;
	private String to;
	private String status;
	


	/**
	 * Contractor to insert details of discount
	 * @param number
	 * @param park
	 * @param discount
	 * @param from
	 * @param to
	 * @param status
	 */
	public Discount(String number, String park, String discount, String from, String to, String status) {
		super();
		this.number = number;
		this.park = park;
		this.discount = discount;
		this.from = from;
		this.to = to;
		this.status = status;
	}
	

	/**
	 * This function return the status of discount (approved / canceled)
	 * @return String
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * This function insert the status of discount
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * This function return the index of discount
	 * @return String
	 */
	public String getNumber() {
		return number;
	}

	
	/**
	 * This function insert the index of discount
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * This function return the name of park
	 * @return String
	 */
	public String getPark() {
		return park;
	}

	
	/**
	 * In this function insert the name of park (of the discount) 
	 * @param park
	 */
	public void setPark(String park) {
		this.park = park;
	}

	
	/**
	 * This function return value fo the discount
	 * @return String
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * This function insert the number of the discount
	 * @param discount
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * This function return the date start of the discount
	 * @return String
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * This function insert the date start of the discount
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * This function return the end date of the discount 
	 * @return String
	 */
	public String getTo() {
		return to;
	}

	/**
	 * This function insert of the end date of the discount
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	
}
