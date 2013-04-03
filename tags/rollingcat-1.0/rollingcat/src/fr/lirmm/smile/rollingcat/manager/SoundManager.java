package fr.lirmm.smile.rollingcat.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static Sound coinPickup;
	
	public static void pickupPlay(){
//		coinPickup.play();
	}
	
	public static void load(){
		if(coinPickup == null){
	//		coinPickup = Gdx.audio.newSound(Gdx.files.internal("data/sound/coin.ogg"));
		}
	}

}
