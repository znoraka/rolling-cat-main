package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._password;
import static fr.lirmm.smile.rollingcat.Localisation._skin;
import static fr.lirmm.smile.rollingcat.Localisation._username;
import static fr.lirmm.smile.rollingcat.Localisation._wrong_info;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.SoundManager;
import fr.lirmm.smile.rollingcat.model.doctor.Doctor;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class LoginScreen implements Screen, InputProcessor{
	
	private RollingCat game;
	private Stage stage;
	private Skin skin;
	private TextButton button;
	private TextField loginTextField, passwordTextField;
	private Table table, tableTop;
	private Doctor doctor;
	private BitmapFont font;
	private SpriteBatch batch;
	private static boolean wrong;
	private TextureRegion region, background;
	private InputMultiplexer multiplexer;
	private ImageButton est1, est2, est3;
	
	
	public LoginScreen(RollingCat game){
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
		
		stage.act(delta);
		batch.begin();
		batch.draw(background, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.draw(region, GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.2f, GameConstants.DISPLAY_HEIGHT * 0.6f, GameConstants.DISPLAY_WIDTH * 0.4f, GameConstants.DISPLAY_HEIGHT * 0.12f);
		batch.draw(region, GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.2f, GameConstants.DISPLAY_HEIGHT * 0.45f, GameConstants.DISPLAY_WIDTH * 0.4f, GameConstants.DISPLAY_HEIGHT * 0.12f);
		font.draw(batch, "version " + RollingCat.VERSION, 10, 30);
		
		if(RollingCat.skin == 1)
			batch.draw(GdxRessourcesGetter.getSkin().getRegion("bottom_green"), est1.getX() + tableTop.getX(), est1.getY() + tableTop.getY(), 0, 0, est1.getWidth(), est1.getHeight(), 1, 1, 0);
		else if(RollingCat.skin == 2)
			batch.draw(GdxRessourcesGetter.getSkin().getRegion("bottom_green"), est2.getX() + tableTop.getX(), est2.getY() + tableTop.getY(), 0, 0, est2.getWidth(), est2.getHeight(), 1, 1, 0);
		else if(RollingCat.skin == 3)
			batch.draw(GdxRessourcesGetter.getSkin().getRegion("bottom_green"), est3.getX() + tableTop.getX(), est3.getY() + tableTop.getY(), 0, 0, est3.getWidth(), est3.getHeight(), 1, 1, 0);
		
		batch.end();
		
		if(wrong){
			batch.begin();
			font.draw(batch, localisation(_wrong_info), 500, 50);
			batch.end();
		}
		
		if(InternetManager.isReady())
			game.setScreen(new PatientSelectLoadingScreen(game));
		
		stage.draw();
		
		

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		SoundManager.load();
		skin = getSkin();
		batch = getSpriteBatch();
		font = getBigFont();
		stage = getStage();
		table = new Table();
		doctor = Doctor.getDoctor();
		wrong = false;
		region = skin.getRegion("textfield");
		background = skin.getRegion("background_base");
		multiplexer = new InputMultiplexer();
		
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(stage);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;

		button = new TextButton("OK", style);
		button.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				wrong = false;
				doctor.login(loginTextField.getText(), passwordTextField.getText());
			}
		});
		
		est1 = new ImageButton(GdxRessourcesGetter.getGameSkin().getDrawable("skin0"));
		est2 = new ImageButton(GdxRessourcesGetter.getGameSkin().getDrawable("skin1"));
		est3 = new ImageButton(GdxRessourcesGetter.getGameSkin().getDrawable("skin2"));
		
		est1.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				RollingCat.skin = 1;
				Gdx.app.log(RollingCat.LOG, "skin 1 selected");
			}
		});
		
		est2.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				RollingCat.skin = 2;
				Gdx.app.log(RollingCat.LOG, "skin 2 selected");
			}
		});
		
		est3.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				RollingCat.skin = 3;
				Gdx.app.log(RollingCat.LOG, "skin 3 selected");
			}
		});
		
		
		TextFieldStyle s = new TextFieldStyle();
		s.font = font;
		s.fontColor = Color.WHITE;
		s.cursor = skin.getDrawable("cursor");
		s.cursor.setTopHeight(500);
		s.cursor.setLeftWidth(100);
		s.selection = skin.getDrawable("selection");
		
		
		loginTextField = new TextField("", s);
		loginTextField.setMessageText(localisation(_username));
		loginTextField.setX(GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.185f);
		loginTextField.setY(GameConstants.DISPLAY_HEIGHT * 0.615f);
		loginTextField.setWidth(GameConstants.DISPLAY_WIDTH * 0.37f);
		loginTextField.setHeight(GameConstants.DISPLAY_WIDTH * 0.07f);
				
		passwordTextField = new TextField("", s);
		passwordTextField.setMessageText(localisation(_password));
		passwordTextField.setPasswordMode(true);
		passwordTextField.setPasswordCharacter('*');
		passwordTextField.setX(GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.185f);
		passwordTextField.setY(GameConstants.DISPLAY_HEIGHT * 0.465f);
		passwordTextField.setWidth(GameConstants.DISPLAY_WIDTH * 0.37f);
		passwordTextField.setHeight(GameConstants.DISPLAY_WIDTH * 0.07f);
		
		
		
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		
		tableTop = new Table();
//		tableTop.setBackground(GdxRessourcesGetter.getSkin().getDrawable("top_orange"));
		tableTop.setWidth(GameConstants.DISPLAY_WIDTH * 0.51f);
		tableTop.setHeight(GameConstants.DISPLAY_HEIGHT * 0.2f);
		tableTop.setY(GameConstants.DISPLAY_HEIGHT * 0.76f);
		tableTop.setX(GameConstants.DISPLAY_WIDTH * 0.5f - tableTop.getWidth() * 0.5f);
		
		tableTop.add(est1).fill().expand().pad(5);
		tableTop.add(est2).fill().expand().pad(5);
		tableTop.add(est3).fill().expand().pad(5);
		
		table.add(button).padTop(GameConstants.DISPLAY_HEIGHT * 0.3f).padRight(10);
		
		ListStyle ls = new ListStyle();
		ls.font = getBigFont();
		ls.fontColorSelected = Color.WHITE;
		ls.fontColorUnselected = Color.WHITE;
		ls.selection = getSkin().getDrawable("button_up");
		
		stage.addActor(loginTextField);
		stage.addActor(passwordTextField);
		stage.addActor(table);
		stage.addActor(tableTop);
		
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
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ENTER){			
			wrong = false;
			doctor.login(loginTextField.getText(), passwordTextField.getText());
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void setWrong(){
		wrong = true;
	}

}
