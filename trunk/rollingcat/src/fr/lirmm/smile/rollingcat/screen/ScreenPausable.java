package fr.lirmm.smile.rollingcat.screen;

import com.badlogic.gdx.Screen;

public interface ScreenPausable extends Screen{
	public void setElapsedTimeDuringPause(long elapsedTime);
}
