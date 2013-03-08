package fr.lirmm.smile.rollingcat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class TextureFetcher {
	
	private static TextureAtlas atlasGame = new TextureAtlas(GameConstants.ATLAS);
	private static TextureAtlas atlasPatient = new TextureAtlas("data/patientAtlas.atlas");
	private static Skin skin;
	
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
	
	
}
