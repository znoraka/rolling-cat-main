package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;

import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	public LoadingScreen(RollingCat game, Patient patient, Level level, List<String> listOfGem){
		this.game = game;
		this.patient = patient;
		this.level = level;
		this.listOfGem = listOfGem;
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(texture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();
		
		levelAsString = (level.getContent() == null)?InternetManager.getLevel():level.getContent();

		if(levelAsString != null)
		{
			this.stage = LevelBuilder.build(levelAsString);
			level.setContent(levelAsString);
			game.setScreen(new GameScreen(game, patient, stage, level, listOfGem));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if(level.getContent() == null)
			InternetManager.fetchLevel(patient.getID(), level.getId()); 
		else
			levelAsString = level.getContent();
		texture = new Texture("data/loading.png");
		batch = getSpriteBatch();
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
