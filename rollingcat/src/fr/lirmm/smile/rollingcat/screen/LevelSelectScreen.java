package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._back;
import static fr.lirmm.smile.rollingcat.Localisation._duration;
import static fr.lirmm.smile.rollingcat.Localisation._found;
import static fr.lirmm.smile.rollingcat.Localisation._gem;
import static fr.lirmm.smile.rollingcat.Localisation._gems;
import static fr.lirmm.smile.rollingcat.Localisation._level;
import static fr.lirmm.smile.rollingcat.Localisation._locked;
import static fr.lirmm.smile.rollingcat.Localisation._new_level;
import static fr.lirmm.smile.rollingcat.Localisation._next;
import static fr.lirmm.smile.rollingcat.Localisation._not_found;
import static fr.lirmm.smile.rollingcat.Localisation._previous;
import static fr.lirmm.smile.rollingcat.Localisation._score;
import static fr.lirmm.smile.rollingcat.Localisation._start;
import static fr.lirmm.smile.rollingcat.Localisation._tutorial;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.world.World;
import fr.lirmm.smile.rollingcat.utils.Polynome;
import fr.lirmm.smile.rollingcat.utils.WorldBuilder;

public class LevelSelectScreen implements Screen {

	private RollingCat game;
	private Patient patient;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	private TextButton start, next, previous, back, score;
	private ArrayList<Table> tables;
	private int currentButton;
	private Table table;
	private final float X = (GameConstants.DISPLAY_WIDTH / 2);
	private String worldAsString;
	private boolean gen;
	private World world;

	private float elapsedTime;

	private final float SPEED = 0.2f;
	private float[] sizeH;
	private float[] sizeW;
	private float[] posH;
	private float[] posW;
	private int[] Zindexes;
	private int numberOfLevels;
	private int numberOfLevelsDisplayed;

