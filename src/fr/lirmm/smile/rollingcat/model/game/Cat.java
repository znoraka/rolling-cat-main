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
import fr.lirmm.smile.rollingcat.utils.LevelBuilder;

public class Cat extends Entity {


	public static final int FALLING = 0;
	public static final int FLYING = 1;
	public static final int JUMPING = 2;
	public static final int HITTING = 3;
	public static final int WALKING = 4;
	public static final int TELEPORTING = 5;
	
	public static Cat instance;

	/**
	 * état du chat
	 */
	private int state;

	/**
	 * nombre de coin de chaque type que le chat a
	 */
	private int gold, silver, bronze;

	/**
	 * ancienne position en x pour savoir si le chat est immobile
	 */
	private int oldX;

	/**
	 * {@link Rectangle} pour les collisions de chaque coté du chat
	 */
	private Rectangle top, right, bottom, left;

	/**
	 * pour les animations avec Spine
	 */
	private SkeletonData skeletonData;
	private Skeleton skeleton;
	private Bone root;
	private Animation walkAnimation, contactAnimation, fanAnimation;
	private float time;

	/**
	 * variable de Cédric utilité ???
	 */
	private boolean hasCatchedCoin;

	/**
	 * vrai si le chat réussi la scene et faux si le chat tombe
	 */
	private boolean success;

	/**
	 * variable pour savoir si le chat a touché la cible finale
	 */
	private boolean done;

	/**
	 * variable globale pour les collisions
	 */
	private Actor actor;

	/**
	 * variable globale pour le téléport entre les portes
	 */
	private Door door;
	
	/**
	 * etat du succes, de -{@link GameConstants#SUCCESS} à {@link GameConstants#SUCCESS}
	 */
	private int nbSuccess ;
	
	/**
	 * pour savoir dans quel sens vas le chat : 1 = vers la droite, -1 = vers la gauche
	 */
	private int sens;
	

	public static void create(float x, float y, int sens)
	{
		instance = new Cat(x, y, sens);
	}
	
	public static Cat getInstance()
	{
		return instance;
	}
	
	/**
	 * l'acteur principal
	 * @param x
	 * @param y
	 */
	private Cat(float x, float y, int sens){
		super(x + sens, y, GameConstants.TEXTURE_CAT);
		done = false;
		this.setTouchable(Touchable.disabled);
		final String name = "cat-skeleton";
		success = true;
		this.sens = sens;

		TextureAtlas atlas = getCatAtlas();
		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/cat/" + name + ".skel"));


		walkAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-walk.anim"), skeletonData);
		fanAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-upstreamwinp.anim"), skeletonData);
		contactAnimation = binary.readAnimation(Gdx.files.internal("data/cat/" + name + "-contact.anim"), skeletonData);



		skeleton = new Skeleton(skeletonData);
		try {
			skeleton.setSkin("s" + RollingCat.skin);
		} catch (Exception e) {
			Gdx.app.log(RollingCat.LOG, "skin + " + RollingCat.skin + "not found");
			skeleton.setSkin("s1");
		}
		skeleton.setToBindPose();

		root = skeleton.getRootBone();
		root.setScaleX(0.10f * GameConstants.SCALE);
		root.setScaleY(0.10f * GameConstants.SCALE);
		skeleton.updateWorldTransform();

		top = new Rectangle();
		bottom = new Rectangle();
		right = new Rectangle();
		left = new Rectangle();
		
		nbSuccess  = 0;
	}

