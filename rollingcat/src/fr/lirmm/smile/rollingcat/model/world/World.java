package fr.lirmm.smile.rollingcat.model.world;

import java.util.ArrayList;
import java.util.List;

public class World {

	private List<Level> levels;
	private List<String> gems;
	private static World instance;
	private boolean gen;
	
	public static World getInstance(){
		if(instance == null)
			instance = new World();
		
		return instance;
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
		this.levels.add(level);
	}

	public List<String> getGems() {
		this.gems = new ArrayList<String>();
		for (Level level : levels) {
			System.out.println(level.getGem());
			gems.add(level.getGem());
		}
		System.out.println("-----------------");
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
