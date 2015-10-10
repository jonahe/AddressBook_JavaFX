package com.jonahe.addressbook.app;

import java.time.LocalDate;

import javafx.scene.control.Button;
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
	
	private Button btnSaveContact;
	
	
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
	
	public void editEntry(AddressBookEntry entry){
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
	
	private void setTextFieldEditability(boolean editable){
		txtFldFirstName.setEditable(editable);
		txtFldLastName.setEditable(editable);
		txtFldPhoneNum.setEditable(editable);
		txtFldStreetName.setEditable(editable);
		txtFldCity.setEditable(editable);
	}
	
	
	

}
