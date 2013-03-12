package fr.lirmm.smile.rollingcat.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class InternetManager{

	private static String level;
	private static String patients;
	private static String hostName = "localhost";
	private static int port = 9000;
	private static String key = "PLAY_SESSION";
	private static String value;
	
	
	/**
	 * envoie la requete pour récupérer une clé de connexion
	 * @param username
	 * @param password
	 * @return true si tout s'est bien passé
	 */
	public static boolean login(String username, String password) {
		
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		final HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		
		httpGet.setContent("email="+username+"&password="+password);
		
		httpGet.setUrl("http://" + hostName + ":" + port + "/login.json");
		
		Gdx.app.log(RollingCat.LOG, "sending request...");
		
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String s = httpResponse.getResultAsString();
				value = json.readValue(key, String.class, new JsonReader().parse(s));
				Gdx.app.log(RollingCat.LOG, value);
	           	Gdx.app.log(RollingCat.LOG, "success");

		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}});
		
		return true;
	}
	
	/**
	 * envoie une requete pour récupérer la liste de patients depuis le serveur
	 */
	public static void retrievePatients(){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		final HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		
		httpGet.setUrl("http://" + hostName + ":" + port + "/patient/all");
		httpGet.setHeader(key, value);
		Gdx.app.log(RollingCat.LOG, "sending request...");
		
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				patients = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, patients);
	           	Gdx.app.log(RollingCat.LOG, "success");

		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, "something went wrong");
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
				Gdx.app.log(RollingCat.LOG, "something went wrong");
				track.setDate("error");
			}});
		}
	
	/**
	 * récupère le level sur le serveur
	 * @param IDpatient
	 */
	public static void fetchLevel(int IDpatient){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://infolimon.iutmontp.univ-montp2.fr/~lephilippen/rollingcat/getLevel.php?patient="+IDpatient);
		
		Gdx.app.log(RollingCat.LOG, "sending request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				level = httpResponse.getResultAsString();
	           	Gdx.app.log(RollingCat.LOG, "success");
		    }

			@Override
			public void failed(Throwable t) {	
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
		return patients;
	}

	/**
	 * retourne true quand le serveur a récupéré la clé d'idendification sur le serveur
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
