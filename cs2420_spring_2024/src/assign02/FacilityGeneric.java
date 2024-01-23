package assign02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;

public class FacilityGeneric<Type> {
	

	private ArrayList<CurrentPatientGeneric<Type>> patientList;

	/**
	 * Creates an empty facility record.
	 */
	public FacilityGeneric() {
		this.patientList = new ArrayList<CurrentPatientGeneric<Type>>();
	}

	/**
	 * Adds the given patient to the list of patients, avoiding duplicates.
	 *
	 * @param patient - patient to be added to this record
	 * @return true if the patient was added,
	 *         false if the patient was not added because they already exist in the record
	 */
	public boolean addPatient(CurrentPatientGeneric<Type> patient) {
		 // first we create a for loop that will loop through each CurrentPatient in the patientList to check for duplicates
		for (CurrentPatientGeneric<Type> p : patientList) {
			
			// if we find a duplicate then we will return false.
	        if (p.getUHealthID().equals(patient.getUHealthID())) {
	            return false;
	        }
	    }
	    
		// if there is no duplicate then we will add the patient to the list and return true.
		patientList.add(patient);
	    
	    return true;
	}
	
	/**
	 * Retrieves the patient with the given UHealthID.
	 *
	 * @param UHealthID of patient to be retrieved
	 * @return the patient with the given ID, or null if no such patient
	 * 			exists in the record
	 */
	public CurrentPatientGeneric<Type> lookupByUHID(UHealthID patientID) {
		// we will loop through the patientList to find the patient with the corresponding UHealthID
	    for (CurrentPatientGeneric<Type> patient : patientList) {
	        if (patient.getUHealthID().equals(patientID)) {
	        	//if (patient.getUHealthID().equals(patientID)){} works but the tests say it needs to be a patient not currentPatient
	            System.out.println(patient);
	        	return patient;
	        }
	    }
	    
	    // if the UHealthID does not exist then we return null
	    return null;
	}

	/**
	 * Retrieves the patient(s) with the given physician.
	 *
	 * @param physician - physician of patient(s) to be retrieved
	 * @return a list of patient(s) with the given physician (in any order),
	 *         or an empty list if no such patients exist in the record
	 */
	public ArrayList<CurrentPatientGeneric<Type>> lookupByPhysician(Type physician) {
	    // we will use this arraylist to keep track of all the patients seeing the specified physician
	    ArrayList<CurrentPatientGeneric<Type>> patientsSeeingPhysician = new ArrayList<>();

	    // loop through each patient in the patientList
	    for (CurrentPatientGeneric<Type> patient : patientList) {
	        // check to see if the patient's physician is equal to the one we are looking for and make sure its not null
	        if (patient.getPhysician() != null && patient.getPhysician().equals(physician)) {
	            patientsSeeingPhysician.add(patient);
	        }
	    }

	    return patientsSeeingPhysician;
	}

	/**
	 * Retrieves the patient(s) with last visits older than a given date.
	 *
	 * NOTE: If the last visit date equals this date, do not add the patient.
	 *
	 * @param date - cutoff date later than visit date of all returned patients.
	 * @return a list of patient(s) with last visit date before cutoff (in any order),
	 * 	     or an empty list if no such patients exist in the record
	 */
	public ArrayList<CurrentPatientGeneric<Type>> getInactivePatients(GregorianCalendar date) {
		ArrayList<CurrentPatientGeneric<Type>> inactivePatientList = new ArrayList<>();
		
		// will loop through the currentPatient list and check when there last visit was.
		 for (CurrentPatientGeneric<Type> patient : patientList) {
		        if (patient.getLastVisit().before(date)) { // if last visit was before the given date then they are considered inactive
		           inactivePatientList.add(patient);
		        }
		    }
		 
		 return inactivePatientList;
	}

	/**
	 * Retrieves a list of physicians assigned to patients at this facility.
	 *
	 * * NOTE: Do not put duplicates in the list. Make sure each physician
	 *       is only added once.
	 *
	 * @return a list of physician(s) assigned to current patients,
	 * 	     or an empty list if no patients exist in the record
	 */
	public ArrayList<Type> getPhysicianList() {
	    // we will use a hashset because everything added to a hashset must be unique, meaning there can't be any duplicates
	    HashSet<Type> physicians = new HashSet<>(patientList.size());

	    // here we add the patients' given physician to our hashset
	    for (CurrentPatientGeneric<Type> patient : patientList) {
	        physicians.add(patient.getPhysician());
	    }

	    // here we convert the hashset to an ArrayList for the method to remain unchanged from the one provided.
	    ArrayList<Type> physiciansList = new ArrayList<>(physicians);

	    return physiciansList;
	}

	/**
	 * Sets the physician of a patient with the given UHealthID.
	 *
	 * NOTE: If no patient with the ID exists in the collection, then this
	 * 		method has no effect.
	 *
	 * @param patientID - UHealthID of patient to modify
	 * @param physician - identifier of patient's new physician
	 */
	public void setPhysician(UHealthID patientID, Type physician) {
		// first we will check the patient ID to see if they exist
		for (CurrentPatientGeneric<Type> patient : patientList) {
			if(patient.getUHealthID().equals(patientID)) { // this means we found the patient in the list
				patient.updatePhysician(physician); // this will pass the new physician to our updatePhysicaian class in the CurrentPatient class 
				// the passed physician will become the patients new physician
			}
		}
	}

