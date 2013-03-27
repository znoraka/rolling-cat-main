package fr.lirmm.smile.rollingcat.utils;

import fr.lirmm.smile.rollingcat.GameConstants;

public class CoordinateConverter {

	public static float x(float x){
		return (GameConstants.WORKSPACE_WIDTH * x/ GameConstants.DISPLAY_WIDTH) ;
	}
	
	public static float y(float y){
		return (GameConstants.WORKSPACE_HEIGHT * y/ GameConstants.DISPLAY_HEIGHT);
	}

}
