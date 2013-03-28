package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getCatAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;

public class Cat extends Entity {
	
	private int nbcoin;
	private boolean done;
	
	private static final int FALLING = 0;
	private static final int FLYING = 1;
	private static final int JUMPING = 2;
	private static final int HITTING = 3;
	private static final int WALKING = 4;
	
	private int state;
	private int gold, silver, bronze;
	
	private Rectangle top, right, bottom, left, veryBottom;
	
	SkeletonData skeletonData;
	Skeleton skeleton;
	Bone root;
	Animation walkAnimation, contactAnimation, jumpAnimation, fanAnimation, endAnimation;
	float time;
	
	/**
	 * l'acteur principal
	 * @param x
	 * @param y
	 */
	public Cat(float x, float y){
		super(x,y, GameConstants.TEXTURE_CAT);
		nbcoin = 0;
		done = false;
		this.setTouchable(Touchable.disabled);
		
		final String name = "cat-skeleton";
		
		TextureAtlas atlas = getCatAtlas();
		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/cat/" + name + ".skel"));
		
		walkAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-walk.anim"), skeletonData);
		jumpAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-jump.anim"), skeletonData);
		endAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-levelend.anim"), skeletonData);
		fanAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-upstreamwinp.anim"), skeletonData);
		contactAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-contact.anim"), skeletonData);
		
		

		skeleton = new Skeleton(skeletonData);
		skeleton.setToBindPose();

		root = skeleton.getRootBone();
		root.setScaleX(0.10f * GameConstants.SCALE);
		root.setScaleY(0.10f * GameConstants.SCALE);
		skeleton.updateWorldTransform();
		
		top = new Rectangle();
		bottom = new Rectangle();
		right = new Rectangle();
		left = new Rectangle();
		veryBottom = new Rectangle();
	}
	
	/**
	 * methode pour gerer le déplacement
	 * @param stage
	 */
	public void move(Stage stage){
		top.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY() + GameConstants.BLOCK_HEIGHT, 2, 2);
		bottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY(), 2, 2);
		veryBottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY() - (GameConstants.BLOCK_HEIGHT), 2, 2);
		right.set(this.getX() + GameConstants.BLOCK_WIDTH, this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		left.set(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		this.bounds.set(this.getX() + GameConstants.BLOCK_WIDTH / 4, this.getY() + GameConstants.BLOCK_HEIGHT / 4, this.getWidth() / 2, this.getHeight() / 2);
		
		if(state != FLYING & state != JUMPING)
			state = FALLING;
		
		for (Actor actor : stage.getActors()) {
			if(actor instanceof GroundBlock){
				if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible() & state != FLYING)
					state = WALKING;
				else if(((Entity) actor).getBounds().overlaps(veryBottom) & actor.isVisible() & (state == FLYING || state == JUMPING))
					state = WALKING;
			}
			if(actor instanceof Dog & state == WALKING)
				if(((Entity) actor).getBounds().overlaps(right) & actor.isVisible())
					state = HITTING;
			if(actor instanceof Wasp)
				if(((Entity) actor).getBounds().overlaps(top) & actor.isVisible())
					state = FALLING;
			if(actor instanceof Fan)
				if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible())
					state = FLYING;
			if(actor instanceof Carpet)
				if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible())
					state = HITTING;
			if(actor instanceof Target){
				if(((Entity) actor).getBounds().overlaps(bounds))
					done = true;
			}
			if(actor instanceof Coin){
				if(((Entity) actor).getBounds().overlaps(bounds) & actor.isVisible()){
					if(((Coin) actor).getType() == Coin.BRONZE)
						bronze++;
					else if(((Coin) actor).getType() == Coin.SILVER)
						silver++;
					else if(((Coin) actor).getType() == Coin.GOLD)
						gold++;
					actor.setVisible(false);
					actor.setTouchable(Touchable.disabled);
				}
			}
			
			if(actor instanceof Gap){
				if(((Entity) actor).getBounds().overlaps(left) & actor.isVisible()){
					if(state != JUMPING){
						state = HITTING;
						break;
					}
					else{
						actor.setVisible(false);
					}
				}
			}
		}
		
		if(state != JUMPING){
			if(state == FALLING & this.getActions().size == 0){
				this.addAction(Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() - 1) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
			}
			
			else if(state == FLYING & this.getActions().size == 0)
				this.addAction(Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() + 1) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED * 0.75f));
			
			else if(state == WALKING & this.getActions().size == 0){
				this.addAction(Actions.moveTo((this.getXOnGrid() + 1) * GameConstants.BLOCK_WIDTH, (this.getYOnGrid()) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
			}
			
			else if(state != JUMPING && state == HITTING){
				this.getActions().clear();
			}
		}
        
		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.6f);
		root.setY(getY());
		
		
	}
	
	/**
	 * initialise un saut
	 */
	public void jump(){
		if(state != JUMPING)
		{	
			state = JUMPING;
			this.getActions().clear();
			this.addAction(Actions.parallel(Actions.moveBy(GameConstants.BLOCK_WIDTH * 2, 0, GameConstants.SPEED * 2)));
			this.addAction(Actions.parallel(Actions.sequence(
					Actions.moveBy(0, GameConstants.BLOCK_HEIGHT, GameConstants.SPEED, Interpolation.pow2Out),
					Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT, GameConstants.SPEED, Interpolation.pow2In),
					new Action() {
						
						@Override
						public boolean act(float delta) {
							setStuffAfterJump();
							return true;
						}
					}
		)));
		}
	}

	public int getNbCoin() {
		return this.nbcoin;
	}

	/**
	 * 
	 * @return true if the cat hits the target
	 */
	public boolean isDone(){
		return this.done;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
//		if(state == JUMPING)
//			jumpAnimation.apply(skeleton, time, true);
		
		 if(state == FLYING)
			fanAnimation.apply(skeleton, time, true);
		
		else if(state == HITTING)
			contactAnimation.apply(skeleton, time, true);
		
		else if(state == WALKING)
			walkAnimation.apply(skeleton, time, true);
		 
		else if(state == FALLING)
			fanAnimation.apply(skeleton, time, true);
	
		time += Gdx.graphics.getDeltaTime();
		skeleton.updateWorldTransform();
//		skeleton.update(time);
		skeleton.draw(batch);
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLUE);
		sr.rect(top.x, top.y, top.width, top.height);
		sr.setColor(Color.RED);
		sr.rect(bottom.x, bottom.y, bottom.width, bottom.height);
		sr.setColor(Color.GREEN);
		sr.rect(right.x, right.y, right.width, right.height);
		sr.setColor(Color.ORANGE);
		sr.rect(left.x, left.y, left.width, left.height);
		sr.end();
		
	}
	
	private void setStuffAfterJump(){
		top.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY() + GameConstants.BLOCK_HEIGHT, 2, 2);
		bottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY(), 2, 2);
		veryBottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY() - (GameConstants.BLOCK_HEIGHT), 2, 2);
		right.set(this.getX() + GameConstants.BLOCK_WIDTH, this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		left.set(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		this.getActions().clear();
		state = WALKING;
	}

	public int getBronze() {
		return bronze;
	}
	
	public int getSilver() {
		return silver;
	}
	
	public int getGold() {
		return gold;
	}
}
