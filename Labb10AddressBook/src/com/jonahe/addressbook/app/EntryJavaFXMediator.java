package com.jonahe.addressbook.app;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * 
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
	
	
	public EntryJavaFXMediator(	TextField txtFldFirstName, 
								TextField txtFldLastName, 
								DatePicker datePicker,
								ComboBox<String> comboBox_Gender, 
								TextField txtFldPhoneNum, 
								TextField txtFldStreetName,
								TextField txtFldCity, 
								ComboBox<String> comboBox_Country) {
		
		
		this.txtFldFirstName = txtFldFirstName;
		this.txtFldLastName = txtFldLastName;
		this.datePicker = datePicker;
		this.comboBox_Gender = comboBox_Gender;
		this.txtFldPhoneNum = txtFldPhoneNum;
		this.txtFldStreetName = txtFldStreetName;
		this.txtFldCity = txtFldCity;
		this.comboBox_Country = comboBox_Country;
	}
	
	/**
	 * Fill the "form" fields with the right info from selected entry
	 * @param entry
	 */
	public void viewEntry(AddressBookEntry entry){
		setTextFieldEditability(false);
		
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
	
	public void editEntry(AddressBookEntry entry){
		viewEntry(entry);
		setTextFieldEditability(true);
	}
	
	private void setTextFieldEditability(boolean editable){
		txtFldFirstName.setEditable(editable);
		txtFldLastName.setEditable(editable);
		txtFldPhoneNum.setEditable(editable);
		txtFldStreetName.setEditable(editable);
		txtFldCity.setEditable(editable);
	}
	
	

}
