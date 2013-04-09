package fr.lirmm.smile.rollingcat.utils.tutorial;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.controller.MouseCursorGame;
import fr.lirmm.smile.rollingcat.model.game.Box;
import fr.lirmm.smile.rollingcat.model.game.Cat;
import fr.lirmm.smile.rollingcat.model.game.Entity;
import fr.lirmm.smile.rollingcat.model.game.Wasp;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

/**
 * 
 * @author Cedric 
 * This class visit an other screen class and add graphics elements for help the player to play.
 *
 */
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
	private Image genious;
	private Image bubble;
	private Box box;
	private Label label;
	private BitmapFont font;
	private boolean geniousDirectionChange;

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
		label.setText("Bienvenue dans\n le tutoriel \nRolling Cat");
		stage.addActor(label);
		firstCoin = true;
		initGenious();
		bubble = new Image(GdxRessourcesGetter.getAtlas().findRegion("bulle"));
		stage.addActor(bubble);
	}

	/**
	 * 
	 * @return the screen scrolling position
	 */
	public int decalage()
	{
		return (int) (box.getX() + box.getWidth()/2 - GameConstants.DISPLAY_WIDTH/2);
	}
	
	/**
	 * Render the elements
	 * @param delta time
	 */
	public void render(float delta)
	{

		renderFirstBox();
		renderCoins(delta);
		renderSecurePath(delta);
		//		label.setX(decalage() + GameConstants.DISPLAY_WIDTH/2 - label.getMaxWidth());
//		label.setY(3*GameConstants.DISPLAY_HEIGHT/4 - label.getHeight());
		
		genious.setX(decalage() + GameConstants.DISPLAY_WIDTH - genious.getWidth());
		updateText();
		updateTextTime(delta);
		updateGenious();
	}

	/**
	 * Update the text position
	 */
	private void updateText()
	{
		updateBubble();
		label.setX(bubble.getX() + bubble.getWidth() * 0.1f);
		label.setY(bubble.getY() + bubble.getHeight()*0.5f) ;
	}
	
	/**
	 * Draw the text when player must be take an element in a box and must be point the pointink task.
	 */
	private void renderFirstBox()
	{
		if(!MouseCursorGame.isHoldingItem() && isBlockInX() && first)
		{
			label.setText("Pointez sur la caisse");
		}
		if(MouseCursorGame.isHoldingItem())
		{

			List<Entity> tasks = this.getNextsTasks();
			if(!tasks.isEmpty())
			{
				label.setText("Point the task");
				first = false;
			}
		}

		if(mc.isTrigger())
		{
			label.setText("Good job");
			first = true;
			hasCollideWasp = false;
		}
	}

	/**
	 * Draw the text when player put a coin
	 * @param delta
	 */
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

	/**
	 * Draw the text when player put secure path
	 * @param delta
	 */
	private void renderSecurePath(float delta)
	{
		if(mc.isFalling())
		{
			label.setText("Oups, ce n'est pas grave vous pourrez bientot recommencer");
			first = true;
		}
	}
	
	
	/**
	 * Update the text time value
	 * @param delta
	 */
	private void updateTextTime(float delta)
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

	/**
	 *
	 * @return the list of all next pointing tasks, depend of the box content.
	 */
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

	/**
	 * 
	 * @return true if cat is blocked by the pointing task.
	 */
	private boolean isBlockInX()
	{
		return( !cat.isMoving() || hasCollideWasp());		
	}

	/**
	 * 
	 * @return true if cat has touch the wasp.
	 */
	private boolean hasCollideWasp()
	{
		Actor a = stage.hit(cat.getX(), cat.getY() + GameConstants.BLOCK_HEIGHT, false);
		if(a instanceof Wasp)
		{
			hasCollideWasp = true;
		}
		return hasCollideWasp;
	}

	/**
	 * Genious initialisation
	 */
	private void initGenious()
	{
		genious = new Image(GdxRessourcesGetter.getAtlas().findRegion("genie"));
		genious.setWidth(200);
		genious.setHeight(200);
		genious.setX(GameConstants.DISPLAY_WIDTH - genious.getWidth());
		genious.setY(GameConstants.DISPLAY_HEIGHT - genious.getHeight());
		stage.addActor(genious);
		genious.setZIndex(0);
	}

	/**
	 * Update the genious position and genious actions.
	 */
	public void updateGenious()
	{
		if(genious.getActions().size == 0)
		{
			if(!geniousDirectionChange)
			{
				genious.addAction(Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT*0.3f, 1, Interpolation.fade));			
				geniousDirectionChange = true;
			}
			else
			{
				geniousDirectionChange = false;
				genious.addAction(Actions.moveBy(0, GameConstants.BLOCK_HEIGHT*0.3f, 1, Interpolation.fade));
			}
		}
	}
	/**
	 * Update the bubble text position, depend of genious position.
	 */
	public void updateBubble()
	{
		int s = label.getText().length();
		float sizeCharacter = font.getXHeight();
		int nbEnter = label.getText().toString().split("\\n").length;
		float h = nbEnter*font.getLineHeight()*2;
		float w = (s/nbEnter)*sizeCharacter;
		bubble.setHeight(h);
		bubble.setWidth(w);
		bubble.setX(genious.getX() - bubble.getWidth());
		bubble.setY(genious.getY()  +genious.getHeight() - bubble.getHeight());
		bubble.setZIndex(0);
		float scaleX = w / bubble.getImageWidth();
		float scaleY = h / bubble.getImageHeight();
		bubble.setScale(scaleX,scaleY);
	}

	
}
