package fr.lirmm.smile.rollingcat.model.patient;

import java.util.ArrayList;
import java.sql.Date;

import com.badlogic.gdx.graphics.Texture;

public class Patient {
	
	private String nom;
	private String prenom;
	private long birthDate;
	private long strokeDate;
	private String hemiplegia;
	private String dominantMember;
	private Texture face; 
	private ArrayList<Track> listOfTracks;
	private int id;
	
	public Patient(String nom, String prenom, long birthDate, long strokeDate, String hemiplegia, String dominantMember, int id, Texture face) {
		this.nom = nom;
		this.prenom = prenom;
		this.birthDate = birthDate;
		this.strokeDate = strokeDate;
		this.hemiplegia = hemiplegia;
		this.dominantMember = dominantMember;
		this.face = face;
		this.id = id;
		listOfTracks = new ArrayList<Track>();
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
	
	public String getBirthDate() {
		Date date = new Date(birthDate);
		String [] s = date.toLocaleString().split(" ");
		return (birthDate > 0)?(s[0] + " " + s[1] + " " + s[2]):"error";
	}
	
	public String getStrokeDate() {
		Date date = new Date(strokeDate);
		String [] s = date.toLocaleString().split(" ");
		return (strokeDate > 0)?(s[0] + " " + s[1] + " " + s[2]):"error";
	}
	
	public String getHemiplegia() {
		return hemiplegia;
	}
	
	public String getDominantMember() {
		return dominantMember;
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

	public int getID() {
		return this.id;
	}
	
}
