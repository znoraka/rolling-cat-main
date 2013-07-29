package fr.lirmm.smile.rollingcat.controller;

import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.x;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.y;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.SoundManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Gap;
import fr.lirmm.smile.rollingcat.model.game.GroundBlock;
import fr.lirmm.smile.rollingcat.model.game.Wasp;

public class MouseCursorGame implements InputProcessor{
	private float hoverTimer;
	private float standTimer;
	private float x, oldX;
	private float y, oldY;
	private Entity actor;
	private Stage stage;
	private static int item;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private Map<Integer, float []> map;
	private float elapsedTime;
	private OrderedMap<String, String> parameters;
	private boolean isTrigger, hit;
	private Rectangle bounds;
	private int decalage;
	private int task_id;

	public MouseCursorGame (Stage stage){
		batch = stage.getSpriteBatch();
		hoverTimer = 0;
		x = GameConstants.DISPLAY_WIDTH / 2;
		y = GameConstants.DISPLAY_HEIGHT / 2;
		this.stage = stage;
		item = 0;
		atlas = getAtlas();
		map = new HashMap<Integer, float []>();
		elapsedTime = 0;
		parameters = new OrderedMap<String, String>();
		standTimer = 0;
		bounds = new Rectangle(1, 1, 1, 1);
		decalage = 0;
		hit = false;
		task_id = 0;
	}

