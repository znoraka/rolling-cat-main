package fr.lirmm.smile.rollingcat.manager.message;

import java.io.StringWriter;

import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;



public class DDAParametersMessage
{
	public DDAParametersMessage()
	{
		
	}
	public String builderId; 
	public String getBuilderId() {
		return builderId;
	}
	public void setBuilderId(String builderId) {
		this.builderId = builderId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public int getNumberOfLines() {
		return numberOfLines;
	}
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	public int getNumberOfRows() {
		return numberOfRows;
	}
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	public int getTotalHeight() {
		return totalHeight;
	}
	public void setTotalHeight(int totalHeight) {
		this.totalHeight = totalHeight;
	}
	public int getTotalWidth() {
		return totalWidth;
	}
	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}
	public int getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}
	public int getVolumePerLevel() {
		return volumePerLevel;
	}
	public void setVolumePerLevel(int volumePerLevel) {
		this.volumePerLevel = volumePerLevel;
	}
	public float getImportanceOfEffort() {
		return ImportanceOfEffort;
	}
	public void setImportanceOfEffort(float importanceOfEffort) {
		ImportanceOfEffort = importanceOfEffort;
	}
	public String getDials() {
		return dials;
	}
	public void setDials(String dials) {
		this.dials = dials;
	}
	public boolean isLeftHemiplegia() {
		return leftHemiplegia;
	}
	public void setLeftHemiplegia(boolean leftHemiplegia) {
		this.leftHemiplegia = leftHemiplegia;
	}
	public AntParameterMessage getParameters() {
		return parameters;
	}
	public void setParameters(AntParameterMessage parameters) {
		this.parameters = parameters;
	}
	public String patientId;
	public int numberOfLines;
	public int numberOfRows;
	public int totalHeight;
	public int totalWidth;
	public int totalVolume;
	public int volumePerLevel;
	public float ImportanceOfEffort;
	public String dials;
	public boolean leftHemiplegia;
	public AntParameterMessage parameters;
	/*
	public String toJson()
	{
		String s = "{" ;
		s += "\"builderId\": "+this.builderId;
		s += ", \n";
		s += "\"patientId\": "+this.patientId;
		s += ", \n";
		s += "\"numberOfLines\": "+this.numberOfLines;
		s += ", \n";
		s += "\"numberOfRows\": "+this.numberOfRows;
		s += ", \n";
		s += "\"totalHeight\": "+this.totalHeight;
		s += ", \n";
		s += "\"totalWidth\": "+this.totalWidth;
		s += ", \n";
		s += "\"totalVolume\": "+this.totalVolume;
		s += ", \n";
		s += "\"volumePerLevel\": "+this.volumePerLevel;
		s += ", \n";
		s += "\"ImportanceOfEffort\": "+this.ImportanceOfEffort;
		s += ", \n";
		s += "\"dials\": \""+this.dials + "\"";
		s += ", \n";
		s += "\"leftHemiplegia\": "+ this.leftHemiplegia ;
		s += ", \n";
		s += "\"parameters\": "+ this.parameters.toJson() ;
		s += "\n";
		s += "}";
		return s;
	}*/
	public String toJson() {
		try{
			StringWriter writer = new StringWriter();
			JsonWriter jwriter = new JsonWriter(writer);
			jwriter.setOutputType(OutputType.json);
			jwriter.object()
			.name("builderId").value(builderId)
			.name("patientId").value(patientId)
			.name("numberOfLines").value(numberOfLines)
			.name("numberOfRows").value(numberOfRows)
			.name("totalHeight").value(totalHeight)
			.name("totalWidth").value(totalWidth)
			.name("totalVolume").value(totalVolume)
			.name("volumePerLevel").value(volumePerLevel)
			.name("ImportanceOfEffort").value(ImportanceOfEffort)
			.name("dials").value(dials)
			.name("leftHemiplegia").value(leftHemiplegia)
			.object("parameters")
				.name("alpha").value(parameters.alpha)
				.name("assessmentDataOnly").value(parameters.assessmentDataOnly)
				.name("evaporationRatioPerDay").value(parameters.evaporationRatioPerDay)
				.name("pathDeltaTime").value(parameters.pathDeltaTime)
				.name("range").value(parameters.range)
				.pop()
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