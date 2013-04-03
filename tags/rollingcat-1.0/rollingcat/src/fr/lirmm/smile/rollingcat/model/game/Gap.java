package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Gap extends Entity{

	private boolean ready;
	private boolean given;
	
	public Gap(float x, float y) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
		ready = false;
		given = false;
	}

	@Override
	public Action getAction(){
		return Actions.visible(false);
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady() {
		this.ready = true;
	}

	public boolean isGiven() {
		return given;
	}

	public void setGiven() {
		this.given = true;
	}
	

}
