package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class Target extends Entity {
	
	private static String color;
	private static Target instance;

	public Target(float x, float y) {
		super(x, y, setColor(x, y)+GameConstants.TEXTURE_GEM);
		this.setTouchable(Touchable.disabled);
	}
	
	public static Target getInstance(){
		return instance;
	}
	
	public static void setInstance(Target target){
		Gdx.app.log(RollingCat.LOG, "setting gem instance");
		Gdx.app.log(RollingCat.LOG, target.getCouleur());
		instance = target;
	}
	
	/**
	 * la couleur est choisie en fonction de la position de la {@link Target} selon si son x/y est pair ou pas, cela permet de toujours avoir la même couleur
	 * @param x
	 * @param y
	 * @return
	 */
	public static String setColor(float x, float y){
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
