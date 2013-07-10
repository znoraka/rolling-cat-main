package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.Localisation.*;
import static fr.lirmm.smile.rollingcat.Localisation._dominant_member;
import static fr.lirmm.smile.rollingcat.Localisation._hemiplegia;
import static fr.lirmm.smile.rollingcat.Localisation._select;
import static fr.lirmm.smile.rollingcat.Localisation._skin;
import static fr.lirmm.smile.rollingcat.Localisation.localisation;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSettingsButton;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSmallFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSpriteBatch;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.Localisation;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.InternetManager;
import fr.lirmm.smile.rollingcat.model.doctor.Doctor;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.world.World;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;
import fr.lirmm.smile.rollingcat.utils.StringUtils;

public class PatientSelectScreen implements Screen{

	private RollingCat game;
	private int nbpatients;
	private Stage stage;
	private BitmapFont font;
	private Skin skin;
	private SpriteBatch batch;
	private TextButton b, selectPatient, back, settings, tracks, assessment;
	private ArrayList<TextButton> buttons;
	private ScrollPane sp;
	private Table tableLeft, tableRightBottom, tableRightTop, zoneTable;
	private ArrayList<Patient> patients;
	private Patient p;
	private Label nom, prenom, hemiplegia, dominantMember, hempiplegiaLabel, dominantMemberLabel, config;
	private LabelStyle labelStyle;
	private Doctor doctor;
	private int selectedButton;
	private TextField workspaceHeight, workspaceWidth, range, evaporationPerDay, alpha, nbSuccess, numberOfRows, totalVolume, volumePerLevel, timeout;
	private Label heightLabel, widthLabel, rangeLabel, reversedLevelLabel, evaporationPerDayLabel, alphaLabel, nbSuccessLabel, numberOfRowsLabel, totalVolumeLabel, volumePerLevelLabel, timeoutLabel;
	private CheckBox area_1, area_2, area_3, area_4, avanceCB, reversedLevel;

	public PatientSelectScreen(RollingCat game){
		this.game = game;
		this.doctor = Doctor.getDoctor();
		World.clearInstance();
		Patient.clearInstance();
		InternetManager.clearLevel();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		buttons.get(selectedButton).setColor(Color.GREEN);
		Patient.setCurrentPatient(p, selectedButton);
		
		batch.begin();
		batch.draw(skin.getRegion("backgroundtrack"), 0, 0, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT, 1, 1, 0);
		batch.end();

		workspaceHeight.getStyle().font.setUseIntegerPositions(true);
		stage.draw();
		stage.act(delta);

		if(avanceCB.isChecked())
			rangeLabel.setText(localisation(_range));
		else
			rangeLabel.setText(localisation(_cadran));

		zoneTable.setVisible(!avanceCB.isChecked());
		range.setVisible(avanceCB.isChecked());
		evaporationPerDay.setVisible(avanceCB.isChecked());
		evaporationPerDayLabel.setVisible(avanceCB.isChecked());
		alpha.setVisible(avanceCB.isChecked());
		alphaLabel.setVisible(avanceCB.isChecked());

		avanceCB.setX(GameConstants.DISPLAY_WIDTH * 0.325f);
		avanceCB.setY(GameConstants.DISPLAY_HEIGHT * 0.16f);
	}

	@Override
	public void resize(int width, int height) {

	}


	private void createButtons(TextButtonStyle style) {
		for (int i = 0; i < nbpatients; i++) {
			b = new TextButton(patients.get(i).getFirstName() + "\n" + patients.get(i).getLastName(), style);
			b.setName(""+i);
			buttons.add(b);
			tableLeft.add(b).align(Align.right).width(sp.getWidth()*0.85f).height(65).pad(5);
			tableLeft.row();
		}
		addListeners();
	}

