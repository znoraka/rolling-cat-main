package fr.lirmm.smile.rollingcat.model.patient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class Patient {
	
	private String nom;
	private String prenom;
	private String birthDate;
	private String strokeDate;
	private boolean rightHemiplegia;
	private boolean rightDominantMember;
	private Texture face; 
	private ArrayList<Track> listOfTracks;
	private int id;
	
	public Patient(String nom, String prenom, String birthDate, String strokeDate, boolean rightHemiplegia,	boolean rightDominantMember, int id, Texture face) {
		this.nom = nom;
		this.prenom = prenom;
		this.birthDate = birthDate;
		this.strokeDate = strokeDate;
		this.rightHemiplegia = rightHemiplegia;
		this.rightDominantMember = rightDominantMember;
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
		return birthDate;
	}
	
	public String getStrokeDate() {
		return strokeDate;
	}
	
	public boolean isRightHemiplegia() {
		return rightHemiplegia;
	}
	
	public boolean isRightDominantMember() {
		return rightDominantMember;
	}
	
	public String getDominantMember(){
		if(rightDominantMember)
			return "Right";
		else
			return "Left";
	}
	
	public String getHemiplegia(){
		if(rightHemiplegia)
			return "Right";
		else
			return "Left";
	}
	
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
