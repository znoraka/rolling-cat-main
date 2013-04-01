package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
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
	private TextField workspaceHeight, workspaceWidth, range, pathDeltaTime, evaporationPerDay, alpha, numberOfLines, numberOfRows, totalVolume, volumePerLevel;
	private Skin skin;
	private BitmapFont font;
	private float elapsedTime;
	private RollingCat game;
	private Label heightLabel, widthLabel, rangeLabel, pathDeltaTimeLabel, evaporationPerDayLabel, alphaLabel, numberOfLinesLabel, numberOfRowsLabel, totalVolumeLabel, volumePerLevelLabel;
	private TextButton save, discard;
	private Screen oldScreen;
	
	private final float SPEED = 0.05f;
	
	public SettingsScreen(RollingCat game, Screen oldScreen){
		this.game = game;
		this.oldScreen = oldScreen;
	}
	
	@Override
	public void render(float delta) {
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
//		if(heightMinus.isPressed() & elapsedTime > SPEED)
//			heightModify(-1);
//		
//		if(heightPlus.isPressed() & elapsedTime > SPEED)
//			heightModify(1);
//		
//		if(widthMinus.isPressed() & elapsedTime > SPEED)
//			widthModify(-1);
//		
//		if(widthPlus.isPressed() & elapsedTime > SPEED)
//			widthModify(1);
//		
//		if(widthMinus.isPressed() || widthPlus.isPressed() || heightMinus.isPressed() || heightPlus.isPressed())
//			elapsedTime += delta;
//		else
//			elapsedTime = 0;
		
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
		labelStyle.fontColor = Color.WHITE;
		
		heightLabel = new Label("workspace height :", labelStyle);
		widthLabel = new Label("workspace width :", labelStyle);
		rangeLabel = new Label("range", labelStyle);
		pathDeltaTimeLabel = new Label("pathDeltaTime", labelStyle);
		evaporationPerDayLabel = new Label("evaporationPerDay", labelStyle);
		alphaLabel = new Label("alpha", labelStyle);
		numberOfLinesLabel = new Label("numberOfLines", labelStyle);
		numberOfRowsLabel = new Label("numberOfRows", labelStyle);
		totalVolumeLabel = new Label("totalVolume", labelStyle);
		volumePerLevelLabel = new Label("volumePerLevel", labelStyle);
		
		workspaceHeight = new TextField(""+GameConstants.workspaceHeight, textFieldStyle);
		workspaceWidth = new TextField(""+GameConstants.workspaceWidth, textFieldStyle);
		range = new TextField(""+GameConstants.range, textFieldStyle);
		pathDeltaTime = new TextField(""+GameConstants.pathDeltaTime, textFieldStyle);
		evaporationPerDay = new TextField(""+GameConstants.evaporationPerDay, textFieldStyle);
		alpha = new TextField(""+GameConstants.alpha, textFieldStyle);
		numberOfLines = new TextField(""+GameConstants.numberOfLines, textFieldStyle);
		numberOfRows = new TextField(""+GameConstants.numberOfRows, textFieldStyle);
		totalVolume = new TextField(""+GameConstants.totalVolume, textFieldStyle);
		volumePerLevel = new TextField(""+GameConstants.volumePerLevel, textFieldStyle);
		
//		heightPlus = new ImageButton(skin.getDrawable("triangle_up"));
//		heightPlus.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				heightModify(1);
//				return true;
//			}
//			
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				
//			}
//		});
//		
//		heightMinus = new ImageButton(skin.getDrawable("triangle_down"));
//		heightMinus.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				heightModify(-1);
//				return true;
//			}
//			
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				
//			}
//		});
//		
//		widthPlus = new ImageButton(skin.getDrawable("triangle_up"));
//		widthPlus.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				widthModify(1);
//				return true;
//			}
//			
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				
//			}
//		});
//		
//		widthMinus = new ImageButton(skin.getDrawable("triangle_down"));
//		widthMinus.addListener(new InputListener() {
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				widthModify(-1);
//				return true;
//			}
//			
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				
//			}
//		});
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		save = new TextButton("Save", style);
		save.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					GameConstants.workspaceHeight = Integer.valueOf(workspaceHeight.getText());
					GameConstants.workspaceWidth = Integer.valueOf(workspaceWidth.getText());
					GameConstants.range = Integer.valueOf(range.getText());
					GameConstants.pathDeltaTime = Float.valueOf(pathDeltaTime.getText());
					GameConstants.evaporationPerDay = Float.valueOf(evaporationPerDay.getText());
					GameConstants.alpha = Float.valueOf(alpha.getText());
					GameConstants.numberOfLines = Integer.valueOf(numberOfLines.getText());
					GameConstants.numberOfRows = Integer.valueOf(numberOfRows.getText());
					GameConstants.totalVolume = Integer.valueOf(totalVolume.getText());
					GameConstants.volumePerLevel = Integer.valueOf(volumePerLevel.getText());
					game.setScreen(oldScreen);
				}
			});
		
		discard = new TextButton("Discard", style);
		discard.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(oldScreen);
				}
			});
		
		table.add(heightLabel);
		table.add(workspaceHeight);
//		table.add(heightPlus);
//		table.add(heightMinus);
		table.row();
		table.add(widthLabel);
		table.add(workspaceWidth);
//		table.add(widthPlus);
//		table.add(widthMinus);
		table.row();
		table.add(rangeLabel);
		table.add(range);
		table.row();
		table.add(pathDeltaTimeLabel);
		table.add(pathDeltaTime);
		table.row();
		table.add(evaporationPerDayLabel);
		table.add(evaporationPerDay);
		table.row();
		table.add(alphaLabel);
		table.add(alpha);
		table.row();
		table.add(numberOfLinesLabel);
		table.add(numberOfLines);
		table.row();
		table.add(numberOfRowsLabel);
		table.add(numberOfRows);
		table.row();
		table.add(totalVolumeLabel);
		table.add(totalVolume);
		table.row();
		table.add(volumePerLevelLabel);
		table.add(volumePerLevel);
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
		workspaceHeight.setText(""+(Integer.valueOf(workspaceHeight.getText()) + i));
		checkHeightBounds();
	}
	
	private void checkHeightBounds() {
		if(Integer.valueOf(workspaceHeight.getText()) > 500)
			workspaceHeight.setText("500");
		
		if(Integer.valueOf(workspaceHeight.getText()) < 2)
			workspaceHeight.setText(""+1);		
	}

	private void widthModify(int i) {
		elapsedTime = 0;
		workspaceWidth.setText(""+(Integer.valueOf(workspaceWidth.getText()) + i));
		checkWidthBounds();
	}

	private void checkWidthBounds() {
		if(Integer.valueOf(workspaceWidth.getText()) > 500)
			workspaceWidth.setText("500");
		
		if(Integer.valueOf(workspaceWidth.getText()) < 2)
			workspaceWidth.setText(""+1);
	}



}
