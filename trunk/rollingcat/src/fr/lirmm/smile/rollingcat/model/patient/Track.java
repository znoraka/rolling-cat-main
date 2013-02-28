package fr.lirmm.smile.rollingcat.model.patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.manager.TrackingPointsManager;

public class Track {
	
	public static final String ASSESSEMENT = "assessment";
	public static final String GAME = "game";
	
	private Map<Integer, float []> track;
	private java.util.Date date = new java.util.Date();
	private float duration;
	private int id;
	private ArrayList<Integer> indNextSegment;
	private int currentSegment;
	private String type;
	private DateFormat dateFormat;

	

	
	/**
	 * ajout plus tard de la date et de la durée et de plein d'autres informations
	 * @param track
	 */
	public Track(Map<Integer, float []> track, String type, float duration){
		this.track = track;
		this.duration = (float) Math.floor(duration);
		this.id = TrackingPointsManager.getId();
		if(type == GAME){
			this.indNextSegment = new ArrayList<Integer>();
			findSegments();
			currentSegment = 0;
		}
		this.date = new java.util.Date(date.getTime());
		this.type = type;
		dateFormat = new SimpleDateFormat("dd'/'-MM'/'-yy HH'h'mm");

	}
	
	/**
	 * trouve les différends segment dans la map de points
	 * lorsque l'abscisse d'un point est supérieur à la taille de l'écran on ajoute un marqueur
	 * met l'abscisse des points dans des coordonnées interprétables (0 - GameConstants.DISPLAY_WIDTH)
	 */
	private void findSegments() {
		indNextSegment.add(0);
		for (int i = 0; i < track.size(); i++) {
			if((int)(track.get(i)[0] / GameConstants.DISPLAY_WIDTH) == indNextSegment.size()){
				indNextSegment.add(i);
			}
			track.get(i)[0] = track.get(i)[0] - GameConstants.DISPLAY_WIDTH * (indNextSegment.size() - 1);
		}
		indNextSegment.add(track.size() - 1);
	}

	/**
	 * dessine les mouvements avec un point blanc pour un point quelconque et un point rouge pour le premier et le dernier point
	 * @param sr
	 * @param bounds le rectangle dans lequel on souhaite dessiner
	 */
	public void render(ShapeRenderer sr, Rectangle bounds) {
		sr.setColor(Color.WHITE);
		if(type == GAME){
			for (int i = indNextSegment.get(currentSegment); i < indNextSegment.get(currentSegment + 1) - 1; i++) {
				sr.begin(ShapeType.Line);
				sr.line(
						track.get(i)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
						track.get(i+1)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i+1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY()
						);
				sr.end();
				sr.begin(ShapeType.FilledCircle);
				sr.filledCircle(
						track.get(i)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
						3);
				sr.end();
			}
			sr.setColor(Color.RED);
			sr.begin(ShapeType.FilledCircle);
			sr.filledCircle(
					track.get(indNextSegment.get(currentSegment))[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
					track.get(indNextSegment.get(currentSegment))[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					3);
			sr.filledCircle(
					track.get(indNextSegment.get(currentSegment + 1) - 1)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
					track.get(indNextSegment.get(currentSegment + 1) - 1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					3);
			sr.end();
		}
		else{
			for (int i = 0; i < track.size() - 1; i++) {
				sr.begin(ShapeType.Line);
				sr.line(
						track.get(i)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
						track.get(i+1)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i+1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY()
						);
				sr.end();
				sr.begin(ShapeType.FilledCircle);
				sr.filledCircle(
						track.get(i)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
						track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
						3);
				sr.end();
			}
			sr.setColor(Color.RED);
			sr.begin(ShapeType.FilledCircle);
			sr.filledCircle(
					track.get(0)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
					track.get(0)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					3);
			sr.filledCircle(
					track.get(track.size() - 1)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
					track.get(track.size() - 1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					3);
			sr.end();
		}
		
	}
	
	/**
	 * the date contains the day and the hour
	 * @return the date
	 */
	public String getDate(){
		return dateFormat.format(date);
	}
	
	/**
	 * the duration is given in seconds
	 * @return the game duration
	 */
	public float getDuration(){
		return this.duration;
	}
	
	/**
	 * 
	 * @return l'id de la track
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * passe au segment suivant
	 */
	public void next(){
		if(currentSegment < indNextSegment.size() - 2)
			currentSegment ++; 
	}
	
	/**
	 * passe au segment précédent
	 */
	public void prev(){
		if(currentSegment > 0)
			currentSegment--;
	}
	
	/**
	 * retourne au premier segment
	 */
	public void reset() {
		currentSegment = 0;
	}
	
	public String getType(){
		return this.type;
	}
	
	public Map<Integer, float []> getTrack(){
		return this.track;
	}
	
	
}