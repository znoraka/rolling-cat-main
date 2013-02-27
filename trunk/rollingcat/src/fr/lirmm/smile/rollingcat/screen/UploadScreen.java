package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class UploadScreen implements Screen {

	private RollingCat game;
	private Patient patient;
	private Track track;
	private Skin skin;
	private Stage stage;
	private TextButton back, upload, delete, prev, next;
	private BitmapFont font;
	private SpriteBatch batch;
	private Table tableLeftBottom, tableLeftTop, tableRight;
	private int segment;
	
	public UploadScreen(RollingCat game, Patient patient, Track track){
		this.game = game;
		this.patient = patient;
		this.track = track;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(skin.getRegion("backgroundupload"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.end();
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		segment = 0;
		batch = new SpriteBatch();
		stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
		stage.clear();
		skin = new Skin(new TextureAtlas("data/patientAtlas.atlas"));
		
		tableLeftBottom = new Table();
		tableLeftBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.28f);
		tableLeftBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.22f);
		tableLeftBottom.setX(GameConstants.DISPLAY_WIDTH * 0.055f);
		tableLeftBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.08f);
		
		tableRight = new Table();
		tableRight.setHeight(GameConstants.DISPLAY_HEIGHT * 0.10f);
		tableRight.setWidth(GameConstants.DISPLAY_WIDTH * 0.60f);
		tableRight.setX(GameConstants.DISPLAY_WIDTH * 0.325f);
		tableRight.setY(GameConstants.DISPLAY_HEIGHT * 0.08f);
		
		font = new BitmapFont(Gdx.files.internal("data/font_24px.fnt"), false);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		back = new TextButton("Back", style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new TrackingRecapScreen(game, patient));
			}
		});
		
		upload = new TextButton("Upload", style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(RollingCat.LOG, "uploading... just kidding, nothing happened");
			}
		});
		
		delete = new TextButton("Delete", style);
		delete.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				patient.getListOfTracks().remove(track);
				Gdx.app.log(RollingCat.LOG, "one track removed");
				game.setScreen(new TrackingRecapScreen(game, patient));
			}
		});
		
		prev = new TextButton("Previous", style);
		prev.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(segment > GameConstants.DISPLAY_WIDTH)
					segment -= GameConstants.DISPLAY_WIDTH;
			}
		});
		
		next = new TextButton("Next", style);
		next.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				segment += GameConstants.DISPLAY_WIDTH;
			}
		});
		
		back.setY(GameConstants.DISPLAY_HEIGHT - 50);
		back.setX(GameConstants.DISPLAY_WIDTH - 130);
		Gdx.input.setInputProcessor(stage);
		
		tableLeftBottom.add(upload).pad(5).fill().expand();
		tableLeftBottom.row();
		tableLeftBottom.add(delete).pad(5).fill().expand();
		tableLeftBottom.row();
		tableLeftBottom.add(back).pad(5).fill().expand();
		
		tableRight.add(prev);
		tableRight.add(next).padLeft(100);
		
		stage.addActor(tableLeftBottom);
		stage.addActor(tableRight);

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
		skin.dispose();
		stage.dispose();
		font.dispose();
	}

}
