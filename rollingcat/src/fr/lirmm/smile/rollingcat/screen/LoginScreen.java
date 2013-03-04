package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.doctor.Doctor;

public class LoginScreen implements Screen, InputProcessor{
	
	private RollingCat game;
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private TextButton button;
	private TextField loginTextField, passwordTextField;
	private Table table;
	private Doctor doctor;
	private BitmapFont font;
	private SpriteBatch batch;
	private boolean wrong;
	private TextureRegion region, background;
	private InputMultiplexer multiplexer;
	
	
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
		batch.end();

		if(wrong){
			batch.begin();
			font.draw(batch, "informations incorrectes", 500, 50);
			batch.end();
		}
		
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		atlas = new TextureAtlas("data/patientAtlas.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		font = new BitmapFont(Gdx.files.internal("data/font_24px.fnt"), false);
		table = new Table();
		doctor = new Doctor();
		batch = new SpriteBatch();
		wrong = false;
		region = skin.getRegion("textfield");
		background = skin.getRegion("background_base");
		multiplexer = new InputMultiplexer();
		
		stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
		stage.clear();
		
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
				if(doctor.login(loginTextField.getText(), passwordTextField.getText()))
					game.setScreen(new PatientSelectScreen(game, doctor));
				else
					wrong = true;
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
		loginTextField.setMessageText("username");
		loginTextField.setX(GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.185f);
		loginTextField.setY(GameConstants.DISPLAY_HEIGHT * 0.615f);
		loginTextField.setWidth(GameConstants.DISPLAY_WIDTH * 0.37f);
		loginTextField.setHeight(GameConstants.DISPLAY_WIDTH * 0.07f);
				
		passwordTextField = new TextField("", s);
		passwordTextField.setMessageText("password");
		passwordTextField.setPasswordMode(true);
		passwordTextField.setPasswordCharacter('#');
		passwordTextField.setX(GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.185f);
		passwordTextField.setY(GameConstants.DISPLAY_HEIGHT * 0.465f);
		passwordTextField.setWidth(GameConstants.DISPLAY_WIDTH * 0.37f);
		passwordTextField.setHeight(GameConstants.DISPLAY_WIDTH * 0.07f);
		
		
		
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		
		table.add(button).padTop(GameConstants.DISPLAY_HEIGHT * 0.3f);
		
		
		stage.addActor(loginTextField);
		stage.addActor(passwordTextField);
		stage.addActor(table);
	


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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ENTER){
			if(doctor.login(loginTextField.getText(), passwordTextField.getText()))
				game.setScreen(new PatientSelectScreen(game, doctor));
			else
				wrong = true;
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

}
