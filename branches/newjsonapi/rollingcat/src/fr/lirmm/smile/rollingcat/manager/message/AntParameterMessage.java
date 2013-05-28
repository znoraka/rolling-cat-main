package fr.lirmm.smile.rollingcat.manager.message;

import java.io.StringWriter;

import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class AntParameterMessage
{
	public AntParameterMessage()
	{}
	
	public int range;
	public float pathDeltaTime;
	public float alpha;
	public boolean assessmentDataOnly;
	public float evaporationRatioPerDay;
	
	public String toJson() {
		try{
			StringWriter writer = new StringWriter();
			JsonWriter jwriter = new JsonWriter(writer);
			jwriter.setOutputType(OutputType.json);
			jwriter.object()
			.name("range").value(range)
			.name("pathDeltaTime").value(pathDeltaTime)
			.name("alpha").value(alpha)
			.name("assessmentDataOnly").value(assessmentDataOnly)
			.name("evaporationRatioPerDay").value(evaporationRatioPerDay)
			.pop();
			jwriter.flush();
		return jwriter.getWriter().toString();
		} catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		/*
		String s = "{" ;
		s += "\"range\": "+this.range; s += ", \n";
		s += "\"pathDeltaTime\": "+this.pathDeltaTime; s += ", \n";
		s += "\"alpha\": "+this.alpha; s += ", \n";
		s += "\"assessmentDataOnly\": "+this.assessmentDataOnly; s += ", \n";
		s += "\"evaporationRatioPerDay\": "+this.evaporationRatioPerDay; s += ", \n";
		s += "}";
		return s;
		*/
	}
}