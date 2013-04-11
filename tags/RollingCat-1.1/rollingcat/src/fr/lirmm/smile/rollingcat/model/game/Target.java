package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Target extends Entity {
	
	private static String color;

	public Target(float x, float y) {
		super(x, y, getColor(x, y)+GameConstants.TEXTURE_GEM);
		this.setTouchable(Touchable.disabled);
		
	}
	
	public static String getColor(float x, float y){
		String s = "";
		if(x % 2 == 0){
			if(y % 2 == 0)
				s = "green";
			else
				s = "red";
		}
		else{
			if(y % 2 == 0)
				s = "purple";
			else
				s = "yellow";
		}
		color = s;
		return s;
	}
	
	public String getCouleur(){
		return color;
	}

}
