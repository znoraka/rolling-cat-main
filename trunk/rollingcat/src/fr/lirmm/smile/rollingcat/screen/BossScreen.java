package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
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

	private Game game;
	private int bossLife, playerLife;
	private Skill currentSkill;
	private BossState currentBossState;
	private Array<Vector2> tasks;
	private Image cat, rabbit, turtle;
	private TextureRegion heart, halfHeart, halfHeartFlip, player, boss, bossHead;
	private Stage stage;
	private SpriteBatch batch;
	private Table table;
	private float elapsedTime;
	private Rectangle task, mouse, catRectangle, rabbitRectangle, turtleRectangle;
	private float hoverTime, standTime;
	private boolean holdingItem, canGrabItem;
	private Actor bossHeadActor;
	private Random r;
	private float chrono;
	private int s, m;
	private Label chronoLabel;
	
	public BossScreen(Game game)
	{
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		chrono += delta;
		s = (int) chrono;
		
		if(s == 60)
		{
			chrono = 0;
			m++;
		}
		
		chronoLabel.setText(m+":"+ ((s < 10)?("0" + s):s));
		
		chronoLabel.setX(GameConstants.DISPLAY_WIDTH * 0.5f - chronoLabel.getWidth() * 0.5f);
		chronoLabel.setY(GameConstants.DISPLAY_HEIGHT - GameConstants.BLOCK_HEIGHT * 0.5f);

		if(bossHeadActor.getActions().size == 0)
			standTime += delta;
		elapsedTime += delta;

		task.setX(bossHeadActor.getX());
		task.setY(bossHeadActor.getY());

		catRectangle.set(cat.getX() + table.getX(), cat.getY(), cat.getWidth(), cat.getHeight());
		turtleRectangle.set(turtle.getX() + table.getX(), turtle.getY(), turtle.getWidth(), turtle.getHeight());
		rabbitRectangle.set(rabbit.getX() + table.getX(), rabbit.getY(), rabbit.getWidth(), rabbit.getHeight());

		sendBossBack();

		stage.act();
		stage.draw();

		batch.begin();
		drawTask();
		drawHearts();
		batch.draw(player, 0, 0, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		batch.draw(boss, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH, 0, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		drawHeldItem();
		batch.end();

		handleMouse();
		drawHoverTime();
		handleKeysPressed();

		table.setX(GameConstants.DISPLAY_WIDTH * 0.5f - table.getWidth() * 0.5f);
		table.setY(0);
		table.setWidth(GameConstants.BLOCK_WIDTH * 4.5f);
		table.setHeight(GameConstants.BLOCK_WIDTH * 1.5f);
		table.invalidate();
	}

	private void sendBossBack() {
		if(standTime > GameConstants.TIMEOUT)
		{
			standTime = 0;
			playerLife--;
			bossHeadActor.addAction(
					Actions.sequence(
							Actions.moveTo(bossHeadActor.getX(), GameConstants.DISPLAY_HEIGHT, 2, Interpolation.swingIn),
							new Action() {
								
								@Override
								public boolean act(float delta) {
									Vector2 v = tasks.get(0);
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
		InternetManager.getPointingTasks();
		stage = GdxRessourcesGetter.getStage();
		batch = GdxRessourcesGetter.getSpriteBatch();

		r = new Random();
		
		LabelStyle labelStyle = new LabelStyle(GdxRessourcesGetter.getBigFont(), Color.BLACK);
		labelStyle.background = getSkin().getDrawable("empty");

		chronoLabel = new Label("", labelStyle);

		tasks = new Array<Vector2>();

		for (int i = 0; i < 20; i++) {
			tasks.add(new Vector2(1 + r.nextInt(GameConstants.COLS - 1), 3 + r.nextInt(GameConstants.ROWS - 5)));
		}

		playerLife = BASE_LIFE;
		bossLife = BASE_LIFE;

		cat = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin0"));
		rabbit = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin1"));
		turtle = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin2"));

		heart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("heart"));
		halfHeart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheart"));
		halfHeartFlip = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheartflip"));
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

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		int [] n = {0, 0, 0};
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
			batch.draw(heart, GameConstants.BLOCK_WIDTH * 0.4f * i + GameConstants.BLOCK_HEIGHT * 1.5f, 0, GameConstants.BLOCK_WIDTH * 0.4f, GameConstants.BLOCK_HEIGHT * 0.4f);
		}		
		if(playerLife % 2 == 1)
		{
			batch.draw(halfHeart, GameConstants.BLOCK_WIDTH * 0.4f * (playerLife - 1) / 2  + GameConstants.BLOCK_HEIGHT * 1.5f, 0, GameConstants.BLOCK_WIDTH * 0.4f, GameConstants.BLOCK_HEIGHT * 0.4f);
		}

		for (int i = bossLife / 2; i > 0; i--) {
			batch.draw(heart, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 0.4f * i - GameConstants.BLOCK_HEIGHT * 1.5f, 0, GameConstants.BLOCK_WIDTH * 0.4f, GameConstants.BLOCK_HEIGHT * 0.4f);
		}
		if(bossLife % 2 == 1)
		{
			batch.draw(halfHeartFlip, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH * 0.4f * (bossLife + 1) / 2- GameConstants.BLOCK_HEIGHT * 1.5f, 0, GameConstants.BLOCK_WIDTH * 0.4f, GameConstants.BLOCK_HEIGHT * 0.4f);
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
			}

			else if(mouse.overlaps(rabbitRectangle))
			{
				currentSkill = Skill.PLANT;
				holdingItem = true;
				hoverTime = 0;
			}

			else if(mouse.overlaps(turtleRectangle))
			{
				currentSkill = Skill.WATER;
				holdingItem = true;
				hoverTime = 0;
			}
			else
			{
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
		
		if (standTime > 0)
		{
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.ORANGE);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.5f - 16, GameConstants.DISPLAY_HEIGHT - GameConstants.BLOCK_HEIGHT * 1.3f, 70, 20);
			GdxRessourcesGetter.getShapeRenderer().setColor(Color.GREEN);
			GdxRessourcesGetter.getShapeRenderer().rect(GameConstants.DISPLAY_WIDTH * 0.5f - 16, GameConstants.DISPLAY_HEIGHT - GameConstants.BLOCK_HEIGHT * 1.3f, 70 - 70 * standTime / GameConstants.TIMEOUT, 20);
		}
		
		GdxRessourcesGetter.getShapeRenderer().end();
	}

	private void handleKeysPressed()
	{
		if(Gdx.input.isKeyPressed(Keys.LEFT) && elapsedTime > 0.3f)
		{
			playerLife--;
			elapsedTime = 0;
		}

		if(Gdx.input.isKeyPressed(Keys.RIGHT) && elapsedTime > 0.3f)
		{
			bossLife--;
			elapsedTime = 0;
		}
	}

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

}
