package fr.lirmm.smile.rollingcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.*;

public class Localisation {
	private static OrderedMap<String, String> lang;

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


	@SuppressWarnings("unchecked")
	public static void loadLanguage(String language){
		Json json = new Json();

		Gdx.app.log(RollingCat.LOG, "retriving level file...");

		FileHandle file = Gdx.files.internal("data/localisation/"+language+".txt");

		Gdx.app.log(RollingCat.LOG, "done.");
		Gdx.app.log(RollingCat.LOG, "parsing level file...");

		String jsonData = file.readString();

		lang = (OrderedMap<String, String>) new JsonReader().parse(jsonData);
		Gdx.app.log(RollingCat.LOG, json.prettyPrint(lang));
//		Field[] fields = Localisation.class.getFields();
//		for (int i = 0; i < fields.length; i++) {
//			try {
//				fields[i].set(null, (json.readValue(fields[i].getName(), String.class, lang) == null)?fields[i].get(null):(json.readValue(fields[i].getName(), String.class, lang)));
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		getAvailableLanguages();
	
	}	
	
	public static String localisation(String field){
		return lang.get(field);
	}

	public static List getAvailableLanguages(){
		FileHandle dirHandle = Gdx.files.internal("./bin/data/localisation");
		FileHandle[] files = dirHandle.list();
		String [] fileNames = new String[files.length];
	
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = files[i].nameWithoutExtension();
		}
		
		ListStyle ls = new ListStyle();
		ls.font = getBigFont();
		ls.fontColorSelected = Color.WHITE;
		ls.fontColorUnselected = Color.BLACK;
		ls.selection = getSkin().getDrawable("selection");
		return new List(fileNames, ls);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
