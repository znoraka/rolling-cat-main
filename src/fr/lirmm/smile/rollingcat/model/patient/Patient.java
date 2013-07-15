package fr.lirmm.smile.rollingcat.model.patient;

import java.util.ArrayList;
import static fr.lirmm.smile.rollingcat.Localisation.*;
import com.badlogic.gdx.graphics.Texture;

public class Patient {
	
	private String nom;
	private String prenom;
	private String hemiplegia;
	private String dominantMember;
	private Texture face; 
	private ArrayList<Track> listOfTracks;
	private String id;
	private String needsAssessment;
	
	private static Patient instance;
	private static int index;
	
	public static void setCurrentPatient(Patient patient, int indexPatient)
	{
		instance = patient;
		index = indexPatient;
	}
	
	public static Patient getInstance()
	{
		return instance;
	}
	
	public static void clearInstance() 
	{
		instance = null;
	}
	
	public Patient(String nom, String prenom, String hemiplegia, String dominantMember, String id, Texture face) {
		this.nom = nom;
		this.prenom = prenom;
//		this.birthDate = birthDate;
//		this.strokeDate = strokeDate;
		this.hemiplegia = hemiplegia;
		this.dominantMember = dominantMember;
		this.face = face;
		this.id = id;
		listOfTracks = new ArrayList<Track>();
		this.needsAssessment = null;
	}

	public Texture getFace() {
		return face;
	}

	public String getLastName() {
		return nom;
	}
	
	public String getFirstName() {
		return prenom;
	}
	
//	public String getBirthDate() {
//		Date date = new Date(birthDate);
//		String [] s = date.toLocaleString().split(" ");
//		return (birthDate > 0)?(s[0] + " " + s[1] + " " + s[2]):"error";
//	}
//	
//	public String getStrokeDate() {
//		Date date = new Date(strokeDate);
//		String [] s = date.toLocaleString().split(" ");
//		return (strokeDate > 0)?(s[0] + " " + s[1] + " " + s[2]):"error";
//	}
	
	public String getHemiplegia() {
		return (hemiplegia.equals("R")?localisation(_right):localisation(_left));
	}
	

	public boolean isLeftHemiplegia() {
		return hemiplegia.equals("L");
	}
	
	public String getDominantMember() {
		return (dominantMember.equals("R")?localisation(_right_handed):localisation(_left_handed));
	}
//	
//	public String getDominantMember(){
//		if(dominantMember)
//			return "Right";
//		else
//			return "Left";
//	}
//	
//	public String getHemiplegia(){
//		if(hemiplegia)
//			return "Right";
//		else
//			return "Left";
//	}
	
	public void addTrack(Track track){
		if(track.getTrack().size() > 0)
			this.listOfTracks.add(track);
	}
	
	public ArrayList<Track> getListOfTracks() {
		return this.listOfTracks;
	}

	public String getID() {
		return this.id;
	}
	
	public void setNeedsAssessment(String bool){
		needsAssessment = bool;
	}
	
	/**
	 * 
	 * @return
	 */
	public String needsAssessment(){
		return needsAssessment;
	}

	public static int getIndex() {
		return index;
	}


	
}
