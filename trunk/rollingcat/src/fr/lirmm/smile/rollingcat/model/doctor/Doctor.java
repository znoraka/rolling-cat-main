package fr.lirmm.smile.rollingcat.model.doctor;

import java.util.ArrayList;

import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.PatientsManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class Doctor {
	
	private ArrayList<Patient> listOfPatients;
	private static Doctor doctor;
	
	public static Doctor getDoctor(){
		if(doctor == null){
			doctor = new Doctor();
		}
		return doctor;
	}
	
	private Doctor (){
		
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return true si la connexion s'est bien passée
	 */
	public boolean login(String username, String password){
		InternetManager.login(username, password);
//		InternetManager.retrievePatients();
////		listOfPatients = PatientsManager.getPatients();
//		listOfPatients = PatientsManager.getPatientsFromJson();
		return true;
	}

	/**
	 * 
	 * @return la list des patients du médecin
	 */
	public ArrayList<Patient> getPatients() {
		return listOfPatients;
	}
	
	/**
	 * déconnecte le médecin
	 */
	public void logOut(){
		doctor = null;
		listOfPatients = null;
	}

	public void setPatients(ArrayList<Patient> patientsFromJson) {
		listOfPatients = patientsFromJson;
	}

}
