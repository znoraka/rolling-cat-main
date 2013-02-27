package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Fan extends Entity {

	float d;
	boolean given;
	
	/**
	 * le ventilateur fait deux blocs de large, le chat regarde le bloc en bas à droite de lui lors de ses déplacements
	 * il faut donc que le chat puisse taper le ventilateur lors d'aller retours verticaux
	 * @param x
	 * @param y
	 */
	public Fan(float x, float y) {
		super(x, y, "fan");
		d = 0;
		this.setWidth(GameConstants.BLOCK_WIDTH * 2);
		this.setTouchable(Touchable.disabled);
		given = false;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		d += Gdx.graphics.getDeltaTime();
		batch.draw(anim.getKeyFrame(d, true), this.getX(), this.getY() - GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, 2 * GameConstants.BLOCK_HEIGHT);
	}
	
	@Override
	public Action getActionOnCat(){
		if(!given){
			given = true;
			return Actions.sequence(Actions.moveTo(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT, GameConstants.SPEED),Actions.moveBy(0, (GameConstants.ROWS - this.getYOnGrid()) * GameConstants.BLOCK_HEIGHT + GameConstants.BLOCK_HEIGHT, 1.5f, Interpolation.pow2In));
		}
		else
			return Actions.moveBy(0, (GameConstants.ROWS - this.getYOnGrid()) * GameConstants.BLOCK_HEIGHT, 1.5f, Interpolation.pow2In);

	}

}
