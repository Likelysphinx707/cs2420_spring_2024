package assign02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;

/**
 * 
 *
 * @author Eric Heisler, Alex Murdock, and ???
 * @version May 5, 2023
 */
public class FacilityGeneric<Type> {

    private ArrayList<CurrentPatientGeneric<Type>> patientList;

    /**
     * 
     */
    public FacilityGeneric() {
        this.patientList = new ArrayList<>();
    }

    /**
     *
     *
     * @param patient
     * @return
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
     * 
     *
     * @param 
     * @return 
     */
    public CurrentPatientGeneric<Type> lookupByUHID(UHealthID patientID) {
        // we will loop through the patientList to find the patient with the corresponding UHealthID
        for (CurrentPatientGeneric<Type> patient : patientList) {
            if (patient.getUHealthID().equals(patientID)) {
                return patient;
            }
        }

        // if the UHealthID does not exist then we return null
        return null;
    }

    /**
     * 
     *
     * @param physician 
     * @return 
     */
    public ArrayList<CurrentPatientGeneric<Type>> lookupByPhysician(Type physician) {

        // we will use this arraylist to keep track of all the patients seeing the specified physician
        ArrayList<CurrentPatientGeneric<Type>> patientsSeeingPhysician = new ArrayList<>();

        // loop through each patient in the patientList
        for (CurrentPatientGeneric<Type> patient : patientList) {
            // check to see if the patients physician is equal to the one we are looking for
            if (patient.getPhysician().equals(physician)) {
                patientsSeeingPhysician.add(patient);
            }
        }

        return patientsSeeingPhysician;
    }

    /**
     * 
     *
     * 
     *
     * @param date 
     * @return
     */
    public ArrayList<CurrentPatientGeneric<Type>> getInactivePatients(GregorianCalendar date) {
        ArrayList<CurrentPatientGeneric<Type>> inactivePatientList = new ArrayList<>();

        // will loop through the currentPatient list and check when their last visit was.
        for (CurrentPatientGeneric<Type> patient : patientList) {
            if (patient.getLastVisit().before(date)) { // if last visit was before the given date then they are considered inactive
                inactivePatientList.add(patient);
            }
        }

        return inactivePatientList;
    }

    /**
     * 
     *
     * 
     *
     * @return 
     */
    public ArrayList<Type> getPhysicianList() {
        // we will use a hashset because everything added to a hashset must be unique meaning there can't be any duplicates
        HashSet<Type> physicians = new HashSet<>(patientList.size());

        // here we add the patients given physician to our hashset
        for (CurrentPatientGeneric<Type> patient : patientList) {
            physicians.add(patient.getPhysician());
        }

        // here we convert the hashset to an ArrayList for the method to remain unchanged from the one provided.
        ArrayList<Type> physiciansList = new ArrayList<>(physicians);

        return physiciansList;
    }

    /**
     *
     *
     * 
     *
     * @param patientID 
     * @param physician 
     */
    public void setPhysician(UHealthID patientID, Type physician) {
        // first we will check the patient ID to see if they exist
        for (CurrentPatientGeneric<Type> patient : patientList) {
            if (patient.getUHealthID().equals(patientID)) { // this means we found the patient in the list
                patient.updatePhysician(physician); // this will pass the new physician to our updatePhysician class in the CurrentPatient class
                // the passed physician will become the patient's new physician
            }
        }
    }

    /**
     * 
     * 
     *
     * @param patientID 
     * @param date 
     */
    public void setLastVisit(UHealthID patientID, GregorianCalendar date) {
        // first we will check the patient ID to see if they exist
        for (CurrentPatientGeneric<Type> patient : patientList) {
            if (patient.getUHealthID().equals(patientID)) { // this means we found the patient in the list
                patient.updateLastVisit(date);
            }
        }
    }


	
	/////
	
	/**
	* 
	*/
	public ArrayList<CurrentPatientGeneric<Type>>
	getOrderedByUHealthID() {
	ArrayList<CurrentPatientGeneric<Type>> patientListCopy =
	new ArrayList<CurrentPatientGeneric<Type>>();
	for (CurrentPatientGeneric<Type> patient : patientList) {
	patientListCopy.add(patient);
	}
	sort(patientListCopy, new OrderByUHealthID());
	return patientListCopy;
	}
	/**
	* Returns the list of current patients in this facility with a date
	of last visit
	* later than a cutoff date, sorted by name (last name, breaking
	ties with first name)
	* Breaks ties in names using uHealthIDs (lexicographical order).
	* Note: see the OrderByName class started for you below!
	*
	* @param cutoffDate - value that a patient's last visit must be
	later than to be
	* included in the returned list
	*/
	public ArrayList<CurrentPatientGeneric<Type>>
	getRecentPatients(GregorianCalendar cutoffDate) {
	// FILL IN â do not return null
	return null;
	}
	/**
	* Performs a SELECTION SORT on the input ArrayList.
	*
	* 1. Finds the smallest item in the list.
	* 2. Swaps the smallest item with the first item in the list.
	* 3. Reconsiders the list to be the remaining unsorted portion
	(second item to Nth item) and
	* repeats steps 1, 2, and 3.
	*/
	private static <ListType> void sort(ArrayList<ListType> list,
	Comparator<ListType> c) {
	for (int i = 0; i < list.size() - 1; i++) {
	int j, minIndex;
	for (j = i + 1, minIndex = i; j <
	list.size(); j++) {
	if (c.compare(list.get(j),
	list.get(minIndex)) < 0) {
	minIndex = j;
	}
	}
	ListType temp = list.get(i);
	list.set(i, list.get(minIndex));
	list.set(minIndex, temp);
	}
	}
	/**
	* Comparator that defines an ordering among current patients using
	their uHealthIDs.
	* uHealthIDs are guaranteed to be unique, making a tie-breaker
	unnecessary.
	*/
	protected class OrderByUHealthID implements
	Comparator<CurrentPatientGeneric<Type>> {
	/**
	* Returns a negative value if lhs (left-hand side) is
	less than rhs (right-hand side).
	* Returns a positive value if lhs is greater than rhs.
	* Returns 0 if lhs and rhs are equal.
	*/
	public int compare(CurrentPatientGeneric<Type> lhs,
	CurrentPatientGeneric<Type> rhs) {
	return
	lhs.getUHealthID().toString().compareTo(rhs.getUHealthID().toString());
	}
	}
	/**
	* Comparator that defines an ordering among current patients using
	their names.
	* Compares by last name, then first name (if last names are the
	same), then uHealthID
	* (if both names are the same). uHealthIDs are guaranteed to be
	unique.
	*/
	protected class OrderByName implements
	Comparator<CurrentPatientGeneric<Type>> {

		@Override
		public int compare(CurrentPatientGeneric<Type> o1, CurrentPatientGeneric<Type> o2) {
			// TODO Auto-generated method stub
			return 0;
		}
	// FILL IN
	}
	
}
