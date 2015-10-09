package com.jonahe.addressbook.app;

import java.util.ArrayList;
import java.util.List;

public class AddressBookEntry {

	private Person person;
	private ContactInfo contactInfo;
	private List<Person> contacts;
	
	public AddressBookEntry(Person person, ContactInfo contactInfo){
		this.person = person;
		this.contactInfo = contactInfo;
		this.contacts = new ArrayList<Person>();
	}
	
	public List<Person> getContacts() {
		return contacts;
	}

	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
	}

	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public ContactInfo getAddress() {
		return contactInfo;
	}
	public void setAddress(ContactInfo address) {
		this.contactInfo = address;
	}
	
	
	@Override
	public String toString(){
		// fullname 	 city, street
		String format = "%s - %s, %s";
		return String.format(format, person.getFullName(), contactInfo.getCity(), contactInfo.getStreet());
	}
	
}
