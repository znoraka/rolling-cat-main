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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;

public class LevelSelectScreen implements Screen {
	
	private RollingCat game;
	private Patient patient;
	private Table table;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	private TextButton button, next, previous;
	private ArrayList<Label> labels;
	private int currentButton;
	private Label label;
	
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
	
	public LevelSelectScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		stage.act(delta);
		
	}

	private void changeButtonsSize() {
		
		label = labels.get(currentButton);
		label.setVisible(true);
		label.setZIndex(3);
		label.addAction(Actions.parallel(Actions.moveTo(X  - BIG_WIDTH / 2, BIG_Y - BIG_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(BIG_WIDTH, BIG_HEIGHT, SPEED)));
		
		label = labels.get(currentButton - 1);
		label.setVisible(true);
		label.setZIndex(2);
		label.addAction(Actions.parallel(Actions.moveTo(X - MEDIUM_WIDTH / 2, MEDIUM_Y_TOP - MEDIUM_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(MEDIUM_WIDTH, MEDIUM_HEIGHT, SPEED)));
		
		label = labels.get((currentButton + 1)%labels.size());
		label.setVisible(true);
		label.setZIndex(2);
		label.addAction(Actions.parallel(Actions.moveTo(X - MEDIUM_WIDTH / 2, MEDIUM_Y_BOTTOM - MEDIUM_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(MEDIUM_WIDTH, MEDIUM_HEIGHT, SPEED)));
		
		label = labels.get(currentButton - 2);
		label.setVisible(true);
		label.setZIndex(1);
		label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 2, SMALL_Y_TOP - SMALL_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH, SMALL_HEIGHT, SPEED)));
		
		label = labels.get((currentButton + 2)%labels.size());
		label.setVisible(true);
		label.setZIndex(1);
		label.addAction(Actions.parallel(Actions.moveTo(X - SMALL_WIDTH / 2, SMALL_Y_BOTTOM - SMALL_HEIGHT / 2, SPEED)));
		label.addAction(Actions.parallel(Actions.sizeTo(SMALL_WIDTH, SMALL_HEIGHT, SPEED)));
		
		label = labels.get((currentButton + 3)%labels.size());
		label.setVisible(false);
		label.setHeight(0);
		label.setWidth(0);
		
		label = labels.get(currentButton - 3);
		label.setVisible(false);
		label.setHeight(0);
		label.setWidth(0);


		

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		font = getBigFont();
		stage = getStage();
		skin = getSkin();
		
		labels = new ArrayList<Label>();
		
		table = new Table();
		table.setBackground(skin.getDrawable("background_base"));
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		labelStyle.background = skin.getDrawable("button_up");
		
		
		button = new TextButton("go", style);
		button.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new LoadingScreen(game, patient));
				}
			});
		
		
		
		previous = new TextButton("Previous", style);
		previous.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					if(currentButton > 3)
						currentButton--;
					

					changeButtonsSize();
				}
			});
		
		next = new TextButton("Next", style);
		next.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					currentButton = (currentButton + 1)%labels.size();
					
					changeButtonsSize();
				}
			});
		
		createLabels(labelStyle);
		
		currentButton = 10;

		Gdx.input.setInputProcessor(stage);
		
		stage.addActor(previous);
		stage.addActor(next);
		
		changeButtonsSize();
		
		next.setX(GameConstants.DISPLAY_WIDTH - next.getHeight());
		
//		stage.addActor(table);

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
		for (int i = 0; i < 20; i++) {
			label = new Label(""+i, style);
			label.setName(""+i);
			label.setHeight(50);
			label.setWidth(100);
			label.setX(GameConstants.DISPLAY_WIDTH / 2 - label.getWidth() / 2);
			labels.add(label);
			stage.addActor(label);
			label.setOriginY(label.getHeight() / 2);
			label.setOriginX(label.getWidth() / 2);

		}
	}
	

}