	private void addListeners(){
		for (int i = 0; i < buttons.size(); i++) {
			b = buttons.get(i);
			b.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					for (TextButton button : buttons) {
						button.setColor(1, 1, 1, 1);
					}
					selectedButton = Integer.valueOf(event.getListenerActor().getName());
					p = patients.get(selectedButton);
					Patient.setCurrentPatient(p, selectedButton);

					setPatient(patients.get(Integer.valueOf(event.getListenerActor().getName())));
					InternetManager.needsAssessment();
					InternetManager.clearLevel();
					InternetManager.getNumberOfLevels(Patient.getInstance().getID());
				}
			});
		}
	}

	@Override
	public void show() {
		batch = getSpriteBatch();
		skin = getSkin();
		font = getBigFont();
		stage = getStage();
		settings = getSettingsButton(this, game, font);

		tableLeft = new Table();
		tableRightBottom = new Table();
		tableRightTop = new Table();
		buttons = new ArrayList<TextButton>();

		Gdx.input.setInputProcessor(stage);

		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("button_up");
		style.down = skin.getDrawable("button_down");
		style.font = font;
		style.fontColor = Color.BLACK;

		patients = doctor.getPatients();
		this.nbpatients = patients.size();

		if(patients.size() > 0){
			ScrollPaneStyle scrollPanelStyle = new ScrollPaneStyle();
			scrollPanelStyle.corner = skin.getDrawable("button_up");
			scrollPanelStyle.vScroll = skin.getDrawable("button_up");
			scrollPanelStyle.vScrollKnob = skin.getDrawable("background_base");
			scrollPanelStyle.vScrollKnob.setRightWidth(20);
			scrollPanelStyle.vScrollKnob.setMinWidth(20);
			scrollPanelStyle.vScrollKnob.setLeftWidth(20);
			scrollPanelStyle.vScroll.setRightWidth(20);
			scrollPanelStyle.vScroll.setMinWidth(20);
			scrollPanelStyle.vScroll.setLeftWidth(20);	

			sp = new ScrollPane(tableLeft, scrollPanelStyle);
			sp.setX(GameConstants.DISPLAY_WIDTH*0.025f);
			sp.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
			sp.setHeight(GameConstants.DISPLAY_HEIGHT * 0.930f);
			sp.setWidth(GameConstants.DISPLAY_WIDTH * 0.262f);
			sp.setFadeScrollBars(false);
			sp.scrollTo(0, 0, 0, 0);
			sp.setFadeScrollBars(false);

			tableRightBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
			tableRightBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.600f);
			tableRightBottom.setX(GameConstants.DISPLAY_WIDTH*0.315f);
			tableRightBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.658f);

			tableRightBottom.setBackground(skin.getDrawable("background_base"));
			tableRightTop.setBackground(skin.getDrawable("background_base"));

			stage.addActor(sp);
			stage.addActor(tableRightTop);
			stage.addActor(tableRightBottom);

			p = patients.get(Patient.getIndex());
			selectedButton = Patient.getIndex();
			Patient.setCurrentPatient(p, selectedButton);
			InternetManager.needsAssessment();
			InternetManager.clearLevel();
			InternetManager.getNumberOfLevels(Patient.getInstance().getID());

			createButtons(style);

			labelStyle = new LabelStyle(font, Color.BLACK);
			labelStyle.background = skin.getDrawable("textfield");

			config = new Label(localisation(_settings), labelStyle);

			nom = new Label(p.getLastName(), labelStyle);
			prenom = new Label(p.getFirstName(), labelStyle);
			hempiplegiaLabel = new Label(localisation(_hemiplegia), labelStyle);
			hemiplegia = new Label(p.getHemiplegia(), labelStyle);
			dominantMemberLabel = new Label(localisation(_dominant_member), labelStyle);
			dominantMember = new Label(p.getDominantMember(), labelStyle);
			selectPatient = new TextButton(localisation(_select), style);

			selectPatient.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					saveConfig();
					while(Patient.getInstance().needsAssessment() == null)
					{
						continue;
					}
					if(Patient.getInstance().needsAssessment().equals("false"))
						game.setScreen(new CharacterSelectScreen(game));
					else
						game.setScreen(new AssessmentScreen(game));	
				}
			});

			tracks = new TextButton(localisation(_tracks), style);
			tracks.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					saveConfig();
					game.setScreen(new TrackingRecapScreen(game));
				}
			});

			assessment = new TextButton(localisation(_assessment), style);
			assessment.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					saveConfig();
					game.setScreen(new AssessmentScreen(game));
				}
			});

			back = new TextButton(localisation(_back), style);
			back.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					InternetManager.reset();
					game.setScreen(new LoginScreen(game));
				}
			});

			tableRightBottom.add(selectPatient).pad(GameConstants.BLOCK_HEIGHT * 0.07f);
			tableRightBottom.add(assessment).pad(GameConstants.BLOCK_HEIGHT * 0.07f);
			tableRightBottom.add(tracks).pad(GameConstants.BLOCK_HEIGHT * 0.07f);
			tableRightBottom.add(back).pad(GameConstants.BLOCK_HEIGHT * 0.07f);

			stage.addActor(config);

			zoneTable = new Table();

			zoneTable.setBackground(skin.getDrawable("empty"));

			TextFieldStyle textFieldStyle = new TextFieldStyle();
			textFieldStyle.font = font;
			textFieldStyle.fontColor = Color.WHITE;
			textFieldStyle.cursor = skin.getDrawable("cursor");
			textFieldStyle.selection = skin.getDrawable("selection");
			textFieldStyle.background = skin.getDrawable("textfield1");

			LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
			labelStyle.background = skin.getDrawable("empty");
			labelStyle.font = font;
			labelStyle.fontColor = Color.WHITE;

			heightLabel = new Label(localisation(_workspace_height)+" :", labelStyle);
			widthLabel = new Label(localisation(_workspace_width)+" :", labelStyle);
			rangeLabel = new Label(localisation(_cadran), labelStyle);
			evaporationPerDayLabel = new Label(localisation(_evaporation_per_day)+" :", labelStyle);
			alphaLabel = new Label(localisation(_alpha) + " :", labelStyle);
			nbSuccessLabel = new Label(localisation(_nbsuccess)+" :", labelStyle);
			totalVolumeLabel = new Label(localisation(_total_volume)+" :", labelStyle);
			timeoutLabel = new Label(localisation(_timeout)+" :", labelStyle);
			reversedLevelLabel = new Label(localisation(_reversed)+ " :", labelStyle);

			workspaceHeight = new TextField(""+GameConstants.workspaceHeight, textFieldStyle);
			workspaceHeight.setRightAligned(false);
			workspaceHeight.setOriginX(50);


			workspaceHeight.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			workspaceWidth = new TextField(""+GameConstants.workspaceWidth, textFieldStyle);
			workspaceWidth.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			range = new TextField(""+GameConstants.range, textFieldStyle);
			range.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			evaporationPerDay = new TextField(""+GameConstants.evaporationPerDay, textFieldStyle);
			evaporationPerDay.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			alpha = new TextField(""+GameConstants.alpha, textFieldStyle);
			alpha.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			nbSuccess = new TextField(""+GameConstants.SUCCESS, textFieldStyle);
			nbSuccess.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			totalVolume = new TextField(""+GameConstants.totalVolume, textFieldStyle);
			totalVolume.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
			timeout = new TextField(""+GameConstants.TIMEOUT, textFieldStyle);
			timeout.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());


			CheckBoxStyle cbs = new CheckBoxStyle();
			cbs.checkboxOff = getSkin().getDrawable("unchecked1");
			cbs.checkboxOn = getSkin().getDrawable("checked1");
			cbs.checkboxOff.setMinHeight(GameConstants.BLOCK_WIDTH * 2f);
			cbs.checkboxOff.setMinWidth(GameConstants.BLOCK_WIDTH * 2f);
			cbs.checkboxOn.setMinHeight(GameConstants.BLOCK_WIDTH * 2f);
			cbs.checkboxOn.setMinWidth(GameConstants.BLOCK_WIDTH * 2f);
			cbs.font = font;
			cbs.fontColor = Color.BLACK;
			cbs.down = getSkin().getDrawable("square");
			cbs.up = getSkin().getDrawable("square");
			
			
			area_1 = new CheckBox("1", cbs);
			area_2 = new CheckBox("2", cbs);
			area_3 = new CheckBox("3", cbs);
			area_4 = new CheckBox("4", cbs);
			
			CheckBoxStyle cbs1 = new CheckBoxStyle(cbs);
			cbs1.down = getSkin().getDrawable("empty");
			cbs1.up = getSkin().getDrawable("empty");
			cbs1.checkboxOff.setMinHeight(GameConstants.BLOCK_WIDTH * 1);
			cbs1.checkboxOff.setMinWidth(GameConstants.BLOCK_WIDTH * 1);
			cbs1.checkboxOn.setMinHeight(GameConstants.BLOCK_WIDTH * 1);
			cbs1.checkboxOn.setMinWidth(GameConstants.BLOCK_WIDTH * 1);
			
			reversedLevel = new CheckBox("", cbs1);
			reversedLevel.setChecked(GameConstants.reversedLevel);

			area_1.setChecked(GameConstants.area_1);
			area_2.setChecked(GameConstants.area_2);
			area_3.setChecked(GameConstants.area_3);
			area_4.setChecked(GameConstants.area_4);
			
			CheckBoxStyle cbs2 = new CheckBoxStyle(cbs);
			cbs2.down = getSkin().getDrawable("button_up");
			cbs2.up = getSkin().getDrawable("button_down");
			cbs2.checkboxOff.setMinHeight(GameConstants.BLOCK_WIDTH * 0.6f);
			cbs2.checkboxOff.setMinWidth(GameConstants.BLOCK_WIDTH * 0.6f);
			cbs2.checkboxOn.setMinHeight(GameConstants.BLOCK_WIDTH * 0.6f);
			cbs2.checkboxOn.setMinWidth(GameConstants.BLOCK_WIDTH * 0.6f);

			avanceCB = new CheckBox(localisation(_more), cbs2);
			avanceCB.getStyle().down = getSkin().getDrawable("button_up");
			avanceCB.getStyle().up = getSkin().getDrawable("button_up");

			zoneTable.add(area_3).fill().expand();
			zoneTable.add(area_4).fill().expand();
			zoneTable.row();
			zoneTable.add(area_1).fill().expand();
			zoneTable.add(area_2).fill().expand();

			style.up = skin.getDrawable("button_up");
			style.down = skin.getDrawable("button_down");
			style.font = font;
			style.fontColor = Color.BLACK;

			tableRightTop.add(heightLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(workspaceHeight).right().padLeft(GameConstants.BLOCK_WIDTH * 3).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(widthLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(workspaceWidth).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(nbSuccessLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(nbSuccess).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(totalVolumeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(totalVolume).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(timeoutLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(timeout).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();

			tableRightTop.add(reversedLevelLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(reversedLevel).right().padRight(GameConstants.BLOCK_WIDTH * 0).height(GameConstants.BLOCK_HEIGHT);
			tableRightTop.row();

			tableRightTop.add(rangeLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(range).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(evaporationPerDayLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(evaporationPerDay).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();
			tableRightTop.add(alphaLabel).left().pad(GameConstants.BLOCK_WIDTH * 0.2f);
			tableRightTop.add(alpha).right().height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
			tableRightTop.row();

			stage.addActor(tableRightTop);
			stage.addActor(zoneTable);

			Gdx.input.setInputProcessor(stage);

			stage.addActor(avanceCB);

			tableRightBottom.setY(GameConstants.DISPLAY_HEIGHT * 0.038f);
			tableRightBottom.setHeight(GameConstants.DISPLAY_HEIGHT * 0.1f);
			tableRightBottom.setX(GameConstants.DISPLAY_WIDTH*0.315f);
			tableRightBottom.setWidth(GameConstants.DISPLAY_WIDTH * 0.66f);
			tableRightBottom.invalidate();

			tableRightTop.setY(GameConstants.DISPLAY_HEIGHT * 0.15f);
			tableRightTop.setHeight(GameConstants.DISPLAY_HEIGHT * 0.818f);
			tableRightTop.setX(GameConstants.DISPLAY_WIDTH*0.315f);
			tableRightTop.setWidth(GameConstants.DISPLAY_WIDTH * 0.66f);
			tableRightTop.invalidate();

			config.setX(GameConstants.DISPLAY_WIDTH * 0.315f);
			config.setY(GameConstants.DISPLAY_HEIGHT * 0.888f);
			config.setHeight(GameConstants.DISPLAY_HEIGHT * 0.08f);
			config.setWidth(GameConstants.DISPLAY_WIDTH * 0.2f);

			zoneTable.setHeight(GameConstants.DISPLAY_HEIGHT * 0.3f);
			zoneTable.setWidth(GameConstants.DISPLAY_WIDTH * 0.3f);
			zoneTable.setX(GameConstants.DISPLAY_WIDTH * 0.662f);
			zoneTable.setY(GameConstants.DISPLAY_HEIGHT * 0.16f);
			zoneTable.invalidate();
		}

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

	private void setPatient(Patient patient) {
		p = patient;
		nom.setText(patient.getLastName());
		prenom.setText(patient.getFirstName());
		hemiplegia.setText(patient.getHemiplegia());
		dominantMember.setText(patient.getDominantMember());
	}

	private void saveConfig()
	{
		GameConstants.workspaceHeight = Integer.valueOf(workspaceHeight.getText());
		GameConstants.workspaceWidth = Integer.valueOf(workspaceWidth.getText());
		GameConstants.range = Integer.valueOf(range.getText());
		//		GameConstants.pathDeltaTime = Float.valueOf(pathDeltaTime.getText());
		GameConstants.evaporationPerDay = Float.valueOf(evaporationPerDay.getText());
		GameConstants.alpha = Float.valueOf(alpha.getText());
		GameConstants.SUCCESS = Integer.valueOf(nbSuccess.getText());
		//		GameConstants.numberOfRows = Integer.valueOf(numberOfRows.getText());
		GameConstants.totalVolume = Integer.valueOf(totalVolume.getText());
		//		GameConstants.volumePerLevel = Integer.valueOf(volumePerLevel.getText());
		GameConstants.area_1 = area_1.isChecked();
		GameConstants.area_2 = area_2.isChecked();
		GameConstants.area_3 = area_3.isChecked();
		GameConstants.area_4 = area_4.isChecked();
		GameConstants.reversedLevel = reversedLevel.isChecked();
		GameConstants.TIMEOUT = Integer.valueOf(timeout.getText());
	}
}
