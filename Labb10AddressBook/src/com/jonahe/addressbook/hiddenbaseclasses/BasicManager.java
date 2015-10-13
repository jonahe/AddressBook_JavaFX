package com.jonahe.addressbook.hiddenbaseclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
 * Generic manager class to reuse for the basic functions a manager has
 *
 */

public abstract class BasicManager<T> {
	
	List<T> objects;
	
	public BasicManager(){
		objects = new ArrayList<T>();
	}
	
	
	protected List<T> getAll(){
		return objects;
	}
	
	// C for child class
	protected <C extends T> void add(C objectToAdd){
		objects.add(objectToAdd);
	}
	
	protected void addAll(Collection<? extends T> objectsToAdd){
		objects.addAll(objectsToAdd);
	}
	
	protected <C extends T> boolean remove(C objectToRemove){
		return objects.remove(objectToRemove);
	}
	
	
	/**
	 * Get all objects that match the predicate 
	 * @param predicateToFilterBy
	 * @return ArrayList of matches (may be empty)
	 */
	protected final ArrayList<T> getAllMatching(Predicate<? super T> predicateToFilterBy){

		return objects.stream()
				.filter(predicateToFilterBy)
				.collect(Collectors.toCollection(ArrayList<T>::new));
	}
	
	/**
	 * Get all objects that match the predicate 
	 * @param predicateToFilterBy
	 * @return ArrayList of matches (may be empty)
	 */
	@SafeVarargs
	protected final ArrayList<T> getAllMatching(Predicate<T>... predicatesToFilterBy){
		if(predicatesToFilterBy.length == 0){
			return (ArrayList<T>) objects; // no predicates -> everything goes
		}
		// else combine the predicates into one, then filter
		Predicate<T> combinedPredicate = predicatesToFilterBy[0];
		for(int i = 1; i < predicatesToFilterBy.length; i++){
			System.out.println("hej från varargs");
			Predicate<T> pred = predicatesToFilterBy[i];
			combinedPredicate = combinedPredicate.and(pred);
		}
		

		return objects.stream()
				.filter(combinedPredicate)
				.collect(Collectors.toCollection(ArrayList<T>::new));
	}
	
}
