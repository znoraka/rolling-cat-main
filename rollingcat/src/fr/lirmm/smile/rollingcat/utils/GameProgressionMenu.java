package fr.lirmm.smile.rollingcat.utils;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.screen.LevelSelectScreen;

public class GameProgressionMenu implements Screen{

	private int size;
	private int centerX,centerY,rayon;
	private Stage stage;
	private List<Image> entities;
	private List<Image> trous;
	private TextButton back;

	private Skin skin;	
	private BitmapFont font;
	private RollingCat game;
	private Patient patient;

	private Point centralDiamond;
	private float sizeCentralDiamond;
	private int nbLevelsWin;
	private boolean bossWin;
	private Image centralDiamondEntity;
	private List<String> gems;



	public GameProgressionMenu(
			RollingCat game, Patient patient,
			int centerX, 
			int centerY, 
			int rayon, 
			List<String> gems,
			boolean bossWin)
	{
		this.gems = gems;
		this.nbLevelsWin = gems.size();
		this.bossWin = bossWin;

		this.centerX = centerX;
		this.centerY = centerY;
		this.rayon = rayon;
		this.sizeCentralDiamond = rayon*0.7f;
		this.centralDiamond = new Point((int)(centerX-sizeCentralDiamond/2),(int)(centerY-sizeCentralDiamond/2));
		size = (int) ((int) (2*Math.PI * rayon / GameConstants.NB_OF_LEVELS_IN_GAME) * 0.8f); 
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
			/*			int b = i+1 < items.length ? i+1 : 0;
			tmp = items[a];
			items[a] = items[b];
			items[b] = tmp;
			 */
		}	
		for(int i = 0 ; i < trous.size(); i++)
		{
			trous.get(i).setX( (int) (Math.cos(Math.toRadians(i*step + angle)) * rayon) + centerX - trous.get(i).getWidth()/2);
			trous.get(i).setY( (int) (Math.sin(Math.toRadians(i*step + angle)) * rayon) + centerY - trous.get(i).getHeight()/2);
		}
	}

	private void createItemsShape() {
		entities = new ArrayList<Image>();
		this.initTrous();
		float step = 360.0f/GameConstants.NB_OF_LEVELS_IN_GAME;
		for (int i = 0; i < gems.size() ; i++) 
		{
			int x = (int) (Math.cos(Math.toRadians(i*step)) * rayon) + centerX;
			int y = (int) (Math.sin(Math.toRadians(i*step)) * rayon)+ centerY;

			Image button = new Image(GdxRessourcesGetter.getAtlas().findRegion(gems.get(i)));
			button.setWidth(size*0.5f);
			button.setHeight(size*0.5f);
			button.setX(x - button.getWidth()/2);
			button.setY(y - button.getHeight()/2);
			entities.add(button);
			stage.addActor(entities.get(entities.size()-1));
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
		Gdx.gl.glClear(Gdx.gl10.GL_COLOR_BUFFER_BIT);

		elapsedTime+=delta;
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

		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;

		if(entities == null)
		{
			this.createItemsShape();

			back = new TextButton("Back", style);
			back.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new LevelSelectScreen(game, patient,GameConstants.NB_OF_LEVELS_IN_MENU));
				}
			});

		}		
		stage.addActor(back);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
