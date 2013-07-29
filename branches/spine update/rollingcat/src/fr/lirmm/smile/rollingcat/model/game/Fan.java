package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getFanAtlas;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.manager.SoundManager;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;

public class Fan extends Entity {

	private SkeletonData skeletonData;
	private Skeleton skeleton;
	private Animation animation;
	private float time;
	private boolean declenche;
	private float rotationSpeed;
	private boolean soundPlayed;

	/**
	 * le ventilateur fait deux blocs de large, le chat regarde le bloc en bas à droite de lui lors de ses déplacements
	 * il faut donc que le chat puisse taper le ventilateur lors d'aller retours verticaux
	 * @param x
	 * @param y
	 */
	public Fan(float x, float y) {
		super(x, y, "fan");
		this.setWidth(GameConstants.BLOCK_WIDTH * 2);
		this.setTouchable(Touchable.disabled);

		TextureAtlas atlas = getFanAtlas();
		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/fan/ventilo-skeleton.skel"));
		animation = binary.readAnimation(Gdx.files.internal("data/fan/fan-run.anim"), skeletonData);

		skeleton = new Skeleton(skeletonData);
		try {
			skeleton.setSkin("s" + RollingCat.skin);
		} catch (Exception e) {
			Gdx.app.log(RollingCat.LOG, "skin + " + RollingCat.skin + "not found");
			skeleton.setSkin("s1");
		}
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.setScaleX(0.15f * GameConstants.SCALE);
		root.setScaleY(0.15f * GameConstants.SCALE);
		root.setX(getX() + GameConstants.BLOCK_WIDTH * 0.5f);
		root.setY(getY() - GameConstants.BLOCK_HEIGHT * 0.4f);

		time = new Random().nextFloat();
		declenche = false;
		rotationSpeed = 0;
		soundPlayed = false;
	}

	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(this.getEtage() == Cat.getInstance().getEtage() && this.getSegment() == Cat.getInstance().getSegment())
		{
			if(!declenche){
				if(rotationSpeed > 0)
					rotationSpeed -= deltaParent * (rotationSpeed * 0.03f);
			}
			time += Gdx.graphics.getDeltaTime() * rotationSpeed;
			animation.apply(skeleton, time, true);
			skeleton.updateWorldTransform();
			skeleton.update(Gdx.graphics.getDeltaTime());
			skeleton.draw(batch);
		}
	}

	public void declencher(boolean b){
		if(b){
			if(!soundPlayed){
				soundPlayed = true;
				SoundManager.fanPlay();
			}
			rotationSpeed = (float) (Math.PI);
		}
		declenche = b;
	}

	//	/**
	//	 * le chemin assistance est en haut et le chemin challenge est en bas
	//	 * si le chat est en bas (y < {@link GameConstants#VIEWPORT_HEIGHT} alors il est en mode challenge
	//	 * @return le mode
	//	 */
	//	public String getMode(){
	//		return (this.getY() < GameConstants.VIEWPORT_HEIGHT * 1.5f)?GameConstants.CHALLENGE:GameConstants.ASSISTANCE;
	//	}

}