	/**
	 * Sets the last visit date of a patient with the given UHealthID.
	 *
	 * NOTE: If no patient with the ID exists in the collection, then this
	 * 		method has no effect.
	 *
	 * @param patientID - UHealthID of patient to modify
	 * @param date - new date of last visit
	 */
	public void setLastVisit(UHealthID patientID, GregorianCalendar date) {
		// first we will check the patient ID to see if they exist
		for (CurrentPatientGeneric<Type> patient : patientList) {
			if(patient.getUHealthID().equals(patientID)) { // this means we found the patient in the list
				patient.updateLastVisit(date);
			}
		}
	}

	
	
	
	
	
	// given code for phase 3
    /**
	 * Returns the list of current patients in this facility, 
	 * sorted by uHealthID in lexicographical order.
	 */
	public ArrayList<CurrentPatientGeneric<Type>> getOrderedByUHealthID() {
	    ArrayList<CurrentPatientGeneric<Type>> patientListCopy = new ArrayList<CurrentPatientGeneric<Type>>();
		for (CurrentPatientGeneric<Type> patient : patientList) {
			patientListCopy.add(patient);
		}
	    sort(patientListCopy, new OrderByUHealthID());

	    return patientListCopy;
	}


	/**
	 * Returns the list of current patients in this facility with a date of last visit
	 * later than a cutoff date, sorted by name (last name, breaking ties with first name)
	 * Breaks ties in names using uHealthIDs (lexicographical order).
         * Note: see the OrderByName class started for you below!
	 * 
	 * @param cutoffDate - value that a patient's last visit must be later than to be 
	 * 						included in the returned list
	 */
	public ArrayList<CurrentPatientGeneric<Type>> getRecentPatients(GregorianCalendar cutoffDate) {
	    // FILL IN — do not return null
	    return null;
	}

	/**
	 * Performs a SELECTION SORT on the input ArrayList. 
	 * 
	 * 1. Finds the smallest item in the list. 
	 * 2. Swaps the smallest item with the first item in the list. 
	 * 3. Reconsiders the list to be the remaining unsorted portion (second item to Nth item) and 
	 *    repeats steps 1, 2, and 3.
	 */
	private static <ListType> void sort(ArrayList<ListType> list, Comparator<ListType> c) {
		for (int i = 0; i < list.size() - 1; i++) {
			int j, minIndex;
			for (j = i + 1, minIndex = i; j < list.size(); j++) {
				if (c.compare(list.get(j), list.get(minIndex)) < 0) {
					minIndex = j;
				}
			}
			ListType temp = list.get(i);
			list.set(i, list.get(minIndex));
			list.set(minIndex, temp);
		}
	}

	/**
	 * Comparator that defines an ordering among current patients using their uHealthIDs.
	 * uHealthIDs are guaranteed to be unique, making a tie-breaker unnecessary.
	 */
	protected class OrderByUHealthID implements Comparator<CurrentPatientGeneric<Type>> {

		/**
		 * Returns a negative value if lhs (left-hand side) is less than rhs (right-hand side). 
		 * Returns a positive value if lhs is greater than rhs.
		 * Returns 0 if lhs and rhs are equal.
		 */
		public int compare(CurrentPatientGeneric<Type> lhs, CurrentPatientGeneric<Type> rhs) {
			return lhs.getUHealthID().toString().compareTo(rhs.getUHealthID().toString());
		}
	}

	/**
	 * Comparator that defines an ordering among current patients using their names.
	 * Compares by last name, then first name (if last names are the same), then uHealthID 
	 * (if both names are the same).  uHealthIDs are guaranteed to be unique.
	 */
	protected class OrderByName implements Comparator<CurrentPatientGeneric<Type>> {

		@Override
		public int compare(CurrentPatientGeneric<Type> o1, CurrentPatientGeneric<Type> o2) {
			// TODO Auto-generated method stub
			return 0;
		}
		// FILL IN
	}
	
	
	
	
	
	
	
	
	
	// The methods below should not be changed -----------------------------------
	/**
	 * Helper method for parsing the information about a patient from file.
	 *
	 * @param line - string to be parsed
	 * @param lineNumber - line number in file, used for error reporting (see above)
	 * @return the Patient constructed from the information
	 * @throws ParseException if file containing line is not properly formatted (see above)
	 */
	private CurrentPatient parsePatient(String line, int lineNumber) throws ParseException {
		Scanner lineIn = new Scanner(line);
		lineIn.useDelimiter(" ");

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("First name", lineNumber);
		}
		String firstName = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("Last name", lineNumber);
		}
		String lastName = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("UHealth ID", lineNumber);
		}
		String patientIDString = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("physician", lineNumber);
		}
		String physicianString = lineIn.next();
		int physician = Integer.parseInt(physicianString.substring(1, 8));

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("year of last visit", lineNumber);
		}
		String yearString = lineIn.next();
		int year = Integer.parseInt(yearString);

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("month of last visit", lineNumber);
		}
		String monthString = lineIn.next();
		int month = Integer.parseInt(monthString);

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("day of last visit", lineNumber);
		}
		String dayString = lineIn.next();
		int day = Integer.parseInt(dayString);

		GregorianCalendar lastVisit = new GregorianCalendar(year, month, day);

		lineIn.close();

		return new CurrentPatient(firstName, lastName, new UHealthID(patientIDString),
								physician, lastVisit);
	}
}
