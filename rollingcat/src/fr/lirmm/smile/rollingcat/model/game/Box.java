package fr.lirmm.smile.rollingcat.model.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class Box extends Entity{

	public static int BONE = 1;
	public static int SPRING = 2;
	public static int SWATTER = 3;
	public static int FEATHER = 4;
		
	private int item;
	private TextureAtlas atlas;
	private ArrayList<Integer> items;
	
	public Box(float x, float y) {
		super(x, y, GameConstants.TEXTURE_BOX);
		atlas = new TextureAtlas(GameConstants.ATLAS);
		this.setHeight(2 * this.getHeight());
		this.setWidth(2 * this.getWidth());
		this.items = LevelBuilder.getItems();
		this.item = items.get(0);
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(item == BONE)
			batch.draw(atlas.findRegion("box_bone"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == SPRING)
			batch.draw(atlas.findRegion("box_spring"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == SWATTER)
			batch.draw(atlas.findRegion("box_swatter"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
		else if(item == FEATHER)
			batch.draw(atlas.findRegion("box_feather"), this.getX(), this.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 0);
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
		this.item = items.get(0);
	}
	
	/**
	 * 0 if the box is empty
	 * @return true if the box is empty
	 */
	public boolean isEmpty() {
		return item == 0;
	}

}
