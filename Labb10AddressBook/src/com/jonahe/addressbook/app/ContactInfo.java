package com.jonahe.addressbook.app;

import java.io.Serializable;

public class ContactInfo implements Serializable{
	private String country;
	private String city;
	private String street;
	private String phoneNumber;
	
	
	public ContactInfo(String phoneNumber, String country, String city, String street) {
		setPhoneNumber(phoneNumber);
		this.country = country;
		setCity(city);
		setStreet(street);
		
	}
	

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		if("".equals(phoneNumber)) phoneNumber = "000-000 00 00";
		this.phoneNumber = phoneNumber;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if("".equals(city)) city = "Unknown";
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		if("".equals(street)) street = "Unknown";
		this.street = street;
	}

	
}
