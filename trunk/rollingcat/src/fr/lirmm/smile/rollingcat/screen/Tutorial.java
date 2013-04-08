package fr.lirmm.smile.rollingcat.screen;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getAtlas;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getBigFont;
import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getSkin;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.OrderedMap;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.manager.EventManager;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Coin;
import fr.lirmm.smile.rollingcat.model.game.Target;
import fr.lirmm.smile.rollingcat.model.patient.Patient;
import fr.lirmm.smile.rollingcat.model.patient.Track;
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;
import fr.lirmm.smile.rollingcat.utils.tutorial.FirstBoxHelper;


public class Tutorial implements ScreenPausable{

	private RollingCat game;
	private Stage stage;
	private Texture backgroundTexture;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private MouseCursorGame mc;
	private Cat cat;
	private Box box;
	private Patient patient;
	private float duration;
	private BitmapFont font;
    private OrderedMap<String, String> parameters;
    private int segment;
	private InputMultiplexer multiplexer;
	private Tutorial screen;
	private boolean done;
	private Target gem;
	private Image goldImage, silverImage, bronzeImage;
	private Label goldLabel, silverLabel, bronzeLabel;
	private Table table;
	
	private static long elapsedTimeDuringPause;
	
	public static Vector2 gold = new Vector2(GameConstants.BLOCK_WIDTH, GameConstants.DISPLAY_HEIGHT * 0.92f);
	public static Vector2 silver = new Vector2(GameConstants.BLOCK_WIDTH * 3, GameConstants.DISPLAY_HEIGHT * 0.92f);
	public static Vector2 bronze = new Vector2(GameConstants.BLOCK_WIDTH * 5, GameConstants.DISPLAY_HEIGHT * 0.92f);
	
	private FirstBoxHelper  firstBox;

