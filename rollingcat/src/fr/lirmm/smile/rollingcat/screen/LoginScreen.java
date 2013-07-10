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
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.Localisation;
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
	private Table table;
	private Doctor doctor;
	private BitmapFont font;
	private SpriteBatch batch;
	private static boolean wrong;
	private TextureRegion region, background;
	private InputMultiplexer multiplexer;
	private List list;
	
	public LoginScreen(RollingCat game){
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		list.setX(GameConstants.DISPLAY_WIDTH * 0.5f - list.getWidth() * 0.5f);
		list.setY(GameConstants.DISPLAY_HEIGHT * 0.17f);
		
		stage.act(delta);
		batch.begin();
		batch.draw(background, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.draw(region, GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.2f, GameConstants.DISPLAY_HEIGHT * 0.6f, GameConstants.DISPLAY_WIDTH * 0.4f, GameConstants.DISPLAY_HEIGHT * 0.12f);
		batch.draw(region, GameConstants.DISPLAY_WIDTH / 2 - GameConstants.DISPLAY_WIDTH * 0.2f, GameConstants.DISPLAY_HEIGHT * 0.45f, GameConstants.DISPLAY_WIDTH * 0.4f, GameConstants.DISPLAY_HEIGHT * 0.12f);
		font.draw(batch, "version " + RollingCat.VERSION, 10, 30);
		
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
		
		list = Localisation.getAvailableLanguages();
		
		list.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				RollingCat.lang = list.getSelectedIndex();
				try {
					Localisation.loadLanguage(RollingCat.lang);
				} catch (Exception e) {
					Gdx.app.log(RollingCat.LOG, "error in lang file");
					RollingCat.lang = 0;
					Localisation.loadLanguage(RollingCat.lang);
				}
				loginTextField.setMessageText(localisation(_username));
				passwordTextField.setMessageText(localisation(_password));
			}
		});
		
		stage.addActor(list);
		
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
		
		table.add(button).padTop(GameConstants.DISPLAY_HEIGHT * 0.3f).padRight(10);
		
		ListStyle ls = new ListStyle();
		ls.font = getBigFont();
		ls.fontColorSelected = Color.WHITE;
		ls.fontColorUnselected = Color.WHITE;
		ls.selection = getSkin().getDrawable("button_up");
		
		stage.addActor(loginTextField);
		stage.addActor(passwordTextField);
		stage.addActor(table);
		
		Json json = new Json();

		Gdx.app.log(RollingCat.LOG, "retriving config file...");

		FileHandle file = Gdx.files.internal("data/config/config.json");
		Gdx.app.log(RollingCat.LOG, "done.");
		Gdx.app.log(RollingCat.LOG, "parsing config file...");

		String jsonData = file.readString();

		@SuppressWarnings("unchecked")
		OrderedMap<String, String> config = (OrderedMap<String, String>) new JsonReader().parse(jsonData);
		Gdx.app.log(RollingCat.LOG, json.prettyPrint(config));
		
		InternetManager.config(config.get("server"), config.get("port"));
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
