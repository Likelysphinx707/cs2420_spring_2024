package assign02;

import java.util.GregorianCalendar;

/**
 * 
 * @author Alex Murdock and 
 *
 */
public class CurrentPatientGeneric<T> extends Patient {

	private UHealthID uhid = null;
	private T physician;
	private GregorianCalendar lastPatientVisit = null;
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param uHealthID
	 * @param physician
	 * @param lastVisit
	 */
	public CurrentPatientGeneric(String firstName, String lastName, UHealthID uHealthID, T physician, GregorianCalendar lastVisit) {
		super(firstName, lastName, uHealthID);
		uhid = uHealthID;
		lastPatientVisit = lastVisit;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getPhysician() {
		return physician;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public GregorianCalendar getLastVisit() {
		return lastPatientVisit;
	}
	
	
	/**
	 * 
	 * @param newPhysician
	 * 
	 * updates the current patients physician
	 */
	public void updatePhysician(T newPhysician)  {
		this.physician = newPhysician;
	}
	
	/** 
	 * 
	 * @param date
	 */
	public void updateLastVisit(GregorianCalendar date) {
		this.lastPatientVisit = date;
	}
	
	public UHealthID getUHealthID() {
		return uhid;
	}
	

	
}
