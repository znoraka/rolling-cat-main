package fr.lirmm.smile.rollingcat.model.patient;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.TrackingPointsManager;

public class Track {
	
	public static final String ASSESSEMENT = "assessment";
	public static final String GAME = "game";
	
	private Map<Integer, float []> track;
	private String date;
	private float duration;
	private int id;
	private int currentSegment;
	private int segment;
	private String type;
	private String[] listOfEvents;

	
	/**
	 * 
	 * @param track Map<Integer, float[]> si Game ou Map<Integer, Integer> si Assessment
	 * @param type
	 * @param duration
	 */
	public Track(Map<Integer, float []> track, String type, float duration){
		this.track = (Map<Integer, float[]>) track;
		this.duration = (float) Math.floor(duration);
		this.id = TrackingPointsManager.getId();
		if(type == GAME){
			segment = (int) (track.get(track.size() - 1))[0] / GameConstants.DISPLAY_HEIGHT;
			System.out.println("number of segments" + segment);
			currentSegment = 0;
		}
		this.type = type;
		InternetManager.getDate(this);
		listOfEvents = EventManager.getListAsJsonString();
	}

	/**
	 * dessine les mouvements avec un point blanc pour un point quelconque et un point rouge pour le premier et le dernier point
	 * @param sr
	 * @param bounds le rectangle dans lequel on souhaite dessiner
	 */
	public void render(ShapeRenderer sr, Rectangle bounds, SpriteBatch batch) {
		sr.setColor(Color.WHITE);
		if(type == GAME){
			for (int i = 0; i < track.size() - 1; i++) {
				if(track.get(i)[2] == currentSegment){
					sr.begin(ShapeType.Line);
					sr.line(
							(track.get(i)[0] % GameConstants.DISPLAY_WIDTH) * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
							track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
							(track.get(i+1)[0] % GameConstants.DISPLAY_WIDTH) * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
							track.get(i+1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY()
							);
					sr.end();
					sr.begin(ShapeType.Filled);
					sr.circle(
							(track.get(i)[0] % GameConstants.DISPLAY_WIDTH) * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
							track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
							3);
					sr.end();
				}
			}
//			sr.setColor(Color.RED);
//			sr.begin(ShapeType.Filled);
//			sr.circle(
//					track.get(indNextSegment.get(currentSegment))[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
//					track.get(indNextSegment.get(currentSegment))[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
//					3);
//			sr.circle(
//					track.get(indNextSegment.get(currentSegment + 1) - 1)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
//					track.get(indNextSegment.get(currentSegment + 1) - 1)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
//					3);
//			sr.end();
		}
//		else{
//			batch.begin();
//			font.setColor(Color.WHITE);
//			int i = 0;
//			for (Entry<Integer, Integer> map : trackAssessment.entrySet()) {
//				i++;
//				font.draw(batch, map.getKey() + " degrees : " + map.getValue() + " percents", (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(), i * 100 * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY());
//			}
//			batch.end();
//		}
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
                    sr.begin(ShapeType.Filled);
                    sr.circle(
                                    track.get(i)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
                                    track.get(i)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
                                    3);
                    sr.end();
            }
            sr.setColor(Color.RED);
            sr.begin(ShapeType.Filled);
            sr.circle(
                            track.get(0)[0]  * (bounds.width / GameConstants.DISPLAY_WIDTH) + bounds.getX(),
                            track.get(0)[1] * (bounds.height / GameConstants.DISPLAY_HEIGHT) + bounds.getY(),
                            3);
            sr.circle(
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
		return date;
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
	 * @return true s'il y a un segment apres celui là
	 */
	public boolean next(){
		currentSegment++;
		if(currentSegment == segment - 1)
			return false;
		else{
			return true;
		}
	}
	
	/**
	 * passe au segment précédent
	 */
	public boolean prev(){
		currentSegment--;
		return (currentSegment != 0);
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
	
	public String[] getListOfEvents(){
		return listOfEvents;
	}

	public void setDate(String date) {
		this.date = date;
		Gdx.app.log(RollingCat.LOG, date);
//		if(!(this.date.equals("error"))){
//			Json json = new Json();
//			JsonReader j = new JsonReader();
//	//		int hour, minute, second;
//	//		hour = json.readValue("hour", Integer.class, j.parse(track.getDate()));
//	//		minute = json.readValue("minute", Integer.class, j.parse(track.getDate()));
//	//		second = json.readValue("second", Integer.class, j.parse(track.getDate()));
//			this.date = json.readValue("datetime", String.class, j.parse(this.date));
//		}
		if(this.date == null){
			this.date = "error retrieving date";
		}
	}
	
}
