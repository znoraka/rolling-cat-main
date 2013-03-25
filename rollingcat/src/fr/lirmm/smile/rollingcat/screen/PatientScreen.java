package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.doctor.Doctor;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class PatientScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private Table tableLeft, tableRight;
	private Label cellName, name, cellBirthDate, birthDate, cellStrokeDate, strokeDate, cellHemiplegia, hemiplegia, cellDominantMember, dominantMember;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	private TextButton play, assessment, upload, back;
	private SpriteBatch batch;
	
	public PatientScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(skin.getRegion("background"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.draw(patient.getFace(), GameConstants.DISPLAY_WIDTH * 0.078f, GameConstants.DISPLAY_HEIGHT * 0.525f, GameConstants.DISPLAY_WIDTH / 5f, GameConstants.DISPLAY_HEIGHT / 5f * 1.76f);
		batch.end();
		
		stage.draw();
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
		
		tableLeft = new Table();
		tableRight = new Table();
		
		tableLeft.setHeight(GameConstants.DISPLAY_HEIGHT * 0.35f);
		tableLeft.setWidth(GameConstants.DISPLAY_WIDTH * 0.20f);
		tableLeft.setX(GameConstants.DISPLAY_WIDTH * 0.075f);
		tableLeft.setY(GameConstants.DISPLAY_HEIGHT * 0.1f);
		
		tableRight.setHeight(GameConstants.DISPLAY_HEIGHT * 0.80f);
		tableRight.setWidth(GameConstants.DISPLAY_WIDTH * 0.60f);
		tableRight.setX(GameConstants.DISPLAY_WIDTH * 0.325f);
		tableRight.setY(GameConstants.DISPLAY_HEIGHT * 0.1f);

		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		play = new TextButton("Play", style);
		play.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
			        InternetManager.newGameSession(Track.GAME);
					game.setScreen(new LevelSelectScreen(game, patient));
				}
			});
		
		assessment = new TextButton("Assessment", style);
		assessment.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new AssessmentScreen(game, patient));
			}
		});
		
		upload = new TextButton("Upload", style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new TrackingRecapScreen(game, patient));
			}
		});
		
		back = new TextButton("Back", style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new PatientSelectScreen(game, Doctor.getDoctor()));
			}
		});
		
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		labelStyle.background = skin.getDrawable("button_up");
		
//		tableRight.setBackground(skin.getDrawable("green_background"));
		
		cellName = new Label("Name", labelStyle);
		name = new Label("", labelStyle);
		cellBirthDate = new Label("Birth Date", labelStyle);
		birthDate = new Label("", labelStyle);
		cellDominantMember = new Label("Dominant Member", labelStyle);
		dominantMember = new Label("", labelStyle);
		cellHemiplegia = new Label("Hemiplegia", labelStyle);
		hemiplegia = new Label("", labelStyle);
		cellStrokeDate = new Label("Stroke Date", labelStyle);
		strokeDate = new Label("", labelStyle);
		
		name.setText(patient.getFirstName() + " " + patient.getLastName());
		birthDate.setText(""+patient.getBirthDate());
		strokeDate.setText(patient.getStrokeDate());
		hemiplegia.setText(patient.getHemiplegia());
		dominantMember.setText(patient.getDominantMember());
		
		cellName.setAlignment(Align.center);
		cellBirthDate.setAlignment(Align.center);
		cellDominantMember.setAlignment(Align.center);
		cellStrokeDate.setAlignment(Align.center);
		cellHemiplegia.setAlignment(Align.center);
		name.setAlignment(Align.center);
		birthDate.setAlignment(Align.center);
		dominantMember.setAlignment(Align.center);
		hemiplegia.setAlignment(Align.center);
		strokeDate.setAlignment(Align.center);
		
//		tableLeft.add(patient.getFace()).padTop(17).maxHeight(GameConstants.DISPLAY_WIDTH / 5f * 1.3f).maxWidth(GameConstants.DISPLAY_WIDTH / 5f).setWidgetX(200);
//		tableLeft.row();
		tableLeft.add(play).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();
		tableLeft.row();
		tableLeft.add(assessment).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();
		tableLeft.row();
		tableLeft.add(upload).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();
		tableLeft.row();
		tableLeft.add(back).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();
		
		tableRight.add(cellName).fill().expand();
		tableRight.add(name).fill().expand();
		tableRight.row();
		tableRight.add(cellBirthDate).fill().expand();
		tableRight.add(birthDate).fill().expand();
		tableRight.row();
		tableRight.add(cellStrokeDate).fill().expand();
		tableRight.add(strokeDate).fill().expand();
		tableRight.row();
		tableRight.add(cellHemiplegia).fill().expand();
		tableRight.add(hemiplegia).fill().expand();
		tableRight.row();
		tableRight.add(cellDominantMember).fill().expand();
		tableRight.add(dominantMember).fill().expand();
		
		stage.addActor(tableLeft);
		stage.addActor(tableRight);
		
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
		Gdx.app.log(RollingCat.LOG, "disposing...");
	}

}
