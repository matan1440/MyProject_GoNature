package client.logic;

import java.io.Serializable;

/**
 * subscriber class, we will use sub object in the program
 * set and get method for sub values
 * @author tomer
 *
 */
public class Subscriber implements Serializable{
	
	private int subscriber_id;
	private String firstname;
	private String lastname;
	private String	email;
	private int subscriber_amount;
	private String type;
	
	
	public Subscriber(int subscriber_id, String firstname, String lastname, String email, int subscriber_amount,
			String type) {
		super();
		this.subscriber_id = subscriber_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.subscriber_amount = subscriber_amount;
		this.type = type;
	}
	
	public Subscriber(String firstname, String lastname, String email, int subscriber_amount,
			String type) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.subscriber_amount = subscriber_amount;
		this.type = type;
	}
	
	public Subscriber() {
		
	}

	
	public Subscriber(int subscriber_id) {
		this.subscriber_id = subscriber_id;
	}

	/**
	 * 
	 * @return subscriber id
	 */
	public int getSubscriber_id() {
		return subscriber_id;
	}

	/**
	 * set subscriber int id
	 * @param subscriber_id
	 */
	public void setSubscriber_id(int subscriber_id) {
		this.subscriber_id = subscriber_id;
	}

	/**
	 * 
	 * @return string first name
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * set sub name
	 * @param firstname - string 
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * 
	 * @return last name string
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * set last name
	 * @param lastname string
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * 
	 * @return string email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set sub email
	 * @param email - string email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return int amount of visitors related to subscriber
	 */
	public int getSubscriber_amount() {
		return subscriber_amount;
	}

	/**
	 * set amount
	 * @param subscriber_amount int amount of visitors related to sub
	 */
	public void setSubscriber_amount(int subscriber_amount) {
		this.subscriber_amount = subscriber_amount;
	}

	/**
	 * 
	 * @return string type (family, guide)
	 */
	public String getType() {
		return type;
	}

	/**
	 * set type
	 * @param type string type of sub
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + subscriber_amount;
		result = prime * result + subscriber_id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Subscriber other = (Subscriber) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (subscriber_amount != other.subscriber_amount)
			return false;
		if (subscriber_id != other.subscriber_id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
		
}
