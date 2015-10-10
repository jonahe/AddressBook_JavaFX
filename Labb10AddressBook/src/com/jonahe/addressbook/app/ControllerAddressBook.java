package com.jonahe.addressbook.app;

import java.net.URL;
import java.time.LocalDate;


import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.binding.BooleanBinding;
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
	private EntryJavaFXMediator formHelper;
	
	// FX property values  - bound to..
	private BooleanBinding searchFieldEmpty;
	
	
	// FXML variables
	@FXML
	Accordion accordionMain;
	@FXML
	TextField txtFldSearchContacts;
	@FXML
	ListView<AddressBookEntry> listViewContacts;
	// main buttons
	@FXML
	Button btnEdit;
	@FXML
	Button btnDelete;
	@FXML
	Button btnCreate;
	
	// birthdays..
	@FXML
	TitledPane titledPaneBirthdays;
	@FXML
	ListView<String> listViewBirthdays;
	
	
	// FXML variables for FORM
	
	// Person
	@FXML
	TextField txtFldFirstName;
	@FXML
	TextField txtFldLastName;
	@FXML
	DatePicker datePicker;	
	@FXML
	ComboBox<String> comboBox_Gender;
	// ContactInfo
	@FXML
	TextField txtFldPhoneNum;
	@FXML
	TextField txtFldStreetName;
	@FXML
	TextField txtFldCity;
	@FXML
	ComboBox<String> comboBox_Country;
	@FXML
	Button btnSaveContact;
	
	
	
	@FXML
	private void onSelectEntry(){
		if(listViewContacts.getSelectionModel().getSelectedIndex() != -1){
			AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
			formHelper.viewEntry(selectedEntry);
		} // else nothing selected -> do nothing
	}
	
	@FXML
	private void onDelete(){
		System.out.println("Deleting entry..");
		// if we have a selected entry
		if(listViewContacts.getSelectionModel().getSelectedIndex() != -1){
			// remove the selected entry from manager, then populate list from manager
			manager.removeEntry(listViewContacts.getSelectionModel().getSelectedItem());
			populatePrimaryListView();
			// if the searchbox had something in it, make the search again
			if(! searchFieldEmpty.get()){
				// trigger the change listener
				forceSearchUpdate();
			}
			// nothing selected now, so clear fields
			formHelper.clearFields();
			
		} // else nothing
	}
	
	private void forceSearchUpdate() {
		String searchQuery = txtFldSearchContacts.getText();
		txtFldSearchContacts.setText("");
		txtFldSearchContacts.setText(searchQuery);
		
	}

	@FXML 
	private void onEdit(ActionEvent event){
		System.out.println("Edit..");
		if(listViewContacts.getSelectionModel().getSelectedIndex() != -1){
			AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
			formHelper.editEntrySetup(selectedEntry);
		} // else nothing selected -> do nothing
	}
	
	/**
	 * prepare for entry creation - NOTE: will NOT CREATE new. - see onSave
	 */
	@FXML
	private void onCreateNew(){
		formHelper.clearFieldsForCreation();
	}
	
	@FXML
	private void onSave(ActionEvent event){
		Button clickedBtn = (Button) event.getSource();
		// see if we're saving a new entry or just saving changes to old one.
		String btnText = clickedBtn.getText();
		if(btnText.equals("Save new entry")){
			try{
				System.out.println("Trying to create new entry");
				AddressBookEntry entry = formHelper.createEntryFromFields();
				manager.addEntry(entry);
				sortEntries();
				populatePrimaryListView();
				formHelper.clearFields();
				
			} catch (Exception e){
				//TODO: handle validation errors
				e.printStackTrace();
			}
		} else if (btnText.equals("Save changes")) {
			try{
				//TODO: update info
				AddressBookEntry entryToUpdate = listViewContacts.getSelectionModel().getSelectedItem();
				if(entryToUpdate != null){
					formHelper.updateEntry(entryToUpdate);
					// return to viewing whole list  
					sortEntries(); // order might have to change
					populatePrimaryListView(); // refresh listview
					listViewContacts.getSelectionModel().select(entryToUpdate); // reselect the entry
					onSelectEntry(); // to trigger so that field is no longer editable etc.
					txtFldSearchContacts.setText(""); // clear
				} else {
					System.out.println("Can't save, because no selected entry detected");
				}
				System.out.println("About to save changes");
				
			} catch(Exception e) {
				//TODO: handle validation errors
				e.printStackTrace();
			}
		} else {
			System.out.println("Button not recognized");
		}
	}
	
	@FXML
	private void onTESTEETSST(){
		System.out.println("Testing!");
		LocalDate pickedTime = datePicker.getValue();
		System.out.println("Picked time: " + pickedTime);
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
		initializeEntryJavaFXMediator();
		
		manager = new AddressManager();
		createTestPersons();
		populatePrimaryListView();
		
		bindSearchFieldProperty();
		// populateUpcommingBirthdaysList();
		
		setBirthdayPaneExpandedChangeListener();
		
	}


	private void setBirthdayPaneExpandedChangeListener() {
		titledPaneBirthdays.expandedProperty().addListener( (changed, oldValue, newValue) -> {
			if(newValue.booleanValue()){
				populateUpcommingBirthdaysList();
			}
		});
		
	}


	private void bindSearchFieldProperty() {
		searchFieldEmpty = txtFldSearchContacts.textProperty().isEmpty();
		
	}

	private void initializeEntryJavaFXMediator() {
		formHelper = new EntryJavaFXMediator( 	txtFldFirstName, 
												txtFldLastName, 
												datePicker, 
												comboBox_Gender, 
												txtFldPhoneNum, 
												txtFldStreetName, 
												txtFldCity, 
												comboBox_Country,
												btnSaveContact);
	}

	private void setSearchChangeListener() {
		txtFldSearchContacts.textProperty().addListener( (textProperty, oldValue, newValue) -> {
			
			// if(! "".equals(newValue)){
			
			if(! searchFieldEmpty.get()){ // if user has entered a string to search for
				// create Predicate that matches those entries that contain the searchField value
				Predicate<AddressBookEntry> predicateToFilterBy =  entry -> entry.toString().toLowerCase().contains(newValue.toLowerCase());
				// use that predicate to filter out matching entries, and populate the list with them.
				List<AddressBookEntry> matchingEntries = manager.getAllEntriesMatching(predicateToFilterBy);
				populatePrimaryListView(matchingEntries);
			} else {
				// no search string  -> show all
				populatePrimaryListView();
			}
		});
	}

	private void createTestPersons() {
		Person kurt = new Person("Kurt", "Åkesson", Gender.MALE, LocalDate.of(1982, 11, 24));
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
		
		// create random entries..
		manager.addEntries(AddressBookEntry.createRandomEntries(500));
		sortEntries();
	}

	private void sortEntries() {
		Comparator<AddressBookEntry> byAlphabeticAsc = (e1, e2) -> e1.toString().compareTo(e2.toString());
		Collections.sort(manager.getEntries(), byAlphabeticAsc);
		
	}

	private void populatePrimaryListView() {
		List<AddressBookEntry> allEntries = manager.getEntries();
		listViewContacts.getItems().clear();
		listViewContacts.getItems().addAll(allEntries);
		
	}
	
	private void populatePrimaryListView(List<AddressBookEntry> entries) {
		listViewContacts.getItems().clear();
		listViewContacts.getItems().addAll(entries);
		
	}
	
	private void preselectContactsView() {
		//preselect Contact view in accordion
		System.out.println("Accordion main" + accordionMain);
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
		System.out.println("Current country: " + currentCountry);
		int currentCountryIndex = comboBox_Country.getItems().indexOf(currentCountry);
		System.out.println("Current country index: " +currentCountryIndex);
		comboBox_Country.getSelectionModel().select(currentCountryIndex);
	}
	
	private void populateUpcommingBirthdaysList(){
		
		List<AddressBookEntry> matchingEntries = getAllEntriesWithBirthDaysWithinAWeek();
		
		List<String> personsWithBirthDaySoon = new ArrayList<String>();
		
		for(AddressBookEntry entry : matchingEntries){
			LocalDate birthDate = entry.getPerson().getBirthDate();
			LocalDate currentDate = LocalDate.now();
			
			int ageToCelebrate = currentDate.getYear() - birthDate.plusDays(7).getYear();
			LocalDate birthDayDate = birthDate.plusYears(ageToCelebrate);
			long daysLeftToBDay = ChronoUnit.DAYS.between(currentDate, birthDayDate);
			
			// build the info string..
			String daysLeftFormat = daysLeftToBDay != 0 ? "in %d days" : "in %d days (TODAY)";
			String bdayInfo = 
					  entry.getPerson().getFullName() 
					+ ": " + ageToCelebrate 
					+ " years, " 
					+ String.format(daysLeftFormat, daysLeftToBDay);
			System.out.println(bdayInfo);
			personsWithBirthDaySoon.add(bdayInfo);
		}
		
		listViewBirthdays.getItems().clear();
		listViewBirthdays.getItems().addAll(personsWithBirthDaySoon);
		
		
	}
	
	
	private List<AddressBookEntry> getAllEntriesWithBirthDaysWithinAWeek(){
		LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
		LocalDate cutoff = yesterday.plus(9, ChronoUnit.DAYS); // one day added to each "side" to fit methods below
		// generate the predicate to filter out the right entries
		Predicate<AddressBookEntry> birthdayWithinAWeek = entry -> {
			LocalDate birthDate = entry.getPerson().getBirthDate();
			// we want to ignore year for now..
			int yearsToAdd = LocalDate.now().getYear() - birthDate.getYear();
			LocalDate thisYearsBirthDayDate = birthDate.plus(yearsToAdd, ChronoUnit.YEARS);
			// is it within the span? return answer
			return (thisYearsBirthDayDate.isAfter(yesterday) && thisYearsBirthDayDate.isBefore(cutoff));
		};
		
		return manager.getAllEntriesMatching(birthdayWithinAWeek);
	}
	
	
	
	

}
