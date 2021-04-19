package client.logic;

import java.io.Serializable;

/**
 * 
 * worker class, we will use worker objects in the program defines worker with
 * its values has get and set method to return worker values
 * 
 * @author tomer
 *
 */
public class Worker implements Serializable {
	private String firstName;
	private String lastName;
	private String job;
	private String email;
	private String ID;
	private String password;
	private int phone;
	private String parkName;

	public Worker(String firstName, String lastName, String job, String email, String iD, String password, int phone,
			String parkName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.job = job;
		this.email = email;
		this.ID = iD;
		this.password = password;
		this.phone = phone;
		this.parkName = parkName;
	}

	public Worker() {
		super();
	}

	public Worker(String id, String password) {
		super();
		this.ID = id;
		this.password = password;
	}

	/**
	 * 
	 * @return first name string
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * set first name
	 * @param firstName - string first name of worker
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    /**
     * 
     * @return last name string
     */
	public String getLastName() {
		return lastName;
	}
    /**
     * set last name
     * @param lastName string last name of worker
     */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 
	 * @return string type of worker ("service", "entrance", etc)
	 */
	public String getJob() {
		return job;
	}

	/**
	 * set type of worker
	 * @param job - string type of job of worker
	 */
	public void setJob(String job) {
		this.job = job;
	}

	/**
	 * 
	 * @return - string email of worker
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set email value of worker
	 * @param email - string
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return string worker id
	 */
	public String getID() {
		return ID;
	}

	/**
	 * set worker id
	 * @param iD - string worker id
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * 
	 * @return string worker password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set worker password
	 * @param password - string password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return int phone number
	 */
	public int getPhone() {
		return phone;
	}

	/**
	 * set phone number
	 * @param phone int phone
	 */
	public void setPhone(int phone) {
		this.phone = phone;
	}

	/**
	 * 
	 * @return string park name the worker belongs to
	 */
	public String getParkName() {
		return parkName;
	}

	/**
	 * set park worker belongs to
	 * @param parkName - string park name
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((parkName == null) ? 0 : parkName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + phone;
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
		Worker other = (Worker) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (parkName == null) {
			if (other.parkName != null)
				return false;
		} else if (!parkName.equals(other.parkName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone != other.phone)
			return false;
		return true;
	}

}
