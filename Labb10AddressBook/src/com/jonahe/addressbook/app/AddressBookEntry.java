package com.jonahe.addressbook.app;

import java.util.List;

public class AddressBookEntry {

	private Person person;
	private Address address;
	private List<Person> contacts;
	
	public AddressBookEntry(Person person, Address address){
		
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
