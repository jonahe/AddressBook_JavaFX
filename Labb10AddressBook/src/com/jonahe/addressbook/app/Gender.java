package com.jonahe.addressbook.app;

import java.io.Serializable;

public enum Gender implements Serializable {
	MALE, FEMALE, OTHER;
	
	@Override
	public String toString(){
		return super.toString().toLowerCase();
	}
	
}
