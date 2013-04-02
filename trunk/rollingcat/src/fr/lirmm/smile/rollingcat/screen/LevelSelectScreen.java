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
import com.badlogic.gdx.math.Interpolation;
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
import fr.lirmm.smile.rollingcat.utils.Polynome;

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
	private final float X = (GameConstants.DISPLAY_WIDTH / 2);
	private String[] levels, gems;
	private ArrayList<String> listOfGems;
	private String world;
	private boolean gen;


	private float elapsedTime;


	private final float SPEED = 0.2f;
	private float[] sizeH;
	private float[] sizeW;
	private float[] posH;
	private float[] posW;
	private int[] Zindexes;
	private int numberOfLevels;

	public LevelSelectScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
		//		this.numberOfLevels = GameConstants.NB_OF_LEVELS_IN_MENU;
		this.numberOfLevels = 5;

	}

	@Override
	public void render(float delta) {
		if(labels != null)
		{
			for(Label l : labels)
			{
				l.setVisible(false);
			}
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
		}
		if(gen == false & InternetManager.getWorld() != null){
			gen = true;
			show();
		}
	}

	private void initBeforeThreeElements()
	{
		int n = 3 ;
		sizeH = new float[n];
		sizeW = new float[n];
		posH = new float[n];
		posW = new float[n];
		Zindexes = new int[n];

		float maxW = GameConstants.DISPLAY_WIDTH * 0.75f;
		float U0 = GameConstants.DISPLAY_HEIGHT*2.f/(float)n *0.75f;
		sizeH[1] = U0;
		sizeH[0] = sizeH[1]*0.75f;
		sizeH[2] = sizeH[1]*0.75f;
		sizeW[0] = maxW*0.75f;
		sizeW[1] = maxW;
		sizeW[2] = maxW*0.75f;
		for(int i = 0 ; i < n ; i++)
		{
			posW[i] = X - sizeW[i]/2;	
		}
		Zindexes[0] = 0;
		Zindexes[1] = 1;
		Zindexes[2] = 0;
		posH[1] = GameConstants.DISPLAY_HEIGHT/2 - sizeH[1]/2;
		posH[0] = posH[1] - sizeH[1]/4;
		posH[2] = posH[1] + sizeH[1]/2;
	
	}
	private void init()
	{
		int n = this.numberOfLevels ;
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
//		System.out.println(p);
//		System.out.println("solve : " + numberOfLevels);
		float q = p.solve(0.00001f);
//		System.out.println("end solve");
		for(int i = 1 ; i <= (n)/2 ; i++)
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
			posH[i] = y * 0.75f + GameConstants.DISPLAY_WIDTH * 0.05f;
			y+=sizeH[i];
		}
	}

	private void changeButtonsSize() {
		for(int i = 0 ; i < this.sizeH.length ; i++)
		{
			int index = (currentButton + i + labels.size() - sizeH.length /2 )%labels.size(); 
			label = labels.get(index);
			label.setVisible(true);
			label.setZIndex(Zindexes[i] + 1);
			label.getTextBounds().width = sizeW[i]*0.5f;	
			label.getTextBounds().height = sizeH[i]*0.5f;	
			label.addAction(Actions.parallel(Actions.moveTo(posW[i],posH[i], SPEED, Interpolation.pow2Out)));
			label.addAction(Actions.parallel(Actions.sizeTo(sizeW[i], sizeH[i], SPEED, Interpolation.pow2Out)));
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
			@SuppressWarnings("unchecked")
			OrderedMap<String, Object> map = (OrderedMap<String, Object>) new JsonReader().parse(world);
			levels = json.readValue("levels", String[].class, map);
			gems = json.readValue("gems", String[].class, map); 

			listOfGems = new ArrayList<String>();
			for (String s : gems) {
				System.out.println(s);
				if(!s.equals("empty"))
				{
					listOfGems.add(s+GameConstants.TEXTURE_GEM);
				}
				else
				{
					listOfGems.add(s);	
				}
			}
			this.numberOfLevels = levels.length;
			if(levels.length < 3)
			{
				this.initBeforeThreeElements();
			}
			else
			{
				numberOfLevels += numberOfLevels%2+1;
				if(numberOfLevels > GameConstants.NB_OF_LEVELS_IN_MENU)
				{
					numberOfLevels = GameConstants.NB_OF_LEVELS_IN_MENU;
				}
				init();
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
					game.setScreen(new LoadingScreen(game, patient, currentButton, listOfGems));
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
					game.setScreen(new GameProgressionScreen(game, patient, listOfGems, listOfGems.size() == levels.length, null, 0));
				}
			});

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
		labels = new ArrayList<Label>();
		String value = "";
		int s = Math.max(levels.length, 3);
		for (int i = 0; i <= s; i++) {
			value = ""+i;
			if( i >= levels.length)
			{
				value += " ?";
			}
			label = new Label(value, style);
			label.setName(value);
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
