package fr.lirmm.smile.rollingcat.controller;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.*;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Gap;
import fr.lirmm.smile.rollingcat.model.game.Wasp;

public class MouseCursorGame implements InputProcessor{
	private float hoverTimer;
	private float standTimer;
	private float x, oldX;
	private float y, oldY;
	private Entity actor;
	private Stage stage;
	private Cat cat;
	private Box box;
	private int item;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private Map<Integer, float []> map;
	private float elapsedTime;
	private OrderedMap<String, String> parameters;

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
		parameters = new OrderedMap<String, String>();
		standTimer = 0;
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
			if(actor instanceof Gap && item == Box.FEATHER){
				if(!cat.movedX()){
					cat.jump();
					this.trigger();
				}
			}
		
			else if(actor instanceof Box){
				item = box.empty();
				addEvent();
			}
			else if(actor instanceof Dog && item == Box.BONE){
				this.trigger();
			}
			
			else if(actor instanceof Wasp && item == Box.SWATTER)
				this.trigger();
			
			else if(actor instanceof Carpet && item == Box.SCISSORS)
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
		addEvent();
	}
	
	/**
	 * ajoute un event à la liste d'events
	 * appelé lorsque le patient réussi une tache de pointage
	 */
	private void addEvent(){
		parameters = new OrderedMap<String, String>();
		parameters.put("x", ""+ x(actor.getX()%GameConstants.DISPLAY_WIDTH));
		parameters.put("y", ""+ y(actor.getY()));
		parameters.put("z", ""+0);
		EventManager.create(EventManager.pointing_task_end, parameters);
	}
	
	/**
	 * update le timer qui gere l'immobilité du patient
	 * @param stage
	 */
	public void updateStandTimer(){
		if((oldX ==  x || oldY == y) & item != Box.EMPTY & !cat.movedX()){
			standTimer += Gdx.graphics.getDeltaTime() * 1;
		}
		else {
			standTimer = 0;
		}
		
		if(standTimer > GameConstants.TIMEOUT){
			standTimer = 0;
			Gdx.app.log(RollingCat.LOG, "Not moving for too long");
			cat.getActions().clear();
			item = Box.EMPTY;
			this.fall();
		}
	}
	
	public void fall(){
		cat.setY(GameConstants.BLOCK_HEIGHT * 1);
		cat.setX((cat.getXOnGrid() + 1) * GameConstants.BLOCK_WIDTH);
		cat.getActions().clear();
		cat.setState(Cat.FALLING);
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
		sr.begin(ShapeType.Filled);
		if (standTimer > GameConstants.TIMEOUT / 2){
			sr.setColor(Color.GREEN);
			sr.rect(cat.getX(), cat.getY(), 70, 20);
			sr.setColor(Color.ORANGE);
			sr.rect(cat.getX(), cat.getY(), 70*(standTimer - GameConstants.TIMEOUT / 2) / GameConstants.TIMEOUT * 2, 20);
		}
		sr.end();
		
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
		elapsedTime += delta;
		
		if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
			if(x != oldX || y != oldY){
				parameters = new OrderedMap<String, String>();
				oldX = x;
				oldY = y;
				map.put(map.size(), new float[] {x, y});
				parameters.put("x", ""+x(x%GameConstants.DISPLAY_WIDTH));
				parameters.put("y", ""+y(y));
				parameters.put("z", ""+0);
				EventManager.create(EventManager.player_cursor_event_type, parameters);
			}
			elapsedTime = 0;
		}
	}
	
	public Map<Integer, float[]> getMap(){
		return this.map;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		System.out.println("ris,iers,");
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ENTER)
			System.out.println("rinseuirnset");
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return true;
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
		
		x = (screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2);
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
		
		x = (screenX + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH / 2);
		y = Gdx.graphics.getHeight() - screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dropItem() {
		item = Box.EMPTY;
	}

	public float getX() {
		return x;
	}
	
	public void setX(float x){
		this.x = x;
	}

	public boolean isHoldingItem() {
		Gdx.app.log(RollingCat.LOG, "holding an item" + (item != Box.EMPTY));
		return item != Box.EMPTY;
	}
	
}
