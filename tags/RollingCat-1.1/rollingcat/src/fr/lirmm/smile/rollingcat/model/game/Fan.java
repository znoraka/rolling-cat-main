package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getFanAtlas;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
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
	private boolean played;
	
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
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/fan/fan.skel"));
		animation = binary.readAnimation(Gdx.files.internal("data/fan/fan-run.anim"), skeletonData);
		
		skeleton = new Skeleton(skeletonData);
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.setScaleX(0.15f * GameConstants.SCALE);
		root.setScaleY(0.15f * GameConstants.SCALE);
		root.setX(getX() + GameConstants.BLOCK_WIDTH * 0.5f);
		root.setY(getY() - GameConstants.BLOCK_HEIGHT * 0.4f);
		
		time = new Random().nextFloat();
		declenche = false;
		rotationSpeed = 0;
		played = false;
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
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
	
	public void declencher(boolean b){
		if(b){
			if(!played){
				played = true;
				SoundManager.fanPlay();
			}
			rotationSpeed = (float) (Math.PI * 2);
		}
		declenche = b;
	}
	
}
