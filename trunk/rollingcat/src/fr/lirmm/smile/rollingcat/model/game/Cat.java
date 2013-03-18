package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
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
//		this.setTouchable(Touchable.disabled);
		
//		final String name = "cat";

//		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/" + name + ".atlas"));
//		SkeletonBinary binary = new SkeletonBinary(atlas);
//		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/" + name + ".skel"));
//		
//		walkAnimation = binary.readAnimation(Gdx.files.internal("data/" + name + "-running.anim"), skeletonData);
//		jumpAnimation = binary.readAnimation(Gdx.files.internal("data/" + name + "-jump.anim"), skeletonData);
//		endAnimation = binary.readAnimation(Gdx.files.internal("data/" + name + "-levelend.anim"), skeletonData);
//		fanAnimation = binary.readAnimation(Gdx.files.internal("data/" + name + "-upstreamwinp.anim"), skeletonData);
//		contactAnimation = binary.readAnimation(Gdx.files.internal("data/" + name + "-contact.anim"), skeletonData);
//		
//		
//
//		skeleton = new Skeleton(skeletonData);
//		skeleton.setToBindPose();
//
//		root = skeleton.getRootBone();
//		root.setScaleX(0.10f * GameConstants.SCALE);
//		root.setScaleY(0.10f * GameConstants.SCALE);
//		skeleton.updateWorldTransform();
	}
	
	/**
	 * methode pour gerer le déplacement
	 * @param stage
	 */
	public void move(Stage stage){
        setVelocity(stage.hit(this.getX() + GameConstants.BLOCK_WIDTH * 1.5f, this.getY() - GameConstants.BLOCK_HEIGHT / 2, false));
        falling(stage.hit(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY() + 1 - GameConstants.BLOCK_HEIGHT, false));
        pickUp(stage.hit(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY()/*+ GameConstants.BLOCK_HEIGHT / 2*/, false));
        hitElementRight(stage.hit(this.getX() + GameConstants.BLOCK_WIDTH, this.getY() + GameConstants.BLOCK_HEIGHT / 2, false));
        hitElementTop(stage.hit(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT, false));
        
        if(this.getActions().size == 0)
        	state = HITTING;
        
//		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.6f);
//		root.setY(getY());
	}
	
	
	private void falling(Actor hit) {
		//si le chat n'a rien en dessous de lui
		if(!(hit instanceof Block) && this.getActions().size == 0){
			state = FALLING;
			this.getActions().clear();
			this.addAction(Actions.moveBy(0, - GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
		}

	}

	/**
	 * vérifie dans quoi le chat tape à droite
	 * @param hit
	 */
	private void hitElementRight(Actor hit) {
		if(state != FALLING){
			if(hit instanceof Dog){
				this.getActions().clear();
				this.addAction(((Dog) hit).getActionOnCat());
				state = HITTING;
			}
			
			if(hit instanceof Target){
				done = true;
			}
		}
	}
	
	/**
	 * vérifie dans quoi le chat tape en haut
	 * @param hit
	 */
	private void hitElementTop(Actor hit) {
		if(hit instanceof Wasp){
			this.getActions().clear();
			this.addAction(((Wasp) hit).getActionOnCat());
		}
	}

	/**
	 * ramasse les coins et les bones
	 * @param hit
	 */
	private void pickUp(Actor hit) {
		if(hit instanceof Carpet){
			this.getActions().clear();
			this.addAction(((Carpet) hit).getActionOnCat());
			state = HITTING;
		}
		if(hit instanceof Coin)
		{
			hit.setVisible(false);
			nbcoin++;
		}
		else if (hit instanceof Mouse){
			hit.setVisible(false);
		}
	}

	
	/**
	 * le bloc que l'on touche nous donne l'action à effectuer
	 * @param hit l'acteur touché
	 */
	public void setVelocity(Actor hit) {
		if(hit instanceof StopBlock)
			((StopBlock) hit).updateTimer();
		
		if(hit instanceof Block & state != FLYING){
			state = WALKING;
			if(this.getActions().size == 0)
				this.addAction(((Block) hit).getActionOnCat());
		}
		
		else if(hit instanceof Spring){
//			state = FLYING;
			if(this.getActions().size == 0)
				this.addAction(((Spring) hit).getActionOnCat());
		}
		
		else if(hit instanceof Fan){
			if(this.getActions().size == 0)
				this.addAction(((Fan) hit).getActionOnCat());
			state = FLYING;
		}
	}
	
	/**
	 * initialise un saut
	 */
	public void jump(float xDest, float yDest){
		if(state != JUMPING)
		{	
			state = JUMPING;
//			this.addAction(Actions.forever(Actions.moveTo(500, 500)));
			this.addAction(Actions.parallel(Actions.moveBy(GameConstants.BLOCK_WIDTH * 2, 0, 0.5f)));
			this.addAction(Actions.parallel(Actions.sequence(
					Actions.moveBy(0, GameConstants.BLOCK_HEIGHT, 0.25f, Interpolation.pow2Out),
					Actions.moveBy(0, - GameConstants.BLOCK_HEIGHT, 0.25f, Interpolation.pow2In)
//					new Action() {
//						
//						@Override
//						public boolean act(float delta) {
//							jumpAnimation.apply(skeleton, -time, true);
//							return true;
//						}
//					}
		)));
//			this.addAction(Actions.sequence(
//					Actions.moveTo(xDest * GameConstants.BLOCK_WIDTH, yDest * GameConstants.BLOCK_HEIGHT, 1),
//					new Action() {
//						@Override
//						public boolean act(float delta) {
//							jumping = false;
//							return true;
//						}}
//					));
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
	
//	@Override
//	public void draw(SpriteBatch batch, float deltaParent){
////		if(state == JUMPING)
////			jumpAnimation.apply(skeleton, time, true);
//		
//		 if(state == FLYING)
//			fanAnimation.apply(skeleton, time, true);
//		
//		else if(state == HITTING)
//			contactAnimation.apply(skeleton, time, true);
//		
//		else if(state == WALKING)
//			walkAnimation.apply(skeleton, time, true);
//		 
//		else if(state == FALLING)
//			fanAnimation.apply(skeleton, time, true);
//	
//		time += Gdx.graphics.getDeltaTime();
//		skeleton.updateWorldTransform();
////		skeleton.update(time);
//		skeleton.draw(batch);
//	}
}
