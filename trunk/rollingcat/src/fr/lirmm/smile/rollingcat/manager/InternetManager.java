package fr.lirmm.smile.rollingcat.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.screen.LoginScreen;

public class InternetManager{

	private static String level;
	private static String patients;
	private static String hostName = "localhost";
	private static int port = 9000;
	private static String key = "PLAY_SESSION";
	private static String gameid;
	public static String value;
	private static String sessionid;
	
	
	/**
	 * envoie la requete pour récupérer une clé de connexion
	 * @param username
	 * @param password
	 * @return true si tout s'est bien passé
	 */
	public static boolean login(String username, String password) {
		
		Gdx.app.log(RollingCat.LOG, "preparing login request...");
		
		final HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setContent("email="+username+"&password="+password);
		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpGet.setUrl("http://" + hostName + ":" + port + "/login.json");
		
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
		
		return true;
	}
	
	/**
	 * envoie une requete pour récupérer la liste de patients depuis le serveur
	 */
	public static void retrievePatients(){
		Gdx.app.log(RollingCat.LOG, "preparing patient retrieve request...");
		
		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		
		httpGet.setUrl("http://" + hostName + ":" + port + "/patient/all");
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
		
		httpGet.setUrl("http://" + hostName + ":" + port + "/game/"+RollingCat.LOG+"/getid");
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
	public static void newGameSession(String gameType){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + "/gamesession/new ");
		OrderedMap<String, String> map = new OrderedMap<String, String>();
		map.put("name", gameType);
		map.put("comment", "no comment");
		map.put("patient_id", "5149766c44ae21b28266b0f4");
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
	 * récupère le level sur le serveur
	 * @param IDpatient
	 */
	public static void fetchLevel(int IDpatient, int IDgame, int numLevel){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://" + hostName + ":" + port + "/level/playerid="+IDpatient+"/gameid="+IDgame+"/nLevel="+numLevel);
		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		
		Gdx.app.log(RollingCat.LOG, "sending request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				level = httpResponse.getResultAsString();
	           	Gdx.app.log(RollingCat.LOG, "success");
		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});
		level = "cat;0.0;12.0/groundBlock;1.0;5.0/groundBlock;0.0;2.0/groundBlock;1.0;2.0/spring;2.0;2.0;/groundBlock;3.0;4.0/groundBlock;4.0;4.0/groundBlock;5.0;4.0/groundBlock;6.0;4.0/groundBlock;7.0;4.0/groundBlock;8.0;4.0/groundBlock;9.0;4.0/dog;9.0;5.0/fan;10.0;4.0/groundBlock;10.0;9.0/groundBlock;11.0;9.0/gap;12.0;10.0/groundBlock;13.0;9.0/empty;14.0;9.0/groundBlock;14.0;2.0/fan;15.0;2.0/wasp;15.0;7.0/wasp;15.0;10.0/groundBlock;15.0;11.0/groundBlock;16.0;11.0/dog;16.0;12.0/groundBlock;17.0;11.0/groundBlock;18.0;11.0/groundBlock;19.0;11.0/groundBlock;20.0;11.0/empty;21.0;11.0/groundBlock;21.0;10.0/groundBlock;22.0;10.0/groundBlock;23.0;10.0/dog;23.0;11.0/empty;24.0;10.0/groundBlock;24.0;5.0/groundBlock;25.0;5.0/fan;26.0;5.0/dog;25.0;6.0/wasp;26.0;11.0/groundBlock;26.0;12.0/empty;27.0;12.0;groundBlock;27.0;5.0/groundBlock;27.0;5.0/fan;28.0;5.0/groundBlock;28.0;12.0/empty;29.0;12.0/empty;26.0;12.0/groundBlock;29.0;3.0/groundBlock;31.0;3.0/wasp;28.0;9.0/gap;30.0;3.0/groundBlock;32.0;3.0/groundBlock;33.0;3.0/groundBlock;34.0;3.0/groundBlock;35.0;3.0/dog;34.0;4.0/target;35.0;4.0";
	}
	
	/** 
	 * récupère le level sur le serveur
	 * @param IDpatient
	 */
	public static void sendEvents(String events){
		Gdx.app.log(RollingCat.LOG, "preparing send list event request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://" + hostName + ":" + port + "/event/"+sessionid+"/newList");
		httpGet.setContent(events);
		httpGet.setHeader(key, value);
		httpGet.setHeader("Content-Type", "application/json");
		
		Gdx.app.log(RollingCat.LOG, "sending list event request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
	           	Gdx.app.log(RollingCat.LOG, "list event request success");
		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, t.toString());
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});
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
	}

}
