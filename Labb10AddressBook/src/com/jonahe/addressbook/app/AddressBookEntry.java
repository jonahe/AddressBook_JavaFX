package com.jonahe.addressbook.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AddressBookEntry {

	private Person person;
	private ContactInfo contactInfo;
	private List<Long> connectionIDs; // ID numbers of persons
	
	public AddressBookEntry(Person person, ContactInfo contactInfo){
		this.person = person;
		this.contactInfo = contactInfo;
		this.connectionIDs = new ArrayList<Long>();
	}
	
	public List<Long> getConnectionIDs() {
		return connectionIDs;
	}

	public void setConnectionIDs(List<Long> connectionIDs) {
		this.connectionIDs = connectionIDs;
	}

	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(ContactInfo address) {
		this.contactInfo = address;
	}
	
	public void addConnectionID(Long connectionId){
		connectionIDs.add(connectionId);
	}
	
	public void removeConnectionID(Long connectionIdToRemove){
		connectionIDs.remove(connectionIdToRemove);
	}
	
	
	@Override
	public String toString(){
		// fullname 	 city, street
		String format = "%s - %s, %s";
		return String.format(format, person.getFullName(), contactInfo.getCity(), contactInfo.getStreet()) + " " + person.getId();
	}
	
	public static List<AddressBookEntry> createRandomEntries(int numberWanted){
		
		List<AddressBookEntry> entries = new ArrayList<AddressBookEntry>(numberWanted);
		
		Person[] persons = createRandomPersons(numberWanted);
		ContactInfo[] cInfos = createRandomContactInfos(numberWanted);
		
		for(int i = 0; i < numberWanted; i++){
			entries.add(new AddressBookEntry(persons[i], cInfos[i]));
		}
		
		return entries;
	}
	
	
	private static ContactInfo[] createRandomContactInfos(int numberWanted){
		
		Random randGen = new Random();
		
		String[] countryCodes = Locale.getISOCountries();
		ContactInfo[] cInfos = new ContactInfo[numberWanted];
		
		for(int i = 0; i < numberWanted; i++){
			String countryCode = countryCodes[randGen.nextInt(countryCodes.length)];
			
			String country = new Locale("", countryCode).getDisplayCountry(Locale.ENGLISH);
			String phoneNumber =  "0" + 
								+ (10 + randGen.nextInt(90))
								+ "-"
								+ (100 + randGen.nextInt(900)) + " "
								+ (10 + randGen.nextInt(90)) + " "
								+ (10 + randGen.nextInt(90));
			
			cInfos[i] = new ContactInfo(phoneNumber, country, "Unknown", "Unknown");
		}
		
		return cInfos;
		
	}
	
	private static Person[] createRandomPersons(int numberWanted){
		Random randGen = new Random();
		
		// pool to select from
		String[] sampleNames = "Frida,Per,Olof,Malin,Jenny,Birgit,Britt,Viktor,Jonas,Alexandra,Kim,Robin,Stina,Bengt,Johan,George,Micke,Sten,Ove,Katta,Emma,Frans,Frank,Björn,Eva,Irma".split(",");
		String[] sampleLastNameBeginnings = "Vi,Ranne,Ny,Ahl,Alm,Dahl,Setter,Ek".split(",");
		String[] sampleLastNameEndings = "qvist,berg,bäck,ström,kvist,fors".split(",");
		
		Gender[] sampleGenders = Gender.values();
		
		int minYear = 1940;
		int maxYear = 2015 - minYear;
		
		Person[] persons = new Person[numberWanted];
		
		// create "double names" like Bengt-Frans, or Emma-Sten
		for(int i = 0; i < numberWanted; i++){
			// first names
			int rand1 = randGen.nextInt(sampleNames.length);
			int rand2 = randGen.nextInt(sampleNames.length);
			String firstName = sampleNames[rand1] + "-" + sampleNames[rand2];
			// last names
			rand1 = randGen.nextInt(sampleLastNameBeginnings.length);
			rand2 = randGen.nextInt(sampleLastNameEndings.length);
			String lastName = sampleLastNameBeginnings[rand1] + sampleLastNameEndings[rand2];
			// genders
			Gender gender = sampleGenders[randGen.nextInt(sampleGenders.length)];
			// birthDates
			int year = minYear + randGen.nextInt(maxYear);
			int month = 1 + randGen.nextInt(11);
			int day = 1 + randGen.nextInt(27);
			LocalDate birthDate = LocalDate.of(year, month, day);
			
			persons[i] = new Person(firstName, lastName, gender, birthDate);
		}
		
		return persons;
	}
	
	
}
