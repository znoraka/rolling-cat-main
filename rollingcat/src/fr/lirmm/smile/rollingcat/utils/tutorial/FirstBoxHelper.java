package fr.lirmm.smile.rollingcat.utils.tutorial;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class FirstBoxHelper 
{
	private static int NB_SECOND_TO_PRINT_TEXT = 4;
	private ShapeRenderer sr;
	private Stage stage;
	private MouseCursorGame mc;
	private Cat cat;
	private boolean first;
	private boolean firstCoin;
	
	private Label label;
	public FirstBoxHelper(ShapeRenderer sr,Stage stage ,MouseCursorGame mc,Cat cat,Screen screen) {
		
		this.sr = sr;
		this.mc = mc;
		this.stage = stage;
		this.cat = cat;
		first = true;
		LabelStyle style = new LabelStyle();
		style.font = GdxRessourcesGetter.getBigFont();
		style.fontColor = Color.BLACK;
		label = new Label("", style);
		label.setText("Bienvenue dans le tutoriel \nRolling Cat");
     	stage.addActor(label);
     	firstCoin = true;
	}

	public int decalage()
	{
		return (int) ((int)mc.getX() / GameConstants.VIEWPORT_WIDTH) * GameConstants.VIEWPORT_WIDTH;
		
	}
	public void render(float delta)
	{
		renderFirstBox();
		renderCoins(delta);
		label.setX(decalage() + GameConstants.DISPLAY_WIDTH/2 - label.getMaxWidth());
		label.setY(3*GameConstants.DISPLAY_HEIGHT/4 - label.getHeight());

		timeText(delta);
	}

	
	private void renderFirstBox()
	{
		if(!mc.isHoldingItem() && !cat.isMoving() && first)
		{
			label.setText("Pointez sur la caisse");
			sr.setColor(Color.RED);
			sr.begin(ShapeType.Line);
			sr.circle(decalage() + GameConstants.DISPLAY_WIDTH/2, 0, 100);
			sr.end();
			
		}
		if(mc.isHoldingItem())
		{
			Vector2 coord = mc.getCoordTasks();
			label.setText("Point the task");
			sr.setColor(Color.RED);
			sr.begin(ShapeType.Line);
			sr.circle(coord.x + GameConstants.BLOCK_WIDTH/2, coord.y+ GameConstants.BLOCK_HEIGHT/2, 50);
			sr.end();
			
			first = false;
		}
		
		if(mc.isTrigger())
		{
			label.setText("Good job");
			first = true;
			
		}
	}
	private float ellapsedTimeCoin = -1;


	private void renderCoins(float delta)
	{
		if(cat.hasCatchCoin() && firstCoin)
		{
			label.setText("Vous avez gagne une piece.\n " +
					"Ces pieces vous permettent \n" +
					"de renforcer votre score");
			ellapsedTimeCoin = 0;
		}
	}
	
	private void timeText(float delta)
	{
		if(ellapsedTimeCoin != -1)
		{
			ellapsedTimeCoin+=delta;
			if(ellapsedTimeCoin >= NB_SECOND_TO_PRINT_TEXT)
			{
				label.setText("");
				firstCoin = false;
				ellapsedTimeCoin = -1;
			}
		}
	}
	
	
	
}
