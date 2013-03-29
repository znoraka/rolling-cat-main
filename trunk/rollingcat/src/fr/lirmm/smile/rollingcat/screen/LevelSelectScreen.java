package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class LevelSelectScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private Table table;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	private TextButton start, next, previous, back, score;
	private ArrayList<Label> labels;
	private int currentButton;
	private Label label;
	private String[] levels, gems;
	private ArrayList<String> listOfGems;
	private String world;
	private boolean gen;
	
	private final float X = (GameConstants.DISPLAY_WIDTH / 2);

	
	private final float BIG_HEIGHT = (GameConstants.DISPLAY_HEIGHT * 0.4f);
	private final float BIG_WIDTH = (GameConstants.DISPLAY_WIDTH * 0.7f);
	private final float BIG_Y = (GameConstants.DISPLAY_HEIGHT / 2);
	
	private final float MEDIUM_HEIGHT = (GameConstants.DISPLAY_HEIGHT * 0.3f);
	private final float MEDIUM_WIDTH = (GameConstants.DISPLAY_WIDTH * 0.6f);
	private final float MEDIUM_Y_BOTTOM = (GameConstants.DISPLAY_HEIGHT * 0.3f);
	private final float MEDIUM_Y_TOP = (GameConstants.DISPLAY_HEIGHT * 0.7f);
	
	private final float SMALL_HEIGHT = (GameConstants.DISPLAY_HEIGHT * 0.2f);
	private final float SMALL_WIDTH = (GameConstants.DISPLAY_WIDTH * 0.5f);
	private final float SMALL_Y_BOTTOM = (GameConstants.DISPLAY_HEIGHT * 0.15f);
	private final float SMALL_Y_TOP = (GameConstants.DISPLAY_HEIGHT * 0.85f);
	
	private final float SPEED = 0.2f;
	
	private float elapsedTime;
	
	public LevelSelectScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
		gen = false;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(gen){
			if(next.isPressed() & elapsedTime > 0.4f)
				next();
			
			if(previous.isPressed() & elapsedTime > 0.4f)
				previous();
			
			if(next.isPressed() || previous.isPressed())
				elapsedTime += delta;
			else
				elapsedTime = 0;
			
			stage.draw();
			stage.act(delta);
		}
		if(gen == false & InternetManager.getWorld() != null){
			gen = true;
			show();
		}
	}

	private void changeButtonsSize() {
		
		if(labels.size() > 6){
			label = labels.get((labels.size() - 3 + currentButton)%labels.size());
			label.setVisible(false);
			label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 4, SMALL_Y_TOP - SMALL_HEIGHT / 4, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH / 2, SMALL_HEIGHT / 2, SPEED)));
		}
		
		if(labels.size() > 4){
			label = labels.get((labels.size() - 2 + currentButton)%labels.size());
			label.setVisible(true);
			label.setZIndex(1);
			label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 2, SMALL_Y_TOP - SMALL_HEIGHT / 2, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH, SMALL_HEIGHT, SPEED)));
		}
		if(labels.size() > 2){
			label = labels.get((currentButton - 1)<0?labels.size() - 1:currentButton - 1);
			label.setVisible(true);
			label.setZIndex(2);
			label.addAction(Actions.parallel(Actions.moveTo(X - MEDIUM_WIDTH / 2, MEDIUM_Y_TOP - MEDIUM_HEIGHT / 2, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(MEDIUM_WIDTH, MEDIUM_HEIGHT, SPEED)));
		}
		
		label = labels.get(currentButton);
		label.setVisible(true);
		label.setZIndex(3);
		label.addAction(Actions.parallel(Actions.moveTo(X  - BIG_WIDTH / 2, BIG_Y - BIG_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(BIG_WIDTH, BIG_HEIGHT, SPEED)));
		
		if(labels.size() > 1){
			label = labels.get((currentButton + 1)%labels.size());
			label.setVisible(true);
			label.setZIndex(2);
			label.addAction(Actions.parallel(Actions.moveTo(X - MEDIUM_WIDTH / 2, MEDIUM_Y_BOTTOM - MEDIUM_HEIGHT / 2, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(MEDIUM_WIDTH, MEDIUM_HEIGHT, SPEED)));
		}
		
		if(labels.size() > 3){
			label = labels.get((currentButton + 2)%labels.size());
			label.setVisible(true);
			label.setZIndex(1);
			label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 2, SMALL_Y_BOTTOM - SMALL_HEIGHT / 2, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH, SMALL_HEIGHT, SPEED)));
		}
		
		if(labels.size() > 5){
			label = labels.get((currentButton + 3)%labels.size());
			label.setVisible(false);
			label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 4, SMALL_Y_BOTTOM - SMALL_HEIGHT / 4, SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH / 2, SMALL_HEIGHT / 2, SPEED)));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if(gen == true){
			font = getBigFont();
			stage = getStage();
			skin = getSkin();
			world = InternetManager.getWorld();
			Json json = new Json();
			Gdx.app.log(RollingCat.LOG, json.prettyPrint(world));
			OrderedMap<String, Object> map = (OrderedMap<String, Object>) new JsonReader().parse(world);
			levels = json.readValue("levels", String[].class, map);
			gems = json.readValue("gems", String[].class, map); 
			
			listOfGems = new ArrayList<String>();
			for (String s : gems) {
				listOfGems.add(s+GameConstants.TEXTURE_GEM);
			}
			
			labels = new ArrayList<Label>();
			
			table = new Table();
			table.setBackground(skin.getDrawable("background_base"));
			table.setHeight(GameConstants.DISPLAY_HEIGHT);
			table.setWidth(GameConstants.DISPLAY_WIDTH);
			
			stage.addActor(table);
			
			TextButtonStyle style = new TextButtonStyle();
			style.up = skin.getDrawable("button_up");
			style.down = skin.getDrawable("button_down");
			style.font = font;
			style.fontColor = Color.BLACK;
			
			LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
			labelStyle.background = skin.getDrawable("button_up");
			
			
			start = new TextButton("Start", style);
			start.addListener(new ClickListener() {
					public void clicked (InputEvent event, float x, float y) {
						Gdx.app.log(RollingCat.LOG, "level selected : "+currentButton);
						game.setScreen(new LoadingScreen(game, patient, currentButton));
					}
				});
			
			back = new TextButton("Back", style);
			back.addListener(new ClickListener() {
					public void clicked (InputEvent event, float x, float y) {
						game.setScreen(new PatientScreen(game, patient));
					}
				});
			
			previous = new TextButton("Previous", style);
			previous.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					previous();
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					
				}
			});
			
			next = new TextButton("Next", style);
			next.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					next();
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					
				}
			});
			
			score = new TextButton("Gemmes", style);
			score.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new GameProgressionScreen(game, patient, listOfGems, listOfGems.size() == levels.length));
				}
			});
	//		
			createLabels(labelStyle);
			
			Gdx.input.setInputProcessor(stage);
			
			stage.addActor(previous);
			stage.addActor(next);
			stage.addActor(start);
			stage.addActor(back);
			stage.addActor(score);
			
			changeButtonsSize();
			
			start.setX(GameConstants.DISPLAY_WIDTH - start.getWidth());
			next.setX(GameConstants.DISPLAY_WIDTH - next.getWidth());
			next.setY(GameConstants.DISPLAY_HEIGHT - next.getHeight()); 
			previous.setX(0);
			previous.setY(GameConstants.DISPLAY_HEIGHT - previous.getHeight()); 
			score.setX(GameConstants.DISPLAY_WIDTH / 2 - score.getWidth() / 2);
		}
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
	
	private void createLabels(LabelStyle style) {
		for (int i = 0; i < levels.length + 1; i++) {
			label = new Label(""+i, style);
			label.setName(""+i);
			label.setX(GameConstants.DISPLAY_WIDTH / 2 - label.getWidth() / 2);
			labels.add(label);
			stage.addActor(label);
			label.setVisible(false);
		}
		currentButton = levels.length;
	}
	
	private void next(){
		currentButton = (currentButton > labels.size() - 2)?0:currentButton + 1;
		changeButtonsSize();
		elapsedTime = 0;
	}
	
	private void previous(){
		currentButton = (currentButton < 1)?(labels.size() - 1):currentButton - 1;
		changeButtonsSize();
		elapsedTime = 0;
	}
	

}
