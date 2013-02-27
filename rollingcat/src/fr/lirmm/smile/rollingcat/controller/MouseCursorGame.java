package fr.lirmm.smile.rollingcat.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Mouse;
import fr.lirmm.smile.rollingcat.model.game.Spring;
import fr.lirmm.smile.rollingcat.model.game.Wasp;

public class MouseCursorGame implements InputProcessor{
	private float hoverTimer;
	private float standTimer;
	private float x;
	private float y;
	private int oldBlockX;
	private int oldBlockY;
	private Entity actor;
	Stage stage;
	Cat cat;
	Box box;
	int item;
	TextureAtlas atlas;
	SpriteBatch batch;
	Map<Integer, float []> map;
	float elapsedTime;

	public MouseCursorGame (Stage stage, Cat cat, Box box){
		hoverTimer = 0;
		x = GameConstants.DISPLAY_WIDTH / 2;
		y = GameConstants.DISPLAY_HEIGHT / 2;
		this.stage = stage;
		this.cat = cat;
		this.box = box;
		item = 0;
		atlas = new TextureAtlas(GameConstants.ATLAS);
		batch = new SpriteBatch();
		map = new HashMap<Integer, float []>();
		elapsedTime = 0;
	}
	
	/**
	 * met le timer de la souris à jour en fonction du bloc sur lequel la souris se trouve
	 * et déclenche les actions si le timer est terminé
	 * @param stage le stage du jeu
	 */
	public void updateHoverTimer(Stage stage){
		actor = (Entity) stage.hit(x, y, true);
		if(actor != null){
			if(actor instanceof Box && ((Box) actor).isEmpty())
				hoverTimer = 0;
			else
				hoverTimer += Gdx.graphics.getDeltaTime() * 1;
		}
		else
			hoverTimer = 0;
		
		if(hoverTimer > GameConstants.HOVER_TIME)
		{	
			hoverTimer = 0;
			if(actor instanceof Cat && item == Box.FEATHER){
				cat.jump(actor.getXOnGrid() + 2, actor.getYOnGrid());
				box.fill();
				item = 0;
			}
		
			else if(actor instanceof Mouse){
				actor.addAction(actor.getAction());
			}
			else if(actor instanceof Box){
				item = box.empty();
			}
			else if(actor instanceof Dog && item == Box.BONE){
				this.trigger();
			}
			
			else if(actor instanceof Wasp && item == Box.SWATTER)
				this.trigger();
			
			else if(actor instanceof Spring && item == Box.SPRING)
				this.trigger();
		}
	}
	
	/**
	 * appelé lorsque l'item et l'entity correspondent
	 * ajoute l'action à l'acteur
	 * remplit la boite
	 * lache l'item tenu
	 */
	private void trigger(){
		actor.addAction(actor.getAction());
		box.fill();
		item = 0;
		actor.setTouchable(Touchable.disabled);
	}
	
	/**
	 * update le timer qui gere l'immobilité du patient
	 * @param stage
	 */
	public void updateHitTimer(Stage stage){
		if(x % GameConstants.BLOCK_WIDTH != oldBlockX & y % GameConstants.BLOCK_HEIGHT != oldBlockY){
			oldBlockX = (int) (x % GameConstants.BLOCK_WIDTH);
			oldBlockY = (int) (y % GameConstants.BLOCK_HEIGHT);
			standTimer = 0;
		}
		else {
			standTimer += Gdx.graphics.getDeltaTime() * 1;
		}
		
		if(standTimer > GameConstants.HOLD_POSITION){
			standTimer = 0;
			Gdx.app.log(RollingCat.LOG, "Not moving for too long");
		}
	}
	
	/**
	 * dessine l'avancement du timer lors d'un hover
	 * dessine l'item tenu
	 * @param sr
	 */
	public void render(ShapeRenderer sr){
        sr.begin(ShapeType.FilledRectangle);
		sr.filledRect(x, y, 10, 10);
		if(hoverTimer > 0)
		{	
			sr.setColor(Color.RED);
			sr.filledRect(x, y, 70, 20);
			sr.setColor(Color.BLUE);
			sr.filledRect(x, y, 70*hoverTimer, 20);
		}
		sr.end();
//		sr.begin(ShapeType.FilledRectangle);
//		if (standTimer > GameConstants.HOLD_POSITION / 2 & false){
//			sr.setColor(Color.RED);
//			sr.filledRect(cat.getX(), cat.getY(), 70, 20);
//			sr.setColor(Color.BLUE);
//			sr.filledRect(cat.getX(), cat.getY(), 70*(standTimer - GameConstants.HOLD_POSITION / 2) / GameConstants.HOLD_POSITION * 2, 20);
//		}
//		sr.end();
		
		batch.setProjectionMatrix(stage.getCamera().combined);
		batch.begin();
		
		if(item == Box.BONE)
			batch.draw(atlas.findRegion("bone"), x, y, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		else if(item == Box.FEATHER)
			batch.draw(atlas.findRegion("feather"), x, y, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		else if(item == Box.SPRING)
			batch.draw(atlas.findRegion("spring"), x, y, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		else if(item == Box.SWATTER)
			batch.draw(atlas.findRegion("swatter"), x, y, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		
		batch.end();
		
	}
	
	public void addTrackingPoint(float delta){
		elapsedTime += delta;
		
		if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
			map.put(map.size(), new float[] {x, y});
			Gdx.app.log(RollingCat.LOG, "new point added");
			System.out.println(elapsedTime);
			elapsedTime = 0;
		}
	}
	
	public Map<Integer, float[]> getMap(){
		return this.map;
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
		x = screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2;
		y = Gdx.graphics.getHeight() - screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		x = screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2;
		y = Gdx.graphics.getHeight() - screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {
		stage.dispose();
		atlas.dispose();
		batch.dispose();
	}
	
}
