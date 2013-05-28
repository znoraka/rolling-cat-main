package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._quit;
import static fr.lirmm.smile.rollingcat.Localisation._resume;
import static fr.lirmm.smile.rollingcat.Localisation._upload;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.SoundManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;
import fr.lirmm.smile.rollingcat.utils.tutorial.FirstBoxHelper;


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
	private float duration, elapsedTime;
	private BitmapFont font;
	private OrderedMap<String, String> parameters;
	private Level level;
	private int segment;
	private InputMultiplexer multiplexer;
	private boolean done, requestSending, displayAbilityZone;
	private Target gem;
	private Image goldImage, silverImage, bronzeImage;
	private Label goldLabel, silverLabel, bronzeLabel;
	private Table table, pauseTable;
	private List<String> listOfGems;
	private boolean paused;
	private static long elapsedTimeDuringPause;
	private TextButton resume, quit, upload;
	private long beginPause;
	private FirstBoxHelper  firstBox;
	private CheckBox music;
	private Track track;


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
			elapsedTime += delta;
			duration += delta;
			mc.updateStandTimer();
			cat.move(stage);
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
			font.setColor(Color.BLACK);
			table.draw(batch, 1);
			batch.end();
			sr.setProjectionMatrix(stage.getCamera().combined);

			if(displayAbilityZone)
				drawAbilityZone();

			stage.draw();
			stage.act(delta);
			mc.render(sr);
			//			cat.render(sr);
			//        for (Actor a: stage.getActors()) {
			//			((Entity) a).drawDebug(sr);
			//		}
			if(firstBox != null)
				firstBox.render(delta);
			updateCamPos();
			mc.addTrackingPoint(delta, segment);
			gem = Target.getInstance();
			if(cat.isDone() && gem != null && gem.getActions().size == 0){
				SoundManager.gameMusicPlay(false);
				SoundManager.winPlay();
				gem.addAction(Actions.parallel(Actions.sequence(
						Actions.delay(2),
						new Action() {

							@Override
							public boolean act(float delta) {
								done = true;
								upload.setVisible(true);
								paused = true;
								resume.setVisible(false);
								music.setVisible(false);
								return true;
							}
						})));
				gem.addAction(Actions.parallel(Actions.sequence(Actions.moveTo(stage.getCamera().position.x, stage.getCamera().position.y, 2), Actions.moveTo(GameConstants.DISPLAY_WIDTH * 0.5f,  GameConstants.DISPLAY_HEIGHT * 0.5f))));
			}

			if(cat.requestBoxEmptiing()){
				mc.dropItem();
			}

			setLabelText();
			setVectorCoordinates();
			mc.updateHoverTimer();
		}


		if(paused)
		{	
			pauseStage.draw();
			pauseStage.act();
		}

		if(requestSending && track.getListOfEvents() != null){
			InternetManager.sendEvents(track.getListOfEvents());
			Gdx.app.log(RollingCat.LOG,"Client sauvegarde des données : " + gem.getCouleur());
			level.updateStats(getScore(), (int) duration, gem.getCouleur());
			Gdx.app.log(RollingCat.LOG, gem.getCouleur());
			InternetManager.updateLevelStats(patient.getID(), level.getId(), level.getScore(), level.getDuree(), gem.getCouleur());
			requestSending = false;
		}

		font.setColor(Color.GRAY);
		batch.begin();
		font.draw(batch, "etage : " + (cat.getEtage() + 1) + "/" + LevelBuilder.getNumberOfEtage(), GameConstants.BLOCK_WIDTH * 0.1f, GameConstants.BLOCK_HEIGHT * 0.7f);
		font.draw(batch, "segment : " + segment + "/" + LevelBuilder.getNumberOfSegment(), GameConstants.BLOCK_WIDTH * 2.6f, GameConstants.BLOCK_HEIGHT * 0.7f);
		font.draw(batch, "success : " + cat.getSuccessState(), GameConstants.BLOCK_WIDTH * 6.2f, GameConstants.BLOCK_HEIGHT * 0.7f);
		font.draw(batch, "fps : " + Gdx.graphics.getFramesPerSecond(), GameConstants.DISPLAY_WIDTH - 100, GameConstants.DISPLAY_HEIGHT - 20);
		batch.end();
		

		if(Gdx.input.isKeyPressed(Keys.ENTER) & elapsedTime > 0.3f)
		{
			displayAbilityZone = !displayAbilityZone;
			elapsedTime = 0;
		}
	}

	/**
	 * gere la position de la camera
	 */
	private void updateCamPos() {
		stage.getCamera().position.set(segment * GameConstants.BLOCK_WIDTH * GameConstants.COLS + GameConstants.DISPLAY_WIDTH * 0.5f + GameConstants.BLOCK_WIDTH, cat.getEtage() * GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT + GameConstants.DISPLAY_HEIGHT * 0.5f, 0);
//		stage.getCamera().position.set(cat.getX(), cat.getEtage() * GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT + GameConstants.DISPLAY_HEIGHT * 0.5f, 0);
//		stage.getCamera().position.set(segment * GameConstants.BLOCK_WIDTH * GameConstants.COLS + GameConstants.BLOCK_WIDTH, cat.getEtage() * GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT + GameConstants.DISPLAY_HEIGHT * 0.5f, 0);

		box.setEtageAndSegment(cat.getEtage(), segment);
		box.setX(stage.getCamera().position.x - box.getWidth() / 2);
		box.setY(stage.getCamera().position.y - GameConstants.DISPLAY_HEIGHT * 0.5f);
		mc.setDecalage(cat.getEtage() * GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT);
		segment = cat.getSegment();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		pauseStage = new Stage();
		font = getBigFont();
		EventManager.clear();
		sr = new ShapeRenderer();

		elapsedTime = 3;

		displayAbilityZone = false;
		
		Gdx.app.log(RollingCat.LOG, "showing");
		paused = false;
		//		elapsedTimeDuringPause = 0;
		parameters = new OrderedMap<String, String>();
		
		backgroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND + RollingCat.skin + ".png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		cat = (Cat) stage.getActors().get(0);
		box = new Box(GameConstants.COLS / 2, -2);
		box.setItems(LevelBuilder.getItems());

		stage.getActors().removeValue(cat, true);
		stage.addActor(cat);
		stage.addActor(box);

		mc = new MouseCursorGame(stage, cat, box);
		multiplexer = new InputMultiplexer(stage, mc, pauseStage);
		Gdx.input.setInputProcessor(multiplexer);
		duration = 0;
		
		parameters = new OrderedMap<String, String>();
		parameters.put("game", RollingCat.getCurrentGameName());
		parameters.put("version", RollingCat.VERSION);
		parameters.put("timeout", ""+GameConstants.TIMEOUT);
		parameters.put("success_window", ""+GameConstants.SUCCESS);
		
		EventManager.create(EventManager.game_info_event_type, parameters);
		
		parameters = new OrderedMap<String, String>();
		parameters.put("session_type", Track.GAME);
		parameters.put("level_id", ""+level.getId());
		parameters.put("game_screen_width", ""+GameConstants.DISPLAY_WIDTH);
		parameters.put("game_screen_height", ""+GameConstants.DISPLAY_HEIGHT);
		EventManager.create(EventManager.start_game_event_type, parameters);

		goldImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.GOLD));
		silverImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.SILVER));
		bronzeImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.BRONZE));

		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
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

		upload = new TextButton(localisation(_upload), style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(done){
					parameters = new OrderedMap<String, String>();
					parameters.put("duration", ""+(int)duration);
					InternetManager.endGameSession();
					EventManager.create(EventManager.end_game_event_type, parameters);
					track = new Track(mc.getMap(), Track.GAME, duration);
					patient.addTrack(track);
					requestSending = true;
					pauseStage.clear();
					upload = InternetManager.getOkButton(new GameProgressionScreen(game, patient, listOfGems, gem, level.getId()), game);
					pauseStage.addActor(upload);
					upload.setX(GameConstants.DISPLAY_WIDTH * 0.5f - upload.getWidth() * 0.5f);
					upload.setY(GameConstants.DISPLAY_HEIGHT * 0.5f - upload.getHeight() * 0.5f);
				}
			}
		});

		upload.setVisible(false);

		quit = new TextButton(localisation(_quit), style);
		quit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(paused){
					game.setScreen(new PatientScreen(game, patient));
					SoundManager.gameMusicPlay(false);
				}
			}
		});

		CheckBoxStyle cbs = new CheckBoxStyle();
		cbs.checkboxOff = getSkin().getDrawable("unchecked");
		cbs.checkboxOn = getSkin().getDrawable("checked");
		cbs.checkboxOff.setMinHeight(GameConstants.BLOCK_HEIGHT * 0.8f);
		cbs.checkboxOff.setMinWidth(GameConstants.BLOCK_WIDTH * 0.8f);
		cbs.checkboxOn.setMinHeight(GameConstants.BLOCK_HEIGHT * 0.8f);
		cbs.checkboxOn.setMinWidth(GameConstants.BLOCK_WIDTH * 0.8f);
		cbs.font = font;
		cbs.fontColor = Color.BLACK;
		cbs.down = getSkin().getDrawable("button_up");
		cbs.up = getSkin().getDrawable("button_up");
		music = new CheckBox("musique", cbs);
		music.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(music.isChecked())
					SoundManager.gameMusicPlay(true);
				else
					SoundManager.gameMusicPlay(false);
			}
		});

		pauseTable = new Table();
		pauseTable.add(resume).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseTable.row();
		pauseTable.add(upload).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseTable.row();
		pauseTable.add(music).pad(GameConstants.BLOCK_WIDTH * 0.5f);
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

				if(keycode == Keys.UP){
					if(cat.getEtage() < LevelBuilder.getNumberOfEtage() - 1)
						cat.setY((cat.getEtage() + 1) * GameConstants.DISPLAY_HEIGHT * 2 + GameConstants.BLOCK_HEIGHT * (GameConstants.ROWS - 1));
					cat.setState(Cat.FALLING);
					cat.getActions().clear();
				}

				if(keycode == Keys.DOWN){
					if(cat.getEtage() > 0)
						cat.setY((cat.getEtage() - 1) * GameConstants.DISPLAY_HEIGHT * 2 + GameConstants.BLOCK_HEIGHT * (GameConstants.ROWS - 1));
					cat.setState(Cat.FALLING);
					cat.getActions().clear();
				}
				return true;
			}

		});

		if(level.getId() == 0)
			firstBox = new FirstBoxHelper(sr,stage,mc,cat,this,box);	

		SoundManager.gameMusicPlay(true);
		music.setChecked(true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		dispose();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		Gdx.app.log(RollingCat.LOG, "disposing...");
		SoundManager.gameMusicPlay(false);
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
		gold.y = table.getY() + goldImage.getY() + stage.getCamera().position.y - GameConstants.DISPLAY_HEIGHT * 0.5f;

		silver.x = table.getX() + silverImage.getX();
		silver.y = table.getY() + silverImage.getY() + stage.getCamera().position.y - GameConstants.DISPLAY_HEIGHT * 0.5f;

		bronze.x = table.getX() + bronzeImage.getX();
		bronze.y = table.getY() + bronzeImage.getY() + stage.getCamera().position.y - GameConstants.DISPLAY_HEIGHT * 0.5f;
	}

	private void handleElapsedTime(){
		if(paused){
			beginPause = System.currentTimeMillis();
		}
		else{
			elapsedTimeDuringPause += (System.currentTimeMillis() - beginPause);
		}

	}

	private void drawAbilityZone(){
		sr.begin(ShapeType.Filled);
		int a = 0;
		float [] abilityZone = InternetManager.ability;
		for (int i = 0; i < GameConstants.numberOfLines; i++) {
			for (int j = 0; j < GameConstants.numberOfRows; j++) {
				if(abilityZone[a] != 0){
					sr.setColor(1, 1 - abilityZone[a] * abilityZone.length * 0.75f, 0, 1);
					sr.rect(GameConstants.BLOCK_WIDTH * j + stage.getCamera().position.x - GameConstants.DISPLAY_WIDTH * 0.5f, GameConstants.BLOCK_HEIGHT * i  + stage.getCamera().position.y - GameConstants.DISPLAY_HEIGHT * 0.5f + GameConstants.BLOCK_HEIGHT * 2, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
				}
				a++;
			}
		}
		sr.end();
	}
}