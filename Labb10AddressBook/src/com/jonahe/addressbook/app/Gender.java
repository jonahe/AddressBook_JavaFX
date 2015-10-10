package com.jonahe.addressbook.app;

public enum Gender {
	MALE, FEMALE, OTHER;
	
	@Override
	public String toString(){
		return super.toString().toLowerCase();
	}
	
}
