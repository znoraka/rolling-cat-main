package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.TextureFetcher.getSkin;

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
	private Table tableLeft, tableRight;
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
		batch.draw(skin.getRegion("backgroundtrack"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.end();
		
		if(track != null)
			track.render(sr, drawArea);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		buttons = new ArrayList<TextButton>();
		tableLeft = new Table();
		tableRight = new Table();
		
		batch = new SpriteBatch();
		
		skin = getSkin();
		
		font = new BitmapFont(Gdx.files.internal("data/font_24px.fnt"), false);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		TextButtonStyle style1 = new TextButtonStyle();
		style1.up = skin.getDrawable("empty");
		style1.down = skin.getDrawable("empty");
		style1.font = font;
		style1.fontColor = Color.BLACK;
		
		ScrollPaneStyle scrollPanelStyle = new ScrollPaneStyle();
		scrollPanelStyle.background = skin.getDrawable("empty");
		scrollPanelStyle.vScroll = skin.getDrawable("backgroundtrack");
		scrollPanelStyle.corner = skin.getDrawable("button_up");
		scrollPanelStyle.vScrollKnob = skin.getDrawable("button_up");
		scrollPanelStyle.vScrollKnob.setRightWidth(20);
		scrollPanelStyle.vScrollKnob.setMinWidth(20);
		scrollPanelStyle.vScrollKnob.setLeftWidth(20);
		scrollPanelStyle.vScroll.setRightWidth(20);
		scrollPanelStyle.vScroll.setMinWidth(20);
		scrollPanelStyle.vScroll.setLeftWidth(20);		
		stage = new Stage(GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		back = new TextButton("Back", style);
		back.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new PatientScreen(game, patient));
			}
		});

		select = new TextButton("", style1);
		select.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(track != null){
					game.setScreen(new UploadScreen(game, patient, track));
					}
			}
		});
		
		tableRight.add(select).fill().expand();
		tableRight.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
		tableRight.setHeight(GameConstants.DISPLAY_HEIGHT * 0.930f);
		tableRight.setX(GameConstants.DISPLAY_WIDTH*0.315f);
		tableRight.setWidth(GameConstants.DISPLAY_WIDTH * 0.658f);

		
		scrollPane = new ScrollPane(tableLeft, scrollPanelStyle);
		scrollPane.setX(GameConstants.DISPLAY_WIDTH*0.025f);
		scrollPane.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
		scrollPane.setHeight(GameConstants.DISPLAY_HEIGHT * 0.930f);
		scrollPane.setWidth(GameConstants.DISPLAY_WIDTH * 0.262f);
		scrollPane.setFadeScrollBars(false);
		scrollPane.scrollTo(0, 0, 0, 0);
		
		createButtons(style);
		
		back.setY(GameConstants.DISPLAY_HEIGHT - 50);
		back.setX(GameConstants.DISPLAY_WIDTH - 130);
		
		stage.addActor(scrollPane);
		stage.addActor(tableRight);
		stage.addActor(back);
		
		sr = new ShapeRenderer();
		
		drawArea = new Rectangle(tableRight.getX(), tableRight.getY(), tableRight.getWidth(), tableRight.getHeight());
	}

	@Override
	public void hide() {
		dispose();
		
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
		stage.dispose();
		font.dispose();
		batch.dispose();
		sr.dispose();
	}
	
	private void createButtons(TextButtonStyle style) {
		for (int i = 0; i < patient.getListOfTracks().size(); i++) {
			b = new TextButton(patient.getListOfTracks().get(i).getType() + " " + patient.getListOfTracks().get(i).getId(), style);
			b.setName(""+i);
			buttons.add(b);
			tableLeft.add(b).align(Align.right).width(scrollPane.getWidth()*0.85f).height(65).pad(5);
			tableLeft.row();
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
