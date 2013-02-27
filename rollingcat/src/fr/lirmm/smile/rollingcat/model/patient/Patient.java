package fr.lirmm.smile.rollingcat.model.patient;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class Patient {
	
	private String nom;
	private String prenom;
	private int age;
	private String strokeDate;
	private boolean rightHemiplegia;
	private boolean rightDominantMember;
	private Texture face;
	private ArrayList<Track> listOfTracks;
	
	public Patient(String nom, String prenom, int age, String strokeDate, boolean rightHemiplegia,	boolean rightDominantMember, Texture face) {
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.strokeDate = strokeDate;
		this.rightHemiplegia = rightHemiplegia;
		this.rightDominantMember = rightDominantMember;
		this.face = face;
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
	
	public int getAge() {
		return age;
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
		this.listOfTracks.add(track);
	}
	
	public ArrayList<Track> getListOfTracks() {
		return this.listOfTracks;
	}

}
