package client.logic;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is for Order Object
 * @author Omer
 *
 */

public class Order implements Serializable{
	private int orderId;
	private String parkName;
	private String date;
	private String time;
	private int numberOfVisitors;
	private String email;
	private int ownerId;
	private String typeOfOrder;
	private String status;
	private String paynow;
	
	private String full_price;
	private String discount;
	private String total_price;

	
	public Order() {
		super();
	}
	
	/**
	 * Order Constructor
	 * 
	 * @param orderId - order id
	 * @param parkName - park name
	 * @param date - order date
	 * @param time - order time
	 * @param numberOfVisitors - order number of visitors
	 * @param email - the order owner email
	 * @param ownerId - owner id
	 * @param typeOfOrder - type of order : Single / Familiy / Guide
	 * @param status - order status
	 */
	public Order(int orderId, String parkName, String date, String time, int numberOfVisitors, String email,
			int ownerId, String typeOfOrder, String status) {
		super();
		this.orderId = orderId;
		this.parkName = parkName;
		this.date = date;
		this.time = time;
		this.numberOfVisitors = numberOfVisitors;
		this.email = email;
		this.ownerId = ownerId;
		this.typeOfOrder = typeOfOrder;
		this.status = status;
	}

	/**
	 * Order Constructor
	 * @param id_visitor - id visitor
	 */
	public Order(String id_visitor) {
		this.ownerId = Integer.parseInt(id_visitor);
	}

	/**
	 * order id getter
	 * 
	 * @return order id
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * order id setter
	 * @param orderId to set the order object  by
	 */
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	} 
	
	/**
	 * park name getter
	 * 
	 * @return the park name
	 */
	
	public String getParkName() {
		return parkName;
	}
	
	/**
	 * park name setter
	 * 
	 * @return the park name
	 */
	
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
	/**
	 * date getter
	 * 
	 * @return the order date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * date setter
	 * @param date of order
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * order time getter
	 * @return order time
	 */
	
	public String getTime() {
		return time;
	}
	
	/**
	 * order time setter
	 * @param time to set the order object
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * number of visitors getter
	 * @return the number of visitors in the order
	 */
	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}
	/**
	 * set the number of visitors
	 * @param numberOfVisitors to set the object by
	 */
	public void setNumberOfVisitors(int numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}
	/**
	 * 
	 * @return order email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @param email to set the object by
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 
	 * @return owner id
	 */
	public int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * 
	 * @param ownerId - set the ownerid of the object
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * 
	 * @return type of order (single/familiy/guide) , family is subscriber!
	 */
	public String getTypeOfOrder() {
		return typeOfOrder;
	}
	
	/**
	 * 
	 * @param typeOfOrder - set the type of order 
	 */
	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}
	
	/**
	 * 
	 * @return the order status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @param status - set the order status by a given status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 
	 * @return price full string
	 */

	public String getPaynow() {
		return paynow;
	}

	/**
	 * 
	 * @param set the price full string
	 */
	public void setPaynow(String paynow) {
		this.paynow = paynow;
	}
	
	/**
	 * 
	 * @return order full price , before discounts
	 */
	
	public String getFull_price() {
		return full_price;
	}
	
	/**
	 * 
	 * @param full_price - sets the full price
	 */

	public void setFull_price(String full_price) {
		this.full_price = full_price;
	}

	/**
	 * 
	 * @return the order discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * 
	 * @param discount - set the discount of the order
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	/**
	 * 
	 * @return order total price
	 */

	public String getTotal_price() {
		return total_price;
	}
	
	/**
	 * 
	 * @param total_price to set the order by
	 */

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((full_price == null) ? 0 : full_price.hashCode());
		result = prime * result + numberOfVisitors;
		result = prime * result + orderId;
		result = prime * result + ownerId;
		result = prime * result + ((parkName == null) ? 0 : parkName.hashCode());
		result = prime * result + ((paynow == null) ? 0 : paynow.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((total_price == null) ? 0 : total_price.hashCode());
		result = prime * result + ((typeOfOrder == null) ? 0 : typeOfOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (full_price == null) {
			if (other.full_price != null)
				return false;
		} else if (!full_price.equals(other.full_price))
			return false;
		if (numberOfVisitors != other.numberOfVisitors)
			return false;
		if (orderId != other.orderId)
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (parkName == null) {
			if (other.parkName != null)
				return false;
		} else if (!parkName.equals(other.parkName))
			return false;
		if (paynow == null) {
			if (other.paynow != null)
				return false;
		} else if (!paynow.equals(other.paynow))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (total_price == null) {
			if (other.total_price != null)
				return false;
		} else if (!total_price.equals(other.total_price))
			return false;
		if (typeOfOrder == null) {
			if (other.typeOfOrder != null)
				return false;
		} else if (!typeOfOrder.equals(other.typeOfOrder))
			return false;
		return true;
	}


}
