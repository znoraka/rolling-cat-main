package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.screen.GameScreen;

public class EventManager {
	public static final String start_game_event_type = "session_start";
	public static final String player_cursor_event_type="player_position";
	public static final String pointing_task_start="pointing_task_start";
	public static final String pointing_task_end="pointing_task_end";
	public static final String pointing_task_position="pointing_task_position";
	public static final String pointing_task_set="pointing_task_set";
	public static final String end_game_event_type = "session_end";
	public static final String game_info_event_type = "game_info";
	
	private static final String timestamp = "timestamp";
	private static String type = "event_type";
	private static String data = "parameters";		
	
	private static ArrayList<Array<OrderedMap<String, Object>>> listOfEvents;

//	/**
//	 * ajoute un event à la liste d'events
//	 * @param event
//	 */
//	public static void add(OrderedMap<String, Object> event) {
//		listOfEvents.add(event);
//	}
	
	/**
	 * vide la liste en la retournant sous forme de String
	 * @return la liste sous forme de String Json
	 */
	public static String[] getListAsJsonString(){
		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);
		String[] s = new String[listOfEvents.size()];
		
		for (int i = 0; i < s.length; i++) {
			s[i] = json.toJson(listOfEvents.get(i));
			Gdx.app.log(RollingCat.LOG, json.prettyPrint(s[i]));
		}
		Gdx.app.log(RollingCat.LOG, ""+listOfEvents.size());
		return s;
	}
	
	/**
	 * crée un event
	 * @param event_type
	 * @param parameters
	 * @return
	 */
	public static void create(String event_type, Object parameters){
		OrderedMap<String, Object> event = new OrderedMap<String, Object>();
		Long l = new Long(System.currentTimeMillis() - GameScreen.getElapsedTimeDuringPause());
		event.put(timestamp, l.toString());
		event.put(type, event_type);
		event.put(data, parameters);
		listOfEvents.get(listOfEvents.size() - 1).add(event);
		if(listOfEvents.get(listOfEvents.size() - 1).size > 200)
			listOfEvents.add(new Array<OrderedMap<String, Object>>());
	}
	
	/**
	 * vide la liste d'évents
	 * devrait être appelé à chaque écran de chargement
	 */
	public static void clear(){
		listOfEvents = new ArrayList<Array<OrderedMap<String, Object>>>();
		listOfEvents.add(new Array<OrderedMap<String, Object>>());
	}
}
