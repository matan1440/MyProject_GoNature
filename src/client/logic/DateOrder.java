package client.logic;

import java.io.Serializable;

/**
 * DateOrder class - class for open dates table objects
 * @author Omer
 *
 */
public class DateOrder implements Serializable {
	public String date;

	/**
	 * DateOrder Constructor
	 * @param date - date and time
	 */
	public DateOrder(String date) {
		super();
		this.date = date;
	}
	/**
	 *
	 * @return date of object
	 */
	public String getDate() {
		return date;
	}

	/**
	 * set the date of the object
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
}
