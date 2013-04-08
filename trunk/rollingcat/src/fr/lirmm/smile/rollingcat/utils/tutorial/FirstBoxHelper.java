package fr.lirmm.smile.rollingcat.utils.tutorial;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

import static fr.lirmm.smile.rollingcat.Localisation.*;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Wasp;
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
	private boolean hasCollideWasp;
	private float ellapsedTimeCoin = -1;
	private Image ra;
	private Image bubble;
	private Box box;
	private Label label;
	private BitmapFont font;
	public FirstBoxHelper(ShapeRenderer sr,Stage stage ,MouseCursorGame mc,Cat cat,Screen screen,Box box) {

		this.box = box;
		this.sr = sr;
		this.mc = mc;
		this.stage = stage;
		this.cat = cat;
		first = true;
		font = GdxRessourcesGetter.getSmallFont();
		LabelStyle style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.BLACK;
	
		label = new Label("", style);
		label.setText(localisation(_welcome));
		stage.addActor(label);
		firstCoin = true;
		initRa();
		bubble = new Image(GdxRessourcesGetter.getAtlas().findRegion("bulle"));
		stage.addActor(bubble);
	}

	public int decalage()
	{
		return (int) (box.getX() + box.getWidth()/2 - GameConstants.DISPLAY_WIDTH/2);
	}
	
	
	public void render(float delta)
	{

		renderFirstBox();
		renderCoins(delta);
//		label.setX(decalage() + GameConstants.DISPLAY_WIDTH/2 - label.getMaxWidth());
//		label.setY(3*GameConstants.DISPLAY_HEIGHT/4 - label.getHeight());
		
		ra.setX(decalage() + GameConstants.DISPLAY_WIDTH - ra.getWidth());
		miseAjourText();
		timeText(delta);

	}

	public void miseAjourText()
	{
		updateBubble();
		label.setX(bubble.getX() + bubble.getWidth() * 0.1f);
		label.setY(bubble.getY() + bubble.getHeight()*0.5f) ;
	}
	
	private void renderFirstBox()
	{
		if(!MouseCursorGame.isHoldingItem() && isBlockInX() && first)
		{	
			label.setText(localisation(_box_point));
		}
		if(MouseCursorGame.isHoldingItem())
		{

			List<Entity> tasks = this.getNextsTasks();
			if(!tasks.isEmpty())
			{	
				label.setText(localisation(_task_point));
				first = false;
			}
		}

		if(mc.isTrigger())
		{
			label.setText(localisation(_gg));
			first = true;
			hasCollideWasp = false;
		}
	}


	private void renderCoins(float delta)
	{
		if(cat.hasCatchCoin() && firstCoin)
		{
			label.setText(localisation(_coin_get));
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

	private List<Entity> getNextsTasks()
	{
		List<Entity> ret = new ArrayList<Entity>();
		if(MouseCursorGame.getItem()  != Box.EMPTY)
		{
			Array<Actor> listActors = stage.getActors();
			Entity tmp = null;

			for(Actor a : listActors)
			{
				if(a instanceof Entity)
				{
					tmp = (Entity)a ;
					if(((Entity) a).getItemToAct() == MouseCursorGame.getItem() && a.isVisible())
					{
						ret.add(tmp);
					}
				}
			}
		}
		return ret;
	}

	private boolean isBlockInX()
	{
		return( !cat.isMoving() || hasCollideWasp());		
	}


	private boolean hasCollideWasp()
	{
		Actor a = stage.hit(cat.getX(), cat.getY() + GameConstants.BLOCK_HEIGHT, false);
		if(a instanceof Wasp)
		{
			hasCollideWasp = true;
		}
		return hasCollideWasp;
	}

	private void initRa()
	{
		ra = new Image(GdxRessourcesGetter.getAtlas().findRegion("genie"));
		ra.setWidth(200);
		ra.setHeight(200);
		ra.setX(GameConstants.DISPLAY_WIDTH - ra.getWidth());
		ra.setY(GameConstants.DISPLAY_HEIGHT - ra.getHeight());
		stage.addActor(ra);
		ra.setZIndex(0);
	}


	public void updateBubble()
	{
		int s = label.getText().length();
		float sizeCharacter = font.getXHeight();
		int nbEnter = label.getText().toString().split("\\n").length;
		float h = nbEnter*font.getLineHeight()*2;
		float w = (s/nbEnter)*sizeCharacter;
		bubble.setHeight(h);
		bubble.setWidth(w);
		bubble.setX(ra.getX() - bubble.getWidth());
		bubble.setY(ra.getY()  +ra.getHeight() - bubble.getHeight());
		bubble.setZIndex(0);
		float scaleX = w / bubble.getImageWidth();
		float scaleY = h / bubble.getImageHeight();
		bubble.setScale(scaleX,scaleY);
	}
}
