package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;

public class Gap extends Entity{

	private boolean ready;
	private boolean given;
	
	public Gap(float x, float y) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
		ready = false;
		given = false;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		highlight(batch);
	}
	
	@Override
	public Action getAction(){
		return Actions.visible(false);
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean b) {
		this.ready = b;
	}

	public boolean hasGiven() {
		return given;
	}

	public void setGiven() {
		this.given = true;
	}
	
	@Override
	public int getItemToAct() {
		return Box.FEATHER;
	}
	

}
