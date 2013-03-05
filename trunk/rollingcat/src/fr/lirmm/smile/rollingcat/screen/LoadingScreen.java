package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class LoadingScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private float wait;
	private ShapeRenderer sr;
	private Stage stage;
	private boolean building;
	

	public LoadingScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(wait > 1f & !building){
			building = true;
			this.stage = LevelBuilder.readLevel();
		}
		
		if(wait > 2)
			game.setScreen(new GameScreen(game, patient, stage));
		
		wait += delta;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		wait = 0;
		sr = new ShapeRenderer();
		building = false;
		InternetManager.getLevelOnServer(1);

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

}
