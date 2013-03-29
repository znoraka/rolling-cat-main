package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;


public class GameScreen implements Screen{

	private RollingCat game;
	private Stage stage;
	private Texture backgroundTexture;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private MouseCursorGame mc;
	private Cat cat;
	private Box box;
	private Patient patient;
	private float duration;
	private BitmapFont font;
    private OrderedMap<String, String> parameters;
    private int level;
    private int segment;
	private InputMultiplexer multiplexer;
	private GameScreen screen;
	private boolean done;
	private Target gem;
	
	private static long elapsedTimeDuringPause;

	

	public GameScreen(RollingCat game, Patient patient, Stage stage, int level){
		this.game = game;
		this.patient = patient;
		this.stage = stage;
		this.level = level;
		elapsedTimeDuringPause = 0;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		duration += delta;
		mc.updateHoverTimer();
		mc.updateStandTimer();
		cat.move(stage);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		font.draw(batch, "bronze : " + cat.getBronze(), GameConstants.DISPLAY_WIDTH * 0.05f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		font.draw(batch, "silver : " + cat.getSilver(), GameConstants.DISPLAY_WIDTH * 0.25f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		font.draw(batch, "gold : " + cat.getGold(), GameConstants.DISPLAY_WIDTH * 0.50f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		batch.end();
		stage.act(delta);
		stage.draw();
		sr.setProjectionMatrix(stage.getCamera().combined);
        mc.render(sr);
        cat.render(sr);
//        for (Actor a: stage.getActors()) {
//			((Entity) a).drawDebug(sr);
//		}
        updateCamPos();
        mc.addTrackingPoint(delta);
        if(cat.isDone() && gem.getActions().size == 0){
        	gem.addAction(Actions.parallel(Actions.sequence(
        			Actions.sizeTo(GameConstants.DISPLAY_WIDTH / 4, GameConstants.DISPLAY_HEIGHT / 4, 3),
        			Actions.delay(2),
        			new Action() {
						
						@Override
						public boolean act(float delta) {
							done = true;
							return true;
						}
					})));
        	gem.addAction(Actions.parallel(Actions.moveTo((GameConstants.DISPLAY_WIDTH) * (segment - 0.25f), GameConstants.DISPLAY_HEIGHT * 0.25f, 3)));
        	
        }
    	if(done){
        	InternetManager.endGameSession();
        	InternetManager.updateLevelStats(patient.getID(), level, getScore(), (int) duration);
        	parameters = new OrderedMap<String, String>();
        	parameters.put("duration", ""+duration);
        	EventManager.create(EventManager.end_game_event_type, parameters);
        	patient.addTrack(new Track(mc.getMap(), Track.GAME, duration));
        	game.setScreen(new TrackingRecapScreen(game, patient));
    	}
        if(cat.requestBoxEmptiing()){
        	mc.dropItem();
        	cat.requestOk();
        }
        
	}
	
	/**
	 * translate la camera si le chat est au bout de l'ecran
	 */
	private void updateCamPos() {
		if(stage.getCamera().position.x + GameConstants.DISPLAY_WIDTH / 2 - GameConstants.BLOCK_WIDTH * 3 < cat.getX()){
        	box.emptyAfterNotMoving(segment);
        	if(!mc.isHoldingItem())
        		box.fill();
        	mc.setX(mc.getX() + GameConstants.VIEWPORT_WIDTH);
			segment++;
			stage.getCamera().translate(GameConstants.VIEWPORT_WIDTH, 0, 0);
			box.setX(box.getX() + GameConstants.VIEWPORT_WIDTH);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		if(screen == null){
			Gdx.app.log(RollingCat.LOG, "showing");
			screen = this;
			batch = new SpriteBatch();
			sr = new ShapeRenderer();
			font = getBigFont();
			EventManager.clear();
	//		elapsedTimeDuringPause = 0;
			parameters = new OrderedMap<String, String>();
			backgroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND);
			backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			cat = (Cat) stage.getActors().get(0);
	     	gem = (Target) stage.getActors().get(stage.getActors().size -1);
			box = new Box(GameConstants.COLS / 2, -2);
			box.setItems(LevelBuilder.getItems());
			stage.addActor(box);
			mc = new MouseCursorGame(stage, cat, box);
			multiplexer = new InputMultiplexer(stage, mc);
			Gdx.input.setInputProcessor(multiplexer);
			duration = 0;
			parameters = new OrderedMap<String, String>();
			parameters.put("game", RollingCat.LOG);
			parameters.put("version", RollingCat.VERSION);
			EventManager.create(EventManager.game_info_event_type, parameters);
			parameters = new OrderedMap<String, String>();
			parameters.put("session_type", Track.GAME);
			parameters.put("game_screen_width", ""+GameConstants.DISPLAY_WIDTH);
			parameters.put("game_screen_height", ""+GameConstants.DISPLAY_HEIGHT);
	     	EventManager.create(EventManager.start_game_event_type, parameters); 
	     	stage.addListener(new InputListener() {
	     		
	     		@Override
	     		public boolean keyDown (InputEvent event, int keycode) {
	     			return true;
	     		}
	
	     		@Override
	     		public boolean keyUp (InputEvent event, int keycode) {
	     			if(keycode == Keys.ESCAPE){
	     				Gdx.app.log(RollingCat.LOG, "escape pressed !");
	     				game.setScreen(new PauseScreen(game, screen, patient));
	     			}
	     			return true;
	     		}
				
			});
		}
		else
			Gdx.input.setInputProcessor(multiplexer);

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
//		LevelBuilder.writeLevel(stage);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Gdx.app.log(RollingCat.LOG, "disposing...");
		backgroundTexture.dispose();
		sr.dispose();
	}
	
	public int getScore(){
		return cat.getBronze() + cat.getSilver() * 2 + cat.getGold() * 3;
	}
	
	public void setElapsedTimeDuringPause(long elapsedTime){
		elapsedTimeDuringPause += elapsedTime;
	}
	
	public static long getElapsedTimeDuringPause(){
		return elapsedTimeDuringPause;
	}

}
