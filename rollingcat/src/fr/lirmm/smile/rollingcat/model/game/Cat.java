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
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.SoundManager;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;

public class Cat extends Entity {

	private boolean done, requestBoxEmptiing;

	public static final int FALLING = 0;
	public static final int FLYING = 1;
	public static final int JUMPING = 2;
	public static final int HITTING = 3;
	public static final int WALKING = 4;

	private int state;
	private int gold, silver, bronze;

	private int oldX;

	private Rectangle top, right, bottom, left;

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
		done = false;
		this.setTouchable(Touchable.disabled);
		final String name = "cat-skeleton";

		requestBoxEmptiing = false;

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
	}

	/**
	 * methode pour gerer le déplacement
	 * @param stage
	 */
	public void move(Stage stage){
		top.set(this.getX() + GameConstants.BLOCK_WIDTH *0.5f, this.getY() + GameConstants.BLOCK_HEIGHT, 2, 2);
		bottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY(), 2, 2);
		right.set(this.getX() + GameConstants.BLOCK_WIDTH, this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		left.set(this.getX(), this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		this.bounds.set(this.getX() + GameConstants.BLOCK_WIDTH / 4, this.getY() + GameConstants.BLOCK_HEIGHT / 4, this.getWidth() / 2, this.getHeight() / 2);
		if(state != FLYING & state != JUMPING)
			state = FALLING;
		for (Actor actor : stage.getActors()) {
			if(actor instanceof GroundBlock){
				if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible() & state != FLYING)
					state = WALKING;
				else if(((Entity) actor).getBounds().overlaps(top) & actor.isVisible() & state == FLYING){
					if(this.getActions().size == 0){
						this.addAction(Actions.sequence(
								Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() + 3) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED * 2),
								new Action() {

									@Override
									public boolean act(float delta) {
										state = FALLING;
										return true;
									}
								}
								));
					}
				}
			}
			if(actor instanceof Dog & state == WALKING)
				if((((Entity) actor).getBounds().overlaps(right) || ((Entity) actor).getBounds().overlaps(bounds)) & actor.isVisible())
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
				if(((Entity) actor).getBounds().overlaps(bounds) & !((Coin) actor).pickedUp()){
					SoundManager.pickupPlay();
					if(((Coin) actor).getType() == Coin.BRONZE)
						bronze++;
					else if(((Coin) actor).getType() == Coin.SILVER)
						silver++;
					else if(((Coin) actor).getType() == Coin.GOLD)
						gold++;
					((Coin) actor).pickUp();
				}
			}

			if(actor instanceof Door){
				if(((Entity) actor).getBounds().overlaps(right) & ((Door) actor).getType() == Door.LEFT){
					Gdx.app.log(RollingCat.LOG, "hitting door");
					this.getActions().clear();
					requestBoxEmptiing = true;
					this.addAction(Actions.moveTo(((Door) actor).getNextX() * GameConstants.BLOCK_WIDTH, ((Door) actor).getNextY() * GameConstants.BLOCK_HEIGHT));
				}
			}

			if(actor instanceof Gap){
				if(((Entity) actor).getBounds().overlaps(left) & actor.isVisible()){
					this.addAction(Actions.moveTo(this.getX(), this.getY()));
				}
			}
		}

		if(!this.isDone()){
			if(state != JUMPING){
				if(state == FALLING & this.getActions().size == 0){
					this.addAction(Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() - 1) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
				}

				else if(state == FLYING & this.getActions().size == 0)
					this.addAction(Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() + 1) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED * 0.75f));

				else if(state == WALKING & this.getActions().size == 0){
					this.addAction(Actions.moveTo((this.getXOnGrid() + 1) * GameConstants.BLOCK_WIDTH, (this.getYOnGrid()) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
				}

				else if(state == HITTING){
					this.getActions().clear();
				}
			}
			else
				this.getActions().clear();
		}

		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.6f);
		root.setY(getY());
		oldX = this.getXOnGrid();
	}

	/**
	 * initialise un saut
	 */
	public void jump(){
		
		this.getActions().clear();
		final float xDest = (this.getXOnGrid() + 2) * GameConstants.BLOCK_WIDTH;
		final float yDest = this.getYOnGrid() * GameConstants.BLOCK_HEIGHT;
		this.addAction(Actions.parallel(Actions.moveBy(GameConstants.BLOCK_WIDTH * 2, 0, GameConstants.SPEED * 2)));
		this.addAction(Actions.parallel(Actions.sequence(
				Actions.moveBy(0, GameConstants.BLOCK_HEIGHT, GameConstants.SPEED, Interpolation.pow2Out),
				Actions.moveBy(0, -GameConstants.BLOCK_HEIGHT, GameConstants.SPEED, Interpolation.pow2In),
				new Action() {
					@Override
					public boolean act(float delta) {
						setX(xDest);
						setY(yDest);
						state = WALKING;
						Gdx.app.log(RollingCat.LOG, "landed");
						return true;
					}
				}
				)));
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
		if(state == JUMPING)
			walkAnimation.apply(skeleton, time, true);

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

	/**
	 * dessine les zones de contact du chat
	 * @param sr
	 */
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLUE);
		sr.rect(top.x, top.y, top.width, top.height);
		sr.setColor(Color.RED);
		sr.rect(bottom.x, bottom.y, bottom.width, bottom.height);
		sr.setColor(Color.GREEN);
		sr.rect(right.x, right.y, right.width, right.height);
		sr.setColor(Color.BLACK);
		sr.end();

	}

	/**
	 * 
	 * @return le nombre de pieces de bronze
	 */
	public int getBronze() {
		return bronze;
	}

	/**
	 * 
	 * @return le nombre de pieces d'argent
	 */
	public int getSilver() {
		return silver;
	}

	/**
	 * 
	 * @return le nombre de pieces d'or
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * demande un vidage de la box après une chute
	 * @return true si le vidage est requis
	 */
	public boolean requestBoxEmptiing(){
		return requestBoxEmptiing;
	}

	/**
	 * marque la requete de vidage de la box comme traitée
	 */
	public void requestOk() {
		requestBoxEmptiing = false;
	}

	/**
	 * 
	 * @return true si le state du chat est différent de hitting
	 */
	public boolean isMoving() {
		return state != HITTING;
	}
	
	/**
	 * set le state du chat
	 * @param state
	 */
	public void setState(int state){
		this.state = state;
	}

	/**
	 * 
	 * @return true si l'ancienne position du chat est la meme que la nouvelle
	 */
	public boolean movedX(){
		return oldX != getXOnGrid();
	}
}
