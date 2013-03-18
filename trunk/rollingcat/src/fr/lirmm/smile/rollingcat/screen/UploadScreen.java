package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
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
	private Table tableLeftBottom, tableLeftTop, tableRightBottom, tableRightTop;
	private Rectangle drawArea;
	private ShapeRenderer sr;
	private Label date, duration, dateValue, durationValue;

	
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
		track.render(sr, drawArea, batch);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		skin = getSkin();
		font = getBigFont();
		stage = getStage();
		sr = getShapeRenderer();
		
		tableLeftBottom = new Table();
		tableLeftBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.28f);
		tableLeftBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.22f);
		tableLeftBottom.setX(GameConstants.DISPLAY_WIDTH * 0.055f);
		tableLeftBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.08f);
		
		tableRightBottom = new Table();
		tableRightBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.10f);
		tableRightBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.60f);
		tableRightBottom.setX(GameConstants.DISPLAY_WIDTH * 0.337f);
		tableRightBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.08f);
		
		tableRightTop = new Table();
		tableRightTop.setBackground(skin.getDrawable("background_base"));
		tableRightTop.setHeight(GameConstants.DISPLAY_HEIGHT * 0.750f);
		tableRightTop.setWidth(GameConstants.DISPLAY_WIDTH * 0.65f);
		tableRightTop.setX(GameConstants.DISPLAY_WIDTH * 0.312f);
		tableRightTop.setY(GameConstants.DISPLAY_HEIGHT * 0.2f);
		
		tableLeftTop = new Table();
		tableLeftTop.setBackground(skin.getDrawable("background_base"));
		tableLeftTop.setWidth(GameConstants.DISPLAY_WIDTH * 0.265f);
		tableLeftTop.setX(GameConstants.DISPLAY_WIDTH * 0.035f);
		tableLeftTop.setHeight(GameConstants.DISPLAY_HEIGHT * 0.555f);
		tableLeftTop.setY(GameConstants.DISPLAY_HEIGHT * 0.395f);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		labelStyle.background = skin.getDrawable("top_orange");
		
		LabelStyle labelStyle2 = new LabelStyle(font, Color.BLACK);
		labelStyle2.background = skin.getDrawable("bottom_green");
		
		date = new Label(" Date", labelStyle);
		duration = new Label(" Duration", labelStyle);
		
		dateValue = new Label(track.getDate(), labelStyle2);
		durationValue = new Label(""+track.getDuration() + " s", labelStyle2);
		
		tableLeftTop.add(date).fill().width(tableLeftTop.getWidth() * 0.95f);
		tableLeftTop.row();
		tableLeftTop.add(dateValue).fill().width(tableLeftTop.getWidth() * 0.95f);
		tableLeftTop.row();
		tableLeftTop.add(duration).fill().width(tableLeftTop.getWidth() * 0.95f).padTop(20);
		tableLeftTop.row();
		tableLeftTop.add(durationValue).fill().width(tableLeftTop.getWidth() * 0.95f);
		
		back = new TextButton("Back", style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				track.reset();
				game.setScreen(new TrackingRecapScreen(game, patient));
			}
		});
		
		upload = new TextButton("Upload", style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(RollingCat.LOG, "uploading... just kidding, nothing happened");
				InternetManager.sendEvents(EventManager.getListAsJsonString());
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
				if(!track.prev())
					prev.setVisible(false);
				next.setVisible(true);
			}
		});
		
		next = new TextButton("Next", style);
		next.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(!track.next())
					next.setVisible(false);
				prev.setVisible(true);
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
		
		if(track.getType() == Track.GAME){
			tableRightBottom.add(prev).fill().expand();
			tableRightBottom.add(next).padLeft(100).fill().expand();
		}
		stage.addActor(tableLeftBottom);
		stage.addActor(tableLeftTop);
		stage.addActor(tableRightBottom);
		stage.addActor(tableRightTop);
		
		prev.setVisible(false);
		
		drawArea = new Rectangle(tableRightTop.getX(), tableRightTop.getY(), tableRightTop.getWidth(), tableRightTop.getHeight());
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

}
