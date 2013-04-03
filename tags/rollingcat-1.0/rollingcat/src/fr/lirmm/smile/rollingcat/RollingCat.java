package fr.lirmm.smile.rollingcat;

import com.badlogic.gdx.Game;

import fr.lirmm.smile.rollingcat.screen.LoginScreen;


public class RollingCat extends Game {
	
	public static final boolean DEBUG = false;
	public static final String LOG = "RollingCat";
	public static final String VERSION = "0.1.51a";
	
	@Override
	public void create() {		
		this.setScreen(new LoginScreen(this));

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
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
