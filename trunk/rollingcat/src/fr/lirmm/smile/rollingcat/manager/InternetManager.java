package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class InternetManager{
	
//	{  \"tz\": \"Europe\\/Paris\",   \"hour\": 14,   \"datetime\": \"Tue, 05 Mar 2013 14:16:00 +0100\",   \"second\": 0,   \"error\": false,   \"minute\": 16}
//	http://json-time.appspot.com/time.json?tz=Europe/Paris	
	
	private static String level;
	
	public static ArrayList<Patient> login(String username, String password) {
		if(username.equals("admin") & password.equals("admin"))
			return PatientsManager.getPatients();
		
		else if(username.equals("a") & password.equals("a")){
			ArrayList<Patient> p = PatientsManager.getPatients();
			p.remove(0);
			return p;
		}
			
		else
			return null;
	}
	
	/**
	 * set la date dans la track
	 * @param track
	 */
	public static void getDate(final Track track){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setTimeOut(0);
		httpGet.setUrl("http://json-time.appspot.com/time.json?tz=Europe/Paris");
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
	
	
	public static String getLevelOnServer(int IDpatient){
		Gdx.app.log(RollingCat.LOG, "preparing request...");
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		//httpGet.setTimeOut(0);
		httpGet.setUrl("http://infolimon.iutmontp.univ-montp2.fr/~lephilippen/rollingcat/index.php?patient=1");
		
		Gdx.app.log(RollingCat.LOG, "sending request...");

		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				LevelBuilder.setLevel(httpResponse.getResultAsString());
	           	Gdx.app.log(RollingCat.LOG, "success");
		    }

			@Override
			public void failed(Throwable t) {	
				Gdx.app.log(RollingCat.LOG, "something went wrong");
			}
		});
		
		return level;
	}

}
