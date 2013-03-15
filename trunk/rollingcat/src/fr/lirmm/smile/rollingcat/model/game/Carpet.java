package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Carpet extends Entity {

	public Carpet(float x, float y) {
		super(x, y, GameConstants.TEXTURE_CARPET);
	}
	
	@Override
	public Action getAction(){
		return Actions.visible(false);
	}
	
	@Override
	public Action getActionOnCat(){
		return Actions.moveTo(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT, 0.25f, Interpolation.pow2Out);
	}

}
