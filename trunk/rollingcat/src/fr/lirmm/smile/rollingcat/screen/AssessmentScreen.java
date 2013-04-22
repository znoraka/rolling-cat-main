package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._assessment_;
import static fr.lirmm.smile.rollingcat.Localisation._quit;
import static fr.lirmm.smile.rollingcat.Localisation._resume;
import static fr.lirmm.smile.rollingcat.Localisation._upload;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
import fr.lirmm.smile.rollingcat.utils.StringUtils;

public class AssessmentScreen implements Screen {

	private RollingCat game;
	private ShapeRenderer sr;
	private ArrayList<Triangle> triangles;
	private MouseCursorAssessment mc;
	private int selected;
	private Patient patient;
	private float timeout;
	private float duration, elapsedTime;
	private boolean waitingToEnterInArea, requestSending;
	private OrderedMap<String, String> parameters;
	private boolean done;
	private TextButton resume, quit, upload;
	private BitmapFont font;
	private InputMultiplexer multiplexer;
	private Table pauseTable;
	private Stage pauseStage, stage;
	private int help;
	private Label label;
	private SpriteBatch batch;
	private Track track;


	public AssessmentScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		/**
		 * dessin des triangles
		 */
		for (Triangle triangle : triangles) {
			triangle.render(sr, this);
			triangle.setProgression(mc.getX(), mc.getY(), this);
		}
		/**
		 * dessin du cercle central
		 */
		sr.begin(ShapeType.Filled);
		if(mc.isStarted())
			sr.setColor(Color.WHITE);
		else
			sr.setColor(Color.DARK_GRAY);
		sr.circle(GameConstants.DISPLAY_WIDTH / 2, 0, 100);
		sr.setColor(Color.RED);
		float f = (100 - timeout * 100);
		sr.circle(GameConstants.DISPLAY_WIDTH / 2, 0, (f > 1)?(f):1);
		sr.end();
		mc.addTrackingPoint(delta);
		duration += delta;
		if(!mc.isPaused()){

			if(selected == triangles.size()){
				done = true;
				upload.setVisible(true);
				resume.setVisible(false);
				mc.pause();
			}

			if(canStart() & !mc.isStarted()){
				mc.start();
			}

			if(mc.isInArea() & mc.isStarted() & !waitingToEnterInArea){
				if(timeout < GameConstants.HOVER_TIME )
					timeout += delta;
				else{
					selected++;
					if(help == 1)
						help = 2;
					if(help == 3)
						help = 4;
					timeout = 0;
					waitingToEnterInArea = true;
					mc.stop();
				}
			}
			else{
				timeout = 0;
			}

			if(!mc.isInArea()){
				if(help == 2)
					help = 3;

				if(help == 4)
					help = 5;

				if(selected == triangles.size() - 1)
					help = 6;
				waitingToEnterInArea = false;
			}

			setLabelTextAndPosition();

			stage.draw();

		}
		else
		{
			pauseStage.act(delta);
			pauseStage.draw();
		}
		
		if(requestSending && track.getListOfEvents() != null){
			requestSending = false;
			InternetManager.sendEvents(track.getListOfEvents());
		}
		
		if(Gdx.input.isKeyPressed(Keys.ENTER)){
			if(done){
			parameters = new OrderedMap<String, String>();
			parameters.put("duration", ""+(int)duration);
			InternetManager.endGameSession();
			EventManager.create(EventManager.end_game_event_type, parameters);
			track = new Track(mc.getMap(), Track.ASSESSEMENT, duration);
			patient.addTrack(track);
			requestSending = true;
			pauseStage.clear();
			upload = InternetManager.getOkButton(new PatientScreen(game, patient), game);
			pauseStage.addActor(upload);
			upload.setX(GameConstants.DISPLAY_WIDTH * 0.5f - upload.getWidth() * 0.5f);
			upload.setY(GameConstants.DISPLAY_HEIGHT * 0.5f - upload.getHeight() * 0.5f);
			done = false;
			}
			if(InternetManager.sent != 0)
				game.setScreen(new PatientScreen(game, patient));
		}
		
//		if(Gdx.input.isKeyPressed(Keys.SPACE) & elapsedTime > 1){
//			if(selected > 0)
//				selected--;
//			
//			if(selected > 0)
//				selected--;
//			
//			triangles.get(selected).clearProgression();
//			elapsedTime = 0;
//			
//			waitingToEnterInArea = false;
//		}
//		
//		elapsedTime += delta;
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		sr = getShapeRenderer();
		font = getBigFont();
		pauseStage = getStage();
		stage = getStage();
		batch = getSpriteBatch();
		EventManager.clear();
		duration = 0;
		elapsedTime = 0;
		help = 1;
		done = false;
		requestSending = false;
		triangles = VectorManager.getVectorsFromAreas(9);
		mc = new MouseCursorAssessment();
		multiplexer = new InputMultiplexer(mc, pauseStage);
		selected = -1;
		waitingToEnterInArea = false;
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

		TextButtonStyle style = new TextButtonStyle();
		style.up = getSkin().getDrawable("button_up");
		style.down = getSkin().getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;

		resume = new TextButton(localisation(_resume), style);
		resume.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				mc.unPause();
			}
		});

		upload = new TextButton(localisation(_upload), style);
		upload.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(done){
					parameters = new OrderedMap<String, String>();
					parameters.put("duration", ""+(int)duration);
					InternetManager.endGameSession();
					EventManager.create(EventManager.end_game_event_type, parameters);
					track = new Track(mc.getMap(), Track.ASSESSEMENT, duration);
					patient.addTrack(track);
					requestSending = true;
					pauseStage.clear();
					upload = InternetManager.getOkButton(new PatientScreen(game, patient), game);
					pauseStage.addActor(upload);
					upload.setX(GameConstants.DISPLAY_WIDTH * 0.5f - upload.getWidth() * 0.5f);
					upload.setY(GameConstants.DISPLAY_HEIGHT * 0.5f - upload.getHeight() * 0.5f);
				}
			}
		});
		
		upload.setVisible(false);

		quit = new TextButton(localisation(_quit), style);
		quit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(mc.isPaused())
					game.setScreen(new PatientScreen(game, patient));
			}
		});

		pauseTable = new Table();
		pauseTable.add(resume).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseTable.row();
		pauseTable.add(upload).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseTable.row();
		pauseTable.add(quit).pad(GameConstants.BLOCK_WIDTH * 0.5f);
		pauseStage.addActor(pauseTable);
		pauseTable.setWidth(GameConstants.DISPLAY_WIDTH);
		pauseTable.setHeight(GameConstants.DISPLAY_HEIGHT);

		Gdx.input.setInputProcessor(multiplexer);

		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		labelStyle.background = getSkin().getDrawable("empty");

		label = new Label(localisation(_assessment_ + help), labelStyle);
		stage.addActor(label);
		setLabelTextAndPosition();
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

	public int getSelected(){
		return this.selected;
	}

	public boolean canStart(){
		return mc.isInArea();
	}

	private void setLabelTextAndPosition(){
		String s = StringUtils.addEnters(localisation(_assessment_ + help), 20);
		label.setText(s);
		label.setY(GameConstants.DISPLAY_HEIGHT * 0.85f);
		label.setX(GameConstants.DISPLAY_WIDTH * 0.5f - 20 * 6);
	}

}