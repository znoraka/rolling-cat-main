package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

import fr.lirmm.smile.rollingcat.RollingCat;

public class LoginScreen implements Screen {
	
	private RollingCat game;
	private Stage stage;
	private BitmapFont black;
	private TextureAtlas atlas;
	private Skin skin;
	private SpriteBatch batch;
	private TextButton button;
	private Label loginLabel, passwordLabel;
	private TextField loginTextField, passwordTextField;
	
	
	public LoginScreen(RollingCat game){
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);

		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage = new Stage(width, height, true);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = black;
		style.fontColor = Color.BLACK;

		button = new TextButton("OK", style);
		button.setText("OK");
		button.setWidth(100);
		button.setHeight(50);
		button.setX(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2);
		button.setY(Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);

		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				System.out.println(event.getRelatedActor());
				Gdx.app.log(RollingCat.LOG, loginTextField.getText());
				Gdx.app.log(RollingCat.LOG, passwordTextField.getText());
				game.setScreen(new PatientSelectScreen(game));
			}
		});
		
		LabelStyle ls = new LabelStyle(black, Color.BLACK);
		loginLabel = new Label("Login", ls);
		loginLabel.setX(Gdx.graphics.getHeight() / 2 - 50);
		loginLabel.setY(Gdx.graphics.getHeight() / 2 + 165);
		
		passwordLabel = new Label("Password", ls);
		passwordLabel.setX(Gdx.graphics.getHeight() / 2 - 50);
		passwordLabel.setY(Gdx.graphics.getHeight() / 2 + 80);
		
		TextFieldStyle s = new TextFieldStyle();
		s.font = black;
		s.fontColor = Color.BLACK;
		s.background = skin.getDrawable("background");
		s.cursor = skin.getDrawable("cursor");
		
		loginTextField = new TextField("", s);
		loginTextField.setMessageText("login");
		loginTextField.setHeight(50);
		loginTextField.setWidth(100); 
		loginTextField.setX(Gdx.graphics.getWidth() / 2 - 100/2);
		loginTextField.setY(Gdx.graphics.getHeight() / 2 + 150);
		
		passwordTextField = new TextField("", s);
		passwordTextField.setMessageText("password");
		passwordTextField.setHeight(50);
		passwordTextField.setWidth(100);
		passwordTextField.setX(Gdx.graphics.getWidth() / 2 - 100/2);
		passwordTextField.setY(Gdx.graphics.getHeight() / 2 + 75);
		passwordTextField.setPasswordMode(true);
		passwordTextField.setPasswordCharacter('#');
		
		stage.addActor(button);
		stage.addActor(loginLabel);
		stage.addActor(loginTextField);
		stage.addActor(passwordTextField);
		stage.addActor(passwordLabel);
	


	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/atlas.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		black = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
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
