package fr.lirmm.smile.rollingcat.manager;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.message.EventMessage;
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
	public static final String task_success = "task_success";
	public static final String task_fail = "task_fail";

	
	private static final String timestamp = "timestamp";
	private static String type = "event_type";
	private static String data = "parameters";		
	
	private static ArrayList<Array< EventMessage >> listOfEvents;

//	/**
//	 * ajoute un event à la liste d'events
//	 * @param event
//	 */
//	public static void add(OrderedMap<String, Object> event) {
//		listOfEvents.add(event);
//	}
	
	/**
	 * 
	 * @return la liste sous forme de String Json
	 */
	
	public static String[] getListAsJsonString(){
		//gouaich
		String[] s = new String[listOfEvents.size()];
		
		for (int i = 0; i < s.length; i++) {
			s[i] = EventMessage.toJson( listOfEvents.get(i) );
			Gdx.app.log(RollingCat.LOG, s[i]);
		}
		Gdx.app.log(RollingCat.LOG, ""+listOfEvents.size());
		return s;
	}
	
	public static void create(String event_type, OrderedMap<String,String> map){
		String s="";	
		int size = map.size;
		if (size > 0)
		{
			for(
					Entries<String, String> it = map.entries();
					it.hasNext();
			)
			{
				Entry<String, String> entry = it.next();
				s += "\""+entry.key+"\""+": " + "\""+entry.value+"\"";
				if(it.hasNext()) s += ",";
			}
				
	
		}
		create(event_type,"{"+s+"}");
		}
	/**
	 * crée un event
	 * @param event_type
	 * @param parameters
	 * @return
	 */
	public static void create(String event_type, String parameters){
		EventMessage event = new EventMessage();
		Long l = new Long(System.currentTimeMillis() - GameScreen.getElapsedTimeDuringPause());
		event.timestamp= l.longValue();
		event.event_type =event_type;
		event.parameters = parameters ;
		listOfEvents.get(listOfEvents.size() - 1).add(event);
		if(listOfEvents.get(listOfEvents.size() - 1).size > 200)
			listOfEvents.add(new Array<EventMessage>());
	}
	
	/**
	 * vide la liste d'évents
	 * devrait être appelé à chaque écran de chargement
	 */
	public static void clear(){
		listOfEvents = new ArrayList<Array<EventMessage>>();
		listOfEvents.add(new Array<EventMessage>());
	}
}
