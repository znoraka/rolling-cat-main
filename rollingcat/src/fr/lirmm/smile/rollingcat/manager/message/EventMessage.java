package fr.lirmm.smile.rollingcat.manager.message;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class EventMessage
{
	public String event_type;
	public long timestamp;
	public String parameters;
	
	public static String toJson(Array<EventMessage> ar)
	{
	StringWriter writer = new StringWriter();
	JsonWriter jwriter = new JsonWriter(writer);
	jwriter.setOutputType(OutputType.json);
	try {
		jwriter.array();
	for (EventMessage eventMessage : ar) 
	{
		eventMessage.toJson(jwriter);
	}
	jwriter.pop();
	return jwriter.getWriter().toString();
	} catch (IOException e) {
		e.printStackTrace();
		return "";
	}
	
	}

	public String toJson() 
	{
		StringWriter writer = new StringWriter();
		JsonWriter jwriter = new JsonWriter(writer);
		jwriter.setOutputType(OutputType.json);
		return toJson(jwriter);
	}
	public String toJson(JsonWriter jwriter) {
		try{
			jwriter.object()
			.name("event_type").value(event_type)
			.name("timestamp").value(timestamp)
			.name("parameters").value(parameters)
			.pop();
			jwriter.flush();
		return jwriter.getWriter().toString();
		} catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		}
}