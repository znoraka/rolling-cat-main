package fr.lirmm.smile.rollingcat.model.world;

public class Level {

	private int id;
	private String content;
	private String gameId;
	private int score;
	private int duree;
	private String gem;
	
	public Level(int id, String content, String gameId, int score, int duree, String gem) {
		this.id = id;
		this.content = content;
		this.gameId = gameId;
		this.score = score;
		this.duree = duree;
		this.gem = gem;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getGameId() {
		return gameId;
	}

	public int getScore() {
		return score;
	}

	public int getDuree() {
		return duree;
	}

	public String getGem() {
		return gem;
	}

	public void updateStats(int score, int duration, String couleur, String level) {
		this.score = score;
		this.duree = duration;
		this.gem = couleur;
		this.content = level;
		World.getInstance().add(new Level(World.getInstance().getNumberOfLevels(), null, null, 0, 0, null));
	}
	
	
}
