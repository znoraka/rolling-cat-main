package fr.lirmm.smile.rollingcat.model.event;

public class Event implements EventModel{
	private String id;
	private Long timestamp;
	private String event_type;
	private String parameters;		
	private String created_at;
	private String session_id;
}
