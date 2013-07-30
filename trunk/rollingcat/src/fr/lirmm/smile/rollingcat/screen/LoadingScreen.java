package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._ready;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class LoadingScreen implements Screen {

	private RollingCat game;
	private Patient patient;
	private Stage stage, stage1;
	private Texture texture, backGroundTexture;
	private SpriteBatch batch;
//	private String levelAsString;
	private Level level;
	private List<String> listOfGem;
	private float elapsedTime;
	private ShapeRenderer sr;
	private boolean building, ready, started;
	private int time;
	private TextButton start;

	public LoadingScreen(RollingCat game, Patient patient, Level level, List<String> listOfGem){
		this.game = game;
		this.patient = patient;
		this.level = level;
		this.listOfGem = listOfGem;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if(elapsedTime < time)
			elapsedTime += delta;

		batch.begin();
		batch.draw(backGroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.draw(texture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();

//		sr.setColor(Color.GREEN);
//		sr.begin(ShapeType.Filled);
//		sr.rect(GameConstants.DISPLAY_WIDTH * 0.1f, GameConstants.DISPLAY_HEIGHT * 0.03f, GameConstants.DISPLAY_WIDTH * 0.8f * elapsedTime / time - 2, GameConstants.BLOCK_HEIGHT);
//		sr.end();
//
//		sr.setColor(Color.BLACK);
//		sr.begin(ShapeType.Line);
//		sr.rect(GameConstants.DISPLAY_WIDTH * 0.1f, GameConstants.DISPLAY_HEIGHT * 0.03f, GameConstants.DISPLAY_WIDTH * 0.8f, GameConstants.BLOCK_HEIGHT);
//		sr.end();

		if(!started & InternetManager.sessionid != null)
		{
			started = true;
			InternetManager.fetchLevel(level.getId()); 
		}

//		levelAsString = Level.getContent();

		if(Level.getContent() != null & !building)
		{
			building = true;
			this.stage = LevelBuilder.build(Level.getContent());
		}

		if(Level.getContent() != null & elapsedTime > time & InternetManager.ability != null){
			ready = true;
		}



		if(ready){
			stage1.draw();
			stage1.act(delta);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		sr = getShapeRenderer();

		stage1 = new Stage();
		
		started = false;

		InternetManager.newGameSession(Track.GAME, patient.getID());
		InternetManager.getAbilityZone();

		texture = new Texture("data/loading"+RollingCat.skin+".png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backGroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND + RollingCat.skin + ".png");
		backGroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		elapsedTime = 0;
		building = false;
		time = 5;

		TextButtonStyle style = new TextButtonStyle();
		style.up = getSkin().getDrawable("button_up");
		style.down = getSkin().getDrawable("button_down");
		style.font = getBigFont();
		style.fontColor = Color.BLACK;

		start = new TextButton(localisation(_ready), style);
		start.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game, stage, level));
			}
		});

		start.setX(GameConstants.DISPLAY_WIDTH * 0.5f - start.getWidth() * 0.5f);
		start.setY(GameConstants.DISPLAY_HEIGHT * 0.5f - start.getHeight() * 0.5f);

		stage1.addActor(start);
		
		
		ready = false;

		Gdx.input.setInputProcessor(stage1);
		

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
		texture.dispose();
	}

}