	public Tutorial(RollingCat game, Patient patient, int level, List<String> listOfGems){
		this.game = game;
		this.patient = patient;
		stage = LevelBuilder.build("cat;0.0;7.0/groundBlock;0.0;6.0/fan;1.0;6.0/groundBlock;1.0;10.0/groundBlock;2.0;10.0/empty;3.0;10.0/groundBlock;3.0;8.0/empty;4.0;8.0/groundBlock;4.0;5.0/empty;5.0;5.0/carpet;5.0;2.0/groundBlock;5.0;0.0/groundBlock;6.0;0.0/groundBlock;7.0;0.0/groundBlock;8.0;0.0/fan;9.0;0.0/groundBlock;9.0;3.0/groundBlock;10.0;3.0/empty;11.0;3.0/carpet;11.0;1.0/groundBlock;11.0;0.0/groundBlock;12.0;0.0/groundBlock;13.0;0.0/groundBlock;14.0;0.0/fan;15.0;0.0/groundBlock;15.0;5.0/fan;16.0;5.0/groundBlock;16.0;7.0/groundBlock;17.0;7.0/groundBlock;18.0;7.0/groundBlock;19.0;7.0/groundBlock;20.0;7.0/groundBlock;21.0;7.0/groundBlock;22.0;7.0/groundBlock;23.0;7.0/groundBlock;24.0;7.0/groundBlock;25.0;7.0/groundBlock;26.0;7.0/empty;27.0;7.0/groundBlock;27.0;4.0/empty;28.0;4.0/carpet;28.0;1.0/groundBlock;28.0;0.0/groundBlock;29.0;0.0/fan;30.0;0.0/groundBlock;30.0;5.0/fan;31.0;5.0/groundBlock;31.0;10.0/groundBlock;32.0;10.0/empty;33.0;10.0/groundBlock;33.0;7.0/groundBlock;34.0;7.0/empty;35.0;7.0/groundBlock;35.0;1.0/fan;36.0;1.0/wasp;36.0;4.0/groundBlock;36.0;9.0/empty;37.0;9.0/groundBlock;37.0;6.0/empty;38.0;6.0/carpet;38.0;4.0/groundBlock;38.0;0.0/groundBlock;39.0;0.0/groundBlock;40.0;0.0/fan;41.0;0.0/groundBlock;41.0;4.0/dog;42.0;5.0/groundBlock;42.0;4.0/empty;43.0;4.0/groundBlock;43.0;1.0/empty;44.0;1.0/carpet;44.0;1.0/groundBlock;44.0;0.0/groundBlock;45.0;0.0/groundBlock;46.0;0.0/groundBlock;47.0;0.0/groundBlock;48.0;0.0/groundBlock;49.0;0.0/fan;50.0;0.0/groundBlock;50.0;5.0/empty;51.0;5.0/carpet;51.0;3.0/groundBlock;51.0;0.0/groundBlock;52.0;0.0/groundBlock;53.0;0.0/groundBlock;54.0;0.0/groundBlock;55.0;0.0/fan;56.0;0.0/groundBlock;56.0;2.0/groundBlock;57.0;2.0/fan;58.0;2.0/wasp;58.0;4.0/groundBlock;58.0;5.0/empty;59.0;5.0/carpet;59.0;4.0/groundBlock;59.0;1.0/empty;60.0;1.0/carpet;60.0;1.0/groundBlock;60.0;0.0/groundBlock;61.0;0.0/groundBlock;62.0;0.0/groundBlock;63.0;0.0/groundBlock;64.0;0.0/groundBlock;65.0;0.0/groundBlock;66.0;0.0/groundBlock;67.0;0.0/fan;68.0;0.0/wasp;68.0;2.0/groundBlock;68.0;6.0/groundBlock;69.0;6.0/fan;70.0;6.0/groundBlock;70.0;12.0/empty;71.0;12.0/groundBlock;71.0;6.0/empty;72.0;6.0/groundBlock;72.0;1.0/fan;73.0;1.0/wasp;73.0;6.0/groundBlock;73.0;7.0/empty;74.0;7.0/groundBlock;74.0;3.0/empty;75.0;3.0/groundBlock;75.0;0.0/dog;76.0;1.0/groundBlock;76.0;0.0/fan;77.0;0.0/groundBlock;77.0;2.0/groundBlock;78.0;2.0/fan;79.0;2.0/groundBlock;79.0;4.0/empty;80.0;4.0/groundBlock;80.0;0.0/groundBlock;81.0;0.0/groundBlock;82.0;0.0/fan;83.0;0.0/groundBlock;83.0;2.0/empty;84.0;2.0/carpet;84.0;2.0/groundBlock;84.0;0.0/fan;85.0;0.0/wasp;85.0;4.0/groundBlock;85.0;9.0/groundBlock;86.0;9.0/empty;87.0;9.0/groundBlock;87.0;3.0/empty;88.0;3.0/carpet;88.0;1.0/bronze_coin;5.0;1.0/bronze_coin;6.0;1.0/bronze_coin;7.0;1.0/bronze_coin;9.0;4.0/bronze_coin;12.0;1.0/bronze_coin;13.0;1.0/bronze_coin;14.0;1.0/bronze_coin;29.0;1.0/bronze_coin;30.0;6.0/gold_coin;31.0;11.0/silver_coin;36.0;10.0/silver_coin;37.0;7.0/bronze_coin;38.0;1.0/bronze_coin;39.0;1.0/bronze_coin;40.0;1.0/bronze_coin;43.0;2.0/bronze_coin;44.0;1.0/bronze_coin;45.0;1.0/bronze_coin;46.0;1.0/bronze_coin;51.0;1.0/bronze_coin;52.0;1.0/bronze_coin;53.0;1.0/bronze_coin;58.0;6.0/bronze_coin;59.0;2.0/bronze_coin;60.0;1.0/bronze_coin;61.0;1.0/bronze_coin;62.0;1.0/silver_coin;68.0;7.0/silver_coin;69.0;7.0/gold_coin;70.0;13.0/silver_coin;73.0;8.0/bronze_coin;74.0;4.0/bronze_coin;75.0;1.0/bronze_coin;77.0;3.0/bronze_coin;78.0;3.0/bronze_coin;79.0;5.0/bronze_coin;84.0;1.0/silver_coin;85.0;10.0/silver_coin;86.0;10.0/bronze_coin;87.0;4.0/groundBlock;0.0;-2.0/groundBlock;1.0;-2.0/groundBlock;2.0;-2.0/groundBlock;3.0;-2.0/groundBlock;4.0;-2.0/groundBlock;5.0;-2.0/groundBlock;6.0;-2.0/groundBlock;7.0;-2.0/groundBlock;8.0;-2.0/groundBlock;9.0;-2.0/groundBlock;10.0;-2.0/groundBlock;11.0;-2.0/groundBlock;12.0;-2.0/groundBlock;13.0;-2.0/groundBlock;14.0;-2.0/groundBlock;15.0;-2.0/door_left;16.0;-1.0/door_right;17.0;8.0/groundBlock;16.0;-2.0/groundBlock;17.0;-2.0/groundBlock;18.0;-2.0/groundBlock;19.0;-2.0/groundBlock;20.0;-2.0/groundBlock;21.0;-2.0/groundBlock;22.0;-2.0/groundBlock;23.0;-2.0/groundBlock;24.0;-2.0/groundBlock;25.0;-2.0/groundBlock;26.0;-2.0/groundBlock;27.0;-2.0/groundBlock;28.0;-2.0/groundBlock;29.0;-2.0/groundBlock;30.0;-2.0/groundBlock;31.0;-2.0/door_left;32.0;-1.0/door_right;33.0;8.0/groundBlock;32.0;-2.0/groundBlock;33.0;-2.0/groundBlock;34.0;-2.0/groundBlock;35.0;-2.0/groundBlock;36.0;-2.0/groundBlock;37.0;-2.0/groundBlock;38.0;-2.0/groundBlock;39.0;-2.0/groundBlock;40.0;-2.0/groundBlock;41.0;-2.0/groundBlock;42.0;-2.0/groundBlock;43.0;-2.0/groundBlock;44.0;-2.0/groundBlock;45.0;-2.0/groundBlock;46.0;-2.0/groundBlock;47.0;-2.0/door_left;48.0;-1.0/door_right;49.0;1.0/groundBlock;48.0;-2.0/groundBlock;49.0;-2.0/groundBlock;50.0;-2.0/groundBlock;51.0;-2.0/groundBlock;52.0;-2.0/groundBlock;53.0;-2.0/groundBlock;54.0;-2.0/groundBlock;55.0;-2.0/groundBlock;56.0;-2.0/groundBlock;57.0;-2.0/groundBlock;58.0;-2.0/groundBlock;59.0;-2.0/groundBlock;60.0;-2.0/groundBlock;61.0;-2.0/groundBlock;62.0;-2.0/groundBlock;63.0;-2.0/door_left;64.0;-1.0/door_right;65.0;1.0/groundBlock;64.0;-2.0/groundBlock;65.0;-2.0/groundBlock;66.0;-2.0/groundBlock;67.0;-2.0/groundBlock;68.0;-2.0/groundBlock;69.0;-2.0/groundBlock;70.0;-2.0/groundBlock;71.0;-2.0/groundBlock;72.0;-2.0/groundBlock;73.0;-2.0/groundBlock;74.0;-2.0/groundBlock;75.0;-2.0/groundBlock;76.0;-2.0/groundBlock;77.0;-2.0/groundBlock;78.0;-2.0/groundBlock;79.0;-2.0/door_left;80.0;-1.0/door_right;81.0;1.0/groundBlock;80.0;-2.0/groundBlock;81.0;-2.0/groundBlock;82.0;-2.0/groundBlock;83.0;-2.0/groundBlock;84.0;-2.0/groundBlock;85.0;-2.0/groundBlock;86.0;-2.0/groundBlock;87.0;-2.0/groundBlock;88.0;-2.0/door_left;88.0;-1.0/door_right;88.0;0.0/target;88.0;0.0");
		elapsedTimeDuringPause = 0;
	}

	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		duration += delta;
		mc.updateHoverTimer();
		mc.updateStandTimer();
		cat.move(stage);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, GameConstants.DISPLAY_WIDTH, GameConstants.DISPLAY_HEIGHT);
		table.draw(batch, 1);
		batch.end();
		stage.act(delta);
		
		stage.draw();
		sr.setProjectionMatrix(stage.getCamera().combined);
        mc.render(sr);
        cat.render(sr);
        firstBox.render(delta);
        updateCamPos();
        mc.addTrackingPoint(delta, segment);
        
            
        if(cat.isDone() && gem.getActions().size == 0){
        	gem.addAction(Actions.parallel(Actions.sequence(
        			Actions.delay(2),
        			new Action() {
						
						@Override
						public boolean act(float delta) {
							done = true;
							return true;
						}
					})));
        	gem.addAction(Actions.parallel(Actions.moveTo((GameConstants.DISPLAY_WIDTH) * (segment - 0.25f), GameConstants.DISPLAY_HEIGHT * 0.25f, 2)));
        	
        }
    	if(done){
			game.setScreen(new PatientScreen(game, patient));
    	}
        if(cat.requestBoxEmptiing()){
        	mc.dropItem();
        	cat.requestOk();
        }
        
        setLabelText();
        setVectorCoordinates();
	}
	
	/**
	 * translate la camera si le chat est au bout de l'ecran
	 */
	private void updateCamPos() {
		if(stage.getCamera().position.x + GameConstants.DISPLAY_WIDTH / 2 - GameConstants.BLOCK_WIDTH * 3 < cat.getX()){
        	box.emptyAfterNotMoving(segment);
        	if(!mc.isHoldingItem())
        		box.fill();
        	mc.setX(mc.getX() + GameConstants.VIEWPORT_WIDTH);
			segment++;
			stage.getCamera().translate(GameConstants.VIEWPORT_WIDTH, 0, 0);
			box.setX(box.getX() + GameConstants.VIEWPORT_WIDTH);
			
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if(screen == null){
			Gdx.app.log(RollingCat.LOG, "showing");
			screen = this;
			batch = new SpriteBatch();
			sr = new ShapeRenderer();
			font = getBigFont();
			EventManager.clear();
	//		elapsedTimeDuringPause = 0;
			parameters = new OrderedMap<String, String>();
			backgroundTexture = new Texture(GameConstants.TEXTURE_BACKGROUND);
			backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			cat = (Cat) stage.getActors().get(0);
	     	gem = (Target) stage.getActors().get(stage.getActors().size -1);
			box = new Box(GameConstants.COLS / 2, -2);
			box.setItems(LevelBuilder.getItems());
			stage.addActor(box);
			mc = new MouseCursorGame(stage, cat, box);
			multiplexer = new InputMultiplexer(stage, mc);
			Gdx.input.setInputProcessor(multiplexer);
			duration = 0;
			parameters = new OrderedMap<String, String>();
			parameters.put("game", RollingCat.LOG);
			parameters.put("version", RollingCat.VERSION);
			EventManager.create(EventManager.game_info_event_type, parameters);
			parameters = new OrderedMap<String, String>();
			parameters.put("session_type", Track.GAME);
			parameters.put("game_screen_width", ""+GameConstants.DISPLAY_WIDTH);
			parameters.put("game_screen_height", ""+GameConstants.DISPLAY_HEIGHT);
	     	EventManager.create(EventManager.start_game_event_type, parameters);
	     	
	     	goldImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.GOLD));
	     	silverImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.SILVER));
	     	bronzeImage = new Image(getAtlas().findRegion(GameConstants.TEXTURE_COIN+Coin.BRONZE));
	     	
	     	
	     	LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
            labelStyle.background = getSkin().getDrawable("empty");
            
	     	goldLabel = new Label("", labelStyle);
	     	silverLabel = new Label("", labelStyle);
	     	bronzeLabel  = new Label("", labelStyle);
	     	
	     	table = new Table();
	     	table.setX(GameConstants.DISPLAY_WIDTH * 0.16f);
	     	table.setY(GameConstants.DISPLAY_HEIGHT * 0.95f);
	     	
	     	table.add(goldLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);
	     	table.add(goldImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
	     	table.add(silverLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);;
	     	table.add(silverImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);
	     	table.add(bronzeLabel).padLeft(GameConstants.BLOCK_WIDTH * 0.25f);;
	     	table.add(bronzeImage).height(GameConstants.BLOCK_HEIGHT).width(GameConstants.BLOCK_WIDTH);

	     	stage.addListener(new InputListener() {
	     		
	     		@Override
	     		public boolean keyDown (InputEvent event, int keycode) {
	     			return true;
	     		}
	
	     		@Override
	     		public boolean keyUp (InputEvent event, int keycode) {
	     			if(keycode == Keys.ESCAPE){
	     				Gdx.app.log(RollingCat.LOG, "escape pressed !");
	     				game.setScreen(new PauseScreen(game, screen, patient));
	     			}
	     			if(keycode == Keys.SPACE)
	     				mc.fall();
	     			return true;
	     		}
				
			});
			firstBox = new FirstBoxHelper(sr,stage,mc,cat,this,box);		

		}
		else
			Gdx.input.setInputProcessor(multiplexer);

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
//		LevelBuilder.writeLevel(stage);
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		Gdx.app.log(RollingCat.LOG, "disposing...");
		backgroundTexture.dispose();
		sr.dispose();
	}
	
	public int getScore(){
		return cat.getBronze() + cat.getSilver() * 2 + cat.getGold() * 3;
	}
	
	public void setElapsedTimeDuringPause(long elapsedTime){
		Gdx.app.log(RollingCat.LOG, ""+elapsedTime);
		elapsedTimeDuringPause += elapsedTime;
	}
	
	public static long getElapsedTimeDuringPause(){
		return elapsedTimeDuringPause;
	}
	
	private void setLabelText(){
		goldLabel.setText(cat.getGold() + "x");
		silverLabel.setText(cat.getSilver() + "x");
		bronzeLabel.setText(cat.getBronze() + "x");
	}
	
	private void setVectorCoordinates(){
		gold.x = table.getX() + goldImage.getX();
		gold.y = table.getY() + goldImage.getY();
		
		silver.x = table.getX() + silverImage.getX();
		silver.y = table.getY() + silverImage.getY();
		
		bronze.x = table.getX() + bronzeImage.getX();
		bronze.y = table.getY() + bronzeImage.getY();
	}

}