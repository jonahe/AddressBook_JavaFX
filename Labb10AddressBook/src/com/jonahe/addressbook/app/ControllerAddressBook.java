package com.jonahe.addressbook.app;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

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
	
	private AddressManager manager;
	
	
	// FXML variables
	@FXML
	ComboBox<String> comboBox_Country;
	@FXML
	ComboBox<String> comboBox_Gender;
	@FXML
	Accordion accordionMain;
	@FXML
	TextField txtFldSearchContacts;
	@FXML
	ListView<AddressBookEntry> listViewContacts;
	
	// FXML variables for FORM
	@FXML
	TextField txtFldFirstName;
	@FXML
	TextField txtFldLastName;
	@FXML
	TextField txtFldPhoneNum;
	@FXML
	TextField txtFldStreetName;
	@FXML
	TextField txtFldCity;
	@FXML
	Button btnSaveContact;
	@FXML
	DatePicker datePicker;
	
	
	@FXML 
	private void onEdit(ActionEvent event){
		System.out.println();
	}
	
	@FXML
	private void onTESTEETSST(){
		System.out.println("Testing!");
		LocalDate pickedTime = datePicker.getValue();
		System.out.println(pickedTime);
		datePicker.setValue(LocalDate.of(1980, 01, 01));
		
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
		preselectContactsView();
		setSearchChangeListener();
		
		manager = new AddressManager();
		createTestPersons();
		populatePrimaryListView();

		
	}


	private void setSearchChangeListener() {
		txtFldSearchContacts.textProperty().addListener( (textProperty, oldValue, newValue) -> {
			
			if(! "".equals(newValue)){ // if user has entered a string to search for
				// create Predicate that matches those entries that contain the searchField value
				Predicate<AddressBookEntry> predicateToFilterBy =  entry -> entry.toString().toLowerCase().contains(newValue.toLowerCase());
				// use that predicate to filter out matching entries, and populate the list with them.
				List<AddressBookEntry> matchingEntries = manager.getAllMatching(predicateToFilterBy);
				populatePrimaryListView(matchingEntries);
			} else {
				// no search string  -> show all
				populatePrimaryListView();
			}
		});
	}

	private void createTestPersons() {
		Person kurt = new Person("Kurt", "Åkesson", Gender.MALE, LocalDate.of(1987, 6, 19));
		ContactInfo erInfo = new ContactInfo("0700123456", "Sweden", "Gothenburg", "Tredje långgatan 11");
		manager.createEntry(kurt, erInfo);
		
		Person emma = new Person("Emma", "Karlsson", Gender.FEMALE, LocalDate.of(1986, 2, 15));
		ContactInfo emInfo = new ContactInfo("0710123456", "Denmark", "Copenhagen", "Oahaehegehe 12");
		manager.createEntry(emma, emInfo);
		
		Person filip = new Person("Filip", "Gustavsson", Gender.MALE, LocalDate.of(1975, 8, 4));
		ContactInfo fiInfo = new ContactInfo("0720123456", "Sweden", "Gothenburg", "Tredje långgatan 13");
		manager.createEntry(filip, fiInfo);
		
		Person kim = new Person("Kim", "Robinsson", Gender.OTHER, LocalDate.of(1989, 1, 30));
		ContactInfo kiInfo = new ContactInfo("0730123456", "Sweden", "Gothenburg", "Tredje långgatan 14");
		manager.createEntry(kim, kiInfo);
	}

	private void populatePrimaryListView() {
		List<AddressBookEntry> allEntries = manager.getAll();
		listViewContacts.getItems().clear();
		listViewContacts.getItems().addAll(allEntries);
		
	}
	
	private void populatePrimaryListView(List<AddressBookEntry> entries) {
		listViewContacts.getItems().clear();
		listViewContacts.getItems().addAll(entries);
		
	}
	
	private void preselectContactsView() {
		//preselect Contact view in accordion
		System.out.println(accordionMain);
		TitledPane contactPane = accordionMain.getPanes().get(0);
		accordionMain.setExpandedPane(contactPane);
		
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
