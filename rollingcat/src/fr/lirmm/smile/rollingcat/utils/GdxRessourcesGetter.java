package fr.lirmm.smile.rollingcat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class GdxRessourcesGetter {
	
	private static TextureAtlas atlasGame = new TextureAtlas(GameConstants.ATLAS);
	private static TextureAtlas atlasPatient = new TextureAtlas("data/patientAtlas.atlas");
	private static Skin skin;
	private static SpriteBatch batch = new SpriteBatch();
	private static BitmapFont bigFont, smallFont;
	private static Stage stage;
	private static ShapeRenderer sr;
	
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
//		if(batch == null){
//			Gdx.app.log(RollingCat.LOG, "generating batch");
//			batch = new SpriteBatch();
//		}
//		else{
//			batch.dispose();
//			batch = new SpriteBatch();
//		}
		batch.dispose();
		batch = new SpriteBatch();
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
			stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
		}
		else{
			stage.dispose();
			stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
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
		else{
			sr.dispose();
			sr = new ShapeRenderer();
		}
		return sr;
	}
	
	
	
	
	
	
	
	
	
	
}
