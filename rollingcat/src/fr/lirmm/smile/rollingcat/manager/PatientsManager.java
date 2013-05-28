package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class PatientsManager {
	
	
	/**
	 * crée une liste de patients à partir d'un objet json
	 * @param jsonString
	 * @return une arraylist de patients
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Patient> getPatientsFromJson(String jsonString){
		ArrayList<Patient> patients = new ArrayList<Patient>();
		if(jsonString.length() > 10){
			JsonValue arrayOfPatients =  new JsonReader().parse(jsonString);
			for (int i = 0; i < arrayOfPatients.size(); i++) {
				patients.add(patientFactory(arrayOfPatients.get(i)));
			}
		}
		return patients;
	}

	/**
	 * crée un patient à partir d'un objet json
	 * @param jsonData
	 * @param id
	 * @return un patient
	 */
	private static Patient patientFactory(JsonValue jsonData) {
		Json json = new Json();
		String firstName, lastName, hemiplegia, dominantMember, id, s;
		
		s = json.readValue("firstname", String.class, jsonData);
		firstName = (s != null)?s:"unkown";
		
		s = json.readValue("lastname", String.class, jsonData);
		lastName = (s != null)?s:"unkown";
		
		s = json.readValue("stringId", String.class, jsonData);
		id = (s != null)?s:"error";
		
		s = json.readValue("handed", String.class, jsonData);
		if(s != null)
			dominantMember = s;
		else
			dominantMember = "unknown";
		
		s = json.readValue("side", String.class, jsonData);
		if(s != null)
			hemiplegia = s;
		else
			hemiplegia = "unkown";

		return new Patient(lastName, firstName, hemiplegia, dominantMember, id, new Texture("data/oldwoman.jpg"));
	}
	
	public static Patient getFakePatient(){
		return new Patient("Martin", "Marianne", "R", "F", "1", new Texture("data/oldwoman.jpg"));
	}
}
