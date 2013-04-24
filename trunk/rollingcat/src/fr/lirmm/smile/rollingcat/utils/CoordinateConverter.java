package fr.lirmm.smile.rollingcat.utils;

import com.badlogic.gdx.Game;

import fr.lirmm.smile.rollingcat.GameConstants;

public class CoordinateConverter {

	public static float x(float x){
		return ((GameConstants.workspaceWidth * x / GameConstants.DISPLAY_WIDTH) % GameConstants.workspaceWidth) ;
	}
	
	public static float y(float y){
		return ((GameConstants.workspaceHeight * y / GameConstants.DISPLAY_HEIGHT) % GameConstants.workspaceHeight);
	}

}
