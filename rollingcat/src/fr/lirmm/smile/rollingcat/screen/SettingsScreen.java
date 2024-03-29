package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation.*;
import static fr.lirmm.smile.rollingcat.Localisation._cadran;
import static fr.lirmm.smile.rollingcat.Localisation._discard;
import static fr.lirmm.smile.rollingcat.Localisation._evaporation_per_day;
import static fr.lirmm.smile.rollingcat.Localisation._more;
import static fr.lirmm.smile.rollingcat.Localisation._nbsuccess;
import static fr.lirmm.smile.rollingcat.Localisation._range;
import static fr.lirmm.smile.rollingcat.Localisation._save;
import static fr.lirmm.smile.rollingcat.Localisation._timeout;
import static fr.lirmm.smile.rollingcat.Localisation._total_volume;
import static fr.lirmm.smile.rollingcat.Localisation._workspace_height;
import static fr.lirmm.smile.rollingcat.Localisation._workspace_width;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
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
	private TextField workspaceHeight, workspaceWidth, range, evaporationPerDay, alpha, nbSuccess, numberOfRows, totalVolume, volumePerLevel, timeout;
	private Skin skin;
	private BitmapFont font;
	private RollingCat game;
	private Label heightLabel, widthLabel, rangeLabel, reversedLevelLabel, evaporationPerDayLabel, alphaLabel, nbSuccessLabel, numberOfRowsLabel, totalVolumeLabel, volumePerLevelLabel, timeoutLabel;
	private TextButton save, discard;
	private Screen oldScreen;
	private List list;
	private CheckBox area_1, area_2, area_3, area_4, avanceCB, reversedLevel;
	private Label warningMessage;
	private boolean avance;

	public SettingsScreen(RollingCat game, Screen oldScreen){
		this.game = game;
		this.oldScreen = oldScreen;
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(skin.getRegion("background_base"), 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		batch.end();

		stage.draw();
		stage.act(delta);

		batch.begin();
		warningMessage.draw(batch, 1);
		batch.end();

		checkHeightBounds();
		checkWidthBounds();

		avance = avanceCB.isChecked();

		range.setVisible(avance);
		rangeLabel.setVisible(avance);
		evaporationPerDay.setVisible(avance);
		evaporationPerDayLabel.setVisible(avance);
		alpha.setVisible(avance);
		alphaLabel.setVisible(avance);
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
		//		pathDeltaTimeLabel = new Label(localisation(_path_delta_time)+" :", labelStyle);
		evaporationPerDayLabel = new Label(localisation(_evaporation_per_day)+" :", labelStyle);
		alphaLabel = new Label(localisation(_alpha) + " :", labelStyle);
		nbSuccessLabel = new Label(localisation(_nbsuccess)+" :", labelStyle);
		//		numberOfRowsLabel = new Label(localisation(_number_of_rows)+" :", labelStyle);
		totalVolumeLabel = new Label(localisation(_total_volume)+" :", labelStyle);
		//		volumePerLevelLabel = new Label(localisation(_volume_per_level)+" :", labelStyle);
		timeoutLabel = new Label(localisation(_timeout)+" :", labelStyle);
		reversedLevelLabel = new Label(localisation(_reversed)+ " :", labelStyle);


		workspaceHeight = new TextField(""+GameConstants.workspaceHeight, textFieldStyle);
		workspaceHeight.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		workspaceWidth = new TextField(""+GameConstants.workspaceWidth, textFieldStyle);
		workspaceWidth.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		range = new TextField(""+GameConstants.range, textFieldStyle);
		range.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		//		pathDeltaTime = new TextField(""+GameConstants.pathDeltaTime, textFieldStyle);
		//		pathDeltaTime.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		evaporationPerDay = new TextField(""+GameConstants.evaporationPerDay, textFieldStyle);
		evaporationPerDay.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		alpha = new TextField(""+GameConstants.alpha, textFieldStyle);
		alpha.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		nbSuccess = new TextField(""+GameConstants.SUCCESS, textFieldStyle);
		nbSuccess.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		//		numberOfRows = new TextField(""+GameConstants.numberOfRows, textFieldStyle);
		//		numberOfRows.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		totalVolume = new TextField(""+GameConstants.totalVolume, textFieldStyle);
		totalVolume.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		//		volumePerLevel = new TextField(""+GameConstants.volumePerLevel, textFieldStyle);
		//		volumePerLevel.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		timeout = new TextField(""+GameConstants.TIMEOUT, textFieldStyle);
		timeout.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());

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
		reversedLevel = new CheckBox("", cbs);

		area_1.setChecked(GameConstants.area_1);
		area_2.setChecked(GameConstants.area_2);
		area_3.setChecked(GameConstants.area_3);
		area_4.setChecked(GameConstants.area_4);
		reversedLevel.setChecked(GameConstants.reversedLevel);

		avanceCB = new CheckBox(localisation(_more), cbs);

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
				GameConstants.workspaceHeight = Integer.valueOf(workspaceHeight.getText());
				GameConstants.workspaceWidth = Integer.valueOf(workspaceWidth.getText());
				GameConstants.range = Integer.valueOf(range.getText());
				//				GameConstants.pathDeltaTime = Float.valueOf(pathDeltaTime.getText());
				GameConstants.evaporationPerDay = Float.valueOf(evaporationPerDay.getText());
				GameConstants.alpha = Float.valueOf(alpha.getText());
				GameConstants.SUCCESS = Integer.valueOf(nbSuccess.getText());
				//				GameConstants.numberOfRows = Integer.valueOf(numberOfRows.getText());
				GameConstants.totalVolume = Integer.valueOf(totalVolume.getText());
				//				GameConstants.volumePerLevel = Integer.valueOf(volumePerLevel.getText());
				GameConstants.area_1 = area_1.isChecked();
				GameConstants.area_2 = area_2.isChecked();
				GameConstants.area_3 = area_3.isChecked();
				GameConstants.area_4 = area_4.isChecked();
				GameConstants.reversedLevel = reversedLevel.isChecked();
				GameConstants.TIMEOUT = Integer.valueOf(timeout.getText());
				RollingCat.lang = list.getSelectedIndex();
				try {
					Localisation.loadLanguage(RollingCat.lang);
				} catch (Exception e) {
					Gdx.app.log(RollingCat.LOG, "error in lang file");
					RollingCat.lang = 0;
					Localisation.loadLanguage(RollingCat.lang);
				}
				game.setScreen(oldScreen);
			}
		});
		discard = new TextButton(localisation(_discard), style);
		discard.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(oldScreen);
			}
		});

		table.add(heightLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(workspaceHeight).right();
		table.row();
		table.add(widthLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(workspaceWidth).right();
		table.row();
		table.add(nbSuccessLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(nbSuccess).right();
		table.row();
		table.add(totalVolumeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(totalVolume).right();
		table.row();
		table.add(timeoutLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(timeout).right();
		table.row();

		table.add(reversedLevelLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(reversedLevel).right().padRight(GameConstants.BLOCK_WIDTH * 1.5f);
		table.row();

		table.add(rangeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(range).right();
		table.row();
		table.add(evaporationPerDayLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(evaporationPerDay).right();
		table.row();
		table.add(alphaLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
		table.add(alpha).right();
		table.row();
		table.add(save).expand().padTop(GameConstants.BLOCK_HEIGHT * 2);
		table.add(avanceCB).expand().padTop(GameConstants.BLOCK_HEIGHT * 2);
		table.add(discard).expand().padTop(GameConstants.BLOCK_HEIGHT * 2);
		table.row();

		stage.addActor(table);
		stage.addActor(list);
		stage.addActor(zoneTable);

		list.setSelectedIndex(RollingCat.lang);

		Gdx.input.setInputProcessor(stage);

		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		labelStyle.background = skin.getDrawable("empty");

		warningMessage = new Label(StringUtils.addEnters(localisation(_cadran), 14), ls);
		warningMessage.setX(GameConstants.DISPLAY_WIDTH * 0.6f);
		warningMessage.setY(GameConstants.DISPLAY_HEIGHT * 0.33f - warningMessage.getHeight());
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

	private void checkHeightBounds() {
		if(Integer.valueOf(workspaceHeight.getText()) > 500)
			workspaceHeight.setText("500");

		if(Integer.valueOf(workspaceHeight.getText()) < 2)
			workspaceHeight.setText(""+1);		
	}

	private void checkWidthBounds() {
		if(Integer.valueOf(workspaceWidth.getText()) > 500)
			workspaceWidth.setText("500");

		if(Integer.valueOf(workspaceWidth.getText()) < 2)
			workspaceWidth.setText(""+1);
	}



}
