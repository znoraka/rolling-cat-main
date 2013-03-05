package fr.lirmm.smile.rollingcat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

import fr.lirmm.smile.rollingcat.screen.LoginScreen;


public class RollingCat extends Game {
	
	public static final boolean DEBUG = false;
	public static final String LOG = "RollingCat";
	FPSLogger fps;
	
	@Override
	public void create() {		
		this.setScreen(new LoginScreen(this));
		fps = new FPSLogger();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
//		fps.log();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
