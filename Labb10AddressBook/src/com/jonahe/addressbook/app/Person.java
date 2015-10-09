package com.jonahe.addressbook.app;

import java.time.LocalDate;

public class Person {
	private String firstName;
	private String lastName;
	private Gender gender;
	private LocalDate birthDate;

	public Person(	String firstName, 
					String lastName, 
					LocalDate birthDate 
					) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}
	
	
	
	public Gender getGender() {
		return gender;
	}
	
	
	public void setGender(Gender gender) {
		this.gender = gender;
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


	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	
	public String getFullName(){
		return getFirstName() + " " + getLastName();
	}
	
	
}
