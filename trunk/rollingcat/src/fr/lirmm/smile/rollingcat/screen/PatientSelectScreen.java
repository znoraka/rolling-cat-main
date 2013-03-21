package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
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

public class PatientSelectScreen implements Screen{
	
	private RollingCat game;
	private int nbpatients;
	private Stage stage;
	private BitmapFont black;
	private Skin skin;
	private SpriteBatch batch;
	private TextButton b, selectPatient, back;
	private ArrayList<TextButton> buttons;
	private ScrollPane sp;
	private Table tableLeft, tableRight;
	private ArrayList<Patient> patients;
	private Patient p;
	private Label nom, prenom, date, hemiplegia, dominantMember;
	private Texture face;
	private LabelStyle labelStyle;
	private Doctor doctor;
	
	
	public PatientSelectScreen(RollingCat game, Doctor doctor){
		this.game = game;
		this.doctor = doctor;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		batch.begin();
		batch.draw(skin.getRegion("backgroundtrack"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.draw(skin.getRegion("background_base"), GameConstants.DISPLAY_WIDTH * 0.315f, GameConstants.DISPLAY_HEIGHT * 0.65f, GameConstants.DISPLAY_WIDTH * 0.21f, GameConstants.DISPLAY_HEIGHT * 0.32f);
		batch.draw(face, GameConstants.DISPLAY_WIDTH * 0.32f, GameConstants.DISPLAY_HEIGHT * 0.66f, GameConstants.DISPLAY_WIDTH * 0.2f, GameConstants.DISPLAY_HEIGHT * 0.30f);
		batch.end();

		stage.act(delta);

		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}
		

	private void createButtons(TextButtonStyle style) {
		for (int i = 0; i < nbpatients; i++) {
			b = new TextButton(patients.get(i).getFirstName() + " " + patients.get(i).getLastName(), style);
			b.setName(""+i);
			buttons.add(b);
			tableLeft.add(b).align(Align.right).width(sp.getWidth()*0.85f).height(65).pad(5);
			tableLeft.row();
		}
		addListeners();
	}
	
	private void addListeners(){
		for (int i = 0; i < buttons.size(); i++) {
			b = buttons.get(i);
			b.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					setPatient(patients.get(Integer.valueOf(event.getListenerActor().getName())));
				}
			});
		}
	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		skin = getSkin();
		black = getSmallFont();
		stage = getStage();

		tableLeft = new Table();
		tableRight = new Table();
		buttons = new ArrayList<TextButton>();
		
		Gdx.input.setInputProcessor(stage);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = black;
		style.fontColor = Color.BLACK;
		
		patients = doctor.getPatients();
		this.nbpatients = patients.size();
		
		if(patients.size() > 0){
			ScrollPaneStyle scrollPanelStyle = new ScrollPaneStyle();
			scrollPanelStyle.corner = skin.getDrawable("button_up");
			scrollPanelStyle.vScroll = skin.getDrawable("button_up");
			scrollPanelStyle.vScrollKnob = skin.getDrawable("background_base");
			scrollPanelStyle.vScrollKnob.setRightWidth(20);
			scrollPanelStyle.vScrollKnob.setMinWidth(20);
			scrollPanelStyle.vScrollKnob.setLeftWidth(20);
			scrollPanelStyle.vScroll.setRightWidth(20);
			scrollPanelStyle.vScroll.setMinWidth(20);
			scrollPanelStyle.vScroll.setLeftWidth(20);	
			
			sp = new ScrollPane(tableLeft, scrollPanelStyle);
			sp.setX(GameConstants.DISPLAY_WIDTH*0.025f);
			sp.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
			sp.setHeight(GameConstants.DISPLAY_HEIGHT * 0.930f);
			sp.setWidth(GameConstants.DISPLAY_WIDTH * 0.262f);
			sp.setFadeScrollBars(false);
			sp.scrollTo(0, 0, 0, 0);
			sp.setFadeScrollBars(false);
			
			tableRight.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
			tableRight.setHeight(GameConstants.DISPLAY_HEIGHT * 0.600f);
			tableRight.setX(GameConstants.DISPLAY_WIDTH*0.315f);
			tableRight.setWidth(GameConstants.DISPLAY_WIDTH * 0.658f);
			stage.addActor(sp);
			stage.addActor(tableRight);
			
			p = patients.get(0);
			
			createButtons(style);
	
			labelStyle = new LabelStyle(black, Color.BLACK);
			labelStyle.background = skin.getDrawable("button_up");
			nom = new Label(p.getLastName(), labelStyle);
			prenom = new Label(p.getFirstName(), labelStyle);
			date = new Label(p.getStrokeDate(), labelStyle);
			hemiplegia = new Label(p.getHemiplegia(), labelStyle);
			dominantMember = new Label(p.getDominantMember(), labelStyle);
			selectPatient = new TextButton("Select", style);
			face = p.getFace();
			
			selectPatient.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new PatientScreen(game, p));
				}
			});
			
			tableRight.add(prenom).fill().expand();
			tableRight.add(nom).fill().expand();
			tableRight.row();
			tableRight.add(date).fill().expand();
			tableRight.row();
			tableRight.add(hemiplegia).fill().expand();
			tableRight.row();
			tableRight.add(dominantMember).fill().expand();
			tableRight.row();
			
			selectPatient.setX(GameConstants.DISPLAY_WIDTH * 0.7f);
			selectPatient.setY(GameConstants.DISPLAY_HEIGHT * 0.77f);
			
			stage.addActor(selectPatient);
		}
		else 
			face = new Texture("data/nopatient.png");
		
		back = new TextButton("Back", style);
		
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				InternetManager.reset();
				game.setScreen(new LoginScreen(game));
			}
		});
		back.setX(GameConstants.DISPLAY_WIDTH * 0.70f);
		back.setY(GameConstants.DISPLAY_HEIGHT * 0.70f);
		
		stage.addActor(back);
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

	private void setPatient(Patient patient) {
		p = patient;
		nom.setText(patient.getLastName());
		prenom.setText(patient.getFirstName());
		date.setText(patient.getStrokeDate());
		hemiplegia.setText(patient.getHemiplegia());
		dominantMember.setText(patient.getDominantMember());
		face = patient.getFace();
	}
}