package fr.lirmm.smile.rollingcat.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.PatientsManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class PatientSelectScreen implements Screen{
	
	private RollingCat game;
	private int nbpatients;
	private Stage stage;
	private BitmapFont black;
	private TextureAtlas atlas;
	private Skin skin;
	private SpriteBatch batch;
	private TextButton b, selectPatient;
	private ArrayList<TextButton> buttons;
	private ScrollPane sp;
	private Table t, tprofile;
	private ArrayList<Patient> patients;
	private Patient p;
	private Label nom, prenom, date, hemiplegia, dominantMember;
	private Image face;
	private LabelStyle labelStyle;
	
	
	public PatientSelectScreen(RollingCat game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		if(p != null){
			
			selectPatient.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new PatientScreen(game, p));
				}
			});
			
			selectPatient.setVisible(true);
			nom.setText(p.getLastName());
			prenom.setText(p.getFirstName());
			date.setText(p.getStrokeDate());
			hemiplegia.setText(p.getHemiplegia());
			dominantMember.setText(p.getDominantMember());
//			face = p.getFace();
		}
		
		
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
		
		ScrollPaneStyle spstyle = new ScrollPaneStyle();
		spstyle.background = skin.getDrawable("button_patient");
		spstyle.corner = skin.getDrawable("button_up");
		spstyle.vScroll = skin.getDrawable("button_up");
		spstyle.vScrollKnob = skin.getDrawable("button_up");
		
		sp = new ScrollPane(null, spstyle);
		sp.setX(10);
		sp.setWidth(200);
		sp.setHeight(Gdx.graphics.getHeight());
		sp.setWidget(t);
		
		stage.addActor(sp);
		stage.addActor(tprofile);
			
	}
		

	private void createButtons(TextButtonStyle style) {
		for (int i = 0; i < nbpatients; i++) {
			b = new TextButton(patients.get(i).getFirstName() + " " + patients.get(i).getLastName(), style);
			b.setName(""+i);
			buttons.add(b);
			t.add(b).align(Align.right).width(100).height(50).pad(10);
			t.row();
		}
		addListeners();
	}
	
	private void addListeners(){
		for (int i = 0; i < buttons.size(); i++) {
			b = buttons.get(i);
			b.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					p = patients.get(Integer.valueOf(event.getListenerActor().getName()));
				}
			});
		}
	}

	@Override
	public void show() {
		patients = PatientsManager.getPatients();
		this.nbpatients = patients.size();
		
		t = new Table();
		tprofile = new Table();
		buttons = new ArrayList<TextButton>();
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/atlas.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		black = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = black;
		style.fontColor = Color.BLACK;
		
		createButtons(style);

		labelStyle = new LabelStyle(black, Color.BLACK);
		nom = new Label("", labelStyle);
		prenom = new Label("", labelStyle);
		date = new Label("", labelStyle);
		hemiplegia = new Label("", labelStyle);
		dominantMember = new Label("", labelStyle);
		selectPatient = new TextButton("Select", style);
		selectPatient.setVisible(false);
		face = new Image();
		
		tprofile.add(face);
		tprofile.row();
		tprofile .add(nom);
		tprofile.row();
		tprofile.add(prenom);
		tprofile.row();
		tprofile.add(date);
		tprofile.row();
		tprofile.add(hemiplegia);
		tprofile.row();
		tprofile.add(dominantMember);
		tprofile.row();
		tprofile.add(selectPatient);
		
		tprofile.setX(Gdx.graphics.getWidth() / 3);
		tprofile.setY(500);
		
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
		stage.dispose();
		black.dispose();
		atlas.dispose();
		skin.dispose();
		batch.dispose();
	}

}
