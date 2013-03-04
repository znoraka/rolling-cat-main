package fr.lirmm.smile.rollingcat.model.doctor;

import java.util.ArrayList;

import fr.lirmm.smile.rollingcat.manager.ServerManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class Doctor {
	
	private ArrayList<Patient> listOfPatients;
	
	public Doctor (){
		
	}
	
	public boolean login(String username, String password){
		listOfPatients = ServerManager.login(username, password);
		
		return listOfPatients != null;
	}

	public ArrayList<Patient> getPatients() {
		return listOfPatients;
	}

}
