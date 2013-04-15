package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation._alpha;
import static fr.lirmm.smile.rollingcat.Localisation._discard;
import static fr.lirmm.smile.rollingcat.Localisation._evaporation_per_day;
import static fr.lirmm.smile.rollingcat.Localisation._name;
import static fr.lirmm.smile.rollingcat.Localisation._needsAtLeastOneArea;
import static fr.lirmm.smile.rollingcat.Localisation._number_of_lines;
import static fr.lirmm.smile.rollingcat.Localisation._number_of_rows;
import static fr.lirmm.smile.rollingcat.Localisation._path_delta_time;
import static fr.lirmm.smile.rollingcat.Localisation._range;
import static fr.lirmm.smile.rollingcat.Localisation._save;
import static fr.lirmm.smile.rollingcat.Localisation._total_volume;
import static fr.lirmm.smile.rollingcat.Localisation._volume_per_level;
import static fr.lirmm.smile.rollingcat.Localisation._workspace_height;
import static fr.lirmm.smile.rollingcat.Localisation._workspace_width;
import static fr.lirmm.smile.rollingcat.Localisation.loadLanguage;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.Localisation;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.utils.StringUtils;

public class SettingsScreen implements Screen {

	private Stage stage;
	private Table table, zoneTable;
	private SpriteBatch batch;
//	private ImageButton heightPlus, heightMinus, widthPlus, widthMinus;
	private TextField workspaceHeight, workspaceWidth, range, pathDeltaTime, evaporationPerDay, alpha, numberOfLines, numberOfRows, totalVolume, volumePerLevel;
	private Skin skin;
	private BitmapFont font;
//	private float elapsedTime;
	private RollingCat game;
	private Label heightLabel, widthLabel, rangeLabel, pathDeltaTimeLabel, evaporationPerDayLabel, alphaLabel, numberOfLinesLabel, numberOfRowsLabel, totalVolumeLabel, volumePerLevelLabel;
	private TextButton save, discard;
	private Screen oldScreen;
	private List list;
	private CheckBox area_1, area_2, area_3, area_4;
	private boolean needsToBeChecked;
	private Label warningMessage;
	
//	private final float SPEED = 0.05f;
	
	public SettingsScreen(RollingCat game, Screen oldScreen){
		this.game = game;
		this.oldScreen = oldScreen;
	}
	
	@Override
	public void render(float delta) {
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glEnable(GL10.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
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
		
		batch.begin();
		batch.draw(skin.getRegion("background_base"), 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		
		if(needsToBeChecked){
			font.setColor(Color.WHITE);
			warningMessage.setText(StringUtils.addEnters(localisation(_needsAtLeastOneArea), 15));
			warningMessage.draw(batch, 1);
		}
		
		batch.end();
		
		stage.draw();
		stage.act(delta);
		
		checkHeightBounds();
		checkWidthBounds();
		
	}

	@Override
	public void resize(int width, int height) {
		GameConstants.DISPLAY_WIDTH = width;
		GameConstants.DISPLAY_HEIGHT = height;
	}

	@Override
	public void show() {
		stage = getStage();
		skin = getSkin();
		font = getBigFont();
		batch = getSpriteBatch();
		
		list = Localisation.getAvailableLanguages();
		list.setX(GameConstants.DISPLAY_WIDTH * 0.75f);
		list.setY(GameConstants.DISPLAY_HEIGHT * 0.75f);
		
		table = new Table();
		table.setHeight(GameConstants.DISPLAY_HEIGHT);
		table.setWidth(GameConstants.DISPLAY_WIDTH * 0.75f);
		
		zoneTable = new Table();
		
		zoneTable.setBackground(skin.getDrawable("top_orange"));


		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = font;
		textFieldStyle.fontColor = Color.WHITE;
		textFieldStyle.cursor = skin.getDrawable("cursor");
		textFieldStyle.selection = skin.getDrawable("selection");
		textFieldStyle.background = skin.getDrawable("empty");
		
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		labelStyle.background = skin.getDrawable("empty");
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;
		
		heightLabel = new Label(localisation(_workspace_height)+" :", labelStyle);
		widthLabel = new Label(localisation(_workspace_width)+" :", labelStyle);
		rangeLabel = new Label(localisation(_range), labelStyle);
		pathDeltaTimeLabel = new Label(localisation(_path_delta_time)+" :", labelStyle);
		evaporationPerDayLabel = new Label(localisation(_evaporation_per_day)+" :", labelStyle);
		alphaLabel = new Label(localisation(_alpha) + " :", labelStyle);
		numberOfLinesLabel = new Label(localisation(_number_of_lines)+" :", labelStyle);
		numberOfRowsLabel = new Label(localisation(_number_of_rows)+" :", labelStyle);
		totalVolumeLabel = new Label(localisation(_total_volume)+" :", labelStyle);
		volumePerLevelLabel = new Label(localisation(_volume_per_level)+" :", labelStyle);
		
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
		zoneTable.setHeight(GameConstants.DISPLAY_HEIGHT * 0.35f);
		zoneTable.setWidth(GameConstants.DISPLAY_WIDTH * 0.35f);
		zoneTable.setX(GameConstants.DISPLAY_WIDTH * 0.6f);
		zoneTable.setY(GameConstants.DISPLAY_HEIGHT * 0.34f);
		
		CheckBoxStyle cbs = new CheckBoxStyle();
		cbs.checkboxOff = getSkin().getDrawable("unchecked");
		cbs.checkboxOn = getSkin().getDrawable("checked");
		cbs.checkboxOff.setMinHeight(GameConstants.BLOCK_HEIGHT * 0.8f);
		cbs.checkboxOff.setMinWidth(GameConstants.BLOCK_WIDTH * 0.8f);
		cbs.checkboxOn.setMinHeight(GameConstants.BLOCK_HEIGHT * 0.8f);
		cbs.checkboxOn.setMinWidth(GameConstants.BLOCK_WIDTH * 0.8f);
		cbs.font = font;
		cbs.fontColor = Color.BLACK;
		cbs.down = getSkin().getDrawable("button_up");
		cbs.up = getSkin().getDrawable("button_up");
		
		area_1 = new CheckBox("1", cbs);
		area_2 = new CheckBox("2", cbs);
		area_3 = new CheckBox("3", cbs);
		area_4 = new CheckBox("4", cbs);
		
		area_1.setChecked(GameConstants.area_1);
		area_2.setChecked(GameConstants.area_2);
		area_3.setChecked(GameConstants.area_3);
		area_4.setChecked(GameConstants.area_4);
		
		zoneTable.add(area_3).fill().expand();
		zoneTable.add(area_4).fill().expand();
		zoneTable.row();
		zoneTable.add(area_1).fill().expand();
		zoneTable.add(area_2).fill().expand();
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;
		
		save = new TextButton(localisation(_save), style);
		save.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					if(area_1.isChecked() | area_2.isChecked() | area_3.isChecked() | area_4.isChecked()){
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
						GameConstants.area_1 = area_1.isChecked();
						GameConstants.area_2 = area_2.isChecked();
						GameConstants.area_3 = area_3.isChecked();
						GameConstants.area_4 = area_4.isChecked();
						loadLanguage(list.getSelectedIndex());
						RollingCat.lang = list.getSelectedIndex();
						game.setScreen(oldScreen);
					}
					else
						needsToBeChecked = true;
				}
			});
		
