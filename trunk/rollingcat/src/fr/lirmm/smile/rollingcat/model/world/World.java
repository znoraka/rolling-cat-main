package fr.lirmm.smile.rollingcat.model.world;

import java.util.ArrayList;
import java.util.List;

import fr.lirmm.smile.rollingcat.GameConstants;

public class World {

	private List<Level> levels;
	private List<String> gems;
	private static World instance;
	private static boolean gen;
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		
		return instance;
	}
	
	public static void clearInstance() {
		gen = false;
		instance = null;
	}
	
	private World (){
		this.levels = new ArrayList<Level>();
		gen = false;
	}
	
	public List<Level> getLevels(){
		return this.levels;
	}
	
	public void add(Level level){
		gen = true;
		if(levels.size() < GameConstants.NB_OF_LEVELS_IN_GAME)
			this.levels.add(level);
	}

	public List<String> getGems() {
		this.gems = new ArrayList<String>();
		for (Level level : levels) {
			gems.add(level.getGem());
		}
		return gems;
	}

	public Level get(int index) {
		return levels.get(index);
	}

	public int getNumberOfLevels() {
		return levels.size();
	}
	public boolean hasBeenGenerated() {
		return gen;
	}
	
}
