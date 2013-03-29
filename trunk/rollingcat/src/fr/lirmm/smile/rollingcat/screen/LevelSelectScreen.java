package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import utils.MainConstant;

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

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.utils.Polynome;

public class LevelSelectScreen implements Screen {

	private RollingCat game;
	private Patient patient;
	private Table table;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	private TextButton start, next, previous, back;
	private ArrayList<Label> labels;
	private int currentButton;
	private Label label;
	private final float X = (GameConstants.DISPLAY_WIDTH / 2);

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
		this.numberOfLevels = GameConstants.NB_OF_LEVELS_IN_MENU;

		this.init();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
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

	private void init()
	{
		int n = this.numberOfLevels ;
		float maxW = GameConstants.DISPLAY_WIDTH;
		float U0 = GameConstants.DISPLAY_HEIGHT*2.f/(float)n ;
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
		for(int i = 1 ; i <= (n)/2 ; i++)
		{
			int sH = (int) (U0 * Math.pow(q, i));
			int sW = (int) (maxW * Math.pow(q, i));
			sizeH[n/2 - i] = sH;
			sizeH[n/2 +i] = sH;
			sizeW[n/2 - i] = sW;
			sizeW[n/2 + i ] = sW;
			Zindexes[n/2 - i] = i;
			Zindexes[n/2 + i] = i ; 
			posW[n/2-i] = X - sW/2;
			posW[n/2+i] = X - sW/2;
		}


		int y = 0 ; 
		for(int i = 0 ; i< n ; i++)
		{
			posH[i] = y;
			y+=sizeH[i];
		}
		System.out.println("init finish");
	}

	private void changeButtonsSize() {
		System.out.println("change Size buttons");
		for(int i = 0 ; i < labels.size() ; i++)
		{
			if(!(i >= currentButton && i < currentButton + this.sizeH.length))
			{
				labels.get(i).setSize(sizeW[0]*0.5f, sizeH[0]*0.5f);
				labels.get(i).setVisible(false);
			}
		}

		for(int i = 0 ; i < this.sizeH.length ; i++)
		{
			System.out.println();
			System.out.println("currentButton : " +   currentButton);
			System.out.println("currentButtonAffiche : " +   ((currentButton + i )%labels.size()));
			System.out.println();
			int index =(currentButton + i + labels.size() - sizeH.length /2 )%labels.size();  
			
			System.out.println("indes : " + index);
//			label = labels.get((currentButton + i - this.sizeH.length/2 + 1)%labels.size()  );
			label = labels.get(index);
			label.setVisible(true);
			label.setZIndex(Zindexes[i] + 1);
			label.getTextBounds().width = sizeW[i]*0.5f;	
			label.getTextBounds().height = sizeH[i]*0.5f;	
			label.addAction(Actions.parallel(Actions.moveTo(posW[i],posH[i], SPEED)));
			label.addAction(Actions.parallel(Actions.sizeTo(sizeW[i], sizeH[i], SPEED)));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		System.out.println("Show");
		font = getBigFont();
		stage = getStage();
		skin = getSkin();
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

		if(labels == null)
		{
			this.createLabels(labelStyle);
		}

		start = new TextButton("Start", style);
		start.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("level selected : " + currentButton);
				game.setScreen(new LoadingScreen(game, patient,currentButton));
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
		//		

		changeButtonsSize();

		next.setX(GameConstants.DISPLAY_WIDTH - next.getWidth());
		start.setX(GameConstants.DISPLAY_WIDTH - start.getWidth());
		start.setY(GameConstants.DISPLAY_HEIGHT - start.getHeight()); 
		back.setX(0);
		back.setY(GameConstants.DISPLAY_HEIGHT - start.getHeight()); 
		
		//		stage.addActor(table);
		stage.addActor(start);
		stage.addActor(back);
		stage.addActor(previous);
		stage.addActor(next);

		
		Gdx.input.setInputProcessor(stage);
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
		for (int i = 0; i < GameConstants.NB_OF_LEVELS_IN_GAME; i++) {
			label = new Label(""+i, style);
			label.setName(""+i);
			label.setX(GameConstants.DISPLAY_WIDTH / 2 - label.getWidth() / 2);
			labels.add(label);
			stage.addActor(label);
			label.setVisible(false);
		}
		System.out.println("Labels creation");
	}
	private void next(){
		System.out.println("next");
		currentButton = (currentButton > labels.size() - 2)?0:currentButton + 1;
		changeButtonsSize();
		elapsedTime = 0;
	}

	private void previous(){
		System.out.println("previous");
		currentButton = (currentButton < 1)?(labels.size() - 1):currentButton - 1;
		changeButtonsSize();
		elapsedTime = 0;
	}


/*
	public static void main(String args[])
	{
		int tabSize[] = {2,3,4};
		int tabX[] = {2,3,2,3,4,2,3,4,5};
		int zoneWorkX = 16;
		int zoneWorkyY= 12 + 2;;
		int index = 0;
		for(int i = 0 ; i < tabSize.length ; i++)
		{
			for(int j = index ; j < tabSize[i] + index ; j++) 
			{
				tabX[j] = tabX[j] + (zoneWorkX* i);
			}
			index += tabSize[i];
		}
		System.out.println(Arrays.toString(tabX));
	}
	*/
}
