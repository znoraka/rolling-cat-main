package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getGameSkin;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.utils.EntityModel;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class Entity extends Image implements EntityModel {
	protected Animation anim;
	protected float d;
	private String name;
	protected Rectangle bounds;
	protected boolean requestRedHighlight;
	protected ShapeRenderer sr;
	
	/**
	 * 
	 * @param x le x sur la grille (entre 0 et 15)
	 * @param y le y sur la grille (entre 0 et 11)
	 * @param name le nom de l'entity pour trouver sa texture dans le texture atlas
	 */
	public Entity(float x, float y, String name){
		this.setX(x * GameConstants.BLOCK_WIDTH);
		this.setY((y + 2) * GameConstants.BLOCK_HEIGHT);
		this.setWidth(GameConstants.BLOCK_WIDTH);
		this.setHeight(GameConstants.BLOCK_HEIGHT);
        this.anim = new Animation(0.25f, GdxRessourcesGetter.getRegions(name));
        this.name = name;
        this.bounds = new Rectangle(x * GameConstants.BLOCK_WIDTH, (y + 2) * GameConstants.BLOCK_HEIGHT, GameConstants.BLOCK_WIDTH, GameConstants.BLOCK_HEIGHT);
        this.d = new Random().nextFloat();
        this.requestRedHighlight = false;
		sr = GdxRessourcesGetter.getShapeRenderer();
	}

	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(this.getEtage() == Cat.getInstance().getEtage() && this.getSegment() == Cat.getInstance().getSegment())
		{
			d += Gdx.graphics.getDeltaTime();
			batch.draw(anim.getKeyFrame(d, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
	}
	
	
	public void drawDebug(ShapeRenderer sr){
		sr.begin(ShapeType.Line);
		sr.setColor(Color.PINK);
		sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		sr.end();
	}
	
	/**
	 * highlight l'entité en vert si l'objet tenu correspond à l'objet requis en rouge si la souris est sur 
	 * l'entité mais que l'objet tenu ne correspond pas
	 * @param batch
	 */
	protected void highlight(SpriteBatch batch){
		if(MouseCursorGame.isHoldingItem() & MouseCursorGame.getItem() == this.getItemToAct() & Cat.getInstance().getSegment() == this.getSegment() & Cat.getInstance().getEtage() == this.getEtage()){
			batch.draw(getGameSkin().getRegion("green_highlight"), this.getX() - this.getWidth() * 0.25f, this.getY() - this.getHeight() * 0.25f, this.getWidth() * 1.5f, this.getHeight() * 1.5f);
		}
		if(requestRedHighlight){
			batch.draw(getGameSkin().getRegion("red_highlight"), this.getX() - this.getWidth() * 0.25f, this.getY() - this.getHeight() * 0.25f, this.getWidth() * 1.5f, this.getHeight() * 1.5f);
			requestRedHighlight(false);
		}
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
		return (int) ((this.getX() + GameConstants.BLOCK_WIDTH  * 0.5f * Cat.getInstance().getSens())/GameConstants.BLOCK_WIDTH);
	}
	
	/**
	 * donne la position dans la grille et blocs pas en pixels
	 * @return la case en y dans laquelle se trouve le chat
	 */
	public int getYOnGrid(){
		return (int) (this.getY()/GameConstants.BLOCK_HEIGHT);
	}
	
	
	public Rectangle getBounds(){
		return this.bounds;
	}

	public int getItemToAct() {
		return Box.EMPTY;
	}

	public void requestRedHighlight(boolean b) {
		requestRedHighlight = b;
	}
	
	/**
	 * 
	 * @return l'étage auquel se trouve l'entité
	 */
	public int getEtage(){
		return (int) (Math.floor((this.getY() / (GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT))));
	}
	
	/**
	 * 
	 * @return le segment dans lequel se trouve l'entité
	 */
	public int getSegment()
	{
		return (int) (this.getXOnGrid() - 1 * Cat.getInstance().getSens()) / GameConstants.COLS;
	}
	
	public void drawAllowedArea()
	{
//		sr.setColor(Color.BLACK);
//		sr.begin(ShapeType.Line);
//		sr.line(
//				this.getX() % GameConstants.DISPLAY_WIDTH, 
//				this.getY() % (GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT),
//				Box.getInstance().getX() % GameConstants.DISPLAY_WIDTH,
//				Box.getInstance().getY() % (GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT)
//				);
//		sr.end();
	}
}
