package fr.lirmm.smile.rollingcat;

import java.util.ArrayList;

import com.badlogic.gdx.Game;

import fr.lirmm.smile.rollingcat.screen.GameProgressionScreen;


public class RollingCat extends Game {
	
	public static final boolean DEBUG = false;
	public static final String LOG = "RollingCat";
	public static final String VERSION = "0.1.4";
	
	@Override
	public void create() {		
//		this.setScreen(new LoginScreen(this));
		ArrayList<String> gems = new ArrayList<String>();
		String r = "red_gem";
		String g = "green_gem";
		String p = "purple_gem";
		String y = "yellow_gem";
		gems.add(r);
		gems.add(r);
		gems.add(g);
		gems.add(p);
		gems.add(p);		
		gems.add(y);
		gems.add(r);

		this.setScreen(new GameProgressionScreen(
				null,null,
				gems,
				true			));

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
