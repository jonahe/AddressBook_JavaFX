package com.jonahe.addressbook.app;

public class Address {
	private String country;
	private String town;
	private String street;
	private int streetNumber;
	
	
	public Address(String country, String town, String street, int streetNumber) {
		super();
		this.country = country;
		this.town = town;
		this.street = street;
		this.streetNumber = streetNumber;
	}
	

	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	
}
