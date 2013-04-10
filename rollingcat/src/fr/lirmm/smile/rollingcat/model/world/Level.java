package fr.lirmm.smile.rollingcat.model.world;

import fr.lirmm.smile.rollingcat.model.game.Coin;

public class Level {

	private int id;
	private String content;
	private String gameId;
	private int score;
	private int duree;
	private String gem;
	private int maxScore;
	
	public Level(int id, String content, String gameId, int score, int duree, String gem) {
		this.id = id;
		this.content = content;
		this.gameId = gameId;
		this.score = score;
		this.duree = (duree > 0)?score:Integer.MAX_VALUE;
		this.gem = gem;
		if(content != null)
			findMaxScore();
	}

	private void findMaxScore() {
		String tab [] = content.split("/");
		String[] subtab;
		maxScore = 0;

		for (int i = 0; i < tab.length; i++) {
			subtab = tab[i].split(";");
			
			if(subtab[0].contains(Coin.GOLD))
				maxScore += 3;
			else if(subtab[0].contains(Coin.SILVER))
				maxScore += 2;
			else if(subtab[0].contains(Coin.BRONZE))
				maxScore += 1;
		}
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
		return (duree == Integer.MAX_VALUE)?0:duree;
	}

	public String getGem() {
		return gem;
	}
	
	public int getMaxScore() {
		return maxScore;
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
		findMaxScore();
		if(World.getInstance().needsNewLevel())
			World.getInstance().add(new Level(World.getInstance().getNumberOfLevels(), null, null, 0, 0, null));
	}

	public void setContent(String levelAsString) {
		this.content = levelAsString;
	}
	
	
}
