package com.jonahe.addressbook.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Creates objects from JavaFX fields and more.
 * Making the transition between JavaFX objects and AddressBookEntry easier (both ways)
 *
 */
public class EntryJavaFXMediator {
	
	// all relevant fields from the contact info "form"
	// FXML variables for FORM

	// Person
	private TextField txtFldFirstName;
	private TextField txtFldLastName;
	private DatePicker datePicker;	
	private ComboBox<String> comboBox_Gender;
	// ContactInfo
	private TextField txtFldPhoneNum;
	private TextField txtFldStreetName;
	private TextField txtFldCity;
	private ComboBox<String> comboBox_Country;
	
	private Button btnSaveContact;
	
	boolean firstTimeStyling = true;
	List<TextField> textFields;
	
	
	public EntryJavaFXMediator(	TextField txtFldFirstName, 
								TextField txtFldLastName, 
								DatePicker datePicker,
								ComboBox<String> comboBox_Gender, 
								TextField txtFldPhoneNum, 
								TextField txtFldStreetName,
								TextField txtFldCity, 
								ComboBox<String> comboBox_Country,
								Button btnSaveContact) {
		
		
		this.txtFldFirstName = txtFldFirstName;
		this.txtFldLastName = txtFldLastName;
		this.datePicker = datePicker;
		this.comboBox_Gender = comboBox_Gender;
		this.txtFldPhoneNum = txtFldPhoneNum;
		this.txtFldStreetName = txtFldStreetName;
		this.txtFldCity = txtFldCity;
		this.comboBox_Country = comboBox_Country;
		this.btnSaveContact = btnSaveContact;
		
		textFields = new ArrayList<TextField>();
		Collections.addAll(textFields, txtFldFirstName, txtFldLastName, txtFldPhoneNum, txtFldStreetName, txtFldCity);
		
	}
	
	/**
	 * Fill the "form" fields with the right info from selected entry
	 * @param entry
	 */
	public void viewEntry(AddressBookEntry entry){
		setTextFieldEditability(false);
		btnSaveContact.setVisible(false);
		
		
		Person person = entry.getPerson();
		
		txtFldFirstName.setText(person.getFirstName());
		txtFldLastName.setText(person.getLastName());
		datePicker.setValue(person.getBirthDate());
		comboBox_Gender.setValue(person.getGender().toString());
		
		ContactInfo cInfo = entry.getContactInfo();
		
		txtFldPhoneNum.setText(cInfo.getPhoneNumber());
		txtFldStreetName.setText(cInfo.getStreet());
		txtFldCity.setText(cInfo.getCity());
		comboBox_Country.setValue(cInfo.getCountry());

	}
	
	public void editEntrySetup(AddressBookEntry entry){
		viewEntry(entry);
		setTextFieldEditability(true);
		btnSaveContact.setVisible(true);
		btnSaveContact.setText("Save changes");
	}
	
	public void clearFieldsForCreation(){
		clearFields();
		setTextFieldEditability(true);
		btnSaveContact.setVisible(true);
		btnSaveContact.setText("Save new entry");
		// set focus
		txtFldFirstName.requestFocus();
	}
	
	public AddressBookEntry createEntryFromFields(){
		//TODO: fix validation that throws custom Exception with info to create a message
		// ...  new Exc.. super("phone number can't contain letters")
		// current method would throw this exception to be caught in the Controller
		// alternatively / complementary..  an unsuccessful validation could change that field color to red
		Person person = new Person(	txtFldFirstName.getText(), 
									txtFldLastName.getText(), 
									Gender.valueOf(comboBox_Gender.getValue().toUpperCase()),
									datePicker.getValue()
									);
		
		ContactInfo cInfo = new ContactInfo(	txtFldPhoneNum.getText(), 
												comboBox_Country.getValue(), 
												txtFldCity.getText(), 
												txtFldStreetName.getText()
												);
		
		return new AddressBookEntry(person, cInfo);
	}
	
	public void updateEntry(AddressBookEntry entryToUpdate){
		AddressBookEntry tempEntry = createEntryFromFields();
		Person tempPerson = tempEntry.getPerson();
		ContactInfo tempContactInfo = tempEntry.getContactInfo();
		
		// replace from fields from temp
		Person person = entryToUpdate.getPerson();
		person.setFirstName(tempPerson.getFirstName());
		person.setLastName(tempPerson.getLastName());
		person.setGender(tempPerson.getGender());
		person.setBirthDate(tempPerson.getBirthDate());
		
		ContactInfo cInfo = entryToUpdate.getContactInfo();
		cInfo.setPhoneNumber(tempContactInfo.getPhoneNumber());
		cInfo.setStreet(tempContactInfo.getStreet());
		cInfo.setCity(tempContactInfo.getCity());
		cInfo.setCountry(tempContactInfo.getCountry());
		
		// the field should have all its part updated (excluding the contact connections field..)
		
	}
	
	
	public void clearFields(){
		txtFldFirstName.setText("");
		txtFldLastName.setText("");
		datePicker.setValue(LocalDate.of(1980, 1, 1));
		comboBox_Gender.setValue(Gender.OTHER.toString());
		
		txtFldPhoneNum.setText("");
		txtFldStreetName.setText("");
		txtFldCity.setText("");
		comboBox_Country.setValue("Sweden");
		
		btnSaveContact.setVisible(false);
		
	}
	
	/**
	 * Changes the editability of the fields, as well as adds a styleClass so that "locked" fields look locked
	 * 
	 * @param editable
	 */
	private void setTextFieldEditability(boolean editable){
		// loop
		textFields.forEach( txtFld -> {
			
			txtFld.setEditable(editable);
			String styleClass = editable ? "no-rules-apply-to-me" : "txtFldViewMode";
			
			if(! firstTimeStyling){
				txtFld.getStyleClass().remove(txtFld.getStyleClass().size()-1);
			} else {
				// nothing
				firstTimeStyling = false;
			}
			txtFld.getStyleClass().add(styleClass); // see rule for .txtFldViewMode  in file  uiAddressBook.css
			
			
		});

	}


}
