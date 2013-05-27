package fr.lirmm.smile.rollingcat.model.world;


public class Level {

	private static String content;

	private int id;
	private String gameId;
	private int score;
	private int duree;
	private String gem;
	
	public Level(int id, String gameId, int score, int duree, String gem) {
		this.id = id;
//		this.content = content;
		this.gameId = gameId;
		this.score = score;
		this.duree = (duree > 0)?score:Integer.MAX_VALUE;
		this.gem = gem;
	}

	public int getId() {
		return id;
	}

	public static String getContent() {
		return content;
	}

	public String getGameId() {
		return gameId;
	}

	public int getScore() {
		return score;
	}

	public int getDuree() {
		return (duree == Integer.MAX_VALUE)?0:duree;
	}

	public String getGem() {
		return gem;
	}
	
	/**
	 * update les stats du niveau
	 * met à jour le score uniquement si le nouveau score est meilleur
	 * met à jour la durée du niveau uniquement si elle est plus courte que la durée précédente
	 * @param score
	 * @param duration
	 * @param couleur
	 */
	public void updateStats(int score, int duration, String couleur) {
		this.score = Math.max(this.score,  score);
		this.duree = Math.min(this.duree, duration);
		this.gem = couleur;
		if(World.getInstance().needsNewLevel())
			World.getInstance().add(new Level(World.getInstance().getNumberOfLevels(), null, 0, 0, null));
	}

	public static void setContent(String levelAsString) {
		content = levelAsString;
	}
	
	
}
