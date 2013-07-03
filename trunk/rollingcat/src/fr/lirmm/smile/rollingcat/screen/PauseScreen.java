package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class PauseScreen implements Screen{
	private ScreenPausable gameScreen;
	private RollingCat game;
	private Stage stage;
	private TextButton resume, quit;
	private Skin skin;
	private BitmapFont font;
	private Patient patient;
	private long beginPause;
	
	public PauseScreen(RollingCat game, ScreenPausable gameScreen, Patient patient){
		this.gameScreen = gameScreen;
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		stage.draw();
		stage.act(delta);
		Gdx.app.log(RollingCat.LOG, "elapsed time"+(System.currentTimeMillis() - beginPause));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		stage = getStage();
		skin = getSkin();
		font = getBigFont();
		
		beginPause = System.currentTimeMillis();
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		resume = new TextButton("Resume", style);
		resume.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				gameScreen.setElapsedTimeDuringPause(System.currentTimeMillis() - beginPause);
				game.setScreen(gameScreen);
			}
		});
		
		quit = new TextButton("Quit", style);
		quit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new PatientScreen(game));
			}
		});
		
		Gdx.input.setInputProcessor(stage);
		
		resume.setX(GameConstants.DISPLAY_WIDTH / 2 - resume.getWidth() / 2);
		resume.setY(GameConstants.DISPLAY_HEIGHT * 0.55f);
		
		quit.setX(GameConstants.DISPLAY_WIDTH / 2 - quit.getWidth() / 2);
		quit.setY(GameConstants.DISPLAY_HEIGHT * 0.45f);
		stage.addActor(resume);
		stage.addActor(quit);
		
		stage.addListener(new InputListener() {
     		
     		@Override
     		public boolean keyDown (InputEvent event, int keycode) {
     			return true;
     		}

     		@Override
     		public boolean keyUp (InputEvent event, int keycode) {
     			if(keycode == Keys.ESCAPE){
     				Gdx.app.log(RollingCat.LOG, "escape pressed !");
    				gameScreen.setElapsedTimeDuringPause(System.currentTimeMillis() - beginPause);
    				game.setScreen(gameScreen);
     			}
     			return true;
     		}
			
		});
		
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
