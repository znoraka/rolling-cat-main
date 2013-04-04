package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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

	public LevelSelectScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}

	@Override
	public void render(float delta) {
		if(tables != null)
		{
			for(Table l : tables)
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
	}

	private void changeButtonsSize() {
		for(int i = 0 ; i < this.sizeH.length ; i++)
		{
			int index = (currentButton + i + tables.size() - sizeH.length /2 )%tables.size(); 
			table = tables.get(index);
			table.setVisible(true);
			table.setZIndex(Zindexes[i] + 1);
//			table.getTextBounds().width = sizeW[i]*0.5f;	
//			table.getTextBounds().height = sizeH[i]*0.5f;	
//			table.setBounds(posW[i],posH[i], sizeW[i], sizeH[i]);
			table.addAction(Actions.parallel(Actions.moveTo(posW[i],posH[i], SPEED, Interpolation.pow2Out)));
			table.addAction(Actions.parallel(Actions.sizeTo(sizeW[i], sizeH[i], SPEED, Interpolation.pow2Out)));
//			System.out.println(table.getHeight());
////			table.setX(posW[i]);
////			table.setY(posH[i]);
////			table.setWidth(sizeW[i]);
////			table.setHeight(sizeH[i]);
////			table.drawDebug(stage);
//			for (int j = 0; j < table.getCells().size(); j++) {
//				table.getCells().get(j).fill().expand();
//			}
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
			if(this.numberOfLevels < 3)
			{
				this.initBeforeThreeElements();
			}
			else
			{
				init();
			}


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


			start = new TextButton("Start", style);
			start.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					Gdx.app.log(RollingCat.LOG, "level selected : "+currentButton);
					game.setScreen(new LoadingScreen(game, patient, world.get(currentButton), world.getGems()));
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
					game.setScreen(new GameProgressionScreen(game, patient, world.getGems(), false, null, 0));
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
		tables = new ArrayList<Table>();
		int s = Math.max(numberOfLevels, 3);
		for (int i = 0; i < s; i++) {
			addLabelsToTable(style, i);
			tables.add(table);
			stage.addActor(table);
			table.setVisible(false);
		}
		currentButton = numberOfLevels;
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
		if(world.get(index).getContent() == null){
			style.fontColor = Color.RED;
			table.add(new Label("nouveau niveau !", style)).fill().expand().align(Align.center);
			table.row();
		}	
		style.fontColor = Color.BLACK;
		table.add(new Label("niveau numéro " + index, style)).fill().expand();
		table.add(new Label("score " + world.get(index).getScore(), style)).fill().expand();
		table.row();
		table.add(new Label("durée " + world.get(index).getDuree(), style)).fill().expand();
		table.add(new Label("gemme " + ((world.get(index).getGem() == null)?"inconnue":(world.get(index).getGem().equals("empty"))?"inconnue":"trouvée"), style)).fill().expand();
	}
}
