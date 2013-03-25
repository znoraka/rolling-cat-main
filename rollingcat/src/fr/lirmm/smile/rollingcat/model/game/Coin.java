package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class Coin extends Entity{

	public static String BRONZE = "bronze";
	public static String SILVER = "silver";
	public static String GOLD = "gold";
	
	private String type;
	
	public Coin(float x, float y, String type) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
		this.setTouchable(Touchable.disabled);
		this.type = type;
        this.anim = new Animation(0.25f, GdxRessourcesGetter.getRegions("coin"+type));

	}
	
	public String getType(){
		return this.type;
	}
}
