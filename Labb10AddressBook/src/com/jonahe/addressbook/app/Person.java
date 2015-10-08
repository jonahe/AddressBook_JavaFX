package com.jonahe.addressbook.app;

import java.util.Date;
import java.util.List;

public class Person {
	private String firstName;
	private String lastName;
	private Date birthDate;
	private List<Person> contacts;
	private Address address;
	
	
	public Person(	String firstName, 
					String lastName, 
					Date birthDate, 
					List<Person> contacts, 
					Address address
					) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.contacts = contacts;
		this.address = address;
	}
	
	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setAddress(Address address) {
		this.address = address;
	}
	public Address getAddress() {
		return address;
	}

	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public List<Person> getContacts() {
		return contacts;
	}
	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
	}
	
	public String getFullName(){
		return getFirstName() + " " + getLastName();
	}
	
	
}
