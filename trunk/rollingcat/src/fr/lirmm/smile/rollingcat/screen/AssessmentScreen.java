package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorAssessment;
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
	private float elapsedTime;
	
	public AssessmentScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		elapsedTime += delta;
		
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
        
        if(mc.isDone()){
	        patient.addTrack(new Track(buildTrack(), Track.ASSESSEMENT, elapsedTime));
			game.setScreen(new TrackingRecapScreen(game, patient));
        }
	}

	private Map<Integer, Integer> buildTrack() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Triangle triangle : triangles) {
			map.put(triangle.getAngle(), triangle.getProgression());
		}
		return map;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		sr = getShapeRenderer();
		triangles = VectorManager.getVectorsFromAreas(5);
		mc = new MouseCursorAssessment();
		Gdx.input.setInputProcessor(mc);
		selected = -10;
		timeout = 0;
		
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
		return (Math.sqrt((mc.getX() - 400)*(mc.getX() - 400) + mc.getY()*mc.getY()) < 100);
	}
	
}
