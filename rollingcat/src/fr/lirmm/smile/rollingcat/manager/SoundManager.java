package fr.lirmm.smile.rollingcat.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static Sound coinPickup;
	private static Sound nextButton;
	private static Sound fall;
	private static Sound jump;
	private static Music gameMusic;
	private static Sound win;
	private static Sound fan;
	private static Sound next;

	public static void pickupPlay(){
		coinPickup.play();
	}

	public static void nextPlay(){
		nextButton.play();
	}

	public static void fallPlay(){
		fall.play();
	}

	public static void jumpPlay(){
		jump.play();
	}

	public static void winPlay(){
		win.play();
	}

	public static void fanPlay(){
//		fan.play();
	}
	
	public static void gameMusicPlay(boolean b){
		if(b)
			gameMusic.play();
		else
			gameMusic.stop();
	}

	public static void load(){
		coinPickup = Gdx.audio.newSound(Gdx.files.internal("data/sound/coin.ogg"));
		nextButton = Gdx.audio.newSound(Gdx.files.internal("data/sound/coin.ogg"));
		fall = Gdx.audio.newSound(Gdx.files.internal("data/sound/fall.ogg"));
		//		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sound/coin.ogg"));
		jump = Gdx.audio.newSound(Gdx.files.internal("data/sound/bigjump.ogg"));
		win = Gdx.audio.newSound(Gdx.files.internal("data/sound/gem_1.ogg"));
		fan = Gdx.audio.newSound(Gdx.files.internal("data/sound/fan.ogg"));

	}

}
