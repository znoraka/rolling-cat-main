package fr.lirmm.smile.rollingcat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;

public class TextureFetcher {
	
	private static TextureAtlas t = new TextureAtlas(GameConstants.ATLAS);
	
	/**
	 * 
	 * @param name le nom de la texture de l'élément
	 * @return un array d'AtlasRegion
	 */
	public static Array<AtlasRegion> getRegions(String name) {
		t.findRegion(name).getTexture().bind();
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		return t.findRegions(name) ;
	}
	
	
}
