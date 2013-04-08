package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import static fr.lirmm.smile.rollingcat.Localisation.*;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class GameProgressionScreen implements Screen{

	private int size;
	private int centerX,centerY,rayon;
	private Stage stage;
	private List<Image> entities;
	private List<Image> trous;
	private TextButton back;

	private Texture backgroundTexture;
	private SpriteBatch batch;

	private Skin skin;	
	private BitmapFont font;
	private RollingCat game;
	private Patient patient;

	private Vector2 centralDiamond;
	private float sizeCentralDiamond;
	private int nbLevelsWin;
	private boolean bossWin;
	private Image centralDiamondEntity;
	private Image woordCircle;
	private List<String> gems;
	private Target gem;
	private int level;



	public GameProgressionScreen(RollingCat game, Patient patient, List<String> gems, boolean bossWin, Target gem, int level)
	{
		this.gems = gems;
		this.nbLevelsWin = gems.size();
		this.bossWin = bossWin;

		this.patient = patient;
		this.game = game;

		this.centerX = GameConstants.DISPLAY_WIDTH / 2 ;
		this.centerY = GameConstants.DISPLAY_HEIGHT / 2;
		this.rayon = (int) (GameConstants.DISPLAY_WIDTH  * 0.2f);
		this.sizeCentralDiamond = rayon*0.7f;
		this.centralDiamond = new Vector2((centerX-sizeCentralDiamond/2), (centerY-sizeCentralDiamond/2));
		size = (int) ((int) (2*Math.PI * rayon / GameConstants.NB_OF_LEVELS_IN_GAME) * 0.6f); 
		this.gem = gem;
		this.level = level;
	}

	public void initTrous()
	{
		trous = new ArrayList<Image>();
		float step = 360.0f/GameConstants.NB_OF_LEVELS_IN_GAME;
		for(int i = 0 ; i < GameConstants.NB_OF_LEVELS_IN_GAME ; i++)
		{
			int x = (int) (Math.cos(Math.toRadians(i*step)) * rayon) + centerX;
			int y = (int) (Math.sin(Math.toRadians(i*step)) * rayon)+ centerY;
			Image button = new Image(GdxRessourcesGetter.getAtlas().findRegion("trou1"));
			button.setWidth(size);
			button.setHeight(size);
			button.setX(x - button.getWidth()/2);
			button.setY(y - button.getHeight()/2);
			button.setOrigin(button.getWidth()/2, button.getHeight()/2);
			trous.add(button);
			stage.addActor(trous.get(trous.size()-1));
		}

	}


	public void rotateEffect(int angle)
	{
		float step = 360.0f/GameConstants.NB_OF_LEVELS_IN_GAME;
		for(int i = 0 ; i < entities.size(); i++)
		{
			float x =  (float) ((Math.cos(Math.toRadians(i*step + angle)) * rayon) + centerX - entities.get(i).getWidth()/2);
			float y =  (float) ((Math.sin(Math.toRadians(i*step + angle)) * rayon) + centerY - entities.get(i).getHeight()/2);
			entities.get(i).setX(x);
			entities.get(i).setY(y);
			entities.get(i).rotate(1);
		}	
		for(int i = 0 ; i < trous.size(); i++)
		{
			trous.get(i).setX( (int) (Math.cos(Math.toRadians(i*step + angle)) * rayon) + centerX - trous.get(i).getWidth()/2);
			trous.get(i).setY( (int) (Math.sin(Math.toRadians(i*step + angle)) * rayon) + centerY - trous.get(i).getHeight()/2);
			trous.get(i).rotate(1);
		}
		woordCircle.rotate(1);
	}

	private void createItemsShape() {
		entities = new ArrayList<Image>();
		this.initTrous();
		float step = 360.0f/GameConstants.NB_OF_LEVELS_IN_GAME;
		for (int i = 0; i < gems.size() ; i++) 
		{
			/*int x = (int) (Math.cos(Math.toRadians(i*step)) * rayon) + centerX;
			int y = (int) (Math.sin(Math.toRadians(i*step)) * rayon)+ centerY;
			 */
			Vector2 p = this.getPosition(i);
			if(gems.get(i) != null && !gems.get(i).equals("empty")){
				Image button = new Image(GdxRessourcesGetter.getAtlas().findRegion(gems.get(i)+"_gem"));
				button.setWidth(size*0.5f);
				button.setHeight(size*0.5f);
				button.setX(p.x - button.getWidth()/2);
				button.setY(p.y - button.getHeight()/2);

				button.setOrigin(button.getWidth()/2, button.getHeight()/2);

				entities.add(button);
				stage.addActor(entities.get(entities.size()-1));
			}
		}
		initCentralDiamond();
	}

	private int angle = 0;
	private float elapsedTime;
	private boolean isCentralButtonDeclenched;

	private void initCentralDiamond()
	{
		if(bossWin)
		{
			this.centralDiamondEntity = new Image(GdxRessourcesGetter.getAtlas().findRegion("diamant"));
			this.centralDiamondEntity.setWidth(sizeCentralDiamond);
			this.centralDiamondEntity.setHeight(sizeCentralDiamond);
			this.centralDiamondEntity.setX(centralDiamond.x);
			this.centralDiamondEntity.setY(centralDiamond.y);
			centralDiamondEntity.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					isCentralButtonDeclenched = true;
				}
			});
			stage.addActor(centralDiamondEntity);
		}
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		elapsedTime+=delta;

		batch.begin();
		batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();


		if(isCentralButtonDeclenched)
		{
			if(elapsedTime > 0.3)
			{
				angle = (angle + 1 )%360;
				elapsedTime -= delta;
			}
			rotateEffect(angle);
		}


		stage.act(delta);
		stage.draw();



	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() 
	{

		font = getBigFont();
		stage = getStage();
		skin = getSkin();
		batch = new SpriteBatch();
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		backgroundTexture = new Texture("data/backgroundGameProgression.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		if(entities == null)
		{
			woordCircle =new Image(GdxRessourcesGetter.getAtlas().findRegion("woodCircle"));
			woordCircle.setWidth(rayon*2.4f);
			woordCircle.setHeight(rayon*2.4f);
			woordCircle.setOrigin(woordCircle.getWidth()/2, woordCircle.getHeight()/2);
			woordCircle.setX(centerX - woordCircle.getWidth()/2);
			woordCircle.setY(centerY - woordCircle.getHeight()/2);


			stage.addActor(woordCircle);
			this.createItemsShape();

			back = new TextButton(localisation(_back), style);
			back.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new LevelSelectScreen(game, patient));
				}
			});

		}		
		stage.addActor(back);

		Gdx.input.setInputProcessor(stage);

		if(gem != null){
			Vector2 tmp = getPosition(level);
			this.gem.setOrigin(this.gem.getWidth() * 0.5f, this.gem.getHeight() * 0.5f);
			this.gem.setX(this.gem.getX() % GameConstants.DISPLAY_WIDTH);
			this.gem.addAction(Actions.parallel(Actions.sizeTo(size*0.5f, size*0.5f, 2)));
			this.gem.addAction(Actions.parallel(Actions.moveTo(tmp.x - size * 0.25f, tmp.y - size * 0.25f, 2)));
			this.gem.addAction(Actions.sequence(Actions.delay(2), new Action() {

				@Override
				public boolean act(float delta) {
					gems.add(gem.getCouleur()+"_gem");
					Image tmp = new Image(GdxRessourcesGetter.getAtlas().findRegion(gem.getCouleur()+"_gem"));
					tmp.setBounds(gem.getX(), gem.getY(), gem.getWidth(), gem.getHeight());
					tmp.setOrigin(tmp.getWidth()/2, tmp.getHeight()/2);
					entities.add(tmp);
					gem.setVisible(false);
					stage.addActor(tmp);
					return true;
				}
			}));
			stage.addActor(gem);
		}

	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() 
	{
		backgroundTexture.dispose();
	}

	public Vector2 getPosition(int indexLevel)
	{
		float step = 360.0f/GameConstants.NB_OF_LEVELS_IN_GAME;
		int x = (int) (Math.cos(Math.toRadians(indexLevel*step)) * rayon) + centerX;
		int y = (int) (Math.sin(Math.toRadians(indexLevel*step)) * rayon)+ centerY;
		return new Vector2(x, y);
	}
}
