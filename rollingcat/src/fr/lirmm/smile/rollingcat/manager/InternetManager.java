package fr.lirmm.smile.rollingcat.manager;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.model.world.Level;
import fr.lirmm.smile.rollingcat.model.world.World;
import fr.lirmm.smile.rollingcat.screen.LoginScreen;

public class InternetManager{

	private static String level;
	private static String patients;
	private static String hostName = "localhost";
	private static int port = 9000;
	private static String path = "";
	private static String key = "PLAY_SESSION";
	private static String gameid;
	public static String value;
	public static String sessionid;
	private static String world;
	private static TextButton okButton;
	public static int sent = 0;
	public static float [] ability;
	public static int [] nblevels = new int[3];
	public static String tasks;

	/**
	 * set l'adresse et le port + path du serveur
	 * @param server
	 * @param serverPort
	 */
	public static void config(String server, String serverPort, String serverPath) {
		hostName = server;
		port = Integer.valueOf(serverPort);
		path = serverPath;
	}

	/**
	 * envoie la requete pour récupérer une clé de connexion
	 * @param username
	 * @param password
	 * @return true si tout s'est bien passé
	 */
	public static void login(String username, String password) {

		Gdx.app.log(RollingCat.LOG, "preparing login request...");

		final HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setContent("email="+username+"&password="+password);
		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/login.json");

		Gdx.app.log(RollingCat.LOG, "sending login request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String s = httpResponse.getResultAsString();
				value = json.readValue(key, String.class, new JsonReader().parse(s));
				Gdx.app.log(RollingCat.LOG, value);
				Gdx.app.log(RollingCat.LOG, "login ok");

			}

			@Override
			public void failed(Throwable t) {
				LoginScreen.setWrong();
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong, could not login");
			}});

	}

	/**
	 * envoie une requete pour récupérer la liste de patients depuis le serveur
	 */
	public static void retrievePatients(){
		Gdx.app.log(RollingCat.LOG, "preparing patient retrieve request...");

		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);

		httpGet.setUrl("http://" + hostName + ":" + port + path + "/patient/all");
		httpGet.setHeader(key, value);
		Gdx.app.log(RollingCat.LOG, "sending patient retrieve request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				patients = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, patients);
				Gdx.app.log(RollingCat.LOG, "retrieving complete");

			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong, could not retrieve patient");
			}});
	}

	/**
	 * envoie une requete pour l'ID du jeu sur le serveur
	 */
	public static void getGameId(){
		Gdx.app.log(RollingCat.LOG, "preparing game id retrieve request...");

		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);

		httpGet.setUrl("http://" + hostName + ":" + port + path + "/game/"+RollingCat.rollingCat+"/getid");

		httpGet.setHeader(key, value);
		Gdx.app.log(RollingCat.LOG, "sending game id retrieve request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				gameid = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, "game id : " + gameid);
				Gdx.app.log(RollingCat.LOG, "retrieving complete");

			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong, could not retrieve game id");
			}});
	}

	/**
	 * set la date dans la track
	 * @param track
	 */
	public static void getDate(final Track track){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setTimeOut(0);
		httpGet.setUrl("http://infolimon.iutmontp.univ-montp2.fr/~lephilippen/rollingcat/getDate.php");
		Gdx.app.log(RollingCat.LOG, "sending request...");
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String s = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, s);
				track.setDate(s);
				Gdx.app.log(RollingCat.LOG, "success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
				track.setDate("error");
			}});
	}

	/**
	 * set la date dans la track
	 * @param track
	 */
	public static void newGameSession(String gameType, String patientid){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/gamesession/new");
		OrderedMap<String, String> map = new OrderedMap<String, String>();
		map.put("sessionType", gameType);
		map.put("comment", "no comment");
		map.put("patient_id", patientid);
		map.put("game_id", gameid);
		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);
		httpGet.setContent(json.toJson(map));
		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");
		Gdx.app.log(RollingCat.LOG, "sending new game session retrieve request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				sessionid = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, "game session : " + sessionid);
				Gdx.app.log(RollingCat.LOG, "success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}});
	}

	/**
	 * set la date dans la track
	 * @param track
	 */
	public static void endGameSession(){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/gamesession/"+sessionid+"/end ");

		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "text/plain");

		Gdx.app.log(RollingCat.LOG, "sending end game session request...");
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log(RollingCat.LOG, "success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}});
	}

	/**
	 * récupère le level sur le serveur
	 * @param IDpatient
	 */
	public static void fetchLevel(int numLevel){
		level = null;
		Level.clearContent();

		Gdx.app.log(RollingCat.LOG, "preparing request...");
		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);

		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);

		httpGet.setUrl("http://" + hostName + ":" + port + path + "/level/"+Patient.getInstance().getID()+"/"+gameid+"/"+sessionid+"/"+numLevel+"/"+(RollingCat.skin - 1));

		OrderedMap<String, Object> map = new OrderedMap<String, Object>();

		map.put("builderId",GameConstants.getAlgo());
		map.put("patientId", Patient.getInstance().getID());
		map.put("numberOfLines", GameConstants.numberOfLines);
		map.put("numberOfRows", GameConstants.numberOfRows);
		map.put("totalHeight", GameConstants.workspaceHeight);
		map.put("totalWidth", GameConstants.workspaceWidth);
		map.put("totalVolume", GameConstants.totalVolume);
		map.put("volumePerLevel", GameConstants.volumePerLevel);
		map.put("ImportanceOfEffort", 0.9f);

		if(GameConstants.area_1 & GameConstants.area_2 & GameConstants.area_3 & GameConstants.area_4 || !GameConstants.area_1 & !GameConstants.area_2 & !GameConstants.area_3 & !GameConstants.area_4)
		{
			map.put("dials", "0000");
		}
		else
		{
			if(!GameConstants.reversedLevel)
				map.put("dials", "" + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0"));
			else
				map.put("dials", "" + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0"));
		}
		System.out.println(map.get("dials"));


		map.put("leftHemiplegia", Patient.getInstance().isLeftHemiplegia());
		map.put("reversed", GameConstants.reversedLevel);
		Gdx.app.log(RollingCat.LOG, "reversed level : " + Patient.getInstance().isLeftHemiplegia());

		OrderedMap<String,Object> algoParameterAZ =  new OrderedMap<String,Object>();
		algoParameterAZ.put("range", GameConstants.range);
		algoParameterAZ.put("pathDeltaTime", GameConstants.pathDeltaTime);
		algoParameterAZ.put("evaporationRatioPerDay", GameConstants.evaporationPerDay);
		algoParameterAZ.put("alpha", GameConstants.alpha);
		algoParameterAZ.put("assessmentDataOnly", "true");
		map.put("parameters", algoParameterAZ); 


		httpGet.setContent(json.toJson(map));

		Gdx.app.log(RollingCat.LOG, httpGet.getContent());	

		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");

		Gdx.app.log(RollingCat.LOG, "sending level request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				level = httpResponse.getResultAsString();
				Level.setContent(level);
				Gdx.app.log(RollingCat.LOG, "success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});
		//		level = "cat;0.0;8.0/groundBlock;0.0;7.0/groundBlock;1.0;7.0/groundBlock;2.0;7.0/gap;2.0;8.0/groundBlock;4.0;6.0/groundBlock;5.0;6.0/groundBlock;6.0;7.0";
	}

	public static void needsAssessment(){
		world = null;
		World.clearInstance();
		Gdx.app.log(RollingCat.LOG, "preparing request...");

		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);

		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/patient/"+Patient.getInstance().getID()+"/needsassessment");
		OrderedMap<String, Object> map = new OrderedMap<String, Object>();

		map.put("builderId",GameConstants.getDDAAlgo());
		map.put("patientId", Patient.getInstance().getID());
		map.put("numberOfLines", GameConstants.numberOfLines);
		map.put("numberOfRows", GameConstants.numberOfRows);
		map.put("totalHeight", GameConstants.workspaceHeight);
		map.put("totalWidth", GameConstants.workspaceWidth);
		map.put("totalVolume", GameConstants.totalVolume);
		map.put("volumePerLevel", GameConstants.volumePerLevel);
		map.put("ImportanceOfEffort", 0.9f);

		if(GameConstants.area_1 & GameConstants.area_2 & GameConstants.area_3 & GameConstants.area_4 || !GameConstants.area_1 & !GameConstants.area_2 & !GameConstants.area_3 & !GameConstants.area_4)
		{
			map.put("dials", "0000");
		}
		else
		{
			if(!GameConstants.reversedLevel)
				map.put("dials", "" + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0"));
			else
				map.put("dials", "" + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0"));
		}
		
		System.out.println(map.get("dials"));

		map.put("leftHemiplegia", Patient.getInstance().isLeftHemiplegia());
		Gdx.app.log(RollingCat.LOG, "reversed level : " + Patient.getInstance().isLeftHemiplegia());

		OrderedMap<String,Object> algoParameterAZ =  new OrderedMap<String,Object>();
		algoParameterAZ.put("range", GameConstants.range);
		algoParameterAZ.put("pathDeltaTime", GameConstants.pathDeltaTime);
		algoParameterAZ.put("evaporationRatioPerDay", GameConstants.evaporationPerDay);
		algoParameterAZ.put("alpha", GameConstants.alpha);
		algoParameterAZ.put("assessmentDataOnly", "true");
		map.put("parameters", algoParameterAZ); 


		httpGet.setContent(json.toJson(map));

		Gdx.app.log(RollingCat.LOG, httpGet.getContent());	

		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");

		Gdx.app.log(RollingCat.LOG, "sending level request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String s = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, "needs assessment : " + s);
				Patient.getInstance().setNeedsAssessment(s);
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});
	}

	/** 
	 * envoie les events au serveur dda
	 * @param events les events sous forme de string formatée en JSON
	 */
	public static void sendEvents(String[] events){
		Gdx.app.log(RollingCat.LOG, "preparing send list event request...");
		okButton.setVisible(true);
		okButton.setText("en attente");

		for (String event : events) {
			HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
			httpGet.setUrl("http://" + hostName + ":" + port + path + "/event/"+sessionid+"/newList");
			httpGet.setContent(event);
			httpGet.setHeader(key, value);
			httpGet.setHeader("Content-Type", "application/json");
			sent = 0;


			Gdx.app.log(RollingCat.LOG, "sending list event request...");

			Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					Gdx.app.log(RollingCat.LOG, httpResponse.getResultAsString());
					okButton.setText("envoi réussi");
					sent = 1;
					Gdx.app.log(RollingCat.LOG, "list event request success");
				}

				@Override
				public void failed(Throwable t) {	
					okButton.setText("erreur");
					sent = -1;
					Gdx.app.log(RollingCat.LOG, t.toString());
					Gdx.app.log(RollingCat.LOG, "something went wrong");
				}
			});
		}
	}

	/**
	 * retourne la string du level récupérée par le serveur
	 * @return le level
	 */
	public static String getLevel(){
		return level;
	}

	/**
	 * retourne la liste des patients sous forme de string formatée en json
	 * @return les patients
	 */
	public static String getPatients() {
		Gdx.app.log(RollingCat.LOG, "trying to get patients string");
		return patients;
	}

	/**
	 * retourne le world du patient formaté sous forme de String json
	 * @return
	 */
	public static String getWorld(){
		return world;
	}

	/**
	 * retourne true quand le client a récupéré la clé d'idendification sur le serveur
	 * @return true si value != null
	 */
	public static boolean isReady() {
		return value != null;
	}

	/**
	 * resets the values
	 */
	public static void reset(){
		level = null;
		patients = null;
		value = null;
		world = null;
	}

	public static void updateLevelStats(String patientid, int levelID, int score, int duration,String color_gem, int worldid) {
		Gdx.app.log(RollingCat.LOG, "preparing send score update request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/world/"+patientid+"/"+gameid+"/"+levelID+"/"+score+"/"+duration+"/"+color_gem+"/"+worldid);
		httpGet.setHeader(key, value);	
		httpGet.setHeader("Content-Type", "text/plain");

		Gdx.app.log(RollingCat.LOG, "sending score update request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log(RollingCat.LOG, "score update request success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});

	}

	public static void getWorld(String patientid, int skinid) {
		world = null;
		Gdx.app.log(RollingCat.LOG, "preparing get world request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/world/"+patientid+"/"+gameid+"/"+skinid);
		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "text/plain");

		Gdx.app.log(RollingCat.LOG, "getting world : " + skinid);

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				world = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, "get world request success");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});

	}

	public static void getAbilityZone() {
		ability = null;
		Gdx.app.log(RollingCat.LOG, "preparing get ability zone request...");

		final Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);

		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/abilityzone/"+Patient.getInstance().getID()+"/get");
		OrderedMap<String, Object> map = new OrderedMap<String, Object>();

		map.put("builderId",GameConstants.getAlgo());
		map.put("patientId", Patient.getInstance().getID());
		map.put("numberOfLines", GameConstants.numberOfLines);
		map.put("numberOfRows", GameConstants.numberOfRows);
		map.put("totalHeight", GameConstants.workspaceHeight);
		map.put("totalWidth", GameConstants.workspaceWidth);
		map.put("totalVolume", GameConstants.totalVolume);
		map.put("volumePerLevel", GameConstants.volumePerLevel);

		if(GameConstants.area_1 & GameConstants.area_2 & GameConstants.area_3 & GameConstants.area_4 || !GameConstants.area_1 & !GameConstants.area_2 & !GameConstants.area_3 & !GameConstants.area_4)
		{
			map.put("dials", "0000");
		}
		else
		{
			if(!GameConstants.reversedLevel)
				map.put("dials", "" + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0"));
			else
				map.put("dials", "" + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0"));
		}


		map.put("ImportanceOfEffort", 0.9f);

		map.put("leftHemiplegia", Patient.getInstance().isLeftHemiplegia());
		Gdx.app.log(RollingCat.LOG, "reversed level : " + Patient.getInstance().isLeftHemiplegia());

		OrderedMap<String,Object> algoParameterAZ =  new OrderedMap<String,Object>();
		algoParameterAZ.put("range", GameConstants.range);
		algoParameterAZ.put("pathDeltaTime", GameConstants.pathDeltaTime);
		algoParameterAZ.put("evaporationRatioPerDay", GameConstants.evaporationPerDay);
		algoParameterAZ.put("alpha", GameConstants.alpha);
		algoParameterAZ.put("assessmentDataOnly", "true");
		map.put("parameters", algoParameterAZ); 


		httpGet.setContent(json.toJson(map));

		Gdx.app.log(RollingCat.LOG, httpGet.getContent());	

		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");

		Gdx.app.log(RollingCat.LOG, "sending level request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String string = httpResponse.getResultAsString();
				OrderedMap<String, Object>lang = (OrderedMap<String, Object>) new JsonReader().parse(string);
				ability = json.readValue("content", float[].class, lang);
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});

	}

	public static TextButton getOkButton(final Screen screen, final Game game){
		TextButtonStyle style = new TextButtonStyle();
		style.up = getSkin().getDrawable("button_up");
		style.down = getSkin().getDrawable("button_down");
		style.font = getBigFont();
		style.fontColor = Color.BLACK;
		okButton = new TextButton("ok", style);

		okButton.setVisible(false);
		okButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(sent != 0){
					okButton.setVisible(false);
					game.setScreen(screen);
				}
			}
		});
		return okButton;

	}

	public static void clearLevel()
	{
		Gdx.app.log(RollingCat.LOG, "level cleared");
		level = null;
	}

	public static void getNumberOfLevels(String patientid){
		Gdx.app.log(RollingCat.LOG, "preparing game id retrieve request...");

		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);

		httpGet.setUrl("http://" + hostName + ":" + port + path + "/world/"+patientid+"/"+gameid);
		
		httpGet.setHeader(key, value);
		Gdx.app.log(RollingCat.LOG, "sending game id retrieve request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String s = httpResponse.getResultAsString();
				s = s.replace("\"", "");
				String s1[] = s.split("-");
				for (int i = 0; i < s1.length; i++) {
					nblevels[i] = Integer.valueOf(s1[i]);
				}
//				nblevels[0] = 20;
//				nblevels[1] = 20;
//				nblevels[2] = 20;
				
				Gdx.app.log(RollingCat.LOG, "game id : " + gameid);
				Gdx.app.log(RollingCat.LOG, "retrieving complete");

			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong, could not retrieve number of levels");
			}});
	}

	public static void requestWorldClear() {
		Gdx.app.log(RollingCat.LOG, "preparing world clear request...");

		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);

		httpGet.setUrl("http://" + hostName + ":" + port + path + "/level/boss");

		httpGet.setHeader(key, value);
		Gdx.app.log(RollingCat.LOG, "sending world clear request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log(RollingCat.LOG, "worlds cleared");
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong, could not clear worlds");
			}});
		
	}

	public static void getPointingTasks() {
		Gdx.app.log(RollingCat.LOG, "preparing get pointing tasks request...");
		tasks = null;

		final Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);

		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + path + "/level/boss");
		OrderedMap<String, Object> map = new OrderedMap<String, Object>();

		map.put("builderId",GameConstants.getAlgo());
		map.put("patientId", Patient.getInstance().getID());
		map.put("numberOfLines", GameConstants.numberOfLines);
		map.put("numberOfRows", GameConstants.numberOfRows);
		map.put("totalHeight", GameConstants.workspaceHeight);
		map.put("totalWidth", GameConstants.workspaceWidth);
		map.put("totalVolume", 20);
		map.put("volumePerLevel", GameConstants.volumePerLevel);

		if(GameConstants.area_1 & GameConstants.area_2 & GameConstants.area_3 & GameConstants.area_4 || !GameConstants.area_1 & !GameConstants.area_2 & !GameConstants.area_3 & !GameConstants.area_4)
		{
			map.put("dials", "0000");
		}
		else
		{
			if(!GameConstants.reversedLevel)
				map.put("dials", "" + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0"));
			else
				map.put("dials", "" + ((GameConstants.area_2 == true)?"1":"0") + ((GameConstants.area_1 == true)?"1":"0") + ((GameConstants.area_4 == true)?"1":"0") + ((GameConstants.area_3 == true)?"1":"0"));
		}


		map.put("ImportanceOfEffort", 0.9f);

		map.put("leftHemiplegia", Patient.getInstance().isLeftHemiplegia());
		Gdx.app.log(RollingCat.LOG, "reversed level : " + Patient.getInstance().isLeftHemiplegia());

		OrderedMap<String,Object> algoParameterAZ =  new OrderedMap<String,Object>();
		algoParameterAZ.put("range", GameConstants.range);
		algoParameterAZ.put("pathDeltaTime", GameConstants.pathDeltaTime);
		algoParameterAZ.put("evaporationRatioPerDay", GameConstants.evaporationPerDay);
		algoParameterAZ.put("alpha", GameConstants.alpha);
		algoParameterAZ.put("assessmentDataOnly", "true");
		map.put("parameters", algoParameterAZ); 


		httpGet.setContent(json.toJson(map));

		Gdx.app.log(RollingCat.LOG, httpGet.getContent());	

		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");

		Gdx.app.log(RollingCat.LOG, "sending pointing tasks request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String string = httpResponse.getResultAsString();
				string = string.substring(1, string.length() - 1);
				string = string.replace("],[", "/");
				string =  string.replace("[", "");
				tasks = string.replace("]", "");
				System.out.println(tasks);
			}

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});

		
	}

}
