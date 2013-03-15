package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class PatientsManager {
	
//	public static ArrayList<Patient> getPatients(){
//		ArrayList<Patient> patients = new ArrayList<Patient>();
//		
//		patients.add(new Patient("Martin", "Marianne", ""+75, "06/04/2010", true, false, 1, new Texture("data/oldwoman.jpg")));
////		patients.add(new Patient("Bernard", "Antoine", 65, "17/12/2008", false, true, 2, new Texture("data/oldman.jpg")));
////		patients.add(new Patient("b", "é", 12, "06/04/4587", true, true,3 ,new Texture("data/mouse.png")));
////		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false,4 , new Texture("data/ladder.png")));
////		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false,5 , new Texture("data/dog.png")));
////		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false,6 , new Texture("data/old.png")));
////		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true,7 , new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, 8, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false,9 , new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, 10, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, 11, new Texture("data/old.png")));
////		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, 12, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, 13, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, 14, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, 15, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, 16, new Texture("data/old.png")));
////		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, 17, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, 18, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, 19, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, 20, new Texture(GameConstants.TEXTURE_BACKGROUND)));
////		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, 21, new Texture("data/old.png")));
//		
//		return patients;
//	}
	
	/**
	 * crée une liste de patients à partir d'un objet json
	 * @param jsonString
	 * @return une arraylist de patients
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Patient> getPatientsFromJson(String jsonString){
		ArrayList<Patient> patients = new ArrayList<Patient>();
		if(jsonString.length() > 10){
			Array<Object> arrayOfPatients = (Array<Object>) new JsonReader().parse(jsonString);
			for (int i = 0; i < arrayOfPatients.size; i++) {
				patients.add(patientFactory(arrayOfPatients.get(i), i+1));
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
	private static Patient patientFactory(Object jsonData, int id) {
		Json json = new Json();
		String firstName, lastName, birthDate, strokeDate, hemiplegia, dominantMember, s;
		
		s = json.readValue("firstname", String.class, jsonData);
		firstName = (s != null)?s:"unkown";
		
		s = json.readValue("lastname", String.class, jsonData);
		lastName = (s != null)?s:"unkown";
		
		s = json.readValue("birthdate", String.class, jsonData);
		birthDate = (s != null)?s:"unkown";
		
		s = json.readValue("strokedate", String.class, jsonData);
		strokeDate = (s != null)?s:"unkown";
		
		s = json.readValue("handed", String.class, jsonData);
		if(s != null)
			dominantMember = (s.equals("R"))?"Right":"Left";
		else
			dominantMember = "unknown";
		
		s = json.readValue("side", String.class, jsonData);
		if(s != null)
			hemiplegia = (s.equals("R"))?"Right":"Left";
		else
			hemiplegia = "unkown";

		return new Patient(lastName, firstName, birthDate, strokeDate, hemiplegia, dominantMember, id, new Texture("data/oldwoman.jpg"));
	}
	
	public static Patient getFakePatient(){
		return new Patient("Martin", "Marianne", ""+75, "06/04/2010", "R", "F", 1, new Texture("data/oldwoman.jpg"));
	}
}
