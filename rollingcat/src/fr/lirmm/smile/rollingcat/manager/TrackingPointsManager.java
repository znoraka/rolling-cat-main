package fr.lirmm.smile.rollingcat.manager;


public class TrackingPointsManager {
	private static int id = 0;
	
	public static int getId(){
		id++;
		return id;
	}
}
