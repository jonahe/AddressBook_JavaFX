package com.jonahe.addressbook.app;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class Person {
	
	private long id;
	
	private String firstName;
	private String lastName;
	private Gender gender;
	private LocalDate birthDate;

	public Person(	String firstName, 
					String lastName,
					Gender gender,
					LocalDate birthDate 
					) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		id = generateUniqueID();
		System.out.println(id);
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
	
	public long getId(){
		return id;
	}

	
	public String getFullName(){
		return getFirstName() + " " + getLastName();
	}
	
	// ... not 100% guaranteed to be unique  - exercise in not using static counters..
	private long generateUniqueID(){
		// 
		long startingPoint = birthDate.getLong(ChronoField.EPOCH_DAY);
		long x  = startingPoint + (lastName.length() +1) * (firstName.length() + 1);
		x *= (gender.ordinal() + 1);
		
		return Math.abs(Integer.MAX_VALUE / x);
	}
	
	
}
