package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class Cat extends Entity {
	
	private boolean jumping;
	private int nbcoin;
	private boolean holdingBone;
	private boolean done;
	
	SkeletonData skeletonData;
	Skeleton skeleton;
	Animation animation;
	float time;
	
	/**
	 * l'acteur principal
	 * @param x
	 * @param y
	 */
	public Cat(float x, float y){
		super(x,y, GameConstants.TEXTURE_CAT);
		jumping = false;
		nbcoin = 0;
		holdingBone = false;
		done = false;
//		this.setTouchable(Touchable.disabled);
		
		final String name = "goblins";

		// A regular texture atlas would normally usually be used. This returns a white image for images not found in the atlas.
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/" + name + ".atlas"));


		if (true) {
			SkeletonJson json = new SkeletonJson(atlas);
			// json.setScale(2);
			skeletonData = json.readSkeletonData(Gdx.files.internal("data/" + name + "-skeleton.json"));
			animation = json.readAnimation(Gdx.files.internal("data/" + name + "-walk.json"), skeletonData);
		} else {
			SkeletonBinary binary = new SkeletonBinary(atlas);
			// binary.setScale(2);
			skeletonData = binary.readSkeletonData(Gdx.files.internal("data/" + name + ".skel"));
			animation = binary.readAnimation(Gdx.files.internal("data/" + name + "-walk.anim"), skeletonData);
		}

		skeleton = new Skeleton(skeletonData);
		if (name.equals("goblins")) skeleton.setSkin("goblin");
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.scaleX = 0.2f;
		root.scaleY = 0.2f;
		skeleton.updateWorldTransform();
	}
	
	/**
	 * methode pour gerer le déplacement
	 * @param stage
	 */
	public void move(Stage stage){
        setVelocity(stage.hit(this.getX() + 5 + GameConstants.BLOCK_WIDTH, this.getY() - 5, false));
        falling(stage.hit(this.getX() + 5, this.getY() - 5, false));
        pickUp(stage.hit(this.getX(), this.getY()+ 5f, false));
        hitElementRight(stage.hit(this.getX() + GameConstants.BLOCK_WIDTH, this.getY()+ 5f, false));
        hitElementTop(stage.hit(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT, false));
        
        Bone root = skeleton.getRootBone();
		root.setX(getX() + 16);
		root.setY(getY());
	}
	
	
	private void falling(Actor hit) {
		//si le chat n'a rien en dessous de lui
		if(!(hit instanceof Block) && this.getActions().size == 0){
			this.getActions().removeAll(this.getActions(), true);
			this.addAction(Actions.moveBy(0, - GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
		}

	}

	/**
	 * vérifie dans quoi le chat tape à droite
	 * @param hit
	 */
	private void hitElementRight(Actor hit) {
		if(hit instanceof Dog){
			this.getActions().removeAll(this.getActions(), true);
		}
		
		if(hit instanceof Target){
			done = true;
		}
	}
	
	/**
	 * vérifie dans quoi le chat tape en haut
	 * @param hit
	 */
	private void hitElementTop(Actor hit) {
		if(hit instanceof Wasp){
			this.getActions().removeAll(this.getActions(), true);
			this.addAction(((Wasp) hit).getActionOnCat());
		}
	}

	/**
	 * ramasse les coins et les bones
	 * @param hit
	 */
	private void pickUp(Actor hit) {
		if(hit instanceof Coin)
		{
			hit.setVisible(false);
			nbcoin++;
			Gdx.app.log(RollingCat.LOG, "coin picked up");
		}
		else if(hit instanceof Bone_Dog)
		{
			hit.setVisible(false);
			holdingBone = true;
			Gdx.app.log(RollingCat.LOG, "bone picked up");
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
		
		if(hit instanceof Block){
			if(this.getActions().size == 0)
				this.addAction(((Block) hit).getActionOnCat());
		}
		
		else if(hit instanceof Spring){
			if(this.getActions().size == 0)
				this.addAction(((Spring) hit).getActionOnCat());
		}
		
		else if(hit instanceof Fan){
			if(this.getActions().size == 0)
				this.addAction(((Fan) hit).getActionOnCat());
		}
	}
	
	/**
	 * initialise un saut
	 */
	public void jump(float xDest, float yDest){
		System.out.println("trying to jump...");
		if(!jumping)
		{	
			System.out.println("jumping !");
			jumping = true;
			this.addAction(Actions.parallel(Actions.moveBy(GameConstants.BLOCK_WIDTH * 2, 0, 0.5f)));
			this.addAction(Actions.parallel(Actions.sequence(
					Actions.moveBy(0, GameConstants.BLOCK_HEIGHT, 0.25f, Interpolation.pow2Out),
					Actions.moveBy(0, - GameConstants.BLOCK_HEIGHT, 0.25f, Interpolation.pow2Out),
					new Action() {
						
						@Override
						public boolean act(float delta) {
							jumping = false;
							return true;
						}
					})));
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
		else
			System.out.println("already jumping");
	}

	public int getNbCoin() {
		return this.nbcoin;
	}

	public boolean isHoldingBone() {
		return holdingBone;
	}
	
	public void dropBone(){
		holdingBone = false;
		Gdx.app.log(RollingCat.LOG, "bone dropped");
	}
	
	/**
	 * 
	 * @return true if the cat hit the target
	 */
	public boolean isDone(){
		return this.done;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(this.getActions().size == 0)
			time = 0;
		else
			time += Gdx.graphics.getDeltaTime() * 5;
		
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		skeleton.update(Gdx.graphics.getDeltaTime());
		skeleton.draw(batch);



	}
}
