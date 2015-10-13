package com.jonahe.addressbook.app;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.jonahe.addressbook.hiddenbaseclasses.BasicManager;

public class AddressManager extends BasicManager<AddressBookEntry> {
	
	private File saveFile;
	
	public AddressManager(File saveFile){
		this.saveFile = saveFile;
	}


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
	
	@SafeVarargs
	public final ArrayList<AddressBookEntry> getAllEntriesMatching(Predicate<AddressBookEntry>... predicatesToFilterBy) {
		return super.getAllMatching(predicatesToFilterBy);
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
	
	public void saveAll(){
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream(saveFile);
			oos = new ObjectOutputStream(fout);
			
//			for(AddressBookEntry entry : getEntries()){
//				oos.writeObject(entry);
//			}
			oos.writeObject(getEntries()); // save whole list at once..
			
			
		} catch (FileNotFoundException e) {
			System.out.println("File 'saveFile' not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Object output stream crashed..");
			e.printStackTrace();
		} finally {
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void loadSavedEntries(){
		
		boolean fileExists = saveFile.exists();
		if(!fileExists){
			System.out.println("Save file (" + saveFile.getAbsolutePath() + ") missing. Trying to create it.. ");
			boolean created = saveFile.mkdirs();
			System.out.println("File created = " + created);
		} // else nothing
		
		System.out.println("Loading from file (" + saveFile.getAbsolutePath() + ") ..");
		
		
		try (	FileInputStream fis = new FileInputStream(saveFile);
				ObjectInputStream ois = new ObjectInputStream(fis);) {
			
			ArrayList<AddressBookEntry> loadedEntries = new ArrayList<AddressBookEntry>();
			try {
				loadedEntries = (ArrayList<AddressBookEntry>) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(loadedEntries != null){
				addEntries(loadedEntries);				
			} else {
				System.out.println("No saved entries in file.");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EOFException e){
			System.out.println("End of file reached early.. empty file perhaps?");
			e.printStackTrace();
		}
		
		catch (IOException e) {
			System.out.println("Loading Entries failed");
			e.printStackTrace();
		} 
	}
								
								
				

	
}
