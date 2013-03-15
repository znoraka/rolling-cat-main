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

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
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
	private Stage stage;
	private Cat cat;
	private Box box;
	private int item;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private Map<Integer, float []> map;
	private float elapsedTime;
	private boolean started;

	public MouseCursorGame (Stage stage, Cat cat, Box box){
		batch = getSpriteBatch();
		hoverTimer = 0;
		x = GameConstants.DISPLAY_WIDTH / 2;
		y = GameConstants.DISPLAY_HEIGHT / 2;
		this.stage = stage;
		this.cat = cat;
		this.box = box;
		item = 0;
		atlas = getAtlas();
		map = new HashMap<Integer, float []>();
		elapsedTime = 0;
		started = false;
	}
	
	/**
	 * met le timer de la souris à jour en fonction du bloc sur lequel la souris se trouve
	 * et déclenche les actions si le timer est terminé
	 * @param stage le stage du jeu
	 */
	public void updateHoverTimer(){
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
				this.start();
			}
			else if(actor instanceof Dog && item == Box.BONE){
				this.trigger();
			}
			
			else if(actor instanceof Wasp && item == Box.SWATTER)
				this.trigger();
			
			else if(actor instanceof Spring && item == Box.SPRING)
				this.trigger();
			
			else if(actor instanceof Carpet && item == Box.SCISSORS)
				this.trigger();
		}
	}
	
	/**
	 * démarre l'enregistrement de la trace
	 */
	private void start() {
		started = true;
	}
	
	/**
	 * stop l'enregistrement de la trace
	 */
	public void stop(){
		started = false;
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
	public void updateHitTimer(){
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
        sr.begin(ShapeType.Filled);
		sr.rect(x, y, 10, 10);
		if(hoverTimer > 0)
		{	
			sr.setColor(Color.RED);
			sr.rect(x, y, 70, 20);
			sr.setColor(Color.BLUE);
			sr.rect(x, y, 70*hoverTimer, 20);
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
		else if(item == Box.SCISSORS)
			batch.draw(atlas.findRegion("scissors"), x, y, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		batch.end();
		
	}
	
	public void addTrackingPoint(float delta){
		if(started){
			elapsedTime += delta;
			
			if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
				map.put(map.size(), new float[] {x, y});
				elapsedTime = 0;
			}
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
		if(screenX > GameConstants.DISPLAY_WIDTH)
			screenX = GameConstants.DISPLAY_WIDTH - 1;
		
		if(screenX < 0)
			screenX = 0;
		
		if(screenY > GameConstants.DISPLAY_HEIGHT)
			screenY = GameConstants.DISPLAY_HEIGHT - 1;
		
		if(screenY < 0)
			screenY = 0;
		
		x = screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2;
		y = Gdx.graphics.getHeight() - screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(screenX > GameConstants.DISPLAY_WIDTH)
			screenX = GameConstants.DISPLAY_WIDTH - 1;
		
		if(screenX < 0)
			screenX = 0;
		
		if(screenY > GameConstants.DISPLAY_HEIGHT)
			screenY = GameConstants.DISPLAY_HEIGHT - 1;
		
		if(screenY < 0)
			screenY = 0;
		
		x = screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2;
		y = Gdx.graphics.getHeight() - screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
