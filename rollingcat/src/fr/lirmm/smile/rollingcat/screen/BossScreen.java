package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._loseboss;
import static fr.lirmm.smile.rollingcat.Localisation._quit;
import static fr.lirmm.smile.rollingcat.Localisation._resume;
import static fr.lirmm.smile.rollingcat.Localisation._upload;
import static fr.lirmm.smile.rollingcat.Localisation._winboss;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.x;
import static fr.lirmm.smile.rollingcat.utils.CoordinateConverter.y;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getDogAtlas;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class BossScreen implements Screen {

	private static final int BASE_LIFE = 20;

	private enum Skill
	{
		SLASH,
		WATER,
		PLANT
	};

	private enum BossState
	{
		MEAT,
		FIRE,
		ROCK
	}

	private RollingCat game;
	private int bossLife, playerLife;
	private Skill currentSkill;
	private BossState currentBossState, oldBossState;
	private Array<Array<Vector3>> tasks;
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
	private Texture gui, background;
	private Image slash, water, leaf, leaf2;
	private Vector2[] lighting;
	private float animationTimer, frame;
	private int animationIndex, bossFrameIndex, modifier;
	private Animation deadStone, deadFire, deadSnake;
	private boolean dead;
	private Vector2 lastPosition, angle;
	private Texture corps;
	private int difficulty;
	//	private SkeletonData skeletonData;
	//	private Skeleton skeleton;
	//	private fr.lirmm.smile.rollingcat.spine.Animation bossAnim;
	//	private Bone root;

	public BossScreen(RollingCat game)
	{
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		water.setOrigin(water.getWidth() * 0.5f, water.getHeight() * 0.5f);

		animationTimer += delta;
		if(dead)
			frame += delta;
		else
			frame = 0;

		if(animationTimer > 0.1f)
		{
			animationTimer = 0;
			animationIndex = (animationIndex + 1) % 3;
			bossFrameIndex += modifier;
		}

		batch.begin();
		batch.draw(background, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.draw(gui, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();
		drawStandTime();

		keysDelay += delta;
		s = (int) chrono;

		if(s == 60)
		{
			chrono = 0;
			m++;
		}

		chronoLabel.setText(m+":"+ ((s < 10)?("0" + s):s));

		chronoLabel.setX(GameConstants.DISPLAY_WIDTH * 0.485f - chronoLabel.getWidth() * 0.5f);
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
		{
			drawBoss();
			drawTask();
		}
		drawHearts();
		batch.draw(player, GameConstants.BLOCK_WIDTH * 0.02f, GameConstants.BLOCK_HEIGHT * 0.08f, GameConstants.BLOCK_WIDTH * 1.3f, GameConstants.BLOCK_WIDTH * 1.3f);
		batch.draw(boss, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 1.55f, GameConstants.BLOCK_HEIGHT * 0.0f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);
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
		if(!paused & !done)
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

		if((bossLife == 0 || playerLife == 0) & bossHeadActor.getActions().size == 0)
		{
			bossHeadActor.addAction(
					Actions.sequence(
							Actions.delay(2),
							new Action() {

								@Override
								public boolean act(float delta) {
									done = true;
									endWindow.setVisible(true);
									if(bossLife < playerLife)
										endText.setText(localisation(_winboss));
									else
										endText.setText(localisation(_loseboss));
									return true;
								}
							}));

		}

		if(Gdx.input.isKeyPressed(Keys.SPACE))
			bossLife = 1;

		slash.act(delta);
		water.act(delta);
		leaf.act(delta);
		leaf2.act(delta);
		batch.begin();
		if(slash.isVisible())
			slash.draw(batch, 1);
		if(water.isVisible())
			water.draw(batch, 1);
		if(leaf.isVisible())
			leaf.draw(batch, 1);
		if(leaf2.isVisible())
			leaf2.draw(batch, 1);
		batch.end();
	}

	private void sendBossBack() {
		if(standTime > GameConstants.TIMEOUT)
		{
			if(difficulty > 0)
			{
				difficulty--;
			}
			standTime = 0;
			playerLife--;
			holdingItem = false;
			addEvent(EventManager.pointing_task_end, tasks.get(difficulty).get(0).x, tasks.get(difficulty).get(0).y, (int) tasks.get(difficulty).get(0).z);
			addEvent(EventManager.task_fail, tasks.get(difficulty).get(0).x, tasks.get(difficulty).get(0).y, (int) tasks.get(difficulty).get(0).z);
			bossHeadActor.addAction(
					Actions.sequence(
							Actions.moveTo(bossHeadActor.getX(), GameConstants.DISPLAY_HEIGHT, 2, Interpolation.swingIn),
							new Action() {

								@Override
								public boolean act(float delta) {
									Vector3 v = tasks.get(difficulty).get(0);
									tasks.get(difficulty).removeIndex(0);
									tasks.get(difficulty).add(v);
									bossHeadActor.setX(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH);
									setCurrentBossState();
									bossHeadActor.addAction(
											Actions.sequence(
													Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT, 2, Interpolation.bounceOut),
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
		background = new Texture("data/backgroundboss.png");

		map = new HashMap<Integer, float[]>();

		lastPosition = new Vector2();

		lighting = new Vector2 [20];

		oldBossState = BossState.FIRE;

		modifier = 1;

		//		TextureAtlas atlas = new TextureAtlas("data/boss/serpent.atlas");
		//
		//		SkeletonBinary binary = new SkeletonBinary(atlas);
		//		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/boss/serpent.skel"));
		//		bossAnim = binary.readAnimation(Gdx.files.internal("data/boss/serpent.anim"), skeletonData);
		//		
		//		skeleton.setToBindPose();
		//		
		//		root = skeleton.getRootBone();
		//		root.setScaleX(0.35f * GameConstants.SCALE);
		//		root.setScaleY(0.35f * GameConstants.SCALE);

		deadStone = new Animation(0.08f, GdxRessourcesGetter.getRegions("snake_stone_broken"));
		deadFire = new Animation(0.08f, GdxRessourcesGetter.getRegions("snake_fire_dead"));
		deadSnake = new Animation(0.08f, GdxRessourcesGetter.getRegions("snake_dead"));

		corps = new Texture("data/serpent-anim.gif");

		for (int i = 0; i < lighting.length; i++) {
			lighting[i] = new Vector2();
		}

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
					track = new Track(map, Track.BOSS, m * 60 + s);
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

		slash = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("slash"));
		water = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("water"));
		leaf =  new Image(GdxRessourcesGetter.getGameSkin().getDrawable("leaf"));
		leaf2 =  new Image(GdxRessourcesGetter.getGameSkin().getDrawable("leaf"));

		heart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("heart"));
		halfHeart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheart"));
		bossHead = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));

		boss = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("snake_fire0"));
		player = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("turtlehead"));

		table = new Table();
		table.add(cat).pad(5);
		table.add(rabbit).pad(5);
		table.add(turtle).pad(5);

		holdingItem = false;
		canGrabItem = true;

		stage.addActor(table);
		slash.setVisible(false);
		water.setVisible(false);
		leaf.setVisible(false);
		leaf2.setVisible(false);
		water.setHeight(GameConstants.BLOCK_HEIGHT);
		water.setWidth(GameConstants.BLOCK_WIDTH);
		slash.setHeight(GameConstants.BLOCK_HEIGHT * 2f);
		slash.setWidth(GameConstants.BLOCK_WIDTH * 2f);
		leaf.setHeight(GameConstants.BLOCK_HEIGHT);
		leaf.setWidth(GameConstants.BLOCK_WIDTH);
		leaf.setOrigin(leaf.getWidth() * 0.5f, leaf.getHeight() * 0.5f);
		leaf2.setHeight(GameConstants.BLOCK_HEIGHT);
		leaf2.setWidth(GameConstants.BLOCK_WIDTH);
		leaf2.setOrigin(leaf2.getWidth() * 0.5f, leaf2.getHeight() * 0.5f);

		task = new Rectangle(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		mouse = new Rectangle();
		catRectangle = new Rectangle();
		rabbitRectangle = new Rectangle();
		turtleRectangle = new Rectangle();

		bossHeadActor = new Actor();
		stage.addActor(bossHeadActor);

		bossHeadActor.setX(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH);
		bossHeadActor.setY(GameConstants.DISPLAY_HEIGHT);
		bossHeadActor.addAction(Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT, 4, Interpolation.bounceOut));

		currentBossState = BossState.FIRE;

		stage.addActor(chronoLabel);

		Gdx.input.setInputProcessor(stage);

	}

	private void parseTasks() {
		tasks = new Array<Array<Vector3>>();
		String [] tabOfTabs = tasksAsString.split("#");
		String [] tab;
		int difficulty = 0;
		
		for (int i = 0; i < 5; i++) {
			tasks.add(new Array<Vector3>());
		}

		for (String string : tabOfTabs) {
			tab = string.split("/");
			for (String s : tab) {
				String [] coord = s.split(",");
				int y = Integer.valueOf(coord[1]);
				if(y < 2)
					y = y+2;
				tasks.get(difficulty).add(new Vector3(Integer.valueOf(coord[0]), y, tasks.get(difficulty).size));
			}
			difficulty++;
		}

	}
	
//	private void parseTasks() {
//		tasks = new Array<Vector3>();
//		String [] tabOfTabs = tasksAsString.split("#");
//		String [] tab;
//
//		for (String string : tabOfTabs) {
//			tab = string.split("/");
//			for (String s : tab) {
//				String [] coord = s.split(",");
//				int y = Integer.valueOf(coord[1]);
//				if(y < 2)
//					y = y+2;
//				tasks.add(new Vector3(Integer.valueOf(coord[0]), y, tasks.size));
//			}
//		}
//
//	}

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
		if(currentBossState.equals(BossState.MEAT))
		{
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("snake"), task.x - GameConstants.BLOCK_WIDTH * 0.5f, task.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);
		}
		else if(currentBossState.equals(BossState.FIRE))
		{
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("snake_fire" + animationIndex), task.x - GameConstants.BLOCK_WIDTH * 0.5f, task.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);
		}
		else if(currentBossState.equals(BossState.ROCK))
		{
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("snake_stone"), task.x - GameConstants.BLOCK_WIDTH * 0.5f, task.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);		}

		if(oldBossState.equals(BossState.MEAT) && dead)
		{
			batch.draw(deadSnake.getKeyFrame(frame, false), lastPosition.x - GameConstants.BLOCK_WIDTH * 0.5f, lastPosition.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);
		}
		else if(oldBossState.equals(BossState.FIRE) && dead)
		{
			batch.draw(deadFire.getKeyFrame(frame, false), lastPosition.x - GameConstants.BLOCK_WIDTH * 0.5f, lastPosition.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);

		}
		else if(oldBossState.equals(BossState.ROCK) && dead)
		{
			batch.draw(deadStone.getKeyFrame(frame, false), lastPosition.x - GameConstants.BLOCK_WIDTH * 0.5f, lastPosition.y - GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 2f, GameConstants.BLOCK_HEIGHT * 2f);
		}
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
				currentSkill = Skill.SLASH;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(difficulty).get(0).z);
			}

			else if(mouse.overlaps(rabbitRectangle))
			{
				currentSkill = Skill.PLANT;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(difficulty).get(0).z);
			}

			else if(mouse.overlaps(turtleRectangle))
			{
				currentSkill = Skill.WATER;
				holdingItem = true;
				hoverTime = 0;
				addEvent(EventManager.pointing_task_start, catRectangle.getX(), catRectangle.getY(), (int) tasks.get(difficulty).get(0).z);
			}
			else if(tasks.get(difficulty).size > 1)
			{
				lastPosition.x = tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH;
				lastPosition.y = tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT;

				switch (currentSkill) {
				case SLASH: playSlashAnimation(); break;
				case WATER: playWaterAnimation(); break;
				case PLANT: playLeafAnimation(); break;

				default:
					break;
				}

				addEvent(EventManager.pointing_task_end, tasks.get(difficulty).get(0).x, tasks.get(difficulty).get(0).y, (int) tasks.get(difficulty).get(0).z);
				addEvent(EventManager.task_success, tasks.get(difficulty).get(0).x, tasks.get(difficulty).get(0).y, (int) tasks.get(difficulty).get(0).z);

				bossLife--;

				tasks.get(difficulty).removeIndex(0);
				
				if(difficulty < 4)
				{
					difficulty++;
				}

				hoverTime = 0;
				standTime = 0;
				if(!((bossLife == 0 || playerLife == 0)))
				{
					bossHeadActor.addAction(
							Actions.sequence(
									Actions.delay(0.8f),
									Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH, GameConstants.DISPLAY_HEIGHT),
									new Action() {

										@Override
										public boolean act(float delta) {
											setCurrentBossState();
											return true;
										}
									},
									Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT, 2, Interpolation.bounceOut),
									new Action() {

										@Override
										public boolean act(float delta) {
											standTime = 0;
											dead = false;
											return true;
										}
									}));
				}
				else
				{
					bossHeadActor.addAction(
							Actions.sequence(
									Actions.delay(0.8f),
									Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH * 2, GameConstants.DISPLAY_HEIGHT * 2)));
				}
				holdingItem = false;
			}
			else
			{
				bossLife = 0;
			}
		}
	}

	private void setCurrentBossState() {
		oldBossState = currentBossState;

		int n = r.nextInt(3);

		switch (n) {
		case 0:
			currentBossState = BossState.FIRE;
			boss.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("snake_fire0"));
			player.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("turtlehead"));
			break;
		case 1:
			currentBossState = BossState.MEAT;
			boss.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("snake"));
			player.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("cathead"));
			break;
		case 2:
			currentBossState = BossState.ROCK;
			boss.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("snake_stone"));
			player.setRegion(GdxRessourcesGetter.getGameSkin().getRegion("rabbithead"));
			break;
		}

	}

	private boolean skillCorresponds() {
		if(currentSkill.equals(Skill.SLASH) && currentBossState.equals(BossState.MEAT))
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
			GdxRessourcesGetter.getShapeRenderer().rect(mouse.x, Math.max(mouse.y - GameConstants.BLOCK_HEIGHT, 0) , 70, 20);
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.BLUE);
			GdxRessourcesGetter.getShapeRenderer().rect(mouse.x, Math.max(mouse.y - GameConstants.BLOCK_HEIGHT, 0), 70*hoverTime / GameConstants.HOVER_TIME, 20);

		}

		GdxRessourcesGetter.getShapeRenderer().end();
	}

	private void drawStandTime()
	{
		GdxRessourcesGetter.getShapeRenderer().begin(ShapeType.Filled);

		if (standTime > -1)
		{
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.ORANGE);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.46f, GameConstants.DISPLAY_HEIGHT * 0.91f, GameConstants.DISPLAY_WIDTH * 0.081f, GameConstants.DISPLAY_HEIGHT * 0.03f);
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.GREEN);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.46f, GameConstants.DISPLAY_HEIGHT * 0.91f, GameConstants.DISPLAY_WIDTH * 0.081f - GameConstants.DISPLAY_WIDTH * 0.081f * standTime / GameConstants.TIMEOUT, GameConstants.DISPLAY_HEIGHT * 0.03f);
			//			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.462f, GameConstants.DISPLAY_HEIGHT * 0.91f, 70 - 70 * standTime / GameConstants.TIMEOUT, 20);
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
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("water"), mouse.x - GameConstants.BLOCK_WIDTH * 0.5f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.5f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
				break;

			case SLASH:
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("paw"), mouse.x - GameConstants.BLOCK_WIDTH * 0.5f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.5f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
				break;

			case PLANT:
				batch.draw(GdxRessourcesGetter.getAtlas().findRegion("leaf"), mouse.x - GameConstants.BLOCK_WIDTH * 0.5f, mouse.y - GameConstants.BLOCK_HEIGHT * 0.5f, mouse.x, mouse.y, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT, 1, 1, 0);
				break;

			}
		}
	}

	public void addTrackingPoint(){
		elapsedTime += Gdx.graphics.getDeltaTime();

		if(Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0){
			if(elapsedTime * 1000 > GameConstants.DELTATRACKINGMILLISEC){
				OrderedMap<String, String> parameters = new OrderedMap<String, String>();
				parameters.put("x", "" + Gdx.input.getX());
				parameters.put("y", "" + (GameConstants.DISPLAY_HEIGHT - Gdx.input.getY()));
				parameters.put("z", "" + 0);
				parameters.put("id", ""+ (int) tasks.get(difficulty).get(0).z);
				map.put(map.size(), new float[] {Gdx.input.getX(), (GameConstants.DISPLAY_HEIGHT - Gdx.input.getY())});
				EventManager.create(EventManager.player_cursor_event_type, parameters);
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

	private void playSlashAnimation()
	{
		slash.setX(table.getX() + cat.getX());
		slash.setY(table.getY() + cat.getY());
		slash.addAction(
				Actions.sequence(
						Actions.moveTo(tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH - GameConstants.BLOCK_WIDTH * 0.5f, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT - GameConstants.BLOCK_HEIGHT * 0.5f), 
						Actions.show(),
						Actions.delay(0.8f),
						new Action() {

							@Override
							public boolean act(float delta) {
								dead = true;
								return true;
							}},
							Actions.hide()
						));
	}

	private void playWaterAnimation()
	{
		water.setX(table.getX() + turtle.getX());
		water.setY(table.getY() + turtle.getY());

		float b = tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH * 1.5f - (table.getX() + turtle.getX() + turtle.getWidth() * 0.5f);
		float a = tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT + table.getHeight();

		water.addAction(
				Actions.sequence(
						Actions.rotateTo((b > 0)?((float) Math.toDegrees(Math.atan(a/b) + 90)):((float) Math.toDegrees(Math.atan(-a/b) + 90))),
						Actions.show(),
						Actions.moveTo(lastPosition.x, lastPosition.y, 0.3f, Interpolation.pow2Out),
						Actions.hide(),
						Actions.rotateTo(0),
						Actions.moveTo(tasks.get(difficulty).get(0).x * (GameConstants.BLOCK_WIDTH), tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT + GameConstants.BLOCK_HEIGHT * 2), 
						Actions.show(),
						Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT * 2, 0.4f),
						Actions.delay(0.1f),
						new Action() {

							@Override
							public boolean act(float delta) {
								dead = true;
								return true;
							}},
							Actions.hide()
						));
	}

	private void playLeafAnimation()
	{
		leaf.setX(table.getX() + rabbit.getX());
		leaf.setY(table.getY() + rabbit.getY());

		float b = tasks.get(difficulty).get(0).x * GameConstants.BLOCK_WIDTH * 1.5f - GameConstants.DISPLAY_WIDTH * 0.5f;
		float a = tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT + table.getHeight();

		leaf.addAction(
				Actions.sequence(
						Actions.rotateTo((b > 0)?((float) Math.toDegrees(Math.atan(a/b)) - 90):((float) Math.toDegrees(Math.atan(a/b)) - 270)),
						Actions.show(),
						Actions.moveTo(lastPosition.x, lastPosition.y, 0.3f, Interpolation.pow2Out),
						Actions.hide(),
						Actions.rotateTo(-120),
						Actions.moveTo(tasks.get(difficulty).get(0).x * (GameConstants.BLOCK_WIDTH) + GameConstants.BLOCK_WIDTH * 0.5f, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT + GameConstants.BLOCK_HEIGHT * 2), 
						Actions.show(),
						Actions.parallel(
								Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT * 2, 0.3f, Interpolation.pow2Out),
								Actions.rotateBy(-90, 0.3f), Actions.moveBy(-GameConstants.BLOCK_WIDTH * 0.5f, 0, 0.3f, Interpolation.pow2In)) ,
								Actions.delay(0.2f), Actions.hide()
						));


		leaf2.setX(table.getX() + rabbit.getX());
		leaf2.setY(table.getY() + rabbit.getY());
		leaf2.setRotation(-120);
		leaf2.addAction(
				Actions.sequence(
						Actions.delay(0.5f),
						Actions.moveTo(tasks.get(difficulty).get(0).x * (GameConstants.BLOCK_WIDTH) - GameConstants.BLOCK_WIDTH * 0.5f, tasks.get(difficulty).get(0).y * GameConstants.BLOCK_HEIGHT + GameConstants.BLOCK_HEIGHT * 2), 
						Actions.show(),
						Actions.parallel(
								Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT * 2, 0.3f, Interpolation.pow2Out),
								Actions.rotateBy(10, 0.3f), Actions.moveBy(GameConstants.BLOCK_WIDTH * 0.5f, 0, 0.3f, Interpolation.pow2In)),
								Actions.hide(),
								Actions.delay(0.1f),
								new Action() {

							@Override
							public boolean act(float delta) {
								dead = true;
								return true;
							}
						}));

	}

	private void drawBoss()
	{
		try {
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("move" + bossFrameIndex), bossHeadActor.getX() - GameConstants.BLOCK_WIDTH * 1.15f, bossHeadActor.getY() + GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 3, GameConstants.DISPLAY_HEIGHT);
		} catch (Exception e) {
			batch.draw(GdxRessourcesGetter.getAtlas().findRegion("move0"), bossHeadActor.getX() - GameConstants.BLOCK_WIDTH * 1.15f, bossHeadActor.getY() + GameConstants.BLOCK_HEIGHT * 0.5f, GameConstants.BLOCK_WIDTH * 3, GameConstants.DISPLAY_HEIGHT);
			modifier *= -1;
			bossFrameIndex += modifier;
		}

	}
}
