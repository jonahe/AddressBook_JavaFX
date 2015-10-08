package com.jonahe.addressbook.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class ControllerAddressBook implements Initializable{
	/*
	 * Use manager with "current entry". When a entry is selected in the listview, make that the current entry
	 * then another method (updateView) can get the relevant info from current entry and fill in the
	 * info in the view
	 * 
	 * 
	 * 
	 */
	
	// regular variables
	private ArrayList<String> countries;
	private Gender[] genders = Gender.values();
	
	
	// FXML variables
	@FXML
	ComboBox<String> comboBox_Country;
	@FXML
	ComboBox<String> comboBox_Gender;
	
	
	@FXML 
	private void onEdit(ActionEvent event){
		System.out.println();
	}
	
	@FXML
	private void onTESTEETSST(){
		System.out.println("Testing!");
	}
	
	private void generateCountryList(){
		countries = new ArrayList<String>();
		String[] countryCodes = Locale.getISOCountries();
		for(String countryCode : countryCodes){
			Locale countryLocale = new Locale("", countryCode);
			countries.add(countryLocale.getDisplayCountry(Locale.ENGLISH));
		}
		
		countries.sort((String s1,String s2) -> s1.compareTo(s2));
		// countries.forEach(System.out::println);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generateCountryList();
		populateCountryComboBox();
		populateGenderComboBox();
		
	}

	private void populateGenderComboBox() {
		ObservableList<String> genderComboOptions = comboBox_Gender.getItems();
		for(Gender gender : genders){
			genderComboOptions.add(gender.toString());
		}
		
	}

	private void populateCountryComboBox() {
		// populate, get current locale, get language, search for it, make that index the selected index.
		//  ->   Sweden (or what ever country the JVM detected) will be preselected 
		
		// Populate
		comboBox_Country.getItems().addAll(countries);
		
		// preselect a country
		//TODO: the preselected value should be different if we are working with an entry
		Locale currentLocale = Locale.getDefault();
		String currentCountry = currentLocale.getDisplayCountry(Locale.ENGLISH);
		System.out.println(currentCountry);
		int currentCountryIndex = comboBox_Country.getItems().indexOf(currentCountry);
		System.out.println(currentCountryIndex);
		comboBox_Country.getSelectionModel().select(currentCountryIndex);
	}

}