	/**
	 * methode pour gerer le déplacement
	 * change l'état du chat selon l'{@link Entity} en contact
	 * @param stage
	 */
	public void move(Stage stage){
		hasCatchedCoin = false;
		
		top.set(this.getX() + GameConstants.BLOCK_WIDTH *0.5f, this.getY() + GameConstants.BLOCK_HEIGHT, 2, 2);
		bottom.set(this.getX() + GameConstants.BLOCK_WIDTH / 2, this.getY(), 2, 2);
		right.set(this.getX() + ((sens == 1)?GameConstants.BLOCK_WIDTH:0), this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		left.set(this.getX() + ((sens == -1)?GameConstants.BLOCK_WIDTH:0), this.getY() + GameConstants.BLOCK_HEIGHT / 2, 2, 2);
		this.bounds.set(this.getX() + GameConstants.BLOCK_WIDTH / 4, this.getY() + GameConstants.BLOCK_HEIGHT / 4, this.getWidth() / 2, this.getHeight() / 2);


		if(state != TELEPORTING){

			if(state != FLYING & state != JUMPING)
				state = FALLING;

			for (Actor actor : stage.getActors()) {
				if(actor instanceof Entity && ((Entity) actor).getEtage() == this.getEtage() && ((Entity) actor).getSegment() == this.getSegment())
				{
					if(actor instanceof GroundBlock){
						if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible() & state != FLYING){
							state = WALKING;
							this.actor = actor;
						}
						else if(((Entity) actor).getBounds().overlaps(top) & actor.isVisible() & state == FLYING){
							this.actor = actor;
							if(this.getActions().size == 0){
								this.addAction(Actions.sequence(
										Actions.moveTo(this.getXOnGrid() * GameConstants.BLOCK_WIDTH, (this.getYOnGrid() + 3) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED * 3f, Interpolation.pow2Out),
										new Action() {

											@Override
											public boolean act(float delta) {
												getInstance().addAction(Actions.moveTo(getInstance().getXOnGrid() * GameConstants.BLOCK_WIDTH, (getInstance().getYOnGrid() - 1) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED * 2f, Interpolation.pow2In));
												state = WALKING;
												return true;
											}
										}
										));
							}
						}
					}
					if(actor instanceof Dog & state == WALKING)
						if((((Entity) actor).getBounds().overlaps(right) || ((Entity) actor).getBounds().overlaps(bounds)) & actor.isVisible()){
							state = HITTING;
							this.actor = actor;
						}
					if(actor instanceof Wasp){
						if(this.getXOnGrid() == ((Entity) actor).getXOnGrid()){
							((Wasp) actor).declencher(true);
							this.actor = actor;
						}
						else
							((Wasp) actor).declencher(false);

						if(((Entity) actor).getBounds().overlaps(top) & actor.isVisible())
							state = FALLING;
					}
					if(actor instanceof Fan){
						if(this.getXOnGrid() == ((Entity) actor).getXOnGrid() && this.getEtage() == ((Entity) actor).getEtage() && this.getY() > actor.getY()){
							((Fan) actor).declencher(true);
						}
						else
							((Fan) actor).declencher(false);
						if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible()){
							state = FLYING;
						}
					}
					if(actor instanceof Carpet)
						if(((Entity) actor).getBounds().overlaps(bottom) & actor.isVisible()){
							state = HITTING;
							this.actor = actor;
						}
					if(actor instanceof Target){
						if(((Entity) actor).getBounds().overlaps(bounds)){
							Target.setInstance((Target) actor);
							done = true;
						}
					}

					if(actor instanceof Coin){
						if(((Entity) actor).getBounds().overlaps(bounds) & !((Coin) actor).pickedUp()){
							hasCatchedCoin = true;
							SoundManager.pickupPlay();
							if(((Coin) actor).getType() == Coin.BRONZE)
							{
								bronze++;
							}
							else if(((Coin) actor).getType() == Coin.SILVER)
							{
								silver++;
							}
							else if(((Coin) actor).getType() == Coin.GOLD)
							{
								gold++;
							}
							((Coin) actor).pickUp();
						}
					}

					/**
					 * si le chat touche une bouee et que success == true 
					 * 
					 */
					if(actor instanceof Door){
						if((((Entity) actor).getBounds().overlaps(right) & ((Door) actor).getType() == Door.LEFT))
						{
							state = TELEPORTING;
							actor.setVisible(false);
							actor.setTouchable(Touchable.disabled);
							
							if(success)
								nbSuccess ++;
							else
								nbSuccess --;

							if(nbSuccess == GameConstants.SUCCESS)
							{	
								nbSuccess = 0;
								door = this.nextDoorUp((Door) actor);
							}
							else if(nbSuccess == -GameConstants.SUCCESS)
							{	
								nbSuccess = 0;
								door = this.nextDoorDown((Door) actor);
							}
							else
								door = (Door) actor;
							

							this.addAction(
									Actions.sequence(
											Actions.delay(0.5f),
											Actions.moveTo(door.getNextX() * GameConstants.BLOCK_WIDTH, door.getNextY() * GameConstants.BLOCK_HEIGHT),
											new Action() {
												@Override
												public boolean act(float delta) {
													state = HITTING;
													success = true;
													return true;
												}
											}
											)
									);
						}


					}

					if(actor instanceof Gap){
						if(((Entity) actor).getBounds().overlaps(left)){
							this.actor = actor;
							if((! ((Gap) actor).hasGiven())){
								state = HITTING;
							}
							if(((Gap) actor).isReady()){
								((Gap) actor).setGiven();
								jump();
							}
						}
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
						this.addAction(Actions.moveTo((this.getXOnGrid() + sens) * GameConstants.BLOCK_WIDTH, (this.getYOnGrid()) * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED));
					}

					else if(state == HITTING){
						this.clearActions();
					}
				}
			}

		}
		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.6f);
		root.setY(getY());
		oldX = this.getXOnGrid();
	}

	/**
	 * retourne la porte au meme emplacement un étage au dessus ou la porte meme si on est au dernier étage
	 * @param door
	 * @return Door
	 */
	private Door nextDoorUp(Door door) {
		if(this.getSegment() < LevelBuilder.getNumberOfSegment())
			return door.getNextDoor((this.getEtage() == LevelBuilder.getNumberOfEtage() - 1)?0:(int) (GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT));
		else
			return door;
	}

	/**
	 * retourne la porte au meme emplacement un étage en dessous ou la porte meme si on est au premier étage
	 * @param door
	 * @return Door
	 */
	private Door nextDoorDown(Door door) {
		if(this.getSegment() < LevelBuilder.getNumberOfSegment())
			return door.getNextDoor((this.getEtage() == 0)?0:(int) (- GameConstants.DECALAGE * GameConstants.BLOCK_HEIGHT));
		else
			return door;
	}

	public boolean hasCatchedCoin()
	{
		return hasCatchedCoin;
	}

	/**
	 * initialise un saut
	 */
	public void jump(){

		if(state != JUMPING){
			state = JUMPING;
			this.getActions().clear();
			SoundManager.jumpPlay();
			final float xDest = (this.getXOnGrid() + 2 * sens) * GameConstants.BLOCK_WIDTH;
			final float yDest = this.getYOnGrid() * GameConstants.BLOCK_HEIGHT;
			this.addAction(Actions.parallel(Actions.moveBy(GameConstants.BLOCK_WIDTH * 2 * sens, 0, GameConstants.SPEED * 2)));
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

		else if(state == TELEPORTING)
			fanAnimation.apply(skeleton, time, true);

		time += Gdx.graphics.getDeltaTime();
		skeleton.setFlipX((sens == 1)?false:true);
		skeleton.updateWorldTransform();
		skeleton.update(time);
		try {
			
			skeleton.draw(batch);
		} catch (Exception e) {
			// TODO: handle exception
		}
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
	 * @return true si le vidage est requis, càd si le chat est tombé (success == false)
	 */
	public boolean requestBoxEmptiing(){
		return !success;
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
	 * @return the current state
	 */
	public int getState()
	{
		return state;
	}
	/**
	 * 
	 * @return true si l'ancienne position du chat est la meme que la nouvelle
	 */
	public boolean movedX(){
		return oldX != getXOnGrid();
	}


	public void setSuccess(boolean success){
		this.success = success;
	}

	/**
	 * 
	 * @return le dernier acteur touché par le chat
	 */
	public Actor getLastActorHit() {
		return actor;
	}
	
	/**
	 * 
	 * @return l'état du success/echec du chat
	 */
	public int getSuccessState() {
		return nbSuccess;
	}
	
	public int getSens()
	{
		return sens;
	}
}
