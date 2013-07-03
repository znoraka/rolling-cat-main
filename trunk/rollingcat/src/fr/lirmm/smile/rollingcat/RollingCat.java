package fr.lirmm.smile.rollingcat;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import fr.lirmm.smile.rollingcat.screen.LoginScreen;


public class RollingCat extends Game {

	public static final boolean DEBUG = false;
	public static final String LOG = "RollingCat";
	public static final String VERSION = "0.1.52";
	public static int lang = 0;
	public static int skin = 1;
	private static String rollingCat = "RollingCat";
	private static String jumpingRabbit = "JumpingRabbit";
	private static String swimmingTurtule = "SwimmingTurtle";
	
	@Override
	public void create() {	
		Localisation.initLanguage();
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
		super.resize(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	/**
	 * 
	 * @return le nom du jeu selectionné
	 */
	public static String getCurrentGameName(){
		Gdx.app.log(LOG, "skin : " + skin);
		switch (skin)
		{
		case 1: return rollingCat;
		case 2: return jumpingRabbit;
		case 3: return swimmingTurtule;
		default: return "";
		}
	}

}
