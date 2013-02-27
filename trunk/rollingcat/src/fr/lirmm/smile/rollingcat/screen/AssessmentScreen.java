package fr.lirmm.smile.rollingcat.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
	private Skin skin;
	private Stage stage;
	private ArrayList<Triangle> triangles;
	private MouseCursorAssessment mc;
	private int selected;
	private TextButton back;
	private BitmapFont font;
	private InputMultiplexer inputMultiplexer;
	private Patient patient;
	private float timeout;
	
	public AssessmentScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(canStart())
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
		sr.begin(ShapeType.FilledCircle);
		sr.setColor(Color.DARK_GRAY);
		sr.filledCircle(GameConstants.DISPLAY_WIDTH / 2, 0, 100);
		sr.setColor(Color.GRAY);
		sr.filledCircle(GameConstants.DISPLAY_WIDTH / 2, 0, 100 - timeout);
		sr.end();
		stage.draw();
        mc.addTrackingPoint(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		sr = new ShapeRenderer();
		skin = new Skin(new TextureAtlas("data/patientAtlas.atlas"));
		
		font = new BitmapFont(Gdx.files.internal("data/font_24px.fnt"), false);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
		triangles = VectorManager.getVectorsFromAreas(9);
		mc = new MouseCursorAssessment();
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(mc);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		selected = -10;
		
		back = new TextButton("Back", style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
	        	patient.addTrack(new Track(mc.getMap(), Track.ASSESSEMENT));
				game.setScreen(new PatientScreen(game, patient)); // TODO changer le screen
			}
		});
		
		back.setY(GameConstants.DISPLAY_HEIGHT - 50);
		back.setX(10);
		
		stage.addActor(back);
		timeout = 0;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
		sr.dispose();
		skin.dispose();
		stage.dispose();
		font.dispose();
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
