package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.PatientsManager;
import fr.lirmm.smile.rollingcat.model.doctor.Doctor;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;

public class PatientSelectLoadingScreen implements Screen {

	private RollingCat game;
	private Texture texture;
	private SpriteBatch batch;
	private String patients;
	private Doctor doctor;

	public PatientSelectLoadingScreen(RollingCat game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(texture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();
		
		patients = InternetManager.getPatients();

		if(patients != null)
		{
			doctor.setPatients(PatientsManager.getPatientsFromJson(patients));
			game.setScreen(new PatientSelectScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		InternetManager.getGameId();
		InternetManager.retrievePatients();
		Gdx.app.log(RollingCat.LOG, "loading patient select screen");
		texture = new Texture("data/loading.png");
		batch = getSpriteBatch();
		doctor = Doctor.getDoctor();
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
