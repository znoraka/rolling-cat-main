package fr.lirmm.smile.rollingcat.model.event;

import com.badlogic.gdx.utils.Json;

public class Event implements EventModel{
	
	private Long timestamp;
	private String event_type;
	private String parameters;		
	
	public Event(String event_type, Object parameters) {
		Json json = new Json();
		this.timestamp = System.currentTimeMillis();
		this.event_type = event_type;
		this.parameters = json.toJson(parameters);
		System.out.println(parameters);
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long getTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCreationDate() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}
}
