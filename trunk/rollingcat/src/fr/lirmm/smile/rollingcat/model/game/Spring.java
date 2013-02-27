package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Spring extends Entity{
	
	private boolean hit;
	private boolean repaired;
	
	public Spring(float x, float y) {
		super(x, y, GameConstants.TEXTURE_SPRING);
		hit = false;
		this.setTouchable(Touchable.enabled);
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(repaired)
		{
			if(hit)
				batch.draw(anim.getKeyFrame(0.50f, true), this.getX(), this.getY(), this.getWidth(), this.getHeight() * 2);
			else
				batch.draw(anim.getKeyFrame(0.00f, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else 
			batch.draw(anim.getKeyFrame(0.25f, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	@Override
	public Action getActionOnCat(){
		if(repaired)
		{
			if(!hit){
				return Actions.sequence(
						Actions.moveBy(GameConstants.BLOCK_WIDTH, 0, GameConstants.SPEED),
						Actions.delay(0.25f),
						new Action() {
							
							@Override
							public boolean act(float delta) {
								hit = true;
								return true;
							}
						},
						Actions.moveBy(0, GameConstants.BLOCK_HEIGHT * 3, 0.3f, Interpolation.pow2Out),
						Actions.moveBy(GameConstants.BLOCK_WIDTH / 2, 0, 0.2f, Interpolation.pow2Out),
						Actions.moveBy(GameConstants.BLOCK_WIDTH / 2, 0, 0.2f, Interpolation.pow2Out)
						);
			}
			else return Actions.moveBy(0, 0);
		}
		else
			return Actions.moveBy(0, 0);
	}
	
	@Override
	public Action getAction(){
		return new Action() {
			
			@Override
			public boolean act(float delta) {
				repaired = true;
				return true;
			}
		};
	}
}
