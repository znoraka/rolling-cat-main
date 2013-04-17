package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class LoadingScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private Stage stage;
	private Texture texture;
	private SpriteBatch batch;
	private String levelAsString;
	private Level level;
	private List<String> listOfGem;
	private float elapsedTime;
	private ShapeRenderer sr;
	private boolean building;
	private int time;

	public LoadingScreen(RollingCat game, Patient patient, Level level, List<String> listOfGem){
		this.game = game;
		this.patient = patient;
		this.level = level;
		this.listOfGem = listOfGem;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(elapsedTime < time)
			elapsedTime += delta;

		batch.begin();
		batch.draw(texture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();
		
		sr.setColor(Color.GREEN);
		sr.begin(ShapeType.Filled);
		sr.rect(GameConstants.DISPLAY_WIDTH * 0.1f, GameConstants.DISPLAY_HEIGHT * 0.1f, GameConstants.DISPLAY_WIDTH * 0.8f * elapsedTime / time, GameConstants.BLOCK_HEIGHT);
		sr.end();
		
		sr.setColor(Color.BLACK);
		sr.begin(ShapeType.Line);
		sr.rect(GameConstants.DISPLAY_WIDTH * 0.1f, GameConstants.DISPLAY_HEIGHT * 0.1f, GameConstants.DISPLAY_WIDTH * 0.8f, GameConstants.BLOCK_HEIGHT);
		sr.end();
		
		levelAsString = (level.getContent() == null)?InternetManager.getLevel():level.getContent();

		if(levelAsString != null & !building)
		{
			this.stage = LevelBuilder.build(levelAsString);
			level.setContent(levelAsString);
			building = true;
		}
		
		if(levelAsString != null & elapsedTime > time)
			game.setScreen(new GameScreen(game, patient, stage, level, listOfGem));
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		sr = getShapeRenderer();
		
		if(level.getContent() == null)
			InternetManager.fetchLevel(patient, level.getId()); 
		else
			levelAsString = level.getContent();
		texture = new Texture("data/load.png");
		elapsedTime = 0;
		building = false;
		time = 5;
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
		Gdx.app.log(RollingCat.LOG, "disposing...");
		texture.dispose();
	}

}
