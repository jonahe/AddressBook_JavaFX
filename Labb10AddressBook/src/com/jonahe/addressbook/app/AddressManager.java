package com.jonahe.addressbook.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.jonahe.addressbook.hiddenbaseclasses.BasicManager;

public class AddressManager extends BasicManager<AddressBookEntry> {
	
	

	public List<AddressBookEntry> getEntries() {
		return super.getAll();
	}

	public <C extends AddressBookEntry> void addEntry(C objectToAdd) {
		super.add(objectToAdd);
	}

	public void addEntries(Collection<? extends AddressBookEntry> objectsToAdd) {
		super.addAll(objectsToAdd);
	}

	public <C extends AddressBookEntry> boolean removeEntry(C objectToRemove) {
		return super.remove(objectToRemove);
	}

	public ArrayList<AddressBookEntry> getAllEntriesMatching(Predicate<? super AddressBookEntry> predicateToFilterBy) {
		return super.getAllMatching(predicateToFilterBy);
	}
	
	
//	public void updatePerson( 	Person personToUpdate,
//								String firstName,
//								String lastName,
//								LocalDate birthDate,
//								Gender gender) {
//		
//		personToUpdate.setFirstName(firstName);
//		personToUpdate.setLastName(lastName);
//		personToUpdate.setBirthDate(birthDate);
//		personToUpdate.setGender(gender);
//	}
//	
//	public void updateContactInfo(	ContactInfo contactInfoToUpdate,
//									String phoneNumber,
//									String street,
//									String city,
//									String country) {
//		
//		contactInfoToUpdate.setPhoneNumber(phoneNumber);
//		contactInfoToUpdate.setStreet(street);
//		contactInfoToUpdate.setCity(city);
//		contactInfoToUpdate.setCountry(country);
//	}
	
	/**
	 * Creates AND adds to entry list
	 * @param person
	 * @param contactInfo
	 */
	public void createEntry(Person person, ContactInfo contactInfo){
		this.addEntry(new AddressBookEntry(person, contactInfo));
	}
								
								
				

	
}
