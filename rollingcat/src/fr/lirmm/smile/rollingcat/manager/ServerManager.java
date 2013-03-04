package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class ServerManager {

	public static ArrayList<Patient> login(String username, String password) {
		if(username.equals("admin") & password.equals("admin"))
			return PatientsManager.getPatients();
		else
			return null;
	}

}
