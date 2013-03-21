package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;

public class LoadingScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private Stage stage;
	private Texture texture;
	private SpriteBatch batch;
	private String level;
	private int levelID;

	public LoadingScreen(RollingCat game, Patient patient, int levelID){
		this.game = game;
		this.patient = patient;
		this.levelID = levelID;
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(texture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();
		
		level = InternetManager.getLevel();

		if(level != null)
		{
			this.stage = LevelBuilder.build(level);
			game.setScreen(new GameScreen(game, patient, stage));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		InternetManager.fetchLevel(patient.getID(), 0);
        InternetManager.newGameSession(Track.GAME);
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
