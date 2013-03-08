package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.HttpParametersUtils;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class InternetManager{
	
//	{  \"tz\": \"Europe\\/Paris\",   \"hour\": 14,   \"datetime\": \"Tue, 05 Mar 2013 14:16:00 +0100\",   \"second\": 0,   \"error\": false,   \"minute\": 16}
//	http://json-time.appspot.com/time.json?tz=Europe/Paris	
	
	private static String level;
	
	public static ArrayList<Patient> login(String username, String password) {
		
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		
		httpGet.setContent("username="+username+"&password="+password);
		
		httpGet.setUrl("http://infolimon.iutmontp.univ-montp2.fr/~lephilippen/rollingcat/login.php");
		
		Gdx.app.log(RollingCat.LOG, "sending request...");
		
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String s = httpResponse.getResultAsString();
				Gdx.app.log(RollingCat.LOG, s);
	           	Gdx.app.log(RollingCat.LOG, "success");
		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}});
		
		return PatientsManager.getPatients();
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
	
	
	public static void fetchLevel(int IDpatient){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://infolimon.iutmontp.univ-montp2.fr/~lephilippen/rollingcat/getLevel.php?patient="+IDpatient);
		
		Gdx.app.log(RollingCat.LOG, "sending request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				setLevel(httpResponse.getResultAsString());
	           	Gdx.app.log(RollingCat.LOG, "success");
		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});

	}
	
	private static void setLevel(String resultAsString) {
		level = resultAsString;
	}

	
	public static String getLevel(){
		return level;
	}

}
