package fr.lirmm.smile.rollingcat;

public interface GameConstants {
	
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
	
	public static final String FONT_LOGIN = "data/font.fnt";
	
	public static final float SCALE = 1.0f;
	
	public static final int DISPLAY_WIDTH = (int) (800 * SCALE);
	public static final int DISPLAY_HEIGHT = (int) (600 * SCALE);
	
	public static final int COLS = 16;
	public static final int ROWS = 12;
	
	public static final float BLOCK_WIDTH = DISPLAY_WIDTH / (COLS+2);
	public static final float BLOCK_HEIGHT = DISPLAY_HEIGHT / (ROWS+2);
	
	public static final int VIEWPORT_WIDTH = DISPLAY_WIDTH;
	public static final int VIEWPORT_HEIGHT = DISPLAY_HEIGHT;
	
	public static final float SPEED = 0.25f;
	public static final float ENTITY_SPEED = 3f;
	
	public static final float DELTATRACKINGMILLISEC = 100;
	
	public static final float HOVER_TIME = 1f;
	public static final float HOLD_POSITION = 5f;
}
