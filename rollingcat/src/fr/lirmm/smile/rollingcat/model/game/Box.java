package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getGameSkin;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.Localisation;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.spine.Bone;

public class Box extends Entity{

	public static final int BONE = 1;
	public static final int SPRING = 2;
	public static final int SWATTER = 3;
	public static final int FEATHER = 4;
	public static final int SCISSORS = 5;
	public static final int EMPTY = 0;
		
	private int item;
	private TextureAtlas atlas;
	private ArrayList<Integer[]> items;
	
	public Box(float x, float y) {
		super(x, y, GameConstants.TEXTURE_BOX);
		atlas = getAtlas();
		this.setHeight(2 * this.getHeight());
		this.setWidth(2 * this.getWidth());
		this.setZIndex(2);
	}

	public void setItems(ArrayList<Integer[]> items)
	{
		this.items = items;
		this.item = items.get(0)[0];
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(!MouseCursorGame.isHoldingItem())
			batch.draw(getGameSkin().getRegion("green_highlight"), this.getX() - this.getWidth() * 0.25f, this.getY() - this.getHeight() * 0.25f, this.getWidth() * 1.5f, this.getHeight() * 1.5f);

		if(item == BONE)
			batch.draw(atlas.findRegion("box_bone"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == SPRING)
			batch.draw(atlas.findRegion("box_spring"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == SWATTER)
			batch.draw(atlas.findRegion("box_swatter"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == FEATHER)
			batch.draw(atlas.findRegion("box_feather"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == SCISSORS)
			batch.draw(atlas.findRegion("box_scissors"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else
			batch.draw(atlas.findRegion("box"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
	}
	
	/**
	 * empties the box
	 * @return the item previously in the box
	 */
	public int empty(){
		int a = item;
		item = 0;
		if(items.size() > 1)
			items.remove(0);
		return a;
	}
	
	/**
	 * fills the box with the next item on the items list
	 */
	public void fill(){
		this.item = items.get(0)[0];
	}
	
	/**
	 * 0 if the box is empty
	 * @return true if the box is empty
	 */
	public boolean isEmpty() {
		return item == 0;
	}
	
	public void emptyAfterNotMoving(int segment){
		while(items.get(0)[1] == segment){
			Gdx.app.log(RollingCat.LOG, "dropping "+items.get(0)[0]);
			items.remove(0);
		}
		
	}

	public static String getItemName(int item) {
		switch(item){
		case(BONE): return Localisation._bone;
		case(SWATTER): return Localisation._swatter;
		case(FEATHER): return Localisation._feather;
		case(SCISSORS): return Localisation._scissors;
		}
		return "";
	}

	public static String getRelatedEntity(int item) {
		switch(item){
		case(BONE): return Localisation._dog;
		case(SWATTER): return Localisation._wasp;
		case(FEATHER): return Localisation._cat;
		case(SCISSORS): return Localisation._carpet;
		}
		return "";
	}

}
