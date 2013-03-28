package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class SettingsScreen implements Screen {

	private Stage stage;
	private Table table;
	private ImageButton heightPlus, heightMinus, widthPlus, widthMinus;
	private TextField height, width;
	private Skin skin;
	private BitmapFont font;
	private float elapsedTime;
	private RollingCat game;
	private Label heightLabel, widthLabel;
	private TextButton save, discard;
	private Screen oldScreen;
	
	private final float SPEED = 0.05f;
	
	public SettingsScreen(RollingCat game, Screen oldScreen){
		this.game = game;
		this.oldScreen = oldScreen;
	}
	
	@Override
	public void render(float delta) {
		if(heightMinus.isPressed() & elapsedTime > SPEED)
			heightModify(-1);
		
		if(heightPlus.isPressed() & elapsedTime > SPEED)
			heightModify(1);
		
		if(widthMinus.isPressed() & elapsedTime > SPEED)
			widthModify(-1);
		
		if(widthPlus.isPressed() & elapsedTime > SPEED)
			widthModify(1);
		
		if(widthMinus.isPressed() || widthPlus.isPressed() || heightMinus.isPressed() || heightPlus.isPressed())
			elapsedTime += delta;
		else
			elapsedTime = 0;
		
		stage.draw();
		stage.act(delta);
		
		checkHeightBounds();
		checkWidthBounds();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		stage = getStage();
		skin = getSkin();
		font = getBigFont();
		
		table = new Table();
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		table.setWidth(GameConstants.DISPLAY_WIDTH);
		table.setBackground(skin.getDrawable("background_base"));

		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = font;
		textFieldStyle.fontColor = Color.WHITE;
		textFieldStyle.selection = skin.getDrawable("selection");
		
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		labelStyle.background = skin.getDrawable("empty");
		labelStyle.font = font;
		
		heightLabel = new Label("workspace height :", labelStyle);
		widthLabel = new Label("workspace width :", labelStyle);
		
		height = new TextField(""+GameConstants.WORKSPACE_HEIGHT, textFieldStyle);
		width = new TextField(""+GameConstants.WORKSPACE_WIDTH, textFieldStyle);
		
		heightPlus = new ImageButton(skin.getDrawable("triangle_up"));
		heightPlus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				heightModify(1);
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		heightMinus = new ImageButton(skin.getDrawable("triangle_down"));
		heightMinus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				heightModify(-1);
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		widthPlus = new ImageButton(skin.getDrawable("triangle_up"));
		widthPlus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				widthModify(1);
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		widthMinus = new ImageButton(skin.getDrawable("triangle_down"));
		widthMinus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				widthModify(-1);
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		save = new TextButton("Save", style);
		save.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					GameConstants.WORKSPACE_HEIGHT = Integer.valueOf(height.getText());
					GameConstants.WORKSPACE_WIDTH = Integer.valueOf(width.getText());
					game.setScreen(oldScreen);
				}
			});
		
		discard = new TextButton("Back", style);
		discard.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(oldScreen);
				}
			});
		
		table.add(heightLabel);
		table.add(height);
		table.add(heightPlus);
		table.add(heightMinus);
		table.row();
		table.add(widthLabel);
		table.add(width);
		table.add(widthPlus);
		table.add(widthMinus);
		table.row();
		table.add(save);
		table.add(discard);
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
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
		// TODO Auto-generated method stub
		
	}
	
	private void heightModify(int i) {
		elapsedTime = 0;
		height.setText(""+(Integer.valueOf(height.getText()) + i));
		checkHeightBounds();
	}
	
	private void checkHeightBounds() {
		if(Integer.valueOf(height.getText()) > 500)
			height.setText("500");
		
		if(Integer.valueOf(height.getText()) < 2)
			height.setText(""+1);		
	}

	private void widthModify(int i) {
		elapsedTime = 0;
		width.setText(""+(Integer.valueOf(width.getText()) + i));
		checkWidthBounds();
	}

	private void checkWidthBounds() {
		if(Integer.valueOf(width.getText()) > 500)
			width.setText("500");
		
		if(Integer.valueOf(width.getText()) < 2)
			width.setText(""+1);
	}



}
