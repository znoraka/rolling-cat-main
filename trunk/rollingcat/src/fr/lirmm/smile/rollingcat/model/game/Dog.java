package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getDogAtlas;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;

public class Dog extends Entity {
	
//	
	private SkeletonData skeletonData;
	private Skeleton skeleton;
	private Animation animation;
	private float time;
	
	/**
	 * Touchable enabled
	 * le chien arrete le chat lorsqu'ils sont en contact
	 * @param x
	 * @param y
	 */
	public Dog(float x, float y){
		super(x, y, GameConstants.TEXTURE_DOG);

		TextureAtlas atlas = getDogAtlas();
		
		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/dog/dog.skel"));
		animation = binary.readAnimation(Gdx.files.internal("data/dog/dog-stand.anim"), skeletonData);

		skeleton = new Skeleton(skeletonData);
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.setScaleX(0.35f * GameConstants.SCALE);
		root.setScaleY(0.35f * GameConstants.SCALE);
		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.4f);
		root.setY(getY());
		
		time = new Random().nextFloat();
	}
	
	@Override
	public Action getAction(){
		return Actions.visible(false);
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		highlight(batch);
		time += Gdx.graphics.getDeltaTime();
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		skeleton.update(Gdx.graphics.getDeltaTime());
		skeleton.draw(batch);
	}
	
	@Override
	public int getItemToAct() {
		return Box.BONE;
	}
	
}
