package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._back;
import static fr.lirmm.smile.rollingcat.Localisation._detail;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getShapeRenderer;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;

public class TrackingRecapScreen implements Screen{

	private RollingCat game;
	private Skin skin;
	private Stage stage;
	private TextButton back, b, select;
	private BitmapFont font;
	private Patient patient;
	private ScrollPane scrollPane;
	private ArrayList<TextButton> buttons;
	private Table tableLeftUp, tableLeftBottom, tableRight;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private Track track;
	private Rectangle drawArea;

	
	public TrackingRecapScreen(RollingCat game, Patient patient){
		this.game = game;
		this.patient = patient;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		batch.begin();
		batch.draw(skin.getRegion("black"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();

		if(track != null)
			track.render(sr, drawArea, batch);
		
	
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		skin = getSkin();
		font = getBigFont();
		stage = getStage();
		sr = getShapeRenderer();
		
		buttons = new ArrayList<TextButton>();
		tableLeftUp = new Table();
		tableLeftBottom = new Table();
		tableRight = new Table();

		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
//		TextButtonStyle style1 = new TextButtonStyle();
//		style1.up = skin.getDrawable("empty");
//		style1.down = skin.getDrawable("empty");
//		style1.font = font;
//		style1.fontColor = Color.BLACK;
		
		ScrollPaneStyle scrollPanelStyle = new ScrollPaneStyle();
		scrollPanelStyle.background = skin.getDrawable("bottom_green");
		scrollPanelStyle.vScroll = skin.getDrawable("backgroundtrack");
		scrollPanelStyle.corner = skin.getDrawable("button_up");
		scrollPanelStyle.vScrollKnob = skin.getDrawable("button_up");
		scrollPanelStyle.vScrollKnob.setRightWidth(20);
		scrollPanelStyle.vScrollKnob.setMinWidth(20);
		scrollPanelStyle.vScrollKnob.setLeftWidth(20);
		scrollPanelStyle.vScroll.setRightWidth(20);
		scrollPanelStyle.vScroll.setMinWidth(20);
		scrollPanelStyle.vScroll.setLeftWidth(20);		

		Gdx.input.setInputProcessor(stage);
		
		back = new TextButton(localisation(_back), style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new PatientScreen(game, patient));
			}
		});

		select = new TextButton(localisation(_detail), style);
		select.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(track != null){
					game.setScreen(new UploadScreen(game, patient, track));
					}
			}
		});
		
		tableRight.setBackground(skin.getDrawable("background_base"));
		tableRight.setY(GameConstants.DISPLAY_HEIGHT * 0.016f);
		tableRight.setHeight(GameConstants.DISPLAY_HEIGHT * 0.970f);
		tableRight.setX(GameConstants.DISPLAY_WIDTH*0.3f);
		tableRight.setWidth(GameConstants.DISPLAY_WIDTH * 0.689f);
		
		tableLeftBottom.setBackground(skin.getDrawable("top_orange"));
		tableLeftBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.016f);
		tableLeftBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.255f);
		tableLeftBottom.setX(GameConstants.DISPLAY_WIDTH*0.009f);
		tableLeftBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.28f);
		
		tableLeftBottom.add(select).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();
		tableLeftBottom.row();
		tableLeftBottom.add(back).pad(GameConstants.DISPLAY_HEIGHT / 100 - 2).fill().expand();

		scrollPane = new ScrollPane(tableLeftUp, scrollPanelStyle);
		scrollPane.setX(GameConstants.DISPLAY_WIDTH*0.009f);
		scrollPane.setY(GameConstants.DISPLAY_HEIGHT * 0.286f);
		scrollPane.setHeight(GameConstants.DISPLAY_HEIGHT * 0.7f);
		scrollPane.setWidth(GameConstants.DISPLAY_WIDTH * 0.28f);
		scrollPane.setFadeScrollBars(false);
		scrollPane.scrollTo(0, 0, 0, 0);
		
		
		createButtons(style);
		
//		back.setY(GameConstants.DISPLAY_HEIGHT * 0.03f);
//		back.setX(GameConstants.DISPLAY_WIDTH * 0.31f);
		
		stage.addActor(scrollPane);
		stage.addActor(tableRight);
		stage.addActor(tableLeftBottom);
		
		drawArea = new Rectangle(tableRight.getX(), tableRight.getY(), tableRight.getWidth(), tableRight.getHeight());
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
	}
	
	private void createButtons(TextButtonStyle style) {
		for (int i = 0; i < patient.getListOfTracks().size(); i++) {
			b = new TextButton(patient.getListOfTracks().get(i).getType() + " " + patient.getListOfTracks().get(i).getId(), style);
			b.setName(""+i);
			buttons.add(b);
			tableLeftUp.add(b).align(Align.right).width(scrollPane.getWidth()*0.85f).height(65).pad(5);
			tableLeftUp.row();
		}
		addListeners();
	}
	
	private void addListeners(){
		for (int i = 0; i < buttons.size(); i++) {
			b = buttons.get(i);
			b.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					track = patient.getListOfTracks().get(Integer.valueOf(event.getListenerActor().getName()));
				}
			});
		}
	}

}
