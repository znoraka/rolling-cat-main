package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.event.Event;

public class EventManager {
	private static ArrayList<Event> listOfEvents = new ArrayList<Event>();

	/**
	 * ajoute un event à la liste d'events
	 * @param event
	 */
	public static void add(Event event) {
		listOfEvents.add(event);
	}
	
	public static ArrayList<Event> getListOfEvents(){
		return listOfEvents;
	}
	
	public static String getListAsJsonString(){
		Json json = new Json();
		Gdx.app.log(RollingCat.LOG, json.prettyPrint(json.toJson(listOfEvents)));
		return json.toJson(listOfEvents);
	}
}
