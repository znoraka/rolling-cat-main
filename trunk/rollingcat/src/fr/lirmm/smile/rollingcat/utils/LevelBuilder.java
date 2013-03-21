package fr.lirmm.smile.rollingcat.utils;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;
import java.util.List;

import utils.MainConstant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Bone_Dog;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Fan;
import fr.lirmm.smile.rollingcat.model.game.GroundBlock;
import fr.lirmm.smile.rollingcat.model.game.Mouse;
import fr.lirmm.smile.rollingcat.model.game.Spring;
import fr.lirmm.smile.rollingcat.model.game.StopBlock;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.game.Wasp;
import generation.LevelFactory;


public class LevelBuilder {

	private static ArrayList<Integer> items;
	//private static String level;
	private static int segment = 0;

	private static String getRandomLevel(){
		int tabSize[] = {3,3,2};
		int size = 0;
		for(int i = 0 ; i < tabSize.length ; i++){
			size+=tabSize[i];
		}
		
		int tabX[] = new int[size];
		int tabY[] = new int[size];
		int index = 0;
		int minX = 5;
		int x;
		int y;
		for(int j = 0 ; j < tabSize.length ; j++){
			for(int k = 0 ; k < tabSize[j] ; k++){
				x = (int)(Math.random() * 5) + minX;
				y = (int)(Math.random()*(MainConstant.HEIGHT_MAX - 3)) + 3;
				minX=x;
				tabX[index+k] = x;
				tabY[index+k] = y;
			}
			index+=tabSize[j];
		}
		return LevelFactory.getStringLevelGeneratedByMCTS(tabX, tabY, tabSize);
}

	
	/**
	 * ajoute les acteurs au stage
	 * @param s la string du niveau
	 */
	public static Stage build(String s) {
		Stage stage = getStage();
		items = new ArrayList<Integer>();
		segment = 0;
		s = s.replace("\"", "");
		s = getRandomLevel();
		String tab [] = s.split("/");
		String[] subtab;
		float x;
		float y;

		List<Entity> directions = new ArrayList<Entity>();
		List<Entity> stops = new ArrayList<Entity>();
		for (int i = 0; i < tab.length; i++) {
			subtab = tab[i].split(";");
			x = Float.valueOf(subtab[1]);
			y = Float.valueOf(subtab[2]);

			if(subtab[0].equals("cat"))
			{
				stage.addActor(new Cat(x, y));
				stage.addActor(new Box(GameConstants.COLS / 2, 0));

			}
			else if(subtab[0].equals("coin"))
			{
				stage.addActor(new Coin(x, y));
			}
			else if(subtab[0].equals("wasp"))
			{
				stops.add(new Wasp(x, y));
//				stage.addActor(new Wasp(x, y));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.SWATTER);
			}
			else if(subtab[0].equals("mouse"))
				stage.addActor(new Mouse(x, y));
			else if(subtab[0].equals("dog"))
			{
				stops.add(new Dog(x, y));
//				stage.addActor(new Dog(x, y));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.BONE);
			}
			else if(subtab[0].equals("groundBlock"))
			{
				//	stage.addActor(new GroundBlock(x, y));
				directions.add(new GroundBlock(x, y));
			}
			else if(subtab[0].equals("empty"))
			{
				//	stage.addActor(new StopBlock(x, y));
				directions.add(new StopBlock(x, y));
			}
			else if(subtab[0].equals("bone"))
			{
				stage.addActor(new Bone_Dog(x, y));
			}
			else if(subtab[0].equals("carpet"))
			{
				stops.add(new Carpet(x, y));
//				stage.addActor(new Carpet(x, y));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.SCISSORS);
			}
			else if(subtab[0].equals("spring"))
			{
				stage.addActor(new Spring(x, y));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.SPRING);
			}
			else if(subtab[0].equals("fan"))
			{
				directions.add(new Fan(x,y));
//				stage.addActor(new Fan(x,y));
			}
			else if(subtab[0].equals("gap"))
			{
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.FEATHER);
			}
			else if(subtab[0].equals("target"))
				stage.addActor(new Target(x, y));

			Gdx.app.log(RollingCat.LOG, "new "+subtab[0]+ " added in " + x + ", " + y + " !");
		}
		
		for(Entity e : directions)
		{
			stage.addActor(e);
		}
		for(Entity e : stops)
		{
			stage.addActor(e);
		}
		Gdx.app.log(RollingCat.LOG, "building done " + tab.length + " elements added");
		//		stage.addActor(new Cat(10, 10));
		//		stage.addActor(new Dog(5, 5));
		items.add(0);
		((Box)stage.getActors().get(1)).setItems(items);
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