package fr.lirmm.smile.rollingcat.utils;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Door;
import fr.lirmm.smile.rollingcat.model.game.Fan;
import fr.lirmm.smile.rollingcat.model.game.Gap;
import fr.lirmm.smile.rollingcat.model.game.GroundBlock;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.game.Wasp;


public class LevelBuilder {

	private static ArrayList<Integer[]> items;
	private static int segment = 0;

	//	private static String testRandomReal()
	//	{
	//		int tabSize[] = {3,3,2,4,6,5,6,7};
	//		int size = 0;
	//		for(int i = 0 ; i < tabSize.length ; i++)
	//		{
	//			size+=tabSize[i];
	//		}
	//
	//		int tabX[] = new int[size];
	//		int tabY[] = new int[size];
	//		int index = 0;
	//		int minX = 5;
	//		int x;
	//		int y;
	//		for(int j = 0 ; j < tabSize.length ; j++)
	//		{
	//			minX = MainConstant.WIDTH * j + 5;
	//			for(int k = 0 ; k < tabSize[j] ; k++)
	//			{
	//				x = (int)(Math.random() * (MainConstant.WIDTH-2)) + minX;
	//				if(x == MainConstant.WIDTH * tabSize.length)
	//				{
	//					x = MainConstant.WIDTH * tabSize.length-1;
	//				}
	//
	//				y = (int)(Math.random()*(MainConstant.HEIGHT_MAX - 3)) + 3;
	//				tabX[index+k] = x;
	//				tabY[index+k] = y;
	//			}
	//			index+=tabSize[j];
	//		}
	//		return LevelFactory.getStringLevelGeneratedByMCTS(tabX, tabY, tabSize);
	//	}


	/**
	 * ajoute les acteurs au stage
	 * @param s la string du niveau
	 */
	public static Stage build(String s) {
		Stage stage = new Stage(GameConstants.DISPLAY_WIDTH * 8, GameConstants.DISPLAY_HEIGHT, true);
		items = new ArrayList<Integer[]>();
		segment = 0;
		s = s.replace("\"", "");
		//		Gdx.app.log(RollingCat.LOG, "getting random level");
		//		s = testRandomReal();
		Gdx.app.log(RollingCat.LOG, s);
		String tab [] = s.split("/");
		String[] subtab;
		float x;
		float y;
		int decalage = 0;

		//		stage.addActor(new GroundBlock(-10, -10));

		for (int i = 0; i < tab.length; i++) {
			subtab = tab[i].split(";");
			x = Float.valueOf(subtab[1]);
			y = Float.valueOf(subtab[2]);

			if(subtab[0].equals("cat"))
			{
				stage.addActor(new Cat(x, y + decalage));
			}
			else if(subtab[0].equals("wasp"))
			{
				stage.addActor(new Wasp(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(new Integer []{Box.EMPTY, segment});
				}
				items.add(new Integer [] {Box.SWATTER, segment});
			}
			else if(subtab[0].equals("dog"))
			{
				stage.addActor(new Dog(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(new Integer []{Box.EMPTY, segment});
				}
				items.add(new Integer []{Box.BONE, segment});
			}
			else if(subtab[0].equals("groundBlock"))
			{
				stage.getActors().insert(1, new GroundBlock(x, y + decalage));
				//				stage.addActor(new GroundBlock(x, y + decalage));
			}
			//			else if(subtab[0].equals("empty + decalage"))
			//			{
			//				//	stage.addActor(new StopBlock(x, y + decalage));
			//				directions.add(new StopBlock(x, y + decalage));
			//			}
			else if(subtab[0].equals("bronze_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.BRONZE));
			}
			else if(subtab[0].equals("silver_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.SILVER));
			}
			else if(subtab[0].equals("gold_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.GOLD));
			}
			else if(subtab[0].equals("carpet"))
			{
				stage.addActor(new Carpet(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(new Integer []{Box.EMPTY, segment});
				}
				items.add(new Integer []{Box.SCISSORS, segment});
			}
			else if(subtab[0].equals("fan"))
			{
				stage.addActor(new Fan(x,y + decalage));
			}
			else if(subtab[0].equals("door_left"))
			{	
				String [] temp = tab[i+1].split(";");
				float nextX = Float.valueOf(temp[1]);
				float nextY = Float.valueOf(temp[2]);
				stage.addActor(new Door(x,y + decalage, Door.LEFT, nextX, nextY));
			}
			else if(subtab[0].equals("door_right"))
			{
				stage.addActor(new Door(x,y + decalage, Door.RIGHT, 0, 0));
			}
			else if(subtab[0].equals("gap"))
			{
				stage.addActor(new Gap(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(new Integer []{Box.EMPTY, segment});
				}
				items.add(new Integer []{Box.FEATHER, segment});
			}
			else if(subtab[0].equals("target")){
				stage.addActor(new Target(x, y + decalage));
				decalage = GameConstants.ROWS + 2;
			}

			Gdx.app.log(RollingCat.LOG, "new "+subtab[0]+ " added in " + x + ", " + y + decalage + " !");
		}


		Gdx.app.log(RollingCat.LOG, "building done " + tab.length + " elements added");
		//		stage.addActor(new Cat(10, 10));
		//		stage.addActor(new Dog(5, 5));
		items.add(new Integer []{Box.EMPTY, segment});
		//		((Box)stage.getActors().get(1)).setItems(items);
		return stage;
	}

	public static ArrayList<Integer[]> getItems(){
		return items;
	}

	/**
	 * 
	 * @param x abscisse de l'entité
	 * @return true si l'entité est la première de l'écran
	 */
	private static boolean isFirstOfScreen(float x){
		if(Math.floor(x / (GameConstants.COLS)) > segment){
			segment ++;
			return false;
		}
		else
			return false;
	}	

}
