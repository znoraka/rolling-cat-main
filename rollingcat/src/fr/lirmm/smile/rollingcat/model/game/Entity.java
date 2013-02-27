package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Entity extends Image {
	protected Animation anim;
	private float d;
	private String name;
	
	/**
	 * 
	 * @param x le x sur la grille (entre 0 et 15)
	 * @param y le y sur la grille (entre 0 et 11)
	 * @param name le nom de l'entity pour trouver sa texture dans le texture atlas
	 */
	public Entity(float x, float y, String name){
		super(getRegions(getTexture(), name).get(0)	);
		this.setX(x * GameConstants.BLOCK_WIDTH);
		this.setY(y * GameConstants.BLOCK_HEIGHT);
		this.setBounds(x * GameConstants.BLOCK_WIDTH, y * GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
		this.setWidth(GameConstants.BLOCK_WIDTH);
		this.setHeight(GameConstants.BLOCK_HEIGHT);
        this.anim = new Animation(0.25f, getRegions(getTexture(), name));
        this.name = name;
	}
	
	/**
	 * 
	 * @param texture le textureAtlas
	 * @param name le nom de la texture de l'élément
	 * @return un array d'AtlasRegion
	 */
	private static Array<AtlasRegion> getRegions(TextureAtlas texture, String name) {
		texture.findRegion(name).getTexture().bind();
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		return texture.findRegions(name) ;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		d += Gdx.graphics.getDeltaTime();
		batch.draw(anim.getKeyFrame(d, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	/**
	 * 
	 * @return le texture atlas utilisé
	 */
	private static TextureAtlas getTexture()
	{
		TextureAtlas t = new TextureAtlas(GameConstants.ATLAS);
		return t;
	}
	
	/**
	 * @return le nom de l'entity (nom utilisé pour trouver la texture dans l'atlas)
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @return l'action à effectuer sur l'acteur
	 */
	public Action getAction() {
		return null;
	}
	
	/**
	 * donne la position dans la grille et blocs pas en pixels
	 * @return la case en x dans laquelle se trouve le chat
	 */
	public int getXOnGrid(){
		return (int) (this.getX()/GameConstants.BLOCK_WIDTH);
	}
	
	/**
	 * donne la position dans la grille et blocs pas en pixels
	 * @return la case en y dans laquelle se trouve le chat
	 */
	public int getYOnGrid(){
		return (int) (this.getY()/GameConstants.BLOCK_HEIGHT);
	}
	
	/**
	 * 
	 * @return l'action que la chat va faire au contact de cette Entity
	 */
	public Action getActionOnCat(){
		return null;
	}
}
