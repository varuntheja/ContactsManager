package com.wowlabz.contactmanager;

public class ContactsHelper {

	String contactNumber,contactName,contactEmail;
	int id;
	
	public ContactsHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public ContactsHelper(int id, String contactNumber, String contactName, String contactemail) {
		super();
		this.contactNumber = contactNumber;
		this.contactName = contactName;
		this.contactEmail = contactemail;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

}
