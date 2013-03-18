package fr.lirmm.smile.rollingcat.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.model.event.Event;
import fr.lirmm.smile.rollingcat.model.event.EventModel;

public class MouseCursorAssessment implements InputProcessor{

	private float x;
	private float y;
	private Map<Integer, float []> map;
	private float elapsedTime;
	private boolean start;
	private boolean isDone;
	private OrderedMap<String, String> parameters;
	
	public MouseCursorAssessment(){
		x = GameConstants.DISPLAY_WIDTH / 2;
		y = 101;
		map = new HashMap<Integer, float []>();
		elapsedTime = 0;
		start = false;
		isDone = false;
		parameters = new OrderedMap<String, String>();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ENTER)
			isDone = true;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
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
			
			if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
				map.put(map.size(), new float[] {x, y});
				parameters.put("x", ""+x);
				parameters.put("y", ""+y);
				parameters.put("z", ""+0);
				EventManager.add(new Event(EventModel.player_cursor_event_type, parameters));
				elapsedTime = 0;
			}
		}
	}
	
	public Map<Integer, float[]> getMap(){
		return this.map;
	}
	
	public boolean isDone(){
		return isDone;
	}

	public float getElapsedTime() {
		return this.elapsedTime;
	}

}
