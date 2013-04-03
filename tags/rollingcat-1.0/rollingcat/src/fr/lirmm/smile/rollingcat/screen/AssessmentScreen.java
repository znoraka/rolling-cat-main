package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorAssessment;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.manager.VectorManager;
import fr.lirmm.smile.rollingcat.model.assessment.Triangle;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class AssessmentScreen implements Screen {
        
        private RollingCat game;
        private ShapeRenderer sr;
        private ArrayList<Triangle> triangles;
        private MouseCursorAssessment mc;
        private int selected;
        private Patient patient;
        private float timeout;
        private float duration;
        private OrderedMap<String, String> parameters;
        
        public AssessmentScreen(RollingCat game, Patient patient){
                this.game = game;
                this.patient = patient;
        }
        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            duration += delta;
            
            if(canStart() & !mc.isStarted())
            	mc.start();
            
            for (Triangle triangle : triangles) {
                    triangle.render(sr, this);
                    triangle.setProgression(mc.getX(), mc.getY(), this);
            }
            
            if(selected >= 0){
                    if(timeout < 97)
                            timeout += delta * 3;
                    else{
                            selected = -100;
                    }
            }
            
            else{
                timeout = 0;
            }
            sr.begin(ShapeType.Filled);
            sr.setColor(Color.DARK_GRAY);
            sr.circle(GameConstants.DISPLAY_WIDTH / 2, 0, 100);
            sr.setColor(Color.GRAY);
            sr.circle(GameConstants.DISPLAY_WIDTH / 2, 0, 100 - timeout);
            sr.end();
            mc.addTrackingPoint(delta);
        
	        if(mc.isDone()){
	        	parameters = new OrderedMap<String, String>();
	        	parameters.put("duration", ""+(int)duration);
	        	InternetManager.endGameSession();
	        	EventManager.create(EventManager.end_game_event_type, parameters);
	        	patient.addTrack(new Track(mc.getMap(), Track.ASSESSEMENT, duration));
	        	game.setScreen(new TrackingRecapScreen(game, patient));
        	}
        }

        @Override
        public void resize(int width, int height) {
        }

        @Override
        public void show() {
                sr = getShapeRenderer();
        		EventManager.clear();
                duration = 0;
                triangles = VectorManager.getVectorsFromAreas(5);
                mc = new MouseCursorAssessment();
                Gdx.input.setInputProcessor(mc);
                selected = -10;
                timeout = 0;
                parameters = new OrderedMap<String, String>();
                parameters.put("game", RollingCat.LOG);
                parameters.put("version", RollingCat.VERSION);
                EventManager.create(EventManager.game_info_event_type, parameters);
                parameters = new OrderedMap<String, String>();
                parameters.put("session_type", Track.ASSESSEMENT);
                parameters.put("game_screen_width", ""+GameConstants.DISPLAY_WIDTH);
                parameters.put("game_screen_height", ""+GameConstants.DISPLAY_HEIGHT);
                EventManager.create(EventManager.start_game_event_type, parameters); 
                InternetManager.newGameSession(Track.ASSESSEMENT, patient.getID());
        }

        @Override
        public void hide() {
        }

        @Override
        public void pause() {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void resume() {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void dispose() {
                Gdx.app.log(RollingCat.LOG, "disposing...");
        }
        
        public void setSelected(int selected){
                this.selected = selected;
        }
        
        public int getSelected(){
                return this.selected;
        }
        
        public boolean canStart(){
                return mc.isInArea();
        }
        
}