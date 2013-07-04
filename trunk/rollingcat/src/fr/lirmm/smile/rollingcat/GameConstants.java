package fr.lirmm.smile.rollingcat;


public class GameConstants {
	
	/**
	 * constantes pour le jeu
	 */
	public static final String ATLAS = "data/atlas.atlas";
	public static final String TEXTURE_CAT = "cat";
	public static final String TEXTURE_BONE = "bone";
	public static final String TEXTURE_GROUNDBLOCK = "groundblock";
	public static final String TEXTURE_BACKGROUND = "data/background";
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
	public static final String ASSISTANCE = "assistance";
	public static final String CHALLENGE = "challenge";
	
	public static final String FONT_LOGIN = "data/font.fnt";
	
	
	/**
	 * à initialiser avant de lancer le jeu
	 */
	public static float SCALE = 1.0f;
	public static int DISPLAY_WIDTH;
	public static int DISPLAY_HEIGHT;
	public static int COLS;
	public static int ROWS;
	public static float BLOCK_WIDTH;
	public static float BLOCK_HEIGHT;
	public static int DECALAGE = 0;
	
	public static final float SPEED = 0.25f;
	public static final float ENTITY_SPEED = 3f;
	
	public static final float DELTATRACKINGMILLISEC = 100;
	
	public static final float HOVER_TIME = 1f;
	public static int TIMEOUT = 10;

	public static final int NB_OF_LEVELS_IN_MENU = 7;
	//on ajoute le niveau du tuto qui ne compte pas dans la liste de niveaux effective
	public static final int NB_OF_LEVELS_IN_GAME = 20;
	
	/**
	 * nombre de succes/echec pour changer d'etage
	 */
	public static int SUCCESS = 1;
	
	
	/**
	 * variables pour le serveur
	 */
	public static int workspaceWidth = 100;
	public static int workspaceHeight = 50;
	public static int range = 2;
	public static float pathDeltaTime = 0.001f;
	public static float evaporationPerDay = 0;
	public static float alpha = 0.5f;
	public static int numberOfLines = 20;
	public static int numberOfRows = 20;
	public static int totalVolume = 20;
	public static int volumePerLevel = 5;
	public static boolean area_1 = false;
	public static boolean area_2 = false;
	public static boolean area_3 = false;
	public static boolean area_4 = false;
	
	
	/**
	 * l'algo de génération en fonction de la skin
	 * @return String
	 */
	public static String getAlgo(){
		switch (RollingCat.skin)
		{
		case 1: return "random";
		case 2: return "incremental";
		case 3: return "ant-0.1";
		default: return "";
		}
	}

	/**
	 * initialise la taille de la fenetre et des entités
	 */
	public static void init(int width, int heigth){
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = heigth;
		
		COLS = 16;
		ROWS = 14;
		
		BLOCK_WIDTH = DISPLAY_WIDTH / (COLS);
		BLOCK_HEIGHT = DISPLAY_HEIGHT / (ROWS);

		SCALE = ((float) width / 800f);
		
		numberOfLines = ROWS - 2;
		numberOfRows = COLS;
		
		DECALAGE = ROWS * 2;
		
	}
}