	/**
	 * met le timer de la souris à jour en fonction du bloc sur lequel la souris se trouve
	 * et déclenche les actions si le timer est terminé
	 * @param stage le stage du jeu
	 */
	public void updateHoverTimer(){
		
		this.isTrigger = false;
		bounds.x = x;
		bounds.y = y;
		hit = false;

		for (int i = 0; i < stage.getActors().size & !hit; i++) {
			try {
				actor = (Entity) stage.getActors().get(i);
			} catch (Exception e) {
				continue;
			}
			
			if(actor instanceof GroundBlock)
				continue;
			
			if(actor instanceof Entity && actor.getTouchable() == Touchable.enabled)
			{
				if(actor.getBounds().overlaps(bounds) &&
						Cat.getInstance().getSegment() == actor.getSegment() &&
						Cat.getInstance().getEtage() == actor.getEtage()
						)
				{
					hit = true;
				}
			}
		}
		if(!hit)
			hoverTimer = 0;
		else
		{
			if(actor instanceof Box && ((Box) actor).isEmpty())
				hoverTimer = 0;
			else if(actor.getItemToAct() != item){
				actor.requestRedHighlight(true);
				hoverTimer = 0;
			}
			else{
				hoverTimer += Gdx.graphics.getDeltaTime() * 1;
			}
		}
		if(hoverTimer > GameConstants.HOVER_TIME)
		{	
			hoverTimer = 0;
			if(actor instanceof Gap && item == Box.FEATHER){
				if(!Cat.getInstance().movedX()){
					((Gap) actor).setReady(true);
					this.trigger();
				}
			}

			else if(actor instanceof Box){
				item = Box.getInstance().empty();
				task_id++;
				addEvent(EventManager.pointing_task_start, actor.getX(), 0, task_id);
			}
			else if(actor instanceof Dog && item == Box.BONE){
				SoundManager.barkPlay();
				this.trigger();
			}

			else if(actor instanceof Wasp && item == Box.SWATTER){
				SoundManager.splashPlay();
				this.trigger();
			}
//			session_start
			else if(actor instanceof Carpet && item == Box.SCISSORS){
				SoundManager.scissorsPlay();
				this.trigger();
			}
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
		Box.getInstance().fill();
		item = 0;
		actor.setTouchable(Touchable.disabled);
		addEvent(EventManager.pointing_task_end, actor.getX() + actor.getWidth() * 0.5f, actor.getY() + actor.getHeight() * 0.5f, task_id);
		addEvent(EventManager.task_success, actor.getX() + actor.getWidth() * 0.5f, actor.getY() + actor.getHeight() * 0.5f, task_id);
		this.isTrigger = true;
	}

	/**
	 * ajoute un event à la liste d'events
	 * appelé lorsque le patient réussi une tache de pointage
	 * @param pointingTaskEnd 
	 */
	private void addEvent(String eventType, float x, float y, int id){
		Gdx.app.log(RollingCat.LOG, "x : " + x(x));
		Gdx.app.log(RollingCat.LOG, "y : " + y(y));
		parameters = new OrderedMap<String, String>();
		parameters.put("x", ""+ x(x));
		parameters.put("y", ""+ y(y));
		parameters.put("z", ""+0);
		parameters.put("id", ""+id);
		EventManager.create(eventType, parameters);
	}

	/**
	 * update le timer qui gere l'immobilité du patient
	 * @param stage
	 */
	public void updateStandTimer(){
		if((oldX ==  x || oldY == y) & item != Box.EMPTY & !Cat.getInstance().movedX()){
			standTimer += Gdx.graphics.getDeltaTime() * 1;
		}
		else {
			standTimer = 0;
		}

		if(standTimer > GameConstants.TIMEOUT){
			standTimer = 0;
			Gdx.app.log(RollingCat.LOG, "Not moving for too long");
			Cat.getInstance().getActions().clear();
			item = Box.EMPTY;
			this.fall();
		}
	}

	private boolean isFalling;
	public boolean isFalling()
	{
		if(!Box.getInstance().isEmpty())
		{
			isFalling = false;
		}
		return(isFalling);
	}

	public void fall(){
		SoundManager.fallPlay();
		dropItem();
		Box.getInstance().emptyAfterNotMoving();
		if(Cat.getInstance().getSuccessState() < GameConstants.SUCCESS)
			Cat.getInstance().setSuccess(false);
		
		Cat.getInstance().setY((Cat.getInstance().getEtage()) * GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT + GameConstants.BLOCK_HEIGHT);
		Cat.getInstance().getActions().clear();
		Cat.getInstance().setState(Cat.FALLING);
		isFalling = true;
		
		if(Cat.getInstance().getLastActorHit() != null)
		{
			addEvent(EventManager.pointing_task_end, Cat.getInstance().getLastActorHit().getX(), Cat.getInstance().getLastActorHit().getY(), task_id);
			addEvent(EventManager.task_fail, Cat.getInstance().getLastActorHit().getX(), Cat.getInstance().getLastActorHit().getY(), task_id);
		}
	}

	/**
	 * dessine l'avancement du timer lors d'un hover
	 * dessine l'item tenu
	 * @param sr
	 */
	public void render(ShapeRenderer sr){
//		sr.begin(ShapeType.Line);
//		for (int i = 0; i < GameConstants.COLS; i++) {
//			for (int j = 0; j < GameConstants.ROWS; j++) {
//				sr.rect(Cat.getInstance().getSegment() * GameConstants.BLOCK_WIDTH * GameConstants.COLS + (i + 1)* GameConstants.BLOCK_WIDTH, j * GameConstants.BLOCK_HEIGHT + Cat.getInstance().getEtage() * (GameConstants.DECALAGE) * GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
//			}
//		}
//		sr.end();
		
		sr.begin(ShapeType.Filled);
		if(hoverTimer > 0)
		{	
			sr.setColor(Color.RED);
			sr.rect(x, y - GameConstants.BLOCK_HEIGHT, 70, 20);
			sr.setColor(Color.BLUE);
			sr.rect(x, y - GameConstants.BLOCK_HEIGHT, 70*hoverTimer / GameConstants.HOVER_TIME, 20);
		}
		if (standTimer > GameConstants.TIMEOUT * 0.5f){
			sr.setColor(Color.GREEN);
			sr.rect(Cat.getInstance().getX(), Cat.getInstance().getY(), 70, 20);
			sr.setColor(Color.ORANGE);
			sr.rect(Cat.getInstance().getX(), Cat.getInstance().getY(), 70 * standTimer / GameConstants.TIMEOUT, 20);
		}
		sr.end();

		sr.begin(ShapeType.Line);
		sr.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		sr.end();

		batch.setProjectionMatrix(stage.getCamera().combined);
		batch.begin();

		if(item == Box.BONE)
			batch.draw(atlas.findRegion("bone" + RollingCat.skin), x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, x, y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
		else if(item == Box.FEATHER)
			batch.draw(atlas.findRegion("feather" + RollingCat.skin), x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, x, y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
		else if(item == Box.SPRING)
			batch.draw(atlas.findRegion("spring" + RollingCat.skin), x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, x, y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
		else if(item == Box.SWATTER)
			batch.draw(atlas.findRegion("swatter" + RollingCat.skin), x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, x, y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
		else if(item == Box.SCISSORS)
			batch.draw(atlas.findRegion("scissors" + RollingCat.skin), x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, x, y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT * 1.5f, 1, 1, 0);
		else
			batch.draw(atlas.findRegion("hand"), x - GameConstants.BLOCK_WIDTH * 0.5f, y - GameConstants.BLOCK_HEIGHT * 0.5f, x, y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
		batch.end();

	}

	public void addTrackingPoint(float delta, int segment){
		elapsedTime += delta;

		if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
			if(x != oldX || y != oldY){
				parameters = new OrderedMap<String, String>();
				oldX = x;
				oldY = y;
				map.put(map.size(), new float[] {x%GameConstants.DISPLAY_WIDTH, y%GameConstants.DISPLAY_HEIGHT, segment});
				parameters.put("x", "" + x(x));
				parameters.put("y", "" + y(y));
				parameters.put("z", "" + 0);
				parameters.put("id", ""+task_id);
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
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
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
		y = Gdx.graphics.getHeight() - screenY + decalage;
		
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
		y = Gdx.graphics.getHeight() - screenY + decalage;
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

	public static boolean isHoldingItem() {
		return item != Box.EMPTY;
	}

	public static int getItem() {
		return item;
	}


	public boolean isTrigger()
	{	
		return isTrigger;
	}

	//	private void updateBounds() {
	//		bounds.set(x - GameConstants.BLOCK_WIDTH * 0.75f, y - GameConstants.BLOCK_HEIGHT * 0.75f, GameConstants.BLOCK_WIDTH * 1.5f, GameConstants.BLOCK_HEIGHT * 1.5f);
	//	}

	public Vector2 getCoordTasks()
	{
		if(item == Box.BONE || item == Box.FEATHER)
		{	
			return new Vector2(Cat.getInstance().getX() + GameConstants.BLOCK_WIDTH,Cat.getInstance().getY());
		}
		else if(item == Box.SCISSORS)
		{
			return new Vector2(Cat.getInstance().getX(),Cat.getInstance().getY()- GameConstants.BLOCK_HEIGHT);
		}
		else if(item == Box.SWATTER)
		{
			return new Vector2(Cat.getInstance().getX(),Cat.getInstance().getY()+ GameConstants.BLOCK_HEIGHT);
		}
		else return null;
	}

	public void setDecalage(float f) {
		decalage = (int) f;

	}
}
