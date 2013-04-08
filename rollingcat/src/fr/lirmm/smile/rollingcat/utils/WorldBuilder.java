package fr.lirmm.smile.rollingcat.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.model.world.World;

public class WorldBuilder {

	public static World build(String jsonData){
		World world = World.getInstance();
		Json json = new Json();
		Gdx.app.log(RollingCat.LOG, json.prettyPrint(jsonData));
		@SuppressWarnings("unchecked")
		Array<String> levels =  (Array<String>) new JsonReader().parse(jsonData);
		
		for (int i = 0; i < levels.size; i++) {
			String content = json.readValue("content", String.class, levels.get(i));
			String gameId = json.readValue("gameId", String.class, levels.get(i));
			int score = json.readValue("score", int.class, levels.get(i));
			int duree = json.readValue("time", int.class, levels.get(i));
			String gem = json.readValue("gem", String.class, levels.get(i));
			
			world.add(new Level(i, content, gameId, score, duree, gem));
			
		}
		
		world.add(new Level(levels.size, null, null, 0, 0, null));
		
		return world;
	}

}
