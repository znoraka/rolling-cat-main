package fr.lirmm.smile.rollingcat.controller;

import java.util.HashMap;
import java.util.Map;

import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.*;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.manager.EventManager;

public class MouseCursorAssessment implements InputProcessor{

	private float x, oldX;
	private float y, oldY;
	private Map<Integer, float []> map;
	private float elapsedTime;
	private boolean start;
	private boolean paused;
	private OrderedMap<String, String> parameters;
	private boolean inArea;
	
	public MouseCursorAssessment(){
		x = GameConstants.DISPLAY_WIDTH / 2;
		y = GameConstants.DISPLAY_HEIGHT / 2;
		map = new HashMap<Integer, float []>();
		elapsedTime = 0;
		start = false;
		paused = false;
		parameters = new OrderedMap<String, String>();
		inArea = false;
		parameters = new OrderedMap<String, String>();
		parameters.put("x", ""+x(GameConstants.DISPLAY_WIDTH / 2));
		parameters.put("y", ""+0);
		parameters.put("z", ""+0);
		EventManager.create(EventManager.pointing_task_start, parameters);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
//		if(keycode == Keys.ESCAPE)
//			paused = !paused;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(screenX > GameConstants.DISPLAY_WIDTH)
			x = GameConstants.DISPLAY_WIDTH;
		else if(screenX < 0)
			x = 0;
		else
			x = screenX;
		if(screenY > GameConstants.DISPLAY_HEIGHT)
			y = 0;
		else if(screenY < 0)
			y = GameConstants.DISPLAY_HEIGHT;
		else
			y = GameConstants.DISPLAY_HEIGHT - screenY;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(screenX > GameConstants.DISPLAY_WIDTH)
			x = GameConstants.DISPLAY_WIDTH;
		else if(screenX < 0)
			x = 0;
		else
			x = screenX;
		if(screenY > GameConstants.DISPLAY_HEIGHT)
			y = 0;
		else if(screenY < 0)
			y = GameConstants.DISPLAY_HEIGHT;
		else
			y = GameConstants.DISPLAY_HEIGHT - screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void start(){
		start = true;
	}
	
	public void addTrackingPoint(float delta){
		if(start){
			elapsedTime += delta;
			addEndTask();
			if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
				if(x != oldX & y != oldY){
					parameters = new OrderedMap<String, String>();
					oldX = x;
					oldY = y;
					map.put(map.size(), new float[] {x, y});
					parameters.put("x", ""+x(x));
					parameters.put("y", ""+y(y));
					parameters.put("z", ""+0);
					EventManager.create(EventManager.player_cursor_event_type, parameters);
				}
				elapsedTime = 0;
			}
		}
	}
	
	public Map<Integer, float[]> getMap(){
		return this.map;
	}
	
	public boolean isPaused(){
		return paused;
	}

	public float getElapsedTime() {
		return this.elapsedTime;
	}

	public boolean isStarted() {
		return start;
	}
	
	private void addEndTask(){
		if(enteringArea()){
			parameters = new OrderedMap<String, String>();
			parameters.put("x", ""+x(GameConstants.DISPLAY_WIDTH / 2));
			parameters.put("y", ""+0);
			parameters.put("z", ""+0);
			EventManager.create(EventManager.pointing_task_end, parameters);
			parameters = new OrderedMap<String, String>();
			parameters.put("x", ""+x(GameConstants.DISPLAY_WIDTH / 2));
			parameters.put("y", ""+0);
			parameters.put("z", ""+0);
			EventManager.create(EventManager.pointing_task_start, parameters);
		}
	}

	/**
	 * 
	 * @return true si le curseur de la souris vient d'entrer dans la zone centrale
	 */
	private boolean enteringArea() {
		if(isInArea() & !inArea){
			inArea = true;
			stop();
			return true;
		}
		else if(!isInArea())
			inArea = false;
		
		return false;
	}
	
	/**
	 * 
	 * @return true si le curseur de la souris est à l'interieur de la zone centrale
	 */
	public boolean isInArea(){
		return (Math.sqrt((x - GameConstants.DISPLAY_WIDTH / 2)*(x - GameConstants.DISPLAY_WIDTH / 2) + y*y) < 100);
	}
	
	/**
	 * arrete le timer
	 */
	public void stop() {
		start = false;
	}
	
	/**
	 * enleve la pause
	 */
	public void unPause() {
		paused = false;
		
	}

	public void pause() {
		paused = true;
	}

}