	public LevelSelectScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
		this.numberOfLevelsDisplayed = GameConstants.NB_OF_LEVELS_IN_MENU;
	}

	@Override
	public void render(float delta) {
		if(tables != null)
		{
			changeButtonsSize();

		}



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

			if(currentButton >= numberOfLevels)
				start.setTouchable(Touchable.disabled);
			else
				start.setTouchable(Touchable.enabled);
			
			if(currentButton == GameConstants.NB_OF_LEVELS_IN_GAME)
				start.setTouchable(Touchable.enabled);
		}
		if(gen == false & InternetManager.getWorld() != null){
			gen = true;
			show();
		}
	}

	private void init()
	{
		int n = this.numberOfLevelsDisplayed;
		float maxW = GameConstants.DISPLAY_WIDTH * 0.75f;
		float U0 = GameConstants.DISPLAY_HEIGHT*2.f/(float)n *0.75f;
		float Sn = GameConstants.DISPLAY_HEIGHT/2 + U0/2;

		Polynome p = new Polynome();

		p.add(-Sn/U0 + 1);
		for(int i = 0 ; i < n/2 ; i++)
		{
			p.add(1);
		}		
		sizeH = new float[n];
		sizeW = new float[n];
		posH = new float[n];
		posW = new float[n];
		Zindexes = new int[n];
		sizeH[(n)/2] = U0;
		sizeW[(n)/2] = maxW;
		Zindexes[(n)/2] = (n+1)/2;
		posW[(n)/2] = X - sizeW[(n)/2]/2;
		float q = p.solve(0.00001f);
		for(int i = 1 ; i < (n)/2 ; i++)
		{
			int sH = (int) (U0 * Math.pow(q, i * 0.75f));
			int sW = (int) (maxW * Math.pow(q, i * 0.55f));
			sizeH[n/2 - i] = sH;
			sizeH[n/2 +i] = sH;
			sizeW[n/2 - i] = sW;
			sizeW[n/2 + i ] = sW;
			Zindexes[n/2 - i] = n/2-i;
			Zindexes[n/2 + i] = n/2-i ; 
			posW[n/2-i] = X - sW/2;
			posW[n/2+i] = X - sW/2;
		}


		int y = 0 ; 
		for(int i = n - 1 ; i >= 0 ; i--)
		{
			posH[i] = y * 0.75f + GameConstants.DISPLAY_WIDTH * 0.1f;
			y+=sizeH[i];
		}
		posH[posH.length - 1] = GameConstants.DISPLAY_HEIGHT / 2;
		posH[0] = GameConstants.DISPLAY_HEIGHT / 2;
		posW[posH.length - 1] = GameConstants.DISPLAY_WIDTH / 2;
		posW[0] = GameConstants.DISPLAY_WIDTH / 2;
		Zindexes[0] = 0;
		Zindexes[Zindexes.length - 1] = 0; 
	}

	private void changeButtonsSize() {
		for(int i = 0 ; i < this.sizeH.length ; i++)
		{
			int index = (currentButton + i + tables.size() - sizeH.length /2 )%tables.size(); 
			table = tables.get(index);
			table.setZIndex(Zindexes[i] + 1);
			if(table.getZIndex() > 1)
				table.setVisible(true);
			else
				table.setVisible(false);
			table.addAction(Actions.parallel(Actions.moveTo(posW[i],posH[i], SPEED, Interpolation.pow2Out)));
			table.addAction(Actions.parallel(Actions.sizeTo(sizeW[i], sizeH[i], SPEED, Interpolation.pow2Out)));
			table.invalidate();
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
			world = World.getInstance();
			if(!world.hasBeenGenerated()){
				worldAsString = InternetManager.getWorld();
				WorldBuilder.build(worldAsString);
			}
			this.numberOfLevels = world.getNumberOfLevels();
			init();

			tables = new ArrayList<Table>();

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
			LabelStyle not_possible = new LabelStyle(font, Color.RED);
			not_possible.background = skin.getDrawable("button_up");


			start = new TextButton(localisation(_start), style);
			start.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					Gdx.app.log(RollingCat.LOG, "level selected : "+currentButton);
					game.setScreen(new LoadingScreen(game, patient, world.get(currentButton), world.getGems()));
				}
			});

			back = new TextButton(localisation(_back), style);
			back.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new PatientScreen(game, patient));
				}
			});

			previous = new TextButton(localisation(_previous), style);
			previous.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					previous();
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				}
			});

			next = new TextButton(localisation(_next), style);
			next.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					next();
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				}
			});

			score = new TextButton(localisation(_gems), style);
			score.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new GameProgressionScreen(game, patient, world.getGems(), false, null, 0));
				}
			});

			createLabels(labelStyle,not_possible);

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

	private void createLabels(LabelStyle style,LabelStyle notPossible) {
		tables = new ArrayList<Table>();
		for (int i = 0; i < GameConstants.NB_OF_LEVELS_IN_GAME + 1; i++) {
			addLabelsToTable(style, i);
			tables.add(table);
			stage.addActor(table);
			table.setVisible(true);
		}
		currentButton = numberOfLevels - 1;
	}
	private void next(){
		currentButton = (currentButton > tables.size() - 2)?0:currentButton + 1;
		changeButtonsSize();
		elapsedTime = 0;
	}

	private void previous(){
		currentButton = (currentButton < 1)?(tables.size() - 1):currentButton - 1;
		changeButtonsSize();
		elapsedTime = 0;
	}

	private void addLabelsToTable(LabelStyle style, int index){
		table = new Table();
		table.setBackground(skin.getDrawable("button_up"));
		style.background = skin.getDrawable("empty");
		if(index == 0){
			table.add(new Label(localisation(_tutorial), style)).expand().center();
		}
		else
		{
			if((index < numberOfLevels)){
				if(world.get(index).getContent() == null){
					style.fontColor = Color.RED;
					table.add(new Label(localisation(_new_level), style)).colspan(2).expand().center();
					table.row();
				}	
				style.fontColor = Color.BLACK;
				table.add(new Label(localisation(_level)+" " + (index), style)).left().expand();
				table.add(new Label(localisation(_score) + " : " + world.get(index).getScore() + " / " + ((world.get(index).getContent() != null)?world.get(index).getMaxScore():"?"), style)).right().expand();
				table.row();
				table.add(new Label(localisation(_duration) + " : " + ((world.get(index).getContent() != null)?world.get(index).getDuree():"?") + " s", style)).left().expand();
				table.add(new Label(localisation(_gem) + " " + ((world.get(index).getGem() == null)?localisation(_not_found):(world.get(index).getGem().equals("empty"))?localisation(_not_found):localisation(_found)), style)).right().expand();
			}	
			else{
				table.add(new Label(localisation(_level) + " " + (index), style)).left().expand();
				table.row();
				table.add(new Label(localisation(_locked), style)).center();
				table.row();
				table.add(new Label(" ", style));
			}
		}
	}
}
