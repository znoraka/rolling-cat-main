package fr.lirmm.smile.rollingcat.model.doctor;

import java.util.ArrayList;

import fr.lirmm.smile.rollingcat.manager.InternetManager;
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
		listOfPatients = InternetManager.login(username, password);
		
		return listOfPatients != null;
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
	}

}
