package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Gap extends Entity{

	public Gap(float x, float y) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
	}

	@Override
	public Action getAction(){
		return Actions.visible(false);
	}

}
