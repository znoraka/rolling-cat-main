package fr.lirmm.smile.rollingcat.utils;

import java.util.ArrayList;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Carpet;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Dog;
import fr.lirmm.smile.rollingcat.model.game.Door;
import fr.lirmm.smile.rollingcat.model.game.Fan;
import fr.lirmm.smile.rollingcat.model.game.Gap;
import fr.lirmm.smile.rollingcat.model.game.GroundBlock;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.game.Wasp;


public class LevelBuilder {

	private static ArrayList<Integer> items;
	private static int segment = 0;
	private static OrderedMap<String, ArrayList<Integer>> map;
	private static int decalage;
	private static int oldSegment;
	/**
	 * ajoute les acteurs au stage
	 * @param s la string du niveau
	 */
	public static Stage build(String s) {
//		Stage stage = new Stage(GameConstants.DISPLAY_WIDTH * 8, GameConstants.DISPLAY_HEIGHT, true);
		Stage stage = getStage();
		map = new OrderedMap<String, ArrayList<Integer>>();
		items = new ArrayList<Integer>();
		segment = 0;
		oldSegment = 0;
//		s = "cat;0.0;4.0/groundBlock;0.0;3.0/fan;1.0;3.0/groundBlock;1.0;8.0/groundBlock;2.0;8.0/empty;3.0;8.0/groundBlock;3.0;6.0/empty;4.0;6.0/groundBlock;4.0;3.0/fan;5.0;3.0/wasp;5.0;6.0/groundBlock;5.0;9.0/groundBlock;6.0;9.0/groundBlock;7.0;9.0/groundBlock;8.0;9.0/groundBlock;9.0;9.0/empty;10.0;9.0/groundBlock;10.0;4.0/empty;11.0;4.0/groundBlock;11.0;0.0/fan;12.0;0.0/wasp;12.0;2.0/groundBlock;12.0;3.0/empty;13.0;3.0/carpet;13.0;2.0/groundBlock;13.0;0.0/groundBlock;14.0;0.0/groundBlock;15.0;0.0/groundBlock;16.0;0.0/groundBlock;17.0;0.0/groundBlock;18.0;0.0/groundBlock;19.0;0.0/groundBlock;20.0;0.0/groundBlock;21.0;0.0/groundBlock;22.0;0.0/fan;23.0;0.0/wasp;23.0;6.0/groundBlock;23.0;10.0/groundBlock;24.0;10.0/empty;25.0;10.0/groundBlock;25.0;7.0/groundBlock;26.0;7.0/groundBlock;27.0;7.0/empty;28.0;7.0/groundBlock;28.0;1.0/fan;29.0;1.0/wasp;29.0;3.0/groundBlock;29.0;8.0/groundBlock;30.0;8.0/groundBlock;31.0;8.0/groundBlock;32.0;8.0/groundBlock;33.0;8.0/groundBlock;34.0;8.0/groundBlock;35.0;8.0/empty;36.0;8.0/groundBlock;36.0;5.0/dog;37.0;6.0/groundBlock;37.0;5.0/dog;38.0;6.0/groundBlock;38.0;5.0/groundBlock;39.0;5.0/fan;40.0;5.0/groundBlock;40.0;11.0/groundBlock;41.0;11.0/groundBlock;42.0;11.0/empty;43.0;11.0/groundBlock;43.0;7.0/empty;44.0;7.0/carpet;44.0;2.0/groundBlock;44.0;0.0/fan;45.0;0.0/wasp;45.0;2.0/groundBlock;45.0;3.0/fan;46.0;3.0/groundBlock;46.0;9.0/groundBlock;47.0;9.0/empty;48.0;9.0/groundBlock;48.0;7.0/groundBlock;49.0;7.0/groundBlock;50.0;7.0/groundBlock;51.0;7.0/groundBlock;52.0;7.0/empty;53.0;7.0/groundBlock;53.0;1.0/groundBlock;54.0;1.0/fan;55.0;1.0/wasp;55.0;6.0/groundBlock;55.0;9.0/groundBlock;56.0;9.0/groundBlock;57.0;9.0/groundBlock;58.0;9.0/groundBlock;59.0;9.0/empty;60.0;9.0/groundBlock;60.0;4.0/empty;61.0;4.0/carpet;61.0;3.0/groundBlock;61.0;0.0/groundBlock;62.0;0.0/groundBlock;63.0;0.0/groundBlock;64.0;0.0/groundBlock;65.0;0.0/groundBlock;66.0;0.0/groundBlock;67.0;0.0/fan;68.0;0.0/groundBlock;68.0;5.0/dog;69.0;6.0/groundBlock;69.0;5.0/dog;70.0;6.0/groundBlock;70.0;5.0/dog;71.0;6.0/groundBlock;71.0;5.0/groundBlock;72.0;5.0/groundBlock;73.0;5.0/groundBlock;74.0;5.0/empty;75.0;5.0/groundBlock;75.0;1.0/dog;76.0;2.0/groundBlock;76.0;1.0/dog;77.0;2.0/groundBlock;77.0;1.0/fan;78.0;1.0/groundBlock;78.0;6.0/empty;79.0;6.0/groundBlock;79.0;2.0/empty;80.0;2.0/groundBlock;80.0;0.0/groundBlock;81.0;0.0/groundBlock;82.0;0.0/groundBlock;83.0;0.0/groundBlock;84.0;0.0/groundBlock;85.0;0.0/fan;86.0;0.0/groundBlock;86.0;5.0/dog;87.0;6.0/groundBlock;87.0;5.0/fan;88.0;5.0/groundBlock;88.0;8.0/empty;89.0;8.0/groundBlock;89.0;5.0/empty;90.0;5.0/carpet;90.0;4.0/groundBlock;90.0;0.0/fan;91.0;0.0/groundBlock;91.0;4.0/dog;92.0;5.0/groundBlock;92.0;4.0/empty;93.0;4.0/carpet;93.0;3.0/groundBlock;93.0;0.0/fan;94.0;0.0/groundBlock;94.0;4.0/groundBlock;95.0;4.0/empty;96.0;4.0/groundBlock;96.0;0.0/gap;97.0;1.0/groundBlock;97.0;0.0/groundBlock;99.0;0.0/groundBlock;100.0;0.0/groundBlock;101.0;0.0/groundBlock;102.0;0.0/groundBlock;103.0;0.0/groundBlock;104.0;0.0/groundBlock;105.0;0.0/groundBlock;106.0;0.0/groundBlock;107.0;0.0/fan;108.0;0.0/groundBlock;108.0;3.0/fan;109.0;3.0/groundBlock;109.0;9.0/groundBlock;110.0;9.0/groundBlock;111.0;9.0/empty;112.0;9.0/groundBlock;112.0;4.0/empty;113.0;4.0/carpet;113.0;1.0/groundBlock;113.0;0.0/groundBlock;114.0;0.0/groundBlock;115.0;0.0/groundBlock;116.0;0.0/groundBlock;117.0;0.0/groundBlock;118.0;0.0/groundBlock;119.0;0.0/groundBlock;120.0;0.0/groundBlock;121.0;0.0/groundBlock;122.0;0.0/groundBlock;123.0;0.0/groundBlock;124.0;0.0/groundBlock;125.0;0.0/groundBlock;126.0;0.0/groundBlock;127.0;0.0/groundBlock;128.0;0.0/gap;129.0;1.0/groundBlock;129.0;0.0/groundBlock;131.0;0.0/groundBlock;132.0;0.0/fan;133.0;0.0/groundBlock;133.0;6.0/groundBlock;134.0;6.0/fan;135.0;6.0/groundBlock;135.0;12.0/groundBlock;136.0;12.0/groundBlock;137.0;12.0/groundBlock;138.0;12.0/groundBlock;139.0;12.0/groundBlock;140.0;12.0/groundBlock;141.0;12.0/empty;142.0;12.0/groundBlock;142.0;8.0/groundBlock;143.0;8.0/empty;144.0;8.0/groundBlock;144.0;3.0/empty;145.0;3.0/carpet;145.0;1.0/silver_coin;5.0;10.0/silver_coin;6.0;10.0/silver_coin;7.0;10.0/bronze_coin;12.0;4.0/bronze_coin;13.0;1.0/bronze_coin;14.0;1.0/bronze_coin;15.0;1.0/gold_coin;23.0;11.0/gold_coin;24.0;11.0/silver_coin;25.0;8.0/silver_coin;29.0;9.0/silver_coin;30.0;9.0/silver_coin;31.0;9.0/bronze_coin;39.0;6.0/gold_coin;40.0;12.0/gold_coin;41.0;12.0/bronze_coin;44.0;1.0/bronze_coin;45.0;4.0/silver_coin;46.0;10.0/silver_coin;47.0;10.0/silver_coin;55.0;10.0/silver_coin;56.0;10.0/silver_coin;57.0;10.0/bronze_coin;61.0;1.0/bronze_coin;62.0;1.0/bronze_coin;63.0;1.0/bronze_coin;72.0;6.0/bronze_coin;73.0;6.0/bronze_coin;74.0;6.0/silver_coin;78.0;7.0/bronze_coin;79.0;3.0/bronze_coin;80.0;1.0/silver_coin;88.0;9.0/bronze_coin;89.0;6.0/bronze_coin;90.0;1.0/bronze_coin;91.0;5.0/bronze_coin;93.0;1.0/bronze_coin;94.0;5.0/bronze_coin;95.0;5.0/bronze_coin;99.0;1.0/bronze_coin;100.0;1.0/bronze_coin;101.0;1.0/bronze_coin;114.0;1.0/bronze_coin;115.0;1.0/bronze_coin;116.0;1.0/bronze_coin;131.0;1.0/bronze_coin;132.0;1.0/silver_coin;133.0;7.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;1.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;9.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;8.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;1.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/groundBlock;89.0;-2.0/groundBlock;90.0;-2.0/groundBlock;91.0;-2.0/groundBlock;92.0;-2.0/groundBlock;93.0;-2.0/groundBlock;94.0;-2.0/groundBlock;95.0;-2.0/door_left;96.0;-1.0/door_right;97.0;1.0/groundBlock;96.0;-2.0/groundBlock;97.0;-2.0/groundBlock;98.0;-2.0/groundBlock;99.0;-2.0/groundBlock;100.0;-2.0/groundBlock;101.0;-2.0/groundBlock;102.0;-2.0/groundBlock;103.0;-2.0/groundBlock;104.0;-2.0/groundBlock;105.0;-2.0/groundBlock;106.0;-2.0/groundBlock;107.0;-2.0/groundBlock;108.0;-2.0/groundBlock;109.0;-2.0/groundBlock;110.0;-2.0/groundBlock;111.0;-2.0/door_left;112.0;-1.0/door_right;113.0;5.0/groundBlock;112.0;-2.0/groundBlock;113.0;-2.0/groundBlock;114.0;-2.0/groundBlock;115.0;-2.0/groundBlock;116.0;-2.0/groundBlock;117.0;-2.0/groundBlock;118.0;-2.0/groundBlock;119.0;-2.0/groundBlock;120.0;-2.0/groundBlock;121.0;-2.0/groundBlock;122.0;-2.0/groundBlock;123.0;-2.0/groundBlock;124.0;-2.0/groundBlock;125.0;-2.0/groundBlock;126.0;-2.0/groundBlock;127.0;-2.0/door_left;128.0;-1.0/door_right;129.0;1.0/groundBlock;128.0;-2.0/groundBlock;129.0;-2.0/groundBlock;130.0;-2.0/groundBlock;131.0;-2.0/groundBlock;132.0;-2.0/groundBlock;133.0;-2.0/groundBlock;134.0;-2.0/groundBlock;135.0;-2.0/groundBlock;136.0;-2.0/groundBlock;137.0;-2.0/groundBlock;138.0;-2.0/groundBlock;139.0;-2.0/groundBlock;140.0;-2.0/groundBlock;141.0;-2.0/groundBlock;142.0;-2.0/groundBlock;143.0;-2.0/door_left;144.0;-1.0/door_right;145.0;4.0/groundBlock;144.0;-2.0/groundBlock;145.0;-2.0/door_left;145.0;-1.0/door_right;145.0;0.0/target;145.0;0.0/empty;1.0;5.0/groundBlock;1.0;2.0/fan;2.0;2.0/groundBlock;2.0;6.0/empty;3.0;6.0/groundBlock;3.0;0.0/fan;4.0;0.0/groundBlock;4.0;5.0/fan;5.0;5.0/groundBlock;5.0;11.0/empty;6.0;11.0/groundBlock;6.0;8.0/groundBlock;7.0;8.0/groundBlock;8.0;8.0/empty;9.0;8.0/groundBlock;9.0;6.0/empty;10.0;6.0/groundBlock;10.0;0.0/fan;11.0;0.0/wasp;11.0;2.0/groundBlock;11.0;3.0/fan;12.0;3.0/groundBlock;12.0;8.0/groundBlock;13.0;8.0/groundBlock;14.0;8.0/groundBlock;15.0;8.0/groundBlock;17.0;8.0/groundBlock;18.0;8.0/groundBlock;19.0;8.0/groundBlock;20.0;8.0/groundBlock;21.0;8.0/groundBlock;22.0;8.0/empty;23.0;8.0/groundBlock;23.0;4.0/empty;24.0;4.0/groundBlock;24.0;2.0/fan;25.0;2.0/wasp;25.0;4.0/groundBlock;25.0;6.0/empty;26.0;6.0/groundBlock;26.0;4.0/groundBlock;27.0;4.0/fan;28.0;4.0/groundBlock;28.0;7.0/groundBlock;29.0;7.0/groundBlock;30.0;7.0/groundBlock;31.0;7.0/groundBlock;33.0;7.0/groundBlock;34.0;7.0/groundBlock;35.0;7.0/groundBlock;36.0;7.0/groundBlock;37.0;7.0/groundBlock;38.0;7.0/empty;39.0;7.0/groundBlock;39.0;3.0/empty;40.0;3.0/carpet;40.0;1.0/groundBlock;40.0;0.0/fan;41.0;0.0/groundBlock;41.0;6.0/empty;42.0;6.0/carpet;42.0;2.0/groundBlock;42.0;0.0/groundBlock;43.0;0.0/groundBlock;44.0;0.0/groundBlock;45.0;0.0/groundBlock;46.0;0.0/groundBlock;47.0;0.0/groundBlock;49.0;0.0/groundBlock;50.0;0.0/groundBlock;51.0;0.0/groundBlock;52.0;0.0/groundBlock;53.0;0.0/groundBlock;54.0;0.0/groundBlock;55.0;0.0/fan;56.0;0.0/groundBlock;56.0;3.0/dog;57.0;4.0/groundBlock;57.0;3.0/groundBlock;58.0;3.0/empty;59.0;3.0/groundBlock;59.0;0.0/groundBlock;60.0;0.0/groundBlock;61.0;0.0/fan;62.0;0.0/groundBlock;62.0;2.0/empty;63.0;2.0/groundBlock;63.0;0.0/groundBlock;65.0;0.0/fan;66.0;0.0/groundBlock;66.0;6.0/fan;67.0;6.0/groundBlock;67.0;9.0/groundBlock;68.0;9.0/groundBlock;69.0;9.0/groundBlock;70.0;9.0/empty;71.0;9.0/groundBlock;71.0;6.0/empty;72.0;6.0/carpet;72.0;5.0/groundBlock;72.0;0.0/groundBlock;73.0;0.0/groundBlock;74.0;0.0/groundBlock;75.0;0.0/groundBlock;76.0;0.0/groundBlock;77.0;0.0/groundBlock;78.0;0.0/groundBlock;79.0;0.0/fan;81.0;4.0/groundBlock;81.0;7.0/groundBlock;82.0;7.0/groundBlock;83.0;7.0/groundBlock;84.0;7.0/groundBlock;85.0;7.0/groundBlock;86.0;7.0/empty;87.0;7.0/groundBlock;87.0;4.0/empty;88.0;4.0/carpet;88.0;1.0/groundBlock;88.0;0.0/fan;89.0;0.0/groundBlock;89.0;2.0/empty;90.0;2.0/carpet;90.0;2.0/groundBlock;90.0;0.0/groundBlock;91.0;0.0/groundBlock;92.0;0.0/groundBlock;93.0;0.0/fan;94.0;0.0/groundBlock;94.0;5.0/fan;95.0;5.0/groundBlock;95.0;11.0/groundBlock;97.0;11.0/groundBlock;98.0;11.0/groundBlock;99.0;11.0/empty;100.0;11.0/groundBlock;100.0;7.0/groundBlock;101.0;7.0/groundBlock;102.0;7.0/empty;103.0;7.0/groundBlock;103.0;1.0/empty;104.0;1.0/groundBlock;104.0;0.0/dog;105.0;1.0/groundBlock;105.0;0.0/dog;106.0;1.0/groundBlock;106.0;0.0/groundBlock;107.0;0.0/groundBlock;108.0;0.0/groundBlock;109.0;0.0/groundBlock;110.0;0.0/fan;111.0;0.0/groundBlock;111.0;4.0/fan;113.0;4.0/groundBlock;113.0;8.0/groundBlock;114.0;8.0/groundBlock;115.0;8.0/groundBlock;116.0;8.0/groundBlock;117.0;8.0/groundBlock;118.0;8.0/empty;119.0;8.0/groundBlock;119.0;4.0/empty;120.0;4.0/groundBlock;120.0;0.0/fan;121.0;0.0/wasp;121.0;3.0/groundBlock;121.0;7.0/empty;122.0;7.0/carpet;122.0;1.0/groundBlock;122.0;0.0/groundBlock;123.0;0.0/groundBlock;124.0;0.0/fan;125.0;0.0/groundBlock;125.0;6.0/fan;126.0;6.0/groundBlock;126.0;9.0/groundBlock;127.0;9.0/groundBlock;129.0;9.0/groundBlock;130.0;9.0/groundBlock;131.0;9.0/groundBlock;132.0;9.0/groundBlock;133.0;9.0/empty;134.0;9.0/groundBlock;134.0;3.0/empty;135.0;3.0/groundBlock;135.0;0.0/dog;136.0;1.0/groundBlock;136.0;0.0/fan;137.0;0.0/groundBlock;137.0;5.0/empty;138.0;5.0/carpet;138.0;2.0/groundBlock;138.0;0.0/groundBlock;139.0;0.0/groundBlock;140.0;0.0/groundBlock;141.0;0.0/groundBlock;142.0;0.0/groundBlock;143.0;0.0/groundBlock;145.0;0.0/groundBlock;146.0;0.0/groundBlock;147.0;0.0/groundBlock;148.0;0.0/groundBlock;149.0;0.0/groundBlock;150.0;0.0/groundBlock;151.0;0.0/dog;152.0;1.0/groundBlock;152.0;0.0/bronze_coin;11.0;4.0/silver_coin;12.0;9.0/silver_coin;13.0;9.0/silver_coin;25.0;7.0/bronze_coin;26.0;5.0/bronze_coin;27.0;5.0/silver_coin;41.0;7.0/bronze_coin;42.0;1.0/bronze_coin;43.0;1.0/bronze_coin;44.0;1.0/bronze_coin;58.0;4.0/bronze_coin;59.0;1.0/bronze_coin;60.0;1.0/bronze_coin;72.0;1.0/bronze_coin;73.0;1.0/bronze_coin;74.0;1.0/bronze_coin;89.0;3.0/bronze_coin;90.0;1.0/bronze_coin;91.0;1.0/bronze_coin;92.0;1.0/bronze_coin;107.0;1.0/bronze_coin;108.0;1.0/bronze_coin;109.0;1.0/silver_coin;121.0;8.0/bronze_coin;123.0;1.0/bronze_coin;124.0;1.0/silver_coin;125.0;7.0/bronze_coin;137.0;6.0/bronze_coin;138.0;1.0/bronze_coin;139.0;1.0/bronze_coin;140.0;1.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;9.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;8.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;1.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;8.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/groundBlock;89.0;-2.0/groundBlock;90.0;-2.0/groundBlock;91.0;-2.0/groundBlock;92.0;-2.0/groundBlock;93.0;-2.0/groundBlock;94.0;-2.0/groundBlock;95.0;-2.0/door_left;96.0;-1.0/door_right;97.0;12.0/groundBlock;96.0;-2.0/groundBlock;97.0;-2.0/groundBlock;98.0;-2.0/groundBlock;99.0;-2.0/groundBlock;100.0;-2.0/groundBlock;101.0;-2.0/groundBlock;102.0;-2.0/groundBlock;103.0;-2.0/groundBlock;104.0;-2.0/groundBlock;105.0;-2.0/groundBlock;106.0;-2.0/groundBlock;107.0;-2.0/groundBlock;108.0;-2.0/groundBlock;109.0;-2.0/groundBlock;110.0;-2.0/groundBlock;111.0;-2.0/door_left;112.0;-1.0/door_right;113.0;9.0/groundBlock;112.0;-2.0/groundBlock;113.0;-2.0/groundBlock;114.0;-2.0/groundBlock;115.0;-2.0/groundBlock;116.0;-2.0/groundBlock;117.0;-2.0/groundBlock;118.0;-2.0/groundBlock;119.0;-2.0/groundBlock;120.0;-2.0/groundBlock;121.0;-2.0/groundBlock;122.0;-2.0/groundBlock;123.0;-2.0/groundBlock;124.0;-2.0/groundBlock;125.0;-2.0/groundBlock;126.0;-2.0/groundBlock;127.0;-2.0/door_left;128.0;-1.0/door_right;129.0;10.0/groundBlock;128.0;-2.0/groundBlock;129.0;-2.0/groundBlock;130.0;-2.0/groundBlock;131.0;-2.0/groundBlock;132.0;-2.0/groundBlock;133.0;-2.0/groundBlock;134.0;-2.0/groundBlock;135.0;-2.0/groundBlock;136.0;-2.0/groundBlock;137.0;-2.0/groundBlock;138.0;-2.0/groundBlock;139.0;-2.0/groundBlock;140.0;-2.0/groundBlock;141.0;-2.0/groundBlock;142.0;-2.0/groundBlock;143.0;-2.0/door_left;144.0;-1.0/door_right;145.0;1.0/groundBlock;144.0;-2.0/groundBlock;145.0;-2.0/groundBlock;146.0;-2.0/groundBlock;147.0;-2.0/groundBlock;148.0;-2.0/groundBlock;149.0;-2.0/groundBlock;150.0;-2.0/groundBlock;151.0;-2.0/groundBlock;152.0;-2.0/groundBlock;153.0;-2.0/door_left;153.0;-1.0/door_right;153.0;1.0/target;153.0;1.0";
//		s += "/cat;0.0;4.0/groundBlock;0.0;3.0/fan;1.0;3.0/groundBlock;1.0;8.0/groundBlock;2.0;8.0/empty;3.0;8.0/groundBlock;3.0;6.0/empty;4.0;6.0/groundBlock;4.0;3.0/fan;5.0;3.0/wasp;5.0;6.0/groundBlock;5.0;9.0/groundBlock;6.0;9.0/groundBlock;7.0;9.0/groundBlock;8.0;9.0/groundBlock;9.0;9.0/empty;10.0;9.0/groundBlock;10.0;4.0/empty;11.0;4.0/groundBlock;11.0;0.0/fan;12.0;0.0/wasp;12.0;2.0/groundBlock;12.0;3.0/empty;13.0;3.0/carpet;13.0;2.0/groundBlock;13.0;0.0/groundBlock;14.0;0.0/groundBlock;15.0;0.0/groundBlock;16.0;0.0/groundBlock;17.0;0.0/groundBlock;18.0;0.0/groundBlock;19.0;0.0/groundBlock;20.0;0.0/groundBlock;21.0;0.0/groundBlock;22.0;0.0/fan;23.0;0.0/wasp;23.0;6.0/groundBlock;23.0;10.0/groundBlock;24.0;10.0/empty;25.0;10.0/groundBlock;25.0;7.0/groundBlock;26.0;7.0/groundBlock;27.0;7.0/empty;28.0;7.0/groundBlock;28.0;1.0/fan;29.0;1.0/wasp;29.0;3.0/groundBlock;29.0;8.0/groundBlock;30.0;8.0/groundBlock;31.0;8.0/groundBlock;32.0;8.0/groundBlock;33.0;8.0/groundBlock;34.0;8.0/groundBlock;35.0;8.0/empty;36.0;8.0/groundBlock;36.0;5.0/dog;37.0;6.0/groundBlock;37.0;5.0/dog;38.0;6.0/groundBlock;38.0;5.0/groundBlock;39.0;5.0/fan;40.0;5.0/groundBlock;40.0;11.0/groundBlock;41.0;11.0/groundBlock;42.0;11.0/empty;43.0;11.0/groundBlock;43.0;7.0/empty;44.0;7.0/carpet;44.0;2.0/groundBlock;44.0;0.0/fan;45.0;0.0/wasp;45.0;2.0/groundBlock;45.0;3.0/fan;46.0;3.0/groundBlock;46.0;9.0/groundBlock;47.0;9.0/empty;48.0;9.0/groundBlock;48.0;7.0/groundBlock;49.0;7.0/groundBlock;50.0;7.0/groundBlock;51.0;7.0/groundBlock;52.0;7.0/empty;53.0;7.0/groundBlock;53.0;1.0/groundBlock;54.0;1.0/fan;55.0;1.0/wasp;55.0;6.0/groundBlock;55.0;9.0/groundBlock;56.0;9.0/groundBlock;57.0;9.0/groundBlock;58.0;9.0/groundBlock;59.0;9.0/empty;60.0;9.0/groundBlock;60.0;4.0/empty;61.0;4.0/carpet;61.0;3.0/groundBlock;61.0;0.0/groundBlock;62.0;0.0/groundBlock;63.0;0.0/groundBlock;64.0;0.0/groundBlock;65.0;0.0/groundBlock;66.0;0.0/groundBlock;67.0;0.0/fan;68.0;0.0/groundBlock;68.0;5.0/dog;69.0;6.0/groundBlock;69.0;5.0/dog;70.0;6.0/groundBlock;70.0;5.0/dog;71.0;6.0/groundBlock;71.0;5.0/groundBlock;72.0;5.0/groundBlock;73.0;5.0/groundBlock;74.0;5.0/empty;75.0;5.0/groundBlock;75.0;1.0/dog;76.0;2.0/groundBlock;76.0;1.0/dog;77.0;2.0/groundBlock;77.0;1.0/fan;78.0;1.0/groundBlock;78.0;6.0/empty;79.0;6.0/groundBlock;79.0;2.0/empty;80.0;2.0/groundBlock;80.0;0.0/groundBlock;81.0;0.0/groundBlock;82.0;0.0/groundBlock;83.0;0.0/groundBlock;84.0;0.0/groundBlock;85.0;0.0/fan;86.0;0.0/groundBlock;86.0;5.0/dog;87.0;6.0/groundBlock;87.0;5.0/fan;88.0;5.0/groundBlock;88.0;8.0/empty;89.0;8.0/groundBlock;89.0;5.0/empty;90.0;5.0/carpet;90.0;4.0/groundBlock;90.0;0.0/fan;91.0;0.0/groundBlock;91.0;4.0/dog;92.0;5.0/groundBlock;92.0;4.0/empty;93.0;4.0/carpet;93.0;3.0/groundBlock;93.0;0.0/fan;94.0;0.0/groundBlock;94.0;4.0/groundBlock;95.0;4.0/empty;96.0;4.0/groundBlock;96.0;0.0/gap;97.0;1.0/groundBlock;97.0;0.0/groundBlock;99.0;0.0/groundBlock;100.0;0.0/groundBlock;101.0;0.0/groundBlock;102.0;0.0/groundBlock;103.0;0.0/groundBlock;104.0;0.0/groundBlock;105.0;0.0/groundBlock;106.0;0.0/groundBlock;107.0;0.0/fan;108.0;0.0/groundBlock;108.0;3.0/fan;109.0;3.0/groundBlock;109.0;9.0/groundBlock;110.0;9.0/groundBlock;111.0;9.0/empty;112.0;9.0/groundBlock;112.0;4.0/empty;113.0;4.0/carpet;113.0;1.0/groundBlock;113.0;0.0/groundBlock;114.0;0.0/groundBlock;115.0;0.0/groundBlock;116.0;0.0/groundBlock;117.0;0.0/groundBlock;118.0;0.0/groundBlock;119.0;0.0/groundBlock;120.0;0.0/groundBlock;121.0;0.0/groundBlock;122.0;0.0/groundBlock;123.0;0.0/groundBlock;124.0;0.0/groundBlock;125.0;0.0/groundBlock;126.0;0.0/groundBlock;127.0;0.0/groundBlock;128.0;0.0/gap;129.0;1.0/groundBlock;129.0;0.0/groundBlock;131.0;0.0/groundBlock;132.0;0.0/fan;133.0;0.0/groundBlock;133.0;6.0/groundBlock;134.0;6.0/fan;135.0;6.0/groundBlock;135.0;12.0/groundBlock;136.0;12.0/groundBlock;137.0;12.0/groundBlock;138.0;12.0/groundBlock;139.0;12.0/groundBlock;140.0;12.0/groundBlock;141.0;12.0/empty;142.0;12.0/groundBlock;142.0;8.0/groundBlock;143.0;8.0/empty;144.0;8.0/groundBlock;144.0;3.0/empty;145.0;3.0/carpet;145.0;1.0/silver_coin;5.0;10.0/silver_coin;6.0;10.0/silver_coin;7.0;10.0/bronze_coin;12.0;4.0/bronze_coin;13.0;1.0/bronze_coin;14.0;1.0/bronze_coin;15.0;1.0/gold_coin;23.0;11.0/gold_coin;24.0;11.0/silver_coin;25.0;8.0/silver_coin;29.0;9.0/silver_coin;30.0;9.0/silver_coin;31.0;9.0/bronze_coin;39.0;6.0/gold_coin;40.0;12.0/gold_coin;41.0;12.0/bronze_coin;44.0;1.0/bronze_coin;45.0;4.0/silver_coin;46.0;10.0/silver_coin;47.0;10.0/silver_coin;55.0;10.0/silver_coin;56.0;10.0/silver_coin;57.0;10.0/bronze_coin;61.0;1.0/bronze_coin;62.0;1.0/bronze_coin;63.0;1.0/bronze_coin;72.0;6.0/bronze_coin;73.0;6.0/bronze_coin;74.0;6.0/silver_coin;78.0;7.0/bronze_coin;79.0;3.0/bronze_coin;80.0;1.0/silver_coin;88.0;9.0/bronze_coin;89.0;6.0/bronze_coin;90.0;1.0/bronze_coin;91.0;5.0/bronze_coin;93.0;1.0/bronze_coin;94.0;5.0/bronze_coin;95.0;5.0/bronze_coin;99.0;1.0/bronze_coin;100.0;1.0/bronze_coin;101.0;1.0/bronze_coin;114.0;1.0/bronze_coin;115.0;1.0/bronze_coin;116.0;1.0/bronze_coin;131.0;1.0/bronze_coin;132.0;1.0/silver_coin;133.0;7.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;1.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;9.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;8.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;1.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/groundBlock;89.0;-2.0/groundBlock;90.0;-2.0/groundBlock;91.0;-2.0/groundBlock;92.0;-2.0/groundBlock;93.0;-2.0/groundBlock;94.0;-2.0/groundBlock;95.0;-2.0/door_left;96.0;-1.0/door_right;97.0;1.0/groundBlock;96.0;-2.0/groundBlock;97.0;-2.0/groundBlock;98.0;-2.0/groundBlock;99.0;-2.0/groundBlock;100.0;-2.0/groundBlock;101.0;-2.0/groundBlock;102.0;-2.0/groundBlock;103.0;-2.0/groundBlock;104.0;-2.0/groundBlock;105.0;-2.0/groundBlock;106.0;-2.0/groundBlock;107.0;-2.0/groundBlock;108.0;-2.0/groundBlock;109.0;-2.0/groundBlock;110.0;-2.0/groundBlock;111.0;-2.0/door_left;112.0;-1.0/door_right;113.0;5.0/groundBlock;112.0;-2.0/groundBlock;113.0;-2.0/groundBlock;114.0;-2.0/groundBlock;115.0;-2.0/groundBlock;116.0;-2.0/groundBlock;117.0;-2.0/groundBlock;118.0;-2.0/groundBlock;119.0;-2.0/groundBlock;120.0;-2.0/groundBlock;121.0;-2.0/groundBlock;122.0;-2.0/groundBlock;123.0;-2.0/groundBlock;124.0;-2.0/groundBlock;125.0;-2.0/groundBlock;126.0;-2.0/groundBlock;127.0;-2.0/door_left;128.0;-1.0/door_right;129.0;1.0/groundBlock;128.0;-2.0/groundBlock;129.0;-2.0/groundBlock;130.0;-2.0/groundBlock;131.0;-2.0/groundBlock;132.0;-2.0/groundBlock;133.0;-2.0/groundBlock;134.0;-2.0/groundBlock;135.0;-2.0/groundBlock;136.0;-2.0/groundBlock;137.0;-2.0/groundBlock;138.0;-2.0/groundBlock;139.0;-2.0/groundBlock;140.0;-2.0/groundBlock;141.0;-2.0/groundBlock;142.0;-2.0/groundBlock;143.0;-2.0/door_left;144.0;-1.0/door_right;145.0;4.0/groundBlock;144.0;-2.0/groundBlock;145.0;-2.0/door_left;145.0;-1.0/door_right;145.0;0.0/target;145.0;0.0/empty;1.0;5.0/groundBlock;1.0;2.0/fan;2.0;2.0/groundBlock;2.0;6.0/empty;3.0;6.0/groundBlock;3.0;0.0/fan;4.0;0.0/groundBlock;4.0;5.0/fan;5.0;5.0/groundBlock;5.0;11.0/empty;6.0;11.0/groundBlock;6.0;8.0/groundBlock;7.0;8.0/groundBlock;8.0;8.0/empty;9.0;8.0/groundBlock;9.0;6.0/empty;10.0;6.0/groundBlock;10.0;0.0/fan;11.0;0.0/wasp;11.0;2.0/groundBlock;11.0;3.0/fan;12.0;3.0/groundBlock;12.0;8.0/groundBlock;13.0;8.0/groundBlock;14.0;8.0/groundBlock;15.0;8.0/groundBlock;17.0;8.0/groundBlock;18.0;8.0/groundBlock;19.0;8.0/groundBlock;20.0;8.0/groundBlock;21.0;8.0/groundBlock;22.0;8.0/empty;23.0;8.0/groundBlock;23.0;4.0/empty;24.0;4.0/groundBlock;24.0;2.0/fan;25.0;2.0/wasp;25.0;4.0/groundBlock;25.0;6.0/empty;26.0;6.0/groundBlock;26.0;4.0/groundBlock;27.0;4.0/fan;28.0;4.0/groundBlock;28.0;7.0/groundBlock;29.0;7.0/groundBlock;30.0;7.0/groundBlock;31.0;7.0/groundBlock;33.0;7.0/groundBlock;34.0;7.0/groundBlock;35.0;7.0/groundBlock;36.0;7.0/groundBlock;37.0;7.0/groundBlock;38.0;7.0/empty;39.0;7.0/groundBlock;39.0;3.0/empty;40.0;3.0/carpet;40.0;1.0/groundBlock;40.0;0.0/fan;41.0;0.0/groundBlock;41.0;6.0/empty;42.0;6.0/carpet;42.0;2.0/groundBlock;42.0;0.0/groundBlock;43.0;0.0/groundBlock;44.0;0.0/groundBlock;45.0;0.0/groundBlock;46.0;0.0/groundBlock;47.0;0.0/groundBlock;49.0;0.0/groundBlock;50.0;0.0/groundBlock;51.0;0.0/groundBlock;52.0;0.0/groundBlock;53.0;0.0/groundBlock;54.0;0.0/groundBlock;55.0;0.0/fan;56.0;0.0/groundBlock;56.0;3.0/dog;57.0;4.0/groundBlock;57.0;3.0/groundBlock;58.0;3.0/empty;59.0;3.0/groundBlock;59.0;0.0/groundBlock;60.0;0.0/groundBlock;61.0;0.0/fan;62.0;0.0/groundBlock;62.0;2.0/empty;63.0;2.0/groundBlock;63.0;0.0/groundBlock;65.0;0.0/fan;66.0;0.0/groundBlock;66.0;6.0/fan;67.0;6.0/groundBlock;67.0;9.0/groundBlock;68.0;9.0/groundBlock;69.0;9.0/groundBlock;70.0;9.0/empty;71.0;9.0/groundBlock;71.0;6.0/empty;72.0;6.0/carpet;72.0;5.0/groundBlock;72.0;0.0/groundBlock;73.0;0.0/groundBlock;74.0;0.0/groundBlock;75.0;0.0/groundBlock;76.0;0.0/groundBlock;77.0;0.0/groundBlock;78.0;0.0/groundBlock;79.0;0.0/fan;81.0;4.0/groundBlock;81.0;7.0/groundBlock;82.0;7.0/groundBlock;83.0;7.0/groundBlock;84.0;7.0/groundBlock;85.0;7.0/groundBlock;86.0;7.0/empty;87.0;7.0/groundBlock;87.0;4.0/empty;88.0;4.0/carpet;88.0;1.0/groundBlock;88.0;0.0/fan;89.0;0.0/groundBlock;89.0;2.0/empty;90.0;2.0/carpet;90.0;2.0/groundBlock;90.0;0.0/groundBlock;91.0;0.0/groundBlock;92.0;0.0/groundBlock;93.0;0.0/fan;94.0;0.0/groundBlock;94.0;5.0/fan;95.0;5.0/groundBlock;95.0;11.0/groundBlock;97.0;11.0/groundBlock;98.0;11.0/groundBlock;99.0;11.0/empty;100.0;11.0/groundBlock;100.0;7.0/groundBlock;101.0;7.0/groundBlock;102.0;7.0/empty;103.0;7.0/groundBlock;103.0;1.0/empty;104.0;1.0/groundBlock;104.0;0.0/dog;105.0;1.0/groundBlock;105.0;0.0/dog;106.0;1.0/groundBlock;106.0;0.0/groundBlock;107.0;0.0/groundBlock;108.0;0.0/groundBlock;109.0;0.0/groundBlock;110.0;0.0/fan;111.0;0.0/groundBlock;111.0;4.0/fan;113.0;4.0/groundBlock;113.0;8.0/groundBlock;114.0;8.0/groundBlock;115.0;8.0/groundBlock;116.0;8.0/groundBlock;117.0;8.0/groundBlock;118.0;8.0/empty;119.0;8.0/groundBlock;119.0;4.0/empty;120.0;4.0/groundBlock;120.0;0.0/fan;121.0;0.0/wasp;121.0;3.0/groundBlock;121.0;7.0/empty;122.0;7.0/carpet;122.0;1.0/groundBlock;122.0;0.0/groundBlock;123.0;0.0/groundBlock;124.0;0.0/fan;125.0;0.0/groundBlock;125.0;6.0/fan;126.0;6.0/groundBlock;126.0;9.0/groundBlock;127.0;9.0/groundBlock;129.0;9.0/groundBlock;130.0;9.0/groundBlock;131.0;9.0/groundBlock;132.0;9.0/groundBlock;133.0;9.0/empty;134.0;9.0/groundBlock;134.0;3.0/empty;135.0;3.0/groundBlock;135.0;0.0/dog;136.0;1.0/groundBlock;136.0;0.0/fan;137.0;0.0/groundBlock;137.0;5.0/empty;138.0;5.0/carpet;138.0;2.0/groundBlock;138.0;0.0/groundBlock;139.0;0.0/groundBlock;140.0;0.0/groundBlock;141.0;0.0/groundBlock;142.0;0.0/groundBlock;143.0;0.0/groundBlock;145.0;0.0/groundBlock;146.0;0.0/groundBlock;147.0;0.0/groundBlock;148.0;0.0/groundBlock;149.0;0.0/groundBlock;150.0;0.0/groundBlock;151.0;0.0/dog;152.0;1.0/groundBlock;152.0;0.0/bronze_coin;11.0;4.0/silver_coin;12.0;9.0/silver_coin;13.0;9.0/silver_coin;25.0;7.0/bronze_coin;26.0;5.0/bronze_coin;27.0;5.0/silver_coin;41.0;7.0/bronze_coin;42.0;1.0/bronze_coin;43.0;1.0/bronze_coin;44.0;1.0/bronze_coin;58.0;4.0/bronze_coin;59.0;1.0/bronze_coin;60.0;1.0/bronze_coin;72.0;1.0/bronze_coin;73.0;1.0/bronze_coin;74.0;1.0/bronze_coin;89.0;3.0/bronze_coin;90.0;1.0/bronze_coin;91.0;1.0/bronze_coin;92.0;1.0/bronze_coin;107.0;1.0/bronze_coin;108.0;1.0/bronze_coin;109.0;1.0/silver_coin;121.0;8.0/bronze_coin;123.0;1.0/bronze_coin;124.0;1.0/silver_coin;125.0;7.0/bronze_coin;137.0;6.0/bronze_coin;138.0;1.0/bronze_coin;139.0;1.0/bronze_coin;140.0;1.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;9.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;8.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;1.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;8.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/groundBlock;89.0;-2.0/groundBlock;90.0;-2.0/groundBlock;91.0;-2.0/groundBlock;92.0;-2.0/groundBlock;93.0;-2.0/groundBlock;94.0;-2.0/groundBlock;95.0;-2.0/door_left;96.0;-1.0/door_right;97.0;12.0/groundBlock;96.0;-2.0/groundBlock;97.0;-2.0/groundBlock;98.0;-2.0/groundBlock;99.0;-2.0/groundBlock;100.0;-2.0/groundBlock;101.0;-2.0/groundBlock;102.0;-2.0/groundBlock;103.0;-2.0/groundBlock;104.0;-2.0/groundBlock;105.0;-2.0/groundBlock;106.0;-2.0/groundBlock;107.0;-2.0/groundBlock;108.0;-2.0/groundBlock;109.0;-2.0/groundBlock;110.0;-2.0/groundBlock;111.0;-2.0/door_left;112.0;-1.0/door_right;113.0;9.0/groundBlock;112.0;-2.0/groundBlock;113.0;-2.0/groundBlock;114.0;-2.0/groundBlock;115.0;-2.0/groundBlock;116.0;-2.0/groundBlock;117.0;-2.0/groundBlock;118.0;-2.0/groundBlock;119.0;-2.0/groundBlock;120.0;-2.0/groundBlock;121.0;-2.0/groundBlock;122.0;-2.0/groundBlock;123.0;-2.0/groundBlock;124.0;-2.0/groundBlock;125.0;-2.0/groundBlock;126.0;-2.0/groundBlock;127.0;-2.0/door_left;128.0;-1.0/door_right;129.0;10.0/groundBlock;128.0;-2.0/groundBlock;129.0;-2.0/groundBlock;130.0;-2.0/groundBlock;131.0;-2.0/groundBlock;132.0;-2.0/groundBlock;133.0;-2.0/groundBlock;134.0;-2.0/groundBlock;135.0;-2.0/groundBlock;136.0;-2.0/groundBlock;137.0;-2.0/groundBlock;138.0;-2.0/groundBlock;139.0;-2.0/groundBlock;140.0;-2.0/groundBlock;141.0;-2.0/groundBlock;142.0;-2.0/groundBlock;143.0;-2.0/door_left;144.0;-1.0/door_right;145.0;1.0/groundBlock;144.0;-2.0/groundBlock;145.0;-2.0/groundBlock;146.0;-2.0/groundBlock;147.0;-2.0/groundBlock;148.0;-2.0/groundBlock;149.0;-2.0/groundBlock;150.0;-2.0/groundBlock;151.0;-2.0/groundBlock;152.0;-2.0/groundBlock;153.0;-2.0/door_left;153.0;-1.0/door_right;153.0;1.0/target;153.0;1.0";

		s = s.replace("\"", "");
//		s = s.replace(".0", "");
		Gdx.app.log(RollingCat.LOG, s);
		String tab [] = s.split("/");
		String[] subtab;
		float x;
		float y;
		decalage = 0;
		
		Cat cat = new Cat(1, 0);
		stage.addActor(cat);
		
		for (int i = 0; i < tab.length; i++) {
			subtab = tab[i].split(";");
			x = Float.valueOf(subtab[1]);
			y = Float.valueOf(subtab[2]);

			if(subtab[0].equals("cat"))
			{
				stage.addActor(new Cat(x, y + decalage));
			}
			else if(subtab[0].equals("wasp"))
			{
				stage.addActor(new Wasp(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.SWATTER);
			}
			else if(subtab[0].equals("dog"))
			{
				stage.addActor(new Dog(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.BONE);
			}
			else if(subtab[0].equals("groundBlock"))
			{
				stage.getActors().insert(1, new GroundBlock(x, y + decalage));
				//				stage.addActor(new GroundBlock(x, y + decalage));
			}
			//			else if(subtab[0].equals("empty + decalage"))
			//			{
			//				//	stage.addActor(new StopBlock(x, y + decalage));
			//				directions.add(new StopBlock(x, y + decalage));
			//			}
			else if(subtab[0].equals("bronze_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.BRONZE));
			}
			else if(subtab[0].equals("silver_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.SILVER));
			}
			else if(subtab[0].equals("gold_coin"))
			{
				stage.addActor(new Coin(x, y + decalage, Coin.GOLD));
			}
			else if(subtab[0].equals("carpet"))
			{
				stage.addActor(new Carpet(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.SCISSORS);
			}
			else if(subtab[0].equals("fan"))
			{
				stage.addActor(new Fan(x,y + decalage));
			}
			else if(subtab[0].equals("door_left"))
			{	
				String [] temp = tab[i+1].split(";");
				float nextX = Float.valueOf(temp[1]);
				float nextY = Float.valueOf(temp[2]);
				stage.addActor(new Door(x,y + decalage, Door.LEFT, nextX, nextY + decalage));
			}
			else if(subtab[0].equals("door_right"))
			{
				stage.addActor(new Door(x,y + decalage, Door.RIGHT, 0, 0));
			}
			else if(subtab[0].equals("gap"))
			{
				stage.addActor(new Gap(x, y + decalage));
				if(isFirstOfScreen(x))
				{
					items.add(Box.EMPTY);
				}
				items.add(Box.FEATHER);
			}
			else if(subtab[0].equals("target")){
				stage.addActor(new Target(x, y + decalage));
				items.add(Box.EMPTY);
				addItemsInBox(segment, decalage / (GameConstants.DECALAGE));
				decalage += GameConstants.DECALAGE;
				items = new ArrayList<Integer>();
				oldSegment = segment;
				segment = 0;
			}

			Gdx.app.log(RollingCat.LOG, "new "+subtab[0]+ " added in " + x + ", " + y + decalage + " !");
		}


		Gdx.app.log(RollingCat.LOG, "building done " + tab.length + " elements added");
		//		stage.addActor(new Cat(10, 10));
		//		stage.addActor(new Dog(5, 5));
		//		((Box)stage.getActors().get(1)).setItems(items);
		cat.setY(getNumberOfEtage() * GameConstants.DISPLAY_HEIGHT - GameConstants.BLOCK_HEIGHT);
		return stage;
	}

	public static OrderedMap<String, ArrayList<Integer>> getItems(){
		Gdx.app.log(RollingCat.LOG, map.toString());
		return map;
	}
	
	/**
	 * le premier chiffre de la clé est l'étage : 0 pour le bas (challenge) et 1 pour le haut (assistance)
	 * ce qui vient après est le segment
	 * @param item
	 */
	private static void addItemsInBox(int segment, int etage){
		items.add(Box.EMPTY);
		map.put("" + etage + "" + segment, items);
		items = new ArrayList<Integer>();
	}

	/**
	 * 
	 * @param x abscisse de l'entité
	 * @return true si l'entité est la première de l'écran
	 */
	private static boolean isFirstOfScreen(float x){
		if(Math.floor(x / (GameConstants.COLS)) > segment){
			addItemsInBox(segment, decalage / (GameConstants.DECALAGE));
			segment ++;
			
			return false;
		}
		else
			return false;
	}

	/**
	 * 
	 * @return le nombre d'étages dans le niveau
	 */
	public static int getNumberOfEtage() {
		return decalage / GameConstants.DECALAGE;
	}	
	
	/**
	 * 
	 * @return le nombre de segments dans le niveau
	 */
	public static int getNumberOfSegment() {
		return oldSegment;
	}	

}
