package fr.lirmm.smile.rollingcat.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.game.Bone_Dog;
import fr.lirmm.smile.rollingcat.model.game.Box;
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
	private static String level;
	private static int segment = 0;
	/**
	 * sauvegarde les entity du stage
	 * @param stage
	 */
//	@Deprecated
//	public static void writeLevel(Stage stage){
//	   FileHandle file = new FileHandle("data/foo.txt");
//	   String s;
//	   for (Actor a : stage.getActors()) {
//		   if(a instanceof Entity){
//			   s = a.getName() + ";" + (a.getX()/GameConstants.BLOCK_WIDTH) + ";" + (a.getY()/GameConstants.BLOCK_HEIGHT) + "\n";
//			   file.writeString(s, true);
//		   }
//	   }
//	}
	
	/**
	 * lit un fichier pour constuire le stage - Entity;x;y\n
	 * @return le stage créé
	 */
	public static Stage readLevel(){
		Stage stage = new Stage(GameConstants.VIEWPORT_WIDTH, GameConstants.VIEWPORT_HEIGHT, true);

//		Gdx.app.log(RollingCat.LOG, "retriving level file...");
//		
//		FileHandle file = Gdx.files.internal("data/file.txt");
//		
//		Gdx.app.log(RollingCat.LOG, "done.");
//		Gdx.app.log(RollingCat.LOG, "parsing level file...");
//		
//		String s = file.readString();
//		
		//level = InternetManager.getLevelOnServer(1);
		LevelBuilder.build(level, stage);
		return stage;
	}
	
	/**
	 * ajoute les acteurs au stage
	 * @param s la string du fichier lu
	 * @param stage le stage à créer
	 */
	private static void build(String s, Stage stage) {
		items = new ArrayList<Integer>();
		segment = 0;
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
		items.add(0);
	}
	
	public static ArrayList<Integer> getItems(){
		return items;
	}
	
	private static boolean isFirstOfScreen(float x){
		if(Math.floor(x / GameConstants.COLS) > segment){
			segment ++;
			return true;
		}
		else
			return false;
	}

	public static void setLevel(String s) {
		level = s;
	}
	
}
