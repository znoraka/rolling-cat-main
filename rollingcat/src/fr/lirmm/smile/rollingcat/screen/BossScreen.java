package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._loseboss;
import static fr.lirmm.smile.rollingcat.Localisation._quit;
import static fr.lirmm.smile.rollingcat.Localisation._resume;
import static fr.lirmm.smile.rollingcat.Localisation._upload;
import static fr.lirmm.smile.rollingcat.Localisation._winboss;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.x;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.y;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class BossScreen implements Screen {

	private static final int BASE_LIFE = 20;

	private enum Skill
	{
		FIRE,
		WATER,
		PLANT
	};

	private enum BossState
	{
		WATER,
		FIRE,
		ROCK
	}

	private RollingCat game;
	private int bossLife, playerLife;
	private Skill currentSkill;
	private BossState currentBossState;
	private Array<Vector3> tasks;
	private Image cat, rabbit, turtle;
	private TextureRegion heart, halfHeart, player, boss, bossHead;
	private Stage stage;
	private SpriteBatch batch;
	private Table table;
	private float elapsedTime, keysDelay;
	private Rectangle task, mouse, catRectangle, rabbitRectangle, turtleRectangle;
	private float hoverTime, standTime;
	private boolean holdingItem, canGrabItem;
	private Actor bossHeadActor;
	private Random r;
	private float chrono;
	private int s, m;
	private Label chronoLabel;
	private String tasksAsString;
	private Window pauseWindow, endWindow;
	private boolean paused, done;
	private TextButton resume, quit, upload, quit2;
	private Label pauseText, endText;
	private Track track;
	private HashMap<Integer, float []> map;
	private Texture gui;
	
	public BossScreen(RollingCat game)
	{
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(gui, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();

		keysDelay += delta;
		s = (int) chrono;

		if(s == 60)
		{
			chrono = 0;
			m++;
		}
		
		chronoLabel.setText(m+":"+ ((s < 10)?("0" + s):s));

		chronoLabel.setX(GameConstants.DISPLAY_WIDTH * 0.48f - chronoLabel.getWidth() * 0.5f);
		chronoLabel.setY(GameConstants.DISPLAY_HEIGHT * 1.01f - GameConstants.BLOCK_HEIGHT * 0.5f);


		task.setX(bossHeadActor.getX());
		task.setY(bossHeadActor.getY());

		catRectangle.set(cat.getX() + table.getX(), cat.getY(), cat.getWidth(), cat.getHeight());
		turtleRectangle.set(turtle.getX() + table.getX(), turtle.getY(), turtle.getWidth(), turtle.getHeight());
		rabbitRectangle.set(rabbit.getX() + table.getX(), rabbit.getY(), rabbit.getWidth(), rabbit.getHeight());
		
		pauseWindow.setVisible(paused);

		stage.draw();
		
		if(!paused & !done)
		{
			stage.act();
			sendBossBack();
			chrono += delta;
			
			if(bossHeadActor.getActions().size == 0)
				standTime += delta;
			
			addTrackingPoint();
			handleMouse();
		}		

		batch.begin();
		if(!paused & !done)
			drawTask();
		drawHearts();
		batch.draw(player, GameConstants.BLOCK_WIDTH * 0.15f, GameConstants.BLOCK_HEIGHT * 0.4f, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		batch.draw(boss, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 1.15f, GameConstants.BLOCK_HEIGHT * 0.4f, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		if(!paused & !done)
			drawHeldItem();
		batch.end();

		table.setX(GameConstants.DISPLAY_WIDTH * 0.5f - table.getWidth() * 0.5f);
		table.setY(0);
		table.setWidth(GameConstants.BLOCK_WIDTH * 4.5f);
		table.setHeight(GameConstants.BLOCK_WIDTH * 1.5f);
		table.invalidate();
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) & keysDelay > 0.3f)
		{
			paused = !paused;
			keysDelay = 0;
		}
		drawHoverTime();
		
		pauseWindow.setX(GameConstants.DISPLAY_WIDTH * 0.25f);
		pauseWindow.setY(GameConstants.DISPLAY_HEIGHT * 0.25f);
		pauseWindow.setWidth(GameConstants.DISPLAY_WIDTH * 0.5f);
		pauseWindow.setHeight(GameConstants.DISPLAY_HEIGHT * 0.5f);

		resume.setX(pauseWindow.getWidth() * 0.1f);
		resume.setY(pauseWindow.getHeight() * 0.1f);
		quit.setX(pauseWindow.getWidth() * 0.9f - quit.getWidth());
		quit.setY(pauseWindow.getHeight() * 0.1f);
		
		upload.setX(pauseWindow.getWidth() * 0.1f);
		upload.setY(pauseWindow.getHeight() * 0.1f);
		
		quit2.setX(pauseWindow.getWidth() * 0.9f - quit.getWidth());
		quit2.setY(pauseWindow.getHeight() * 0.1f);
		
		endWindow.setX(GameConstants.DISPLAY_WIDTH * 0.25f);
		endWindow.setY(GameConstants.DISPLAY_HEIGHT * 0.25f);
		endWindow.setWidth(GameConstants.DISPLAY_WIDTH * 0.5f);
		endWindow.setHeight(GameConstants.DISPLAY_HEIGHT * 0.5f);
		
		if(done)
		{
			endWindow.setVisible(true);
			if(bossLife < playerLife)
				endText.setText(localisation(_winboss));
			else
				endText.setText(localisation(_loseboss));
		}
		
		if(Gdx.input.isKeyPressed(Keys.SPACE))
			bossLife = 0;
		
		if(bossLife == 0 || playerLife == 0)
		{
			done = true;
		}
	}
	
	private void sendBossBack() {
		if(standTime > GameConstants.TIMEOUT)
		{
			standTime = 0;
			playerLife--;
			addEvent(EventManager.pointing_task_end, tasks.get(0).x, tasks.get(0).y, (int) tasks.get(0).z);
			addEvent(EventManager.task_fail, tasks.get(0).x, tasks.get(0).y, (int) tasks.get(0).z);
			bossHeadActor.addAction(
					Actions.sequence(
							Actions.moveTo(bossHeadActor.getX(), GameConstants.DISPLAY_HEIGHT, 2, Interpolation.swingIn),
							new Action() {

								@Override
								public boolean act(float delta) {
									Vector3 v = tasks.get(0);
									tasks.removeIndex(0);
									tasks.add(v);
									bossHeadActor.setX(tasks.get(0).x * GameConstants.BLOCK_WIDTH);
									setCurrentBossState();
									bossHeadActor.addAction(
											Actions.sequence(
													Actions.moveTo(tasks.get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(0).y * GameConstants.BLOCK_HEIGHT, 2, Interpolation.bounceOut),
													new Action() {

														@Override
														public boolean act(float delta) {
															standTime = 0;
															return true;
														}
													}));
									return true;
								}
							}));
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		EventManager.clear();
		gui = new Texture("data/gui.png");
		
		map = new HashMap<Integer, float[]>();

		OrderedMap<String, String> parameters = new OrderedMap<String, String>();
		parameters.put("game", RollingCat.getCurrentGameName());
		parameters.put("version", RollingCat.VERSION);
		parameters.put("timeout", ""+GameConstants.TIMEOUT);
		parameters.put("success_window", ""+GameConstants.SUCCESS);
		EventManager.create(EventManager.game_info_event_type, parameters);

		parameters = new OrderedMap<String, String>();
		parameters.put("session_type", Track.BOSS);
		parameters.put("game_screen_width", ""+GameConstants.DISPLAY_WIDTH);
		parameters.put("game_screen_height", ""+GameConstants.DISPLAY_HEIGHT);
		EventManager.create(EventManager.start_game_event_type, parameters);

		tasksAsString = InternetManager.tasks;
		InternetManager.tasks = null;
		parseTasks();

		stage = GdxRessourcesGetter.getStage();
		batch = GdxRessourcesGetter.getSpriteBatch();
		
		WindowStyle ws = new WindowStyle();
		ws.titleFont = GdxRessourcesGetter.getBigFont();
		ws.background = GdxRessourcesGetter.getSkin().getDrawable("background_base");

		pauseWindow = new Window("", ws);
		endWindow = new Window("", ws);

		stage.addActor(pauseWindow);
		stage.addActor(endWindow);
		pauseWindow.setVisible(false);
		endWindow.setVisible(false);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = GdxRessourcesGetter.getSkin().getDrawable("button_up");
		style.down = GdxRessourcesGetter.getSkin().getDrawable("button_down");
		style.font = GdxRessourcesGetter.getBigFont();
		style.fontColor = Color.BLACK;

		resume = new TextButton(localisation(_resume), style);
		resume.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				paused = false;
			}
		});

		quit = new TextButton(localisation(_quit), style);
		quit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new CharacterSelectScreen(game));
			}
		});
		
		quit2 = new TextButton(localisation(_quit), style);
		quit2.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new CharacterSelectScreen(game));
			}
		});
		
		upload = new TextButton(localisation(_upload), style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(done){
					OrderedMap<String, String> parameters = new OrderedMap<String, String>();
					parameters.put("duration", ""+ m * 60 + s);
					InternetManager.endGameSession();
					EventManager.create(EventManager.end_game_event_type, parameters);
					track = new Track(map, Track.GAME, m * 60 + s);
					Patient.getInstance().addTrack(track);
					TextButton b = InternetManager.getOkButton(new CharacterSelectScreen(game), game);
					endWindow.clearChildren();
					endWindow.addActor(b);
					b.setX(pauseWindow.getWidth() * 0.5f- b.getWidth() * 0.5f);
					b.setY(pauseWindow.getHeight() * 0.5f - b.getHeight() * 0.5f);
					InternetManager.sendEvents(track.getListOfEvents());
				}
			}
		});
		

		pauseWindow.addActor(resume);
		pauseWindow.addActor(quit);

		endWindow.addActor(upload);
		endWindow.addActor(quit2);

		LabelStyle labelStyle = new LabelStyle(GdxRessourcesGetter.getBigFont(), Color.WHITE);
		labelStyle.background = GdxRessourcesGetter.getSkin().getDrawable("empty");

		pauseText = new Label("Pause", labelStyle);
		pauseWindow.add(pauseText);
		
		endText = new Label("", labelStyle);
		endWindow.add(endText);

		r = new Random();
		
		LabelStyle labelStyle2 = new LabelStyle(labelStyle);
		labelStyle2.fontColor = Color.BLACK;
		chronoLabel = new Label("", labelStyle);

		playerLife = BASE_LIFE;
		bossLife = BASE_LIFE;

		cat = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin0"));
		rabbit = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin1"));
		turtle = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin2"));

		heart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("heart"));
		halfHeart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheart"));
		bossHead = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));

		boss = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));
		player = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));

		table = new Table();
		table.add(cat).pad(5);
		table.add(rabbit).pad(5);
		table.add(turtle).pad(5);

		holdingItem = false;
		canGrabItem = true;

		stage.addActor(table);

		task = new Rectangle(tasks.get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(0).y * GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		mouse = new Rectangle();
		catRectangle = new Rectangle();
		rabbitRectangle = new Rectangle();
		turtleRectangle = new Rectangle();

		bossHeadActor = new Actor();
		stage.addActor(bossHeadActor);

		bossHeadActor.setX(tasks.get(0).x * GameConstants.BLOCK_WIDTH);
		bossHeadActor.setY(GameConstants.DISPLAY_HEIGHT);
		bossHeadActor.addAction(Actions.moveTo(tasks.get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(0).y * GameConstants.BLOCK_HEIGHT, 4, Interpolation.bounceOut));

		currentBossState = BossState.FIRE;

		stage.addActor(chronoLabel);

		Gdx.input.setInputProcessor(stage);

	}

	private void parseTasks() {
		tasks = new Array<Vector3>();
		String [] tab = tasksAsString.split("/");

		for (String s : tab) {
			String [] coord = s.split(",");
			int y = Integer.valueOf(coord[1]);
			if(y < 2)
				y = y+2;
			tasks.add(new Vector3(Integer.valueOf(coord[0]), y, tasks.size));
		}

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void drawHearts()
	{
		for (int i = 0; i < playerLife / 2; i++) {
			batch.draw(heart, GameConstants.BLOCK_WIDTH * 0.4f * i + GameConstants.BLOCK_HEIGHT * 1.85f, GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 0.38f, GameConstants.BLOCK_HEIGHT * 0.38f);
		}		
		if(playerLife % 2 == 1)
		{
			batch.draw(halfHeart, GameConstants.BLOCK_WIDTH * 0.4f * (playerLife - 1) / 2  + GameConstants.BLOCK_HEIGHT * 1.85f, GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 0.38f, GameConstants.BLOCK_HEIGHT * 0.38f);
		}

		for (int i = bossLife / 2; i > 0; i--) {
			batch.draw(heart, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 0.4f * i - GameConstants.BLOCK_HEIGHT * 1.85f, GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 0.4f, GameConstants.BLOCK_HEIGHT * 0.4f);
		}
		if(bossLife % 2 == 1)
		{
			batch.draw(halfHeart, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 0.4f * (bossLife + 1) / 2- GameConstants.BLOCK_HEIGHT * 1.85f, GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 0.38f, GameConstants.BLOCK_HEIGHT * 0.38f);
		}
	}

	private void drawTask()
	{
		if(currentBossState.equals(BossState.WATER))
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone1"), task.x, task.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		else if(currentBossState.equals(BossState.FIRE))
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone2"), task.x, task.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		else if(currentBossState.equals(BossState.ROCK))
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone3"), task.x, task.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
	}

	private void handleMouse()
	{
		mouse.x = Gdx.input.getX();
		mouse.y = GameConstants.DISPLAY_HEIGHT - Gdx.input.getY();


		if((	mouse.overlaps(catRectangle) || 
				mouse.overlaps(rabbitRectangle) || 
				mouse.overlaps(turtleRectangle) || 
				(holdingItem && mouse.overlaps(task) && skillCorresponds())))
		{
			if(canGrabItem)
				hoverTime += Gdx.graphics.getDeltaTime();
		}
		else
		{
			hoverTime = 0;
			canGrabItem = true;
		}

		if(hoverTime > GameConstants.HOVER_TIME)
		{
			canGrabItem = false;

			if(mouse.overlaps(catRectangle))
			{
				currentSkill = Skill.FIRE;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(0).z);
			}

			else if(mouse.overlaps(rabbitRectangle))
			{
				currentSkill = Skill.PLANT;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(0).z);
			}

			else if(mouse.overlaps(turtleRectangle))
			{
				currentSkill = Skill.WATER;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(0).z);
			}
			else if(tasks.size > 1)
			{
				addEvent(EventManager.pointing_task_end, tasks.get(0).x, tasks.get(0).y, (int) tasks.get(0).z);
				addEvent(EventManager.task_success, tasks.get(0).x, tasks.get(0).y, (int) tasks.get(0).z);
				
				tasks.removeIndex(0);
				hoverTime = 0;
				standTime = 0;
				bossHeadActor.setX(tasks.get(0).x * GameConstants.BLOCK_WIDTH);
				bossHeadActor.setY(GameConstants.DISPLAY_HEIGHT);
				bossHeadActor.addAction(
						Actions.sequence(
								Actions.moveTo(tasks.get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(0).y * GameConstants.BLOCK_HEIGHT, 2, Interpolation.bounceOut),
								new Action() {

									@Override
									public boolean act(float delta) {
										standTime = 0;
										return true;
									}
								}));
				holdingItem = false;

				setCurrentBossState();

				bossLife--;

			}
			else
			{
				done = true;
			}
		}
	}

	private void setCurrentBossState() {
		int n = r.nextInt(3);

		switch (n) {
		case 0:
			currentBossState = BossState.FIRE;
			break;
		case 1:
			currentBossState = BossState.WATER;
			break;
		case 2:
			currentBossState = BossState.ROCK;
			break;
		}
	}

	private boolean skillCorresponds() {
		if(currentSkill.equals(Skill.FIRE) && currentBossState.equals(BossState.WATER))
			return true;
		else if(currentSkill.equals(Skill.WATER) && currentBossState.equals(BossState.FIRE))
			return true;
		else if(currentSkill.equals(Skill.PLANT) && currentBossState.equals(BossState.ROCK))
			return true;
		else
			return false;
	}

	private void drawHoverTime()
	{
		GdxRessourcesGetter.getShapeRenderer().begin(ShapeType.Filled);
		if(hoverTime > 0)
		{	
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.RED);
			GdxRessourcesGetter.getShapeRenderer().rect(mouse.x, mouse.y - GameConstants.BLOCK_HEIGHT, 70, 20);
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.BLUE);
			GdxRessourcesGetter.getShapeRenderer().rect(mouse.x, mouse.y - GameConstants.BLOCK_HEIGHT, 70*hoverTime / GameConstants.HOVER_TIME, 20);

		}

		if (standTime > -1)
		{
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.ORANGE);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.462f, GameConstants.DISPLAY_HEIGHT * 0.91f, 70, 20);
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.GREEN);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.462f, GameConstants.DISPLAY_HEIGHT * 0.91f, 70 - 70 * standTime / GameConstants.TIMEOUT, 20);
		}

		GdxRessourcesGetter.getShapeRenderer().end();
	}

	//	private void handleKeysPressed()
	//	{
	//		if(Gdx.input.isKeyPressed(Keys.LEFT) && elapsedTime > 0.3f)
	//		{
	//			playerLife--;
	//			elapsedTime = 0;
	//		}
	//
	//		if(Gdx.input.isKeyPressed(Keys.RIGHT) && elapsedTime > 0.3f)
	//		{
	//			bossLife--;
	//			elapsedTime = 0;
	//		}
	//	}

	private void drawHeldItem()
	{
		if(holdingItem)
		{
			switch (currentSkill) {
			case WATER:
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone2"), mouse.x - GameConstants.BLOCK_WIDTH * 0.75f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.75f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
				break;

			case FIRE:
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone1"), mouse.x - GameConstants.BLOCK_WIDTH * 0.75f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.75f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
				break;

			case PLANT:
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("bone3"), mouse.x - GameConstants.BLOCK_WIDTH * 0.75f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.75f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH *1.5f, GameConstants.BLOCK_HEIGHT *1.5f, 1, 1, 0);
				break;

			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addTrackingPoint(){
		elapsedTime += Gdx.graphics.getDeltaTime();

		if(Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0){
			if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
				OrderedMap<String, String> parameters = new OrderedMap<String, String>();
				parameters.put("x", "" + Gdx.input.getX());
				parameters.put("y", "" + (GameConstants.DISPLAY_HEIGHT - Gdx.input.getY()));
				parameters.put("z", "" + 0);
				parameters.put("id", ""+ (int) tasks.get(0).z);
				map.put(map.size(), new float[] {Gdx.input.getX(), (GameConstants.DISPLAY_HEIGHT - Gdx.input.getY())});
				EventManager.create(EventManager.player_cursor_event_type, parameters);
				System.out.println(parameters);
				elapsedTime = 0;
			}
		}
	}
	
	/**
	 * ajoute un event à la liste d'events
	 * appelé lorsque le patient réussi une tache de pointage
	 * @param pointingTaskEnd 
	 */
	private void addEvent(String eventType, float x, float y, int id){
		Gdx.app.log(RollingCat.LOG, "x : " + x(x));
		Gdx.app.log(RollingCat.LOG, "y : " + y(y));
		OrderedMap<String, String>parameters = new OrderedMap<String, String>();
		parameters.put("x", ""+ x(x));
		parameters.put("y", ""+ y(y));
		parameters.put("z", ""+0);
		parameters.put("id", ""+id);
		EventManager.create(eventType, parameters);
	}

}
