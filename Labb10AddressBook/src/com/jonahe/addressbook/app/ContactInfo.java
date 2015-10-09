package com.jonahe.addressbook.app;

public class ContactInfo {
	private String country;
	private String city;
	private String street;
	private String phoneNumber;
	
	
	public ContactInfo(String phoneNumber, String country, String city, String street) {
		this.phoneNumber = phoneNumber;
		this.country = country;
		this.city = city;
		this.street = street;
		
	}
	

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
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
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	
}
