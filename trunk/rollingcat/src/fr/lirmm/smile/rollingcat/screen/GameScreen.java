package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import static fr.lirmm.smile.rollingcat.Localisation.*;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.Localisation;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;


public class GameScreen implements ScreenPausable{

	private RollingCat game;
	private Stage stage, pauseStage;
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
	private Level level;
	private int segment;
	private InputMultiplexer multiplexer;
	private boolean done;
	private Target gem;
	private Image goldImage, silverImage, bronzeImage;
	private Label goldLabel, silverLabel, bronzeLabel;
	private Table table, pauseTable;
	private List<String> listOfGems;
	private boolean paused;
	private static long elapsedTimeDuringPause;
	private TextButton resume, quit;
	private long beginPause;

	public static Vector2 gold = new Vector2(GameConstants.BLOCK_WIDTH, GameConstants.DISPLAY_HEIGHT * 0.92f);
	public static Vector2 silver = new Vector2(GameConstants.BLOCK_WIDTH * 3, GameConstants.DISPLAY_HEIGHT * 0.92f);
	public static Vector2 bronze = new Vector2(GameConstants.BLOCK_WIDTH * 5, GameConstants.DISPLAY_HEIGHT * 0.92f);


	public GameScreen(RollingCat game, Patient patient, Stage stage, Level level, List<String> listOfGems){
		this.game = game;
		this.patient = patient;
		this.stage = stage;
		this.level = level;
		elapsedTimeDuringPause = 0;
		beginPause = 0;
		this.listOfGems = listOfGems;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(!paused){
			duration += delta;
			mc.updateStandTimer();
			cat.move(stage);
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
			table.draw(batch, 1);
			batch.end();
			stage.act(delta);
			stage.draw();
			sr.setProjectionMatrix(stage.getCamera().combined);
			mc.render(sr);
//			cat.render(sr);
			//        for (Actor a: stage.getActors()) {
			//			((Entity) a).drawDebug(sr);
			//		}
			updateCamPos();
			mc.addTrackingPoint(delta, segment);
			if(cat.isDone() && gem.getActions().size == 0){
				gem.addAction(Actions.parallel(Actions.sequence(
						Actions.delay(2),
						new Action() {

							@Override
							public boolean act(float delta) {
								done = true;
								return true;
							}
						})));
				gem.addAction(Actions.parallel(Actions.moveTo((GameConstants.DISPLAY_WIDTH) * (segment - 0.25f), GameConstants.DISPLAY_HEIGHT * 0.25f, 2)));

			}
			if(done){
				InternetManager.endGameSession();
				Gdx.app.log(RollingCat.LOG,"Client sauvegarde des données : " + gem.getCouleur());
				InternetManager.updateLevelStats(patient.getID(), level.getId(), getScore(), (int) duration, gem.getCouleur());
				level.updateStats(getScore(), (int) duration, gem.getCouleur());
				parameters = new OrderedMap<String, String>();
				parameters.put("duration", ""+duration);
				EventManager.create(EventManager.end_game_event_type, parameters);
				patient.addTrack(new Track(mc.getMap(), Track.GAME, duration));
				game.setScreen(new GameProgressionScreen(game, patient, listOfGems, true, gem, level.getId()));
			}
			if(cat.requestBoxEmptiing()){
				mc.dropItem();
				cat.requestOk();
			}

			setLabelText();
			setVectorCoordinates();
			mc.updateHoverTimer();
		}
		else
		{	
			pauseStage.draw();
			pauseStage.act();
		}
	}

	/**
	 * translate la camera si le chat est au bout de l'ecran
	 */
	private void updateCamPos() {
		if(stage.getCamera().position.x + GameConstants.DISPLAY_WIDTH / 2 - GameConstants.BLOCK_WIDTH * 3 < cat.getX()){
			box.emptyAfterNotMoving(segment);
			if(!MouseCursorGame.isHoldingItem())
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
		Gdx.app.log(RollingCat.LOG, "showing");
		paused = false;
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		font = getBigFont();
		pauseStage = new Stage();
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
		multiplexer = new InputMultiplexer(stage, mc, pauseStage);
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

		goldImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.GOLD));
		silverImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.SILVER));
		bronzeImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.BRONZE));

		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		labelStyle.background = getSkin().getDrawable("empty");

		goldLabel = new Label("", labelStyle);
		silverLabel = new Label("", labelStyle);
		bronzeLabel  = new Label("", labelStyle);

		TextButtonStyle style = new TextButtonStyle();
		style.up = getSkin().getDrawable("button_up");
		style.down = getSkin().getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;

		resume = new TextButton(localisation(_resume), style);
		resume.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				paused = false;
				handleElapsedTime();
			}
		});

		quit = new TextButton(localisation(_quit), style);
		quit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(paused)
					game.setScreen(new PatientScreen(game, patient));
			}
		});

		pauseTable = new Table();
		pauseTable.add(resume).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseTable.row();
		pauseTable.add(quit).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseStage.addActor(pauseTable);
		pauseTable.setWidth(GameConstants.DISPLAY_WIDTH);
		pauseTable.setHeight(GameConstants.DISPLAY_HEIGHT);

		table = new Table();
		table.setX(GameConstants.DISPLAY_WIDTH * 0.16f);
		table.setY(GameConstants.DISPLAY_HEIGHT * 0.95f);

		table.add(goldLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);
		table.add(goldImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
		table.add(silverLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);;
		table.add(silverImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
		table.add(bronzeLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);;
		table.add(bronzeImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);

		stage.addListener(new InputListener() {

			@Override
			public boolean keyDown (InputEvent event, int keycode) {
				return true;
			}

			@Override
			public boolean keyUp (InputEvent event, int keycode) {
				if(keycode == Keys.ESCAPE){
					paused = !paused;
					handleElapsedTime();
				}
				if(keycode == Keys.SPACE)
					mc.fall();
				return true;
			}

		});

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
		Gdx.app.log(RollingCat.LOG, ""+elapsedTime);
		elapsedTimeDuringPause += elapsedTime;
	}

	public static long getElapsedTimeDuringPause(){
		return elapsedTimeDuringPause;
	}

	private void setLabelText(){
		goldLabel.setText(cat.getGold() + "x");
		silverLabel.setText(cat.getSilver() + "x");
		bronzeLabel.setText(cat.getBronze() + "x");
	}

	private void setVectorCoordinates(){
		gold.x = table.getX() + goldImage.getX();
		gold.y = table.getY() + goldImage.getY();

		silver.x = table.getX() + silverImage.getX();
		silver.y = table.getY() + silverImage.getY();

		bronze.x = table.getX() + bronzeImage.getX();
		bronze.y = table.getY() + bronzeImage.getY();
	}

	private void handleElapsedTime(){
		if(paused)
			beginPause = System.currentTimeMillis();
		else{
			elapsedTimeDuringPause += (System.currentTimeMillis() - beginPause);
		}

	}

}