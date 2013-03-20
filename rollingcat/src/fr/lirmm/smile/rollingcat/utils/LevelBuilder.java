package fr.lirmm.smile.rollingcat.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Bone_Dog;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Fan;
import fr.lirmm.smile.rollingcat.model.game.GroundBlock;
import fr.lirmm.smile.rollingcat.model.game.Mouse;
import fr.lirmm.smile.rollingcat.model.game.Spring;
import fr.lirmm.smile.rollingcat.model.game.StopBlock;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.game.Wasp;


public class LevelBuilder {
	
	private static ArrayList<Integer> items;
	//private static String level;
	private static int segment = 0;
	
	
	/**
	 * ajoute les acteurs au stage
	 * @param s la string du niveau
	 */
	public static Stage build(String s) {
		Stage stage = getStage();
		items = new ArrayList<Integer>();
		segment = 0;
		s = s.replace("\"", "");
//		s = "cat;0.0;12.0/groundBlock;1.0;5.0/groundBlock;0.0;2.0/groundBlock;1.0;2.0/spring;2.0;2.0;/groundBlock;3.0;4.0/groundBlock;4.0;4.0/groundBlock;5.0;4.0/groundBlock;6.0;4.0/groundBlock;7.0;4.0/groundBlock;8.0;4.0/groundBlock;9.0;4.0/dog;9.0;5.0/fan;10.0;4.0/groundBlock;10.0;9.0/groundBlock;11.0;9.0/gap;12.0;10.0/groundBlock;13.0;9.0/empty;14.0;9.0/groundBlock;14.0;2.0/fan;15.0;2.0/wasp;15.0;7.0/wasp;15.0;10.0/groundBlock;15.0;11.0/groundBlock;16.0;11.0/groundBlock;17.0;11.0/groundBlock;18.0;11.0/groundBlock;19.0;11.0/groundBlock;20.0;11.0/empty;21.0;11.0/groundBlock;21.0;10.0/groundBlock;22.0;10.0/groundBlock;23.0;10.0/dog;23.0;11.0/empty;24.0;10.0/groundBlock;24.0;5.0/groundBlock;25.0;5.0/fan;26.0;5.0/dog;25.0;6.0/wasp;26.0;11.0/groundBlock;26.0;12.0/empty;27.0;12.0;groundBlock;27.0;5.0/groundBlock;27.0;5.0/fan;28.0;5.0/groundBlock;28.0;12.0/empty;29.0;12.0/empty;26.0;12.0/groundBlock;29.0;3.0/groundBlock;31.0;3.0/wasp;28.0;9.0/gap;30.0;3.0/groundBlock;32.0;3.0/groundBlock;33.0;3.0/groundBlock;34.0;3.0/groundBlock;35.0;3.0/target;35.0;4.0";
		String tab [] = s.split("/");
		String[] subtab;
		float x;
		float y;
		
		for (int i = 0; i < tab.length; i++) {
			subtab = tab[i].split(";");
			x = Float.valueOf(subtab[1]);
			y = Float.valueOf(subtab[2]);
			
			if(subtab[0].equals("cat"))
				stage.addActor(new Cat(x, y));
			else if(subtab[0].equals("coin"))
				stage.addActor(new Coin(x, y));
			else if(subtab[0].equals("wasp")){
				stage.addActor(new Wasp(x, y));
				if(isFirstOfScreen(x))
					items.add(Box.EMPTY);
				items.add(Box.SWATTER);
			}
			else if(subtab[0].equals("mouse"))
				stage.addActor(new Mouse(x, y));
			else if(subtab[0].equals("dog")){
				stage.addActor(new Dog(x, y));
				if(isFirstOfScreen(x))
					items.add(Box.EMPTY);
				items.add(Box.BONE);
			}
			else if(subtab[0].equals("groundBlock"))
				stage.addActor(new GroundBlock(x, y));
			else if(subtab[0].equals("empty"))
				stage.addActor(new StopBlock(x, y));
			else if(subtab[0].equals("bone"))
				stage.addActor(new Bone_Dog(x, y));
			else if(subtab[0].equals("carpet")){
				stage.addActor(new Carpet(x, y));
				if(isFirstOfScreen(x))
					items.add(Box.EMPTY);
				items.add(Box.SCISSORS);
			}
			else if(subtab[0].equals("spring")){
				stage.addActor(new Spring(x, y));
				if(isFirstOfScreen(x))
					items.add(Box.EMPTY);
				items.add(Box.SPRING);
			}
			else if(subtab[0].equals("fan")){
				stage.addActor(new Fan(x,y));
			}
			else if(subtab[0].equals("gap")){
				if(isFirstOfScreen(x))
					items.add(Box.EMPTY);
				items.add(Box.FEATHER);
			}
			else if(subtab[0].equals("target"))
				stage.addActor(new Target(x, y));
			
			Gdx.app.log(RollingCat.LOG, "new "+subtab[0]+ " added in " + x + ", " + y + " !");
		}
		Gdx.app.log(RollingCat.LOG, "building done " + tab.length + " elements added");
//		stage.addActor(new Cat(10, 10));
//		stage.addActor(new Dog(5, 5));
		items.add(0);
		return stage;
	}
	
	public static ArrayList<Integer> getItems(){
		return items;
	}
	
	/**
	 * 
	 * @param x abscisse de l'entité
	 * @return true si l'entité est la première de l'écran
	 */
	private static boolean isFirstOfScreen(float x){
		if(Math.floor(x / (GameConstants.COLS + 1)) > segment){
			segment ++;
			return true;
		}
		else
			return false;
	}	
	
}
