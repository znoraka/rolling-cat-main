package fr.lirmm.smile.rollingcat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.lirmm.smile.rollingcat.utils.LevelBuilder;
import fr.lirmm.smile.rollingcat.utils.Tutorial;


public class RollingCat extends Game {
	
	public static final boolean DEBUG = false;
	public static final String LOG = "RollingCat";
	public static final String VERSION = "0.1.51a";
	
	@Override
	public void create() {		
//		this.setScreen(new LoginScreen(this));
//

		Stage stage = LevelBuilder.build("cat;0.0;7.0/groundBlock;0.0;6.0/fan;1.0;6.0/groundBlock;1.0;10.0/groundBlock;2.0;10.0/empty;3.0;10.0/groundBlock;3.0;8.0/empty;4.0;8.0/groundBlock;4.0;5.0/empty;5.0;5.0/carpet;5.0;2.0/groundBlock;5.0;0.0/groundBlock;6.0;0.0/groundBlock;7.0;0.0/groundBlock;8.0;0.0/fan;9.0;0.0/groundBlock;9.0;3.0/groundBlock;10.0;3.0/empty;11.0;3.0/carpet;11.0;1.0/groundBlock;11.0;0.0/groundBlock;12.0;0.0/groundBlock;13.0;0.0/groundBlock;14.0;0.0/fan;15.0;0.0/groundBlock;15.0;5.0/fan;16.0;5.0/groundBlock;16.0;7.0/groundBlock;17.0;7.0/groundBlock;18.0;7.0/groundBlock;19.0;7.0/groundBlock;20.0;7.0/groundBlock;21.0;7.0/groundBlock;22.0;7.0/groundBlock;23.0;7.0/groundBlock;24.0;7.0/groundBlock;25.0;7.0/groundBlock;26.0;7.0/empty;27.0;7.0/groundBlock;27.0;4.0/empty;28.0;4.0/carpet;28.0;1.0/groundBlock;28.0;0.0/groundBlock;29.0;0.0/fan;30.0;0.0/groundBlock;30.0;5.0/fan;31.0;5.0/groundBlock;31.0;10.0/groundBlock;32.0;10.0/empty;33.0;10.0/groundBlock;33.0;7.0/groundBlock;34.0;7.0/empty;35.0;7.0/groundBlock;35.0;1.0/fan;36.0;1.0/wasp;36.0;4.0/groundBlock;36.0;9.0/empty;37.0;9.0/groundBlock;37.0;6.0/empty;38.0;6.0/carpet;38.0;4.0/groundBlock;38.0;0.0/groundBlock;39.0;0.0/groundBlock;40.0;0.0/fan;41.0;0.0/groundBlock;41.0;4.0/dog;42.0;5.0/groundBlock;42.0;4.0/empty;43.0;4.0/groundBlock;43.0;1.0/empty;44.0;1.0/carpet;44.0;1.0/groundBlock;44.0;0.0/groundBlock;45.0;0.0/groundBlock;46.0;0.0/groundBlock;47.0;0.0/groundBlock;48.0;0.0/groundBlock;49.0;0.0/fan;50.0;0.0/groundBlock;50.0;5.0/empty;51.0;5.0/carpet;51.0;3.0/groundBlock;51.0;0.0/groundBlock;52.0;0.0/groundBlock;53.0;0.0/groundBlock;54.0;0.0/groundBlock;55.0;0.0/fan;56.0;0.0/groundBlock;56.0;2.0/groundBlock;57.0;2.0/fan;58.0;2.0/wasp;58.0;4.0/groundBlock;58.0;5.0/empty;59.0;5.0/carpet;59.0;4.0/groundBlock;59.0;1.0/empty;60.0;1.0/carpet;60.0;1.0/groundBlock;60.0;0.0/groundBlock;61.0;0.0/groundBlock;62.0;0.0/groundBlock;63.0;0.0/groundBlock;64.0;0.0/groundBlock;65.0;0.0/groundBlock;66.0;0.0/groundBlock;67.0;0.0/fan;68.0;0.0/wasp;68.0;2.0/groundBlock;68.0;6.0/groundBlock;69.0;6.0/fan;70.0;6.0/groundBlock;70.0;12.0/empty;71.0;12.0/groundBlock;71.0;6.0/empty;72.0;6.0/groundBlock;72.0;1.0/fan;73.0;1.0/wasp;73.0;6.0/groundBlock;73.0;7.0/empty;74.0;7.0/groundBlock;74.0;3.0/empty;75.0;3.0/groundBlock;75.0;0.0/dog;76.0;1.0/groundBlock;76.0;0.0/fan;77.0;0.0/groundBlock;77.0;2.0/groundBlock;78.0;2.0/fan;79.0;2.0/groundBlock;79.0;4.0/empty;80.0;4.0/groundBlock;80.0;0.0/groundBlock;81.0;0.0/groundBlock;82.0;0.0/fan;83.0;0.0/groundBlock;83.0;2.0/empty;84.0;2.0/carpet;84.0;2.0/groundBlock;84.0;0.0/fan;85.0;0.0/wasp;85.0;4.0/groundBlock;85.0;9.0/groundBlock;86.0;9.0/empty;87.0;9.0/groundBlock;87.0;3.0/empty;88.0;3.0/carpet;88.0;1.0/bronze_coin;5.0;1.0/bronze_coin;6.0;1.0/bronze_coin;7.0;1.0/bronze_coin;9.0;4.0/bronze_coin;12.0;1.0/bronze_coin;13.0;1.0/bronze_coin;14.0;1.0/bronze_coin;29.0;1.0/bronze_coin;30.0;6.0/gold_coin;31.0;11.0/silver_coin;36.0;10.0/silver_coin;37.0;7.0/bronze_coin;38.0;1.0/bronze_coin;39.0;1.0/bronze_coin;40.0;1.0/bronze_coin;43.0;2.0/bronze_coin;44.0;1.0/bronze_coin;45.0;1.0/bronze_coin;46.0;1.0/bronze_coin;51.0;1.0/bronze_coin;52.0;1.0/bronze_coin;53.0;1.0/bronze_coin;58.0;6.0/bronze_coin;59.0;2.0/bronze_coin;60.0;1.0/bronze_coin;61.0;1.0/bronze_coin;62.0;1.0/silver_coin;68.0;7.0/silver_coin;69.0;7.0/gold_coin;70.0;13.0/silver_coin;73.0;8.0/bronze_coin;74.0;4.0/bronze_coin;75.0;1.0/bronze_coin;77.0;3.0/bronze_coin;78.0;3.0/bronze_coin;79.0;5.0/bronze_coin;84.0;1.0/silver_coin;85.0;10.0/silver_coin;86.0;10.0/bronze_coin;87.0;4.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;8.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;8.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;1.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;1.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/door_left;88.0;-1.0/door_right;88.0;0.0/target;88.0;0.0");
			this.setScreen(new Tutorial(this,null,stage,0,null));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
