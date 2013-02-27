package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class StopBlock extends Block {
	
	float timer;
	ShapeRenderer sr;
	boolean draw;
	
	public StopBlock(float x, float y) {
		super(x, y, new Vector2(0, 0), GameConstants.TEXTURE_EMPTY);
		timer = 0;
		sr = new ShapeRenderer();
		draw = false;
	}
	
	/**
	 * met le timer à jour
	 * si le chat reste trop longtemps sur le bloc le bloc fait avancer le chat
	 */
	public void updateTimer(){
		timer += Gdx.graphics.getDeltaTime();
		draw = true;
		if(timer > 0){
			velocityModifier.set(1, 0);
			timer = -10;
			Gdx.app.log(RollingCat.LOG, "go");
		}
	}
	
	/**
	 * dessine l'avancement du timer lors d'un contact avec le chat
	 * @param sr
	 */
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(timer > 0 & draw == true)
		{	
			super.draw(batch, deltaParent);
			batch.end();
			sr.setProjectionMatrix(batch.getProjectionMatrix());
			sr.begin(ShapeType.FilledRectangle);
			sr.setColor(Color.WHITE);
			sr.filledRect(getX(), getY(), 30, 10);
			sr.setColor(Color.BLUE);
			sr.filledRect(getX(), getY(), 30 * timer / GameConstants.HOLD_POSITION, 10);
			sr.end();
			batch.begin();
			draw = false;
		}
	}
	
	@Override
	public Action getActionOnCat(){
		this.setVisible(false);
		return super.getActionOnCat();
		
	}
}