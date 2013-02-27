package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class PatientsManager {
	
	public static ArrayList<Patient> getPatients(){
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, new Texture("data/old.png")));
		patients.add(new Patient("Zit,ciet,cires,cdane", "Zinedine", 65, "06/04/1658", false, true, new Texture("data/cat.png")));
		patients.add(new Patient("b", "é", 12, "06/04/4587", true, true, new Texture("data/mouse.png")));
		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, new Texture("data/ladder.png")));
		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, new Texture("data/dog.png")));
		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, new Texture("data/old.png")));
		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, new Texture("data/old.png")));
		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, new Texture("data/old.png")));
		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Leboeuf", "Franck", 75, "06/04/2010", true, false, new Texture("data/old.png")));
		patients.add(new Patient("Zidane", "Zinedine", 65, "06/04/1658", false, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Henry", "Thierry", 12, "06/04/4587", true, true, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Makelele", "Claude", 78, "36/14/2010", false, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		patients.add(new Patient("Gurcuff", "Chirstian", 75, "06/04/2510", true, false, new Texture(GameConstants.TEXTURE_BACKGROUND)));
		
		return patients;
	}
	
}
