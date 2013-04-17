package fr.lirmm.smile.rollingcat;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

public class Localisation {
	private static OrderedMap<String, String> lang;
	private static OrderedMap<String, String> langs;
	private static List list;

	public static String _wrong_info = "wrong_info";
	public static String _username = "username";
	public static String _password = "password";
	public static String _settings = "settings";
	public static String _back = "back";
	public static String _select = "select";
	public static String _play = "play";
	public static String _assessment = "assessment";
	public static String _upload = "upload";
	public static String _name = "name";
	public static String _dominant_member = "dominant_member";
	public static String _hemiplegia = "hemiplegia";
	public static String _previous = "previous";
	public static String _start = "start";
	public static String _next = "next";
	public static String _gems = "gems";
	public static String _gem = "gem";
	public static String _new_level = "new_level";
	public static String _level = "level";
	public static String _score = "score";
	public static String _duration = "duration";
	public static String _locked = "locked";
	public static String _not_found = "not_found";
	public static String _found = "found";
	public static String _resume = "resume";
	public static String _quit = "quit";
	public static String _workspace_height = "workspace_height";
	public static String _workspace_width = "workspace_width";
	public static String _range = "range";
	public static String _path_delta_time = "path_delta_time";
	public static String _evaporation_per_day = "evaporation_per_day";
	public static String _alpha = "alpha";
	public static String _number_of_lines = "number_of_lines";
	public static String _number_of_rows = "number_of_rows";
	public static String _total_volume = "total_volume";
	public static String _volume_per_level = "volume_per_level";
	public static String _delete = "delete";
	public static String _save = "save";
	public static String _discard = "discard";
	public static String _tutorial = "tutorial";
	public static String _welcome = "welcome";
	public static String _box_point = "box_point";
	public static String _coin_get = "coin_get";
	public static String _gg = "gg";
	public static String _fall = "fall";
	public static String _task_point_1 = "task_point_1";
	public static String _feather = "feather";
	public static String _bone = "bone";
	public static String _swatter = "swatter";
	public static String _scissors = "scissors";
	public static String _task_point_2 = "task_point_2";
	public static String _dog = "dog";
	public static String _wasp = "wasp";
	public static String _cat = "cat";
	public static String _carpet = "carpet";
	public static String _assessment_ = "assessment_";
	public static String _needsAtLeastOneArea = "needsAtLeastOneArea";
	public static String _tracks = "tracks";
	public static String _left = "left";
	public static String _right = "right";
	public static String _left_handed = "left_handed";
	public static String _right_handed = "right_handed";
	
	/**
	 * charge un langage depuis un fichier de langage
	 * les fichiers de langage sont nommés de 0 à n pour être plus simples à trouver
	 * la correspondance numéro/langage est dans un fichier Json à part
	 * @param language
	 */
	@SuppressWarnings("unchecked")
	public static void loadLanguage(int language){
		Json json = new Json();

		Gdx.app.log(RollingCat.LOG, "retriving langs file...");

		FileHandle file = Gdx.files.internal("data/localisation/"+language+".txt");
		Gdx.app.log(RollingCat.LOG, "done.");
		Gdx.app.log(RollingCat.LOG, "parsing level file...");

		String jsonData = file.readString();

		lang = (OrderedMap<String, String>) new JsonReader().parse(jsonData);
		Gdx.app.log(RollingCat.LOG, json.prettyPrint(lang));

		getAvailableLanguages();

	}	

	/**
	 * 
	 * @param field
	 * @return la valeur correspondant à la clé donnée en parametre
	 */
	public static String localisation(String field){
		return lang.get(field);
	}
	
	/**
	 * les fichiers de langue sont numérotés de 0 à n
	 * avec leur correspondance en lettre dans un fichier Json 
	 * exemple  = 1:"English"
	 * on parcours tous ces fichiers et crée une {@link List} des langues disponibles
	 * @return un {@link List} des langues trouvées
	 */
	@SuppressWarnings("unchecked")
	public static List getAvailableLanguages(){
		if(list == null)
		{
			Json json = new Json();

			Gdx.app.log(RollingCat.LOG, "retriving lang file...");

			FileHandle file = Gdx.files.internal("data/localisation/lang.txt");
			Gdx.app.log(RollingCat.LOG, "done.");
			Gdx.app.log(RollingCat.LOG, "parsing lang file...");

			String jsonData = file.readString();

			langs = (OrderedMap<String, String>) new JsonReader().parse(jsonData);
			Gdx.app.log(RollingCat.LOG, json.prettyPrint(langs));

			ArrayList<String> fileNames = new ArrayList<String>();
			int i = 0;

			while(Gdx.files.internal("data/localisation/"+i+".txt").exists()){
				fileNames.add(langs.get(""+i));
				i++;
			}

			ListStyle ls = new ListStyle();
			ls.font = getBigFont();
			ls.fontColorSelected = Color.WHITE;
			ls.fontColorUnselected = Color.BLACK;
			ls.selection = getSkin().getDrawable("selection");
			list = new List(fileNames.toArray(), ls);
		}
		return list;
	}

}
