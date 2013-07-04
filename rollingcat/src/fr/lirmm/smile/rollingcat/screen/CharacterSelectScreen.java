package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getGameSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.world.World;

public class CharacterSelectScreen implements Screen {

	private RollingCat game;
	private Table table;
	private Image cat, rabbit, turtle;
	private Image goCat, goRabbit, goTurtle;
	private Stage stage;
	private ShapeRenderer sr;
	private float loadingHeight, loadingWidth;

	public CharacterSelectScreen(RollingCat game){
		this.game = game;
		World.clearInstance();
		InternetManager.clearLevel();
		InternetManager.getNumberOfLevels(Patient.getInstance().getID());

	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		loadingWidth = goCat.getX() - cat.getX() - GameConstants.BLOCK_WIDTH * 4;

		stage.draw();
		stage.act();
		
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(cat.getX() + GameConstants.BLOCK_WIDTH * 3, cat.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.rect(rabbit.getX() + GameConstants.BLOCK_WIDTH * 3, rabbit.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.rect(turtle.getX() + GameConstants.BLOCK_WIDTH * 3, turtle.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.setColor(Color.GREEN);
		sr.rect(cat.getX() + GameConstants.BLOCK_WIDTH * 3, cat.getY() * 1.07f, loadingWidth * ((float)InternetManager.nblevels[0] / 20f), loadingHeight);
		sr.rect(rabbit.getX() + GameConstants.BLOCK_WIDTH * 3, rabbit.getY() * 1.07f, loadingWidth * ((float)InternetManager.nblevels[1] / 20f), loadingHeight);
		sr.rect(turtle.getX() + GameConstants.BLOCK_WIDTH * 3, turtle.getY() * 1.07f, loadingWidth * ((float)InternetManager.nblevels[2] / 20f), loadingHeight - 1);
		sr.end();
		
		sr.setColor(Color.WHITE);
		sr.begin(ShapeType.Line);
		sr.rect(cat.getX() + GameConstants.BLOCK_WIDTH * 3, cat.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.rect(rabbit.getX() + GameConstants.BLOCK_WIDTH * 3, rabbit.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.rect(turtle.getX() + GameConstants.BLOCK_WIDTH * 3, turtle.getY() * 1.07f, loadingWidth, loadingHeight);
		sr.end();
		
		if(Gdx.input.isKeyPressed(Keys.ENTER))
			show();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = getStage();
		sr = getShapeRenderer();
		
		table = new Table();
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		
		table.setBackground(getSkin().getDrawable("background_base"));
		
		cat = new Image(getGameSkin().getDrawable("skin0"));
		cat.setHeight(GameConstants.BLOCK_HEIGHT);
		rabbit = new Image(getGameSkin().getDrawable("skin1"));
		turtle = new Image(getGameSkin().getDrawable("skin2"));
		
		goCat = new Image(getGameSkin().getDrawable("shit"));
		goCat.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 0);
				RollingCat.skin = 1;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		
		goRabbit = new Image(getGameSkin().getDrawable("shit"));
		goRabbit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 1);
				RollingCat.skin = 2;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		
		goTurtle = new Image(getGameSkin().getDrawable("shit"));
		goTurtle.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 2);
				RollingCat.skin = 3;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		
		table.add(cat).padRight(GameConstants.DISPLAY_WIDTH * 0.65f).padBottom(GameConstants.BLOCK_HEIGHT);
		table.add(goCat).right().padBottom(GameConstants.BLOCK_HEIGHT);
		table.row();
		table.add(rabbit).left().padBottom(GameConstants.BLOCK_HEIGHT);
		table.add(goRabbit).right().padBottom(GameConstants.BLOCK_HEIGHT);
		table.row();
		table.add(turtle).left();
		table.add(goTurtle).right();
		
		loadingHeight = GameConstants.BLOCK_HEIGHT * 0.7f;
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void hide() {
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
		
	}

}