		needsToBeChecked = false;
		
		discard = new TextButton(localisation(_discard), style);
		discard.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(oldScreen);
				}
			});
		
		table.add(heightLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(workspaceHeight).right();
//		table.add(heightPlus);
//		table.add(heightMinus);
		table.row();
		table.add(widthLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(workspaceWidth).right();
//		table.add(widthPlus);
//		table.add(widthMinus);
		table.row();
		table.add(rangeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(range).right();
		table.row();
		table.add(pathDeltaTimeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(pathDeltaTime).right();
		table.row();
		table.add(evaporationPerDayLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(evaporationPerDay).right();
		table.row();
		table.add(alphaLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(alpha).right();
		table.row();
		table.add(numberOfLinesLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(numberOfLines).right();
		table.row();
		table.add(numberOfRowsLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(numberOfRows).right();
		table.row();
		table.add(totalVolumeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(totalVolume).right();
		table.row();
		table.add(volumePerLevelLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(volumePerLevel).right();
		table.row();
		table.add(save);
		table.add(discard);
		
		stage.addActor(table);
		stage.addActor(list);
		stage.addActor(zoneTable);
		
		list.setSelectedIndex(RollingCat.lang);
		
		Gdx.input.setInputProcessor(stage);
		
		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		labelStyle.background = skin.getDrawable("empty");
		
		warningMessage = new Label("", ls);
		warningMessage.setX(GameConstants.DISPLAY_WIDTH * 0.6f);
		warningMessage.setY(GameConstants.DISPLAY_HEIGHT * 0.3f);

		
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
	
//	private void heightModify(int i) {
//		elapsedTime = 0;
//		workspaceHeight.setText(""+(Integer.valueOf(workspaceHeight.getText()) + i));
//		checkHeightBounds();
//	}
	
	private void checkHeightBounds() {
		if(Integer.valueOf(workspaceHeight.getText()) > 500)
			workspaceHeight.setText("500");
		
		if(Integer.valueOf(workspaceHeight.getText()) < 2)
			workspaceHeight.setText(""+1);		
	}

//	private void widthModify(int i) {
//		elapsedTime = 0;
//		workspaceWidth.setText(""+(Integer.valueOf(workspaceWidth.getText()) + i));
//		checkWidthBounds();
//	}

	private void checkWidthBounds() {
		if(Integer.valueOf(workspaceWidth.getText()) > 500)
			workspaceWidth.setText("500");
		
		if(Integer.valueOf(workspaceWidth.getText()) < 2)
			workspaceWidth.setText(""+1);
	}



}
