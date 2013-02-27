package fr.lirmm.smile.rollingcat.model.patient;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import fr.lirmm.smile.rollingcat.manager.TrackingPointsManager;

public class Track {
	Map<Integer, float []> track;
	private String date;
	private float duration;
	private int id;
	
	/**
	 * ajout plus tard de la date et de la durée et de plein d'autres informations
	 * @param track
	 */
	public Track(Map<Integer, float []> track){
		this.track = track;
		this.id = TrackingPointsManager.getId();
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(Color.WHITE);
		for (int i = 0; i < track.size() - 1; i++) {
			sr.line(track.get(i)[0], track.get(i)[1], track.get(i+1)[0], track.get(i+1)[1]);
		}
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
	
	
}
