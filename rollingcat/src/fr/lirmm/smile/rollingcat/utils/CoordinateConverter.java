package fr.lirmm.smile.rollingcat.utils;

import fr.lirmm.smile.rollingcat.GameConstants;

public class CoordinateConverter {

	public static float x(float x){
		return (GameConstants.workspaceWidth * x/ GameConstants.DISPLAY_WIDTH) ;
	}
	
	public static float y(float y){
		return (GameConstants.workspaceHeight * y/ GameConstants.DISPLAY_HEIGHT);
	}

}
