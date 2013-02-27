package fr.lirmm.smile.rollingcat.model.patient;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.manager.TrackingPointsManager;

public class Track {
	Map<Integer, float []> track;
	private String date;
	private float duration;
	private int id;
	private ArrayList<Integer> indNextSegment;
	private int currentSegment;
	
	/**
	 * ajout plus tard de la date et de la durée et de plein d'autres informations
	 * @param track
	 */
	public Track(Map<Integer, float []> track){
		this.track = track;
		this.id = TrackingPointsManager.getId();
		this.indNextSegment = new ArrayList<Integer>();
		findSegments();
		currentSegment = 0;
	}

	private void findSegments() {
		indNextSegment.add(0);
		for (int i = 0; i < track.size(); i++) {
			if((int)(track.get(i)[0] / GameConstants.DISPLAY_WIDTH) == indNextSegment.size())
				indNextSegment.add(i);
		}
		indNextSegment.add(track.size() - 1);
	}

	public void render(ShapeRenderer sr, Rectangle bounds) {
		sr.setColor(Color.WHITE);
		for (int i = indNextSegment.get(currentSegment) + 1; i < indNextSegment.get(currentSegment + 1) - 1; i++) {
			sr.begin(ShapeType.Line);
			sr.line(
					track.get(i)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX() - GameConstants.DISPLAY_WIDTH * currentSegment * (bounds.width / GameConstants.DISPLAY_WIDTH),
					track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					track.get(i+1)[0] * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX() - GameConstants.DISPLAY_WIDTH * currentSegment * (bounds.width / GameConstants.DISPLAY_WIDTH),
					track.get(i+1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY()
					);
			sr.end();
			sr.begin(ShapeType.FilledCircle);
			sr.filledCircle(
					track.get(i)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX() - GameConstants.DISPLAY_WIDTH * currentSegment * (bounds.width / GameConstants.DISPLAY_WIDTH),
					track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
					3);
			sr.end();
		}
		sr.setColor(Color.RED);
		sr.begin(ShapeType.FilledCircle);
		sr.filledCircle(
				track.get(indNextSegment.get(currentSegment) + 1)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX() - GameConstants.DISPLAY_WIDTH * currentSegment * (bounds.width / GameConstants.DISPLAY_WIDTH),
				track.get(indNextSegment.get(currentSegment) + 1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
				3);
		sr.filledCircle(
				track.get(indNextSegment.get(currentSegment + 1) - 1)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX() - GameConstants.DISPLAY_WIDTH * currentSegment * (bounds.width / GameConstants.DISPLAY_WIDTH),
				track.get(indNextSegment.get(currentSegment + 1) - 1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
				3);
		sr.end();
	}
	
	/**
	 * the date contains the day and the hour
	 * @return the date
	 */
	public String getDate(){
		return this.date;
	}
	
	/**
	 * the duration is given in seconds
	 * @return the game duration
	 */
	public float getDuration(){
		return this.duration;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void addIndNextScreen(int n){
		indNextSegment.add(n);
	}
	
	public void next(){
		if(currentSegment < indNextSegment.size() - 2)
			currentSegment ++; 
	}
	
	public void prev(){
		if(currentSegment > 0)
			currentSegment--;
	}

	public void reset() {
		currentSegment = 0;
	}
	
	
}
