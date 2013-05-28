package fr.lirmm.smile.rollingcat.manager.message;

import java.io.StringWriter;

import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

//Added by Gouaich
public class NewGameSessionMessage
{
	public NewGameSessionMessage()
	{}
	
	public String sessionType;
	public String comment;
	public String patient_id ;
	public String game_id;
	
	public String toJson() {
		try{
			StringWriter writer = new StringWriter();
			JsonWriter jwriter = new JsonWriter(writer);
			jwriter.setOutputType(OutputType.json);
			jwriter.object()
			.name("sessionType").value(sessionType)
			.name("comment").value(comment)
			.name("patient_id").value(patient_id)
			.name("game_id").value(game_id)
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