package fr.lirmm.smile.rollingcat;

public class GameConstants {
	
	/**
	 * constantes pour le jeu
	 */
	public static final String ATLAS = "data/atlas.atlas";
	public static final String TEXTURE_CAT = "cat";
	public static final String TEXTURE_BONE = "bone";
	public static final String TEXTURE_GROUNDBLOCK = "groundblock";
	public static final String TEXTURE_BACKGROUND = "data/desert-background.png";
	public static final String TEXTURE_SPLASHSCREEN = "data/splashscreen.png";
	public static final String TEXTURE_MOUSE = "mouse";
	public static final String TEXTURE_DOG = "dog";
	public static final String TEXTURE_WASP = "wasp";
	public static final String TEXTURE_COIN = "coin";
	public static final String TEXTURE_EMPTY = "empty";
	public static final String TEXTURE_SHIT = "shit";
	public static final String TEXTURE_SPRING = "spring";
	public static final String TEXTURE_BOX = "box";
	public static final String TEXTURE_TARGET = "target";
	public static final String TEXTURE_CARPET = "carpet";
	public static final String TEXTURE_BOUEE = "bouee";
	public static final String TEXTURE_GEM = "_gem";
	
	public static final String FONT_LOGIN = "data/font.fnt";
	
	public static final float SCALE = 1.0f;
	
	public static int DISPLAY_WIDTH = (int) (800 * SCALE);
	public static int DISPLAY_HEIGHT = (int) (600 * SCALE);
	
	public static final int COLS = 16;
	public static final int ROWS = 14;
	
	public static final float BLOCK_WIDTH = DISPLAY_WIDTH / (COLS+2);
	public static final float BLOCK_HEIGHT = DISPLAY_HEIGHT / (ROWS + 2);
	
	public static final int VIEWPORT_WIDTH = (int) (BLOCK_WIDTH * COLS);
	public static final int VIEWPORT_HEIGHT = (int) (BLOCK_HEIGHT * ROWS);
	
	public static final float SPEED = 0.25f;
	public static final float ENTITY_SPEED = 3f;
	
	public static final float DELTATRACKINGMILLISEC = 100;
	
	public static final float HOVER_TIME = 1f;
	public static final float TIMEOUT = 10f;

	public static final int NB_OF_LEVELS_IN_MENU = 7;
	public static final int NB_OF_LEVELS_IN_GAME = 20;
	
	
	/**
	 * variables pour le serveur
	 */
	public static int workspaceWidth = 100;
	public static int workspaceHeight = 50;
	public static int range = 2;
	public static float pathDeltaTime = 0.001f;
	public static float evaporationPerDay = 0;;
	public static float alpha = 0.9f;
	public static int numberOfLines = 20;
	public static int numberOfRows = 20;
	public static int totalVolume = 20;
	public static int volumePerLevel = 5;
	
	
}
