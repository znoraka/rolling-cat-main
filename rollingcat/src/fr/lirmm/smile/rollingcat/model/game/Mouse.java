package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class Mouse extends Entity {

	/**
	 * Touchable enabled
	 * la souris donne une piece lorsque la patient clic dessus
	 * @param x
	 * @param y
	 */
	public Mouse(float x, float y) {
		super(x, y, GameConstants.TEXTURE_MOUSE);
	}
	
	@Override
	public Action getAction(){
		return Actions.sequence(
		new Action() {
					
					@Override
					public boolean act(float delta) {
						setTouchable(Touchable.disabled);
						return true;
					}
				},
		new Action() {
			
			@Override
			public boolean act(float delta) {
				Gdx.app.log(RollingCat.LOG, "coin added !");
				return true;
			}
		},
		Actions.moveBy(-16 * GameConstants.BLOCK_WIDTH, 0, 5),
		
		new Action() {
			
			@Override
			public boolean act(float delta) {
				setVisible(false);
				return true;
			}
		});
	}
}
