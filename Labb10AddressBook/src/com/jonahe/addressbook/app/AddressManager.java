package com.jonahe.addressbook.app;

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
	
	
	
	/**
	 * Creates AND adds to entry list
	 * @param person
	 * @param contactInfo
	 */
	public void createEntry(Person person, ContactInfo contactInfo){
		this.addEntry(new AddressBookEntry(person, contactInfo));
	}
	
	
	public List<AddressBookEntry> getListFromPersonIdList(List<Long> listOfIDs){
		
		Predicate<AddressBookEntry> matchesListOfID;
		matchesListOfID = entry -> {
			return listOfIDs.contains(entry.getPerson().getId());
		};
		
		return getAllEntriesMatching(matchesListOfID);
	}
								
								
				

	
}
