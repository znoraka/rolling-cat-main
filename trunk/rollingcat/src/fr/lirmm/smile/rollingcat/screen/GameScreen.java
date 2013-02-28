package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class GameScreen implements Screen{

	private RollingCat game;
	private Stage stage;
	private Texture backgroundTexture;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private MouseCursorGame mc;
	private Cat cat;
	private Box box;
	private BitmapFont font;
	private Patient patient;
	private float duration;
	

	public GameScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		duration += delta;
		mc.updateHoverTimer(stage);
		mc.updateHitTimer(stage);
		cat.move(stage);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		font.draw(batch, ""+cat.getNbCoin()+ " coins", 10, 590);
		font.draw(batch, "holding bone : "+cat.isHoldingBone(), 200, 590);
		font.draw(batch, "fps : " + Gdx.graphics.getFramesPerSecond(), 26, 65);
		batch.end();
		stage.act(delta);
		stage.draw();
		sr.setProjectionMatrix(stage.getCamera().combined);
        mc.render(sr);
        updateCamPos();
        mc.addTrackingPoint(delta);
        if(cat.isDone()){
        	patient.addTrack(new Track(mc.getMap(), Track.GAME, duration));
        	game.setScreen(new TrackingRecapScreen(game, patient));
        }
	}
	
	/**
	 * translate la camera si le chat est au bout de l'ecran
	 */
	private void updateCamPos() {
		if(stage.getCamera().position.x + GameConstants.DISPLAY_WIDTH / 2 < cat.getX()){
			stage.getCamera().translate(GameConstants.DISPLAY_WIDTH, 0, 0);
			box.setX(box.getX() + GameConstants.DISPLAY_WIDTH);
			mc.stop();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		backgroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		batch = new SpriteBatch();
		this.stage = LevelBuilder.readLevel();
		cat = (Cat) stage.getActors().get(0);
		box = new Box(8, 0);
		stage.addActor(box);
		sr = new ShapeRenderer();
		mc = new MouseCursorGame(stage, cat, box);
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		Gdx.input.setInputProcessor(mc);
		duration = 0;
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
//		LevelBuilder.writeLevel(stage);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		backgroundTexture.dispose();
		sr.dispose();
		batch.dispose();
		font.dispose();
		mc.dispose();
	}

}