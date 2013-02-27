package fr.lirmm.smile.rollingcat.controller;

import com.badlogic.gdx.InputProcessor;

import fr.lirmm.smile.rollingcat.GameConstants;

public class MouseCursorAssessment implements InputProcessor{

	private float x;
	private float y;
	
	public MouseCursorAssessment(){
		x = 0;
		y = 0;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
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
		x = screenX;
		y = GameConstants.DISPLAY_HEIGHT - screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		x = screenX;
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

}
