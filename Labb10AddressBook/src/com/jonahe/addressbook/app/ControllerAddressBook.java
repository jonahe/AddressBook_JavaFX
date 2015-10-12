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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ControllerAddressBook implements Initializable{
	
	// regular variables
	private ArrayList<String> countries;
	private Gender[] genders = Gender.values();
	private boolean inEditOrCreationMode;
	private AddressBookEntry entryBeingEdited;
	
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
	@FXML
	Label labelNumberOfResults;
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
	ListView<AddressBookEntry> listViewConnections;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generateCountryList();
		populateCountryComboBox();
		populateGenderComboBox();
		preselectContactsView();
		setSearchChangeListener();

		manager = new AddressManager();
		initializeEntryJavaFXMediator();
		
		createTestPersons();
		addListViewContactsListener();
		populatePrimaryListView();
		
		bindSearchFieldProperty();
		
		setBirthdayPaneExpandedChangeListener();
		
		setupContextMenu();
		
		listViewConnections.setTooltip(new Tooltip("Right-click on entry in (left) contact list to add/remove"));
		
		
		
		
		
		
	}


	private void addListViewContactsListener() {
		listViewContacts.itemsProperty().get().addListener( new ListChangeListener<AddressBookEntry>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends AddressBookEntry> c) {
				System.out.println("Change detected in list!");
				int size  = listViewContacts.getItems().size();
				String contactOrContacts  = size == 1 ? "contact" : "contacts";
				labelNumberOfResults.setText("Listing " + size + " " + contactOrContacts);
			}
		});
	}
	
	
	private void setupContextMenu() {
		

		ContextMenu cMenu = new ContextMenu();

		MenuItem mItem1 = new MenuItem("Add to connections");
		MenuItem mItem2 = new MenuItem("Remove from connections");
		// define onAction
		mItem1.setOnAction( e -> onAddPersonToConnections());
		mItem2.setOnAction( e -> onRemovePersonFromConnections());
		// add items to menu
		cMenu.getItems().addAll(mItem1, mItem2);
		// add menu to list
		listViewContacts.setContextMenu(cMenu);
		
		// hide menu if in wrong "mode". 
		cMenu.focusedProperty().addListener(change -> {
			System.out.println("Context menu focused");
			if(!inEditOrCreationMode){
				cMenu.getItems().forEach(item -> item.setVisible(false));
			} else {
				if(listViewConnections.isFocused()){
					cMenu.getItems().get(0).setVisible(false);
					cMenu.getItems().get(1).setVisible(true);
				} else {
					cMenu.getItems().forEach(item -> item.setVisible(true));					
				}
				
				
			}
		});
		
		listViewConnections.setContextMenu(cMenu);
		listViewConnections.isFocused();
		
		
	}


	@FXML
	private void onSelectEntry(MouseEvent event){
		System.out.println("onSelect with args");
		if(event.getButton() == MouseButton.PRIMARY){
			
			inEditOrCreationMode = false;
			
			System.out.println("primary mouse btn");
			if(listViewContacts.getSelectionModel().getSelectedIndex() != -1){
				
				AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
				formHelper.viewEntry(selectedEntry);
			} // else nothing selected -> do nothing			
		} // else we are probably dealing with a secondary mouse button, then contet menu will be displayed
		
	}
	
	
	// no args version..
	private void onSelectEntry(){
		
		inEditOrCreationMode = false;
		
		System.out.println("onSelect with no args");
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
	

	@FXML 
	private void onEdit(ActionEvent event){
		
		inEditOrCreationMode = true;
		
		System.out.println("Edit..");
		if(listViewContacts.getSelectionModel().getSelectedIndex() != -1){
			AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
			// TODO: stream-line..
			// set "current entry" - selected entry is not enough, since an entry might be selected
			// to be added to contact connections 
			entryBeingEdited = selectedEntry; 
			formHelper.editEntrySetup(selectedEntry);
		} // else nothing selected -> do nothing
	}
	
	/**
	 * prepare for entry creation - NOTE: will NOT CREATE new. - see onSave
	 */
	@FXML
	private void onCreateNew(){
		System.out.println("Creation mode");
		inEditOrCreationMode = true;
		
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
				// AddressBookEntry entryToUpdate = listViewContacts.getSelectionModel().getSelectedItem();
				AddressBookEntry entryToUpdate = entryBeingEdited;
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
	
//	@FXML
//	private void onTESTEETSST(){
//		System.out.println("Testing!");
//		LocalDate pickedTime = datePicker.getValue();
//		System.out.println("Picked time: " + pickedTime);
//		datePicker.setValue(LocalDate.of(1980, 01, 01));
//		
//	}
	
	
	private void onAddPersonToConnections(){
		System.out.println("add to connections");
		/*
		 * check if we're in edit/creation mode.
		 * check if an entry in contacts is selected
		 * add person id to list of connections (in the entry we're editing)
		 * force an update of that listview so that the added person shows up
		 * 
		 */
		
		if(inEditOrCreationMode){
			System.out.println("in edit or creation mode!");
			AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
			if(selectedEntry != null){
				// don't add duplicates
				if(! listViewConnections.getItems().contains(selectedEntry)){					
					listViewConnections.getItems().add(selectedEntry);
				}
				
			}
			
			
			
		} else {
			System.out.println("NOT in edit of creation mode");
		}

		
	}
	
	private void onRemovePersonFromConnections(){
		System.out.println("Remove from connections");
		
		if(inEditOrCreationMode){
			System.out.println("in edit or creation mode!");
			
			// a selection can be in the contacts list, or in the connections list
			if(listViewConnections.isFocused()){
				AddressBookEntry selectedE = listViewConnections.getSelectionModel().getSelectedItem();
				if(selectedE != null){
					listViewConnections.getItems().remove(selectedE);
				}					
			} else if (listViewContacts.isFocused()){
				AddressBookEntry selectedEntry = listViewContacts.getSelectionModel().getSelectedItem();
				if(selectedEntry != null){
					listViewConnections.getItems().remove(selectedEntry);
				}
			} else {
				// nothing
			}
			
			
			
		} else {
			System.out.println("NOT in edit of creation mode");
		}
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

	private void forceSearchUpdate() {
		String searchQuery = txtFldSearchContacts.getText();
		txtFldSearchContacts.setText("");
		txtFldSearchContacts.setText(searchQuery);
		
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
												btnSaveContact,
												listViewConnections,
												manager
				);
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
		manager.addEntries(AddressBookEntry.createRandomEntries(100));
		
		
		
		// add Apan as connection to everyone
		
		Person apan = new Person("Apan", "Apansson", Gender.MALE, LocalDate.of(0, 1, 1));
		ContactInfo apInfo = new ContactInfo("0700123456", "Sweden", "Gothenburg", "Tredje långgatan 11");
		
		Long apanID = apan.getId();
		manager.getEntries().forEach( entry -> {
			entry.addConnectionID(apanID);
		});
		
		// add apan..
		manager.createEntry(apan, apInfo);
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
