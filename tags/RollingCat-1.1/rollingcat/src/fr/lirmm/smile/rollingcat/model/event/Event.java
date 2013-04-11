package fr.lirmm.smile.rollingcat.model.event;


public class Event {
	private double timestamp;
	private String event_type;
	private Object parameters;
	
	public Event(double timestamp, String event_type, Object parameters){
		this.timestamp = timestamp;
		this.event_type = event_type;
		this.parameters = parameters;
	}
}
