package fr.lirmm.smile.rollingcat;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Localisation {
	private static Element lang;
	private static Array<Element> elem;
	private static List list;

	public static final String _wrong_info = "wrong_info";
	public static final String _username = "username";
	public static final String _password = "password";
	public static final String _settings = "settings";
	public static final String _back = "back";
	public static final String _select = "select";
	public static final String _play = "play";
	public static final String _assessment = "assessment";
	public static final String _upload = "upload";
	public static final String _name = "name";
	public static final String _dominant_member = "dominant_member";
	public static final String _hemiplegia = "hemiplegia";
	public static final String _previous = "previous";
	public static final String _start = "start";
	public static final String _next = "next";
	public static final String _gems = "gems";
	public static final String _gem = "gem";
	public static final String _new_level = "new_level";
	public static final String _level = "level";
	public static final String _score = "score";
	public static final String _duration = "duration";
	public static final String _locked = "locked";
	public static final String _not_found = "not_found";
	public static final String _found = "found";
	public static final String _resume = "resume";
	public static final String _quit = "quit";
	public static final String _workspace_height = "workspace_height";
	public static final String _workspace_width = "workspace_width";
	public static final String _range = "range";
	public static final String _path_delta_time = "path_delta_time";
	public static final String _evaporation_per_day = "evaporation_per_day";
	public static final String _alpha = "alpha";
	public static final String _number_of_lines = "number_of_lines";
	public static final String _number_of_rows = "number_of_rows";
	public static final String _total_volume = "total_volume";
	public static final String _volume_per_level = "volume_per_level";
	public static final String _delete = "delete";
	public static final String _save = "save";
	public static final String _discard = "discard";
	public static final String _tutorial = "tutorial";
	public static final String _welcome = "welcome";
	public static final String _box_point = "box_point";
	public static final String _coin_get = "coin_get";
	public static final String _gg = "gg";
	public static final String _fall = "fall";
	public static final String _task_point_1 = "task_point_1";
	public static final String _feather = "feather";
	public static final String _bone = "bone";
	public static final String _swatter = "swatter";
	public static final String _scissors = "scissors";
	public static final String _task_point_2 = "task_point_2";
	public static final String _dog = "dog";
	public static final String _wasp = "wasp";
	public static final String _cat = "cat";
	public static final String _carpet = "carpet";
	public static final String _assessment_ = "assessment_";
	public static final String _needsAtLeastOneArea = "needsAtLeastOneArea";
	public static final String _tracks = "tracks";
	public static final String _left = "left";
	public static final String _right = "right";
	public static final String _left_handed = "left_handed";
	public static final String _right_handed = "right_handed";
	public static final String _high_score = "high_score";
	public static final String _cadran = "cadran";
	public static final String _ready = "ready";
	public static final String _detail = "detail";
	public static final String _skin = "skin";
	public static final String _timeout = "timeout";
	public static final String _nbsuccess = "nbsuccess";
	public static final String _more = "more";
	public static final String _reversed = "reversed";
	public static final String _patients = "patients";
	public static final String _yes = "yes";
	public static final String _no	= "no";
	public static final String _win = "win";

	public static void initLanguage()
	{
		ArrayList<String> languages = new ArrayList<String>();

		FileHandle file = Gdx.files.internal("data/localisation/language.xml");
		Gdx.app.log(RollingCat.LOG, "done.");
		Gdx.app.log(RollingCat.LOG, "parsing lang file...");

		XmlReader reader = new XmlReader();

		XmlReader.Element langs;

		String s = file.readString();
		
		s = s.replace("&agrave;", "à");
		s = s.replace("&egrave;", "è");
		s = s.replace("&eacute;", "é");
		s = s.replace("&Eacute;", "É");
		s = s.replace("&ecirc;", "ê");
		s = s.replace("&ccedil;", "ç");
		
		langs = reader.parse(s);
		elem = langs.getChildrenByName("language");

		for (Element e : elem) {
			languages.add(e.getAttributes().values().next().toString());
		}


		ListStyle ls = new ListStyle();
		ls.font = getBigFont();
		ls.fontColorSelected = Color.WHITE;
		ls.fontColorUnselected = Color.BLACK;
		ls.selection = getSkin().getDrawable("selection");
		list = new List(languages.toArray(), ls);

		loadLanguage(0);
	}

	/**
	 * charge un langage depuis un fichier de langage
	 * les fichiers de langage sont nommés de 0 à n pour être plus simples à trouver
	 * la correspondance numéro/langage est dans un fichier Json à part
	 * @param language
	 */
	public static void loadLanguage(int language) throws SerializationException{
		lang = elem.get(language);
	}	

	/**
	 * 
	 * @param field
	 * @return la valeur correspondant à la clé donnée en parametre
	 */
	public static String localisation(String field){
		return lang.get(field);
	}

	public static List getAvailableLanguages(){
		return list;
	}

}
