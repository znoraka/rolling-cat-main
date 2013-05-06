package fr.lirmm.smile.rollingcat.utils;

import static fr.lirmm.smile.rollingcat.Localisation._settings;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.screen.SettingsScreen;

public class GdxRessourcesGetter {
	
	private static TextureAtlas atlasGame = new TextureAtlas(GameConstants.ATLAS);
	private static TextureAtlas atlasPatient = new TextureAtlas("data/patientAtlas.atlas");
	private static TextureAtlas dogAtlas, catAtlas, waspAtlas, carpetAtlas, fanAtlas;
	private static Skin skin, gameSkin;
	private static SpriteBatch batch = new SpriteBatch();
	private static BitmapFont bigFont, smallFont;
	private static Stage stage;
	private static ShapeRenderer sr;
	private static TextButton settings;
	
	/**
	 * 
	 * @param name le nom de la texture de l'élément
	 * @return un array d'AtlasRegion
	 */
	public static Array<AtlasRegion> getRegions(String name) {
		atlasGame.findRegion(name).getTexture().bind();
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		return atlasGame.findRegions(name) ;
	}
	
	public static TextureAtlas getAtlas(){
		return atlasGame;
	}
	
	/**
	 * 
	 * @return la skin utilisée par les différents screens
	 */
	public static Skin getGameSkin(){
		if(gameSkin == null){
			Gdx.app.log(RollingCat.LOG, "generating game skin");
			gameSkin = new Skin();
			gameSkin.addRegions(atlasGame);
		}
		return gameSkin;
	}
	
	/**
	 * 
	 * @return la skin utilisée par les différents screens
	 */
	public static Skin getSkin(){
		if(skin == null){
			Gdx.app.log(RollingCat.LOG, "generating skin");
			skin = new Skin();
			skin.addRegions(atlasPatient);
		}
		return skin;
	}
	
	/**
	 * 
	 * @return le batch utilisé par les différents screens
	 */
	public static SpriteBatch getSpriteBatch(){
		if(batch == null){
			Gdx.app.log(RollingCat.LOG, "generating batch");
			batch = new SpriteBatch();
		}
//		else{
//			batch.dispose();
//			batch = new SpriteBatch();
//		}
//		batch.dispose();
//		batch = new SpriteBatch();
		return batch;
	}

	/**
	 * 
	 * @return la font avec une grande police 
	 */
	public static BitmapFont getBigFont() {
		if(bigFont == null){
			Gdx.app.log(RollingCat.LOG, "generating big font");
			bigFont = new BitmapFont(Gdx.files.internal("data/font_24px.fnt"), false);
		}
		return bigFont;
	}
	
	/**
	 * 
	 * @return la font avec une petite police
	 */
	public static BitmapFont getSmallFont() {
		if(smallFont == null){
			Gdx.app.log(RollingCat.LOG, "generating small font");
			smallFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		}
		return smallFont;
	}
	
	/**
	 * 
	 * @return le stage utilisé par les différents screens
	 */
	public static Stage getStage() {
		if(stage == null){
			Gdx.app.log(RollingCat.LOG, "generating stage");
			stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true, getSpriteBatch());
		}
		else{
			stage.clear();
			stage.dispose();
			stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true, getSpriteBatch());
		}
		return stage;
	}
	
	/**
	 * 
	 * @return le shaperenderer utilisé par les différents screens
	 */
	public static ShapeRenderer getShapeRenderer() {
		if(sr == null){
			Gdx.app.log(RollingCat.LOG, "generating shaperenderer");
			sr = new ShapeRenderer();
		}
		return sr;
	}
	
	/**
	 * 
	 * @return le TextureAtlas du Dog
	 */
	public static TextureAtlas getDogAtlas(){
		if(dogAtlas == null)
			dogAtlas = new TextureAtlas("data/dog/dog.atlas");
		return dogAtlas;
	}
	
	/**
	 * 
	 * @return le TextureAtlas de la Wasp
	 */
	public static TextureAtlas getWaspAtlas(){
		if(waspAtlas == null)
			waspAtlas = new TextureAtlas("data/wasp/guepe-skin.atlas");
		return waspAtlas;
	}
	
	/**
	 * 
	 * @return le TextureAtlas du Carpet
	 */
	public static TextureAtlas getCarpetAtlas(){
		if(carpetAtlas == null)
			carpetAtlas = new TextureAtlas("data/carpet/carpet.atlas");
		return carpetAtlas;
	}
	
	/**
	 * 
	 * @return le TextureAtlas du Cat
	 */
	public static TextureAtlas getCatAtlas(){
		if(catAtlas == null)
			catAtlas = new TextureAtlas("data/cat/cat.atlas");
		return catAtlas;
	}
	
	/**
	 * 
	 * @return le TextureAtlas du Fan
	 */
	public static TextureAtlas getFanAtlas(){
		if(fanAtlas == null)
			fanAtlas = new TextureAtlas("data/fan/ventilo-skin.atlas");
		return fanAtlas;
	}
	
	public static TextButton getSettingsButton(final Screen screen, final RollingCat game, BitmapFont font){
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		settings = new TextButton(localisation(_settings), style);

		settings.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				screen.resume();
				game.setScreen(new SettingsScreen(game, screen));
			}
		});
		return settings;
	}
	
	
	
	
	
	
	
	
	
	
}
