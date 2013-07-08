package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getGameSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	private Image temple, tree, water;
	private Image goBoss;
	private Stage stage;
	private ShapeRenderer sr;
	private float loadingHeight, loadingWidth;
	private float originX;
	private boolean catReachedHisGoal, rabbitReachedHisGoal, turtleReachedHisGoal;

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

		loadingWidth = goBoss.getX() - temple.getX() - GameConstants.BLOCK_WIDTH * 4 - GameConstants.BLOCK_HEIGHT * 0.5f;

		if(cat.getActions().size == 0 && !catReachedHisGoal)
		{
			if(InternetManager.nblevels[0] == 20)
			{
				cat.addAction(Actions.sequence(
						Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[0] / 20), cat.getY(), (float) InternetManager.nblevels[0] / 6f),
						Actions.moveTo(originX + loadingWidth, tree.getY() - loadingHeight * 0.5f, 2),
						Actions.moveBy(GameConstants.BLOCK_HEIGHT * 1, 0, 2)));
				catReachedHisGoal = true;
			}
			else
			{
				cat.addAction(Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[0] / 20), cat.getY(), (float) InternetManager.nblevels[0] / 6f, Interpolation.pow2Out));
			}

		}

		if(rabbit.getActions().size == 0 && !rabbitReachedHisGoal)
		{
			if(InternetManager.nblevels[1] == 20)
			{
				rabbit.addAction(Actions.sequence(
						Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[1] / 20), rabbit.getY(), (float) InternetManager.nblevels[1] / 6f),
						Actions.moveBy(GameConstants.BLOCK_HEIGHT * 2, 0, 2)));
				rabbitReachedHisGoal = true;
			}
			else
			{
				rabbit.addAction(Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[1] / 20), rabbit.getY(), (float) InternetManager.nblevels[1] / 6f, Interpolation.pow2Out));
			}

		}

		if(turtle.getActions().size == 0 && !turtleReachedHisGoal)
		{
			if(InternetManager.nblevels[2] == 20)
			{
				turtle.addAction(Actions.sequence(
						Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[2] / 20), turtle.getY(), (float) InternetManager.nblevels[0] / 6f),
						Actions.moveTo(originX + loadingWidth, tree.getY() - loadingHeight * 0.5f, 4),
						Actions.moveBy(0, 0, 2)));
				turtleReachedHisGoal = true;
			}
			else
			{
				turtle.addAction(Actions.moveTo(originX + loadingWidth * ((float)InternetManager.nblevels[2] / 20), turtle.getY(), (float) InternetManager.nblevels[0] / 6f, Interpolation.pow2Out));
			}

		}

		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(temple.getX() + GameConstants.BLOCK_WIDTH * 2, temple.getY(), loadingWidth, loadingHeight);
		sr.rect(tree.getX() + GameConstants.BLOCK_WIDTH * 2, tree.getY(), loadingWidth + GameConstants.BLOCK_HEIGHT * 2, loadingHeight);
		sr.rect(water.getX() + GameConstants.BLOCK_WIDTH * 2, water.getY(), loadingWidth, loadingHeight);

		sr.setColor(Color.GREEN);
		sr.rect(temple.getX() + GameConstants.BLOCK_WIDTH * 2, temple.getY(), Math.min(cat.getX() - originX,loadingWidth), loadingHeight);
		sr.rect(tree.getX() + GameConstants.BLOCK_WIDTH * 2, tree.getY(), Math.min(rabbit.getX() - originX,loadingWidth), loadingHeight);
		sr.rect(water.getX() + GameConstants.BLOCK_WIDTH * 2, water.getY(), Math.min(turtle.getX() - originX,loadingWidth), loadingHeight);
		
		sr.setColor(Color.BLACK);
		sr.rect(originX + loadingWidth + GameConstants.DISPLAY_WIDTH * 0.015f, temple.getY() + loadingHeight, loadingHeight, -temple.getY() + water.getY() - loadingHeight);
		
		if(InternetManager.nblevels[0] + InternetManager.nblevels[1] + InternetManager.nblevels[2] == 60)
		{
			if(cat.getX() > originX + loadingWidth - GameConstants.DISPLAY_WIDTH * 0.008f)
			{
				sr.setColor(Color.GREEN);
				sr.rect(originX + loadingWidth + GameConstants.DISPLAY_WIDTH * 0.015f, temple.getY() + loadingHeight, loadingHeight, -temple.getY() + cat.getY() - loadingHeight * 0.2f);
			}
			
			if(rabbit.getX() > originX + loadingWidth - GameConstants.DISPLAY_WIDTH * 0.008f)
			{
				sr.setColor(Color.GREEN);
				sr.rect(originX + loadingWidth -  GameConstants.DISPLAY_WIDTH * 0.015f, tree.getY(), rabbit.getX() - (originX + loadingWidth - GameConstants.DISPLAY_HEIGHT * 0.04f), loadingHeight);
			}
			
			if(turtle.getX() > originX + loadingWidth - GameConstants.DISPLAY_WIDTH * 0.008f)
			{
				sr.setColor(Color.GREEN);
				sr.rect(originX + loadingWidth + GameConstants.DISPLAY_WIDTH * 0.015f, water.getY(), loadingHeight, -water.getY() + turtle.getY() + loadingHeight * 0.8f);
			}
		}

		sr.end();

		stage.draw();
		stage.act();

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

		catReachedHisGoal = false;
		turtleReachedHisGoal = false;
		rabbitReachedHisGoal = false;

		table = new Table();
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		table.setHeight(GameConstants.DISPLAY_HEIGHT);

		//		table.setBackground(getSkin().getDrawable("background_base"));

		cat = new Image(getGameSkin().getDrawable("skin0"));
		cat.setSize(GameConstants.BLOCK_HEIGHT * 1.5f, GameConstants.BLOCK_HEIGHT * 1.5f);
		rabbit = new Image(getGameSkin().getDrawable("skin1"));
		rabbit.setSize(GameConstants.BLOCK_HEIGHT * 1.5f, GameConstants.BLOCK_HEIGHT * 1.5f);
		turtle = new Image(getGameSkin().getDrawable("skin2"));
		turtle.setSize(GameConstants.BLOCK_HEIGHT * 1.5f, GameConstants.BLOCK_HEIGHT * 1.5f);

		temple = new Image(getGameSkin().getDrawable("shit"));
		temple.setHeight(GameConstants.BLOCK_HEIGHT);
		tree = new Image(getGameSkin().getDrawable("shit"));
		water = new Image(getGameSkin().getDrawable("shit"));
		goBoss = new Image(getGameSkin().getDrawable("shit"));
		
		goBoss.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(RollingCat.LOG, "no boss yet");
			}
		});

		temple.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 0);
				RollingCat.skin = 1;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		cat.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 0);
				RollingCat.skin = 1;
				game.setScreen(new LevelSelectScreen(game));
			}
		});

		tree.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 1);
				RollingCat.skin = 2;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		rabbit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 1);
				RollingCat.skin = 2;
				game.setScreen(new LevelSelectScreen(game));
			}
		});

		water.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 2);
				RollingCat.skin = 3;
				game.setScreen(new LevelSelectScreen(game));
			}
		});
		turtle.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.getWorld(Patient.getInstance().getID(), 2);
				RollingCat.skin = 3;
				game.setScreen(new LevelSelectScreen(game));
			}
		});

		table.add(temple).padRight(GameConstants.DISPLAY_WIDTH * 0.65f).padBottom(GameConstants.BLOCK_HEIGHT);
		table.row();
		table.add(tree).left().padBottom(GameConstants.BLOCK_HEIGHT);
		table.add(goBoss).right().padBottom(GameConstants.BLOCK_HEIGHT);
		table.row();
		table.add(water).left();

		loadingHeight = GameConstants.BLOCK_HEIGHT * 0.7f;

		stage.addActor(table);
		table.invalidate();
		table.validate();
		stage.act();

		originX = temple.getX() + GameConstants.BLOCK_WIDTH * 1.4f;

		cat.setX(originX);
		cat.setY(temple.getY() - loadingHeight * 0.5f);

		rabbit.setX(originX);
		rabbit.setY(tree.getY() - loadingHeight * 0.3f);

		turtle.setX(originX);
		turtle.setY(water.getY() - loadingHeight * 0.5f);

		stage.addActor(rabbit);
		stage.addActor(cat);
		stage.addActor(turtle);

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
