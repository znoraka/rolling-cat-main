package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

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
}
