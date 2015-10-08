package com.jonahe.addressbook.app;

import helpermethods.io.ScannerHelper;

public class AddressBook {
	
	private AddressManager manager;
	private ScannerHelper scanHelp;
	
	public AddressBook(){
		manager = new AddressManager();
		scanHelp = new ScannerHelper();
	}
}
