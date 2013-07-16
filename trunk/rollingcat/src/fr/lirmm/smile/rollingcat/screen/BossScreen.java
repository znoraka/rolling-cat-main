package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	private TextureRegion heart, halfHeart, halfHeartFlip, player, boss;
	private Stage stage;
	private SpriteBatch batch;
	private Table table;
	private float elapsedTime;
	
	public BossScreen(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		elapsedTime += delta;
		
		stage.act();
		stage.draw();
		
		batch.begin();
		drawHearts();
		batch.draw(player, 0, 0, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		batch.draw(boss, GameConstants.DISPLAY_WIDTH - GameConstants.BLOCK_WIDTH, 0, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);

		batch.end();
		
		table.setX(GameConstants.DISPLAY_WIDTH * 0.5f - table.getWidth() * 0.5f);
		table.setY(0);
		table.setWidth(GameConstants.BLOCK_WIDTH * 4.5f);
		table.setHeight(GameConstants.BLOCK_WIDTH * 1.5f);
		table.invalidate();
		
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

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		InternetManager.getPointingTasks();
		stage = GdxRessourcesGetter.getStage();
		batch = GdxRessourcesGetter.getSpriteBatch();
		
		playerLife = BASE_LIFE;
		bossLife = BASE_LIFE;
		
		
		cat = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin0"));
		rabbit = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin1"));
		turtle = new Image(GdxRessourcesGetter.getGameSkin().getDrawable("skin2"));
		
		heart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("heart"));
		halfHeart = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheart"));
		halfHeartFlip = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("halfheartflip"));
		
		boss = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));
		player = new TextureRegion(GdxRessourcesGetter.getGameSkin().getRegion("shit"));
		
		table = new Table();
		table.add(cat);
		table.add(rabbit);
		table.add(turtle);
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);

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

}
