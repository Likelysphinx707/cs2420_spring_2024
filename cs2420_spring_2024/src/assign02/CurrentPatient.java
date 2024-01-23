package assign02;

import java.util.GregorianCalendar;

/**
 * 
 * @author Alex Murdock and ???
 *
 */
public class CurrentPatient extends Patient {

	private UHealthID uhid = null;
	private int physician = -1;
	private GregorianCalendar lastPatientVisit = null;
	
	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param uHealthID
	 * @param physician
	 * @param lastVisit
	 */
	public CurrentPatient(String firstName, String lastName, UHealthID uHealthID, int physician, GregorianCalendar lastVisit) {
		super(firstName, lastName, uHealthID);
		uhid = uHealthID;
		lastPatientVisit = lastVisit;

		//physician was never set
		this.physician = physician;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public int getPhysician() {
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
	public void updatePhysician(int newPhysician)  {
		if (physician != -1) {
			this.physician = newPhysician;
		}
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
