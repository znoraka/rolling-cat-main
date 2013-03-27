package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
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
    private TiledMap map;
	private TileMapRenderer renderer;
	private TileAtlas tileAtlas;
	private OrthographicCamera camera;
  
	

	public GameScreen(RollingCat game, Patient patient, Stage stage){
		this.game = game;
		this.patient = patient;
		this.stage = stage;
	}
	
	@Override
	public void render(float delta) {
	
//        renderer.render(0, 0, renderer.getUnitsPerTileX(), 32);
        renderer.render(camera);
		duration += delta;
		mc.updateHoverTimer();
		mc.updateStandTimer();
		cat.move(stage);
		batch.begin();
//		batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		font.draw(batch, "bronze : " + cat.getBronze(), GameConstants.DISPLAY_WIDTH * 0.05f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		font.draw(batch, "silver : " + cat.getSilver(), GameConstants.DISPLAY_WIDTH * 0.25f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		font.draw(batch, "gold : " + cat.getGold(), GameConstants.DISPLAY_WIDTH * 0.50f, GameConstants.DISPLAY_HEIGHT * 0.95f);
		batch.end();
		stage.act(delta);
		stage.draw();
		sr.setProjectionMatrix(stage.getCamera().combined);
        mc.render(sr);
//        cat.render(sr);
//        for (Actor a: stage.getActors()) {
//			((Entity) a).drawDebug(sr);
//		}
        updateCamPos();
        mc.addTrackingPoint(delta);
        if(cat.isDone()){
        	InternetManager.endGameSession();
        	parameters = new OrderedMap<String, String>();
        	parameters.put("duration", ""+duration);
        	EventManager.create(EventManager.end_game_event_type, parameters);
        	patient.addTrack(new Track(mc.getMap(), Track.GAME, duration));
        	game.setScreen(new TrackingRecapScreen(game, patient));
        }
        
	}
	
	/**
	 * translate la camera si le chat est au bout de l'ecran
	 */
	private void updateCamPos() {
		if(stage.getCamera().position.x + GameConstants.DISPLAY_WIDTH / 2 - GameConstants.BLOCK_WIDTH * 3 < cat.getX()){
			stage.getCamera().translate(GameConstants.VIEWPORT_WIDTH, 0, 0);
//			box.empty();
//			box.fill();
			box.setX(box.getX() + GameConstants.VIEWPORT_WIDTH);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		font = getBigFont();
		EventManager.clear();
		
		map = TiledLoader.createMap(Gdx.files.internal("data/tilemap/level1-background.tmx"));
		tileAtlas = new SimpleTileAtlas(map, Gdx.files.internal("data/tilemap/"));
		renderer = new TileMapRenderer(map, tileAtlas, 1, 1);
		camera = new OrthographicCamera(GameConstants.DISPLAY_WIDTH / (GameConstants.SCALE * GameConstants.SCALE), GameConstants.DISPLAY_HEIGHT / (GameConstants.SCALE * GameConstants.SCALE));
		camera.translate(GameConstants.DISPLAY_WIDTH * 0.5f, GameConstants.DISPLAY_HEIGHT * 0.5f);
//		camera.setToOrtho(false);
//		camera.setToOrtho(false, GameConstants.DISPLAY_WIDTH * 0.9f, GameConstants.DISPLAY_HEIGHT * 0.9f);
		camera.update();

				
		parameters = new OrderedMap<String, String>();
		backgroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cat = (Cat) stage.getActors().get(0);
//		box = (Box) stage.getActors().get(1);
		box = new Box(GameConstants.COLS / 2, -2);
		box.setItems(LevelBuilder.getItems());
		stage.addActor(box);
		mc = new MouseCursorGame(stage, cat, box);
		Gdx.input.setInputProcessor(mc);
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

}
