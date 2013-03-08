package fr.lirmm.smile.rollingcat.model.game;

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
import fr.lirmm.smile.rollingcat.spine.SkeletonData;
import fr.lirmm.smile.rollingcat.spine.SkeletonJson;

public class Dog extends Entity {
	
	
	SkeletonData skeletonData;
	Skeleton skeleton;
	Animation animation;
	float time;
	
	/**
	 * Touchable enabled
	 * le chien arrete le chat lorsqu'ils sont en contact
	 * @param x
	 * @param y
	 */
	public Dog(float x, float y){
		super(x, y, GameConstants.TEXTURE_DOG);
		
		final String name = "skeleton";
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/dog-atlas.atlas"));

		SkeletonJson json = new SkeletonJson(atlas);
		// json.setScale(2);
		skeletonData = json.readSkeletonData(Gdx.files.internal("data/" + name + "-skeleton.json"));
		animation = json.readAnimation(Gdx.files.internal("data/" + name + "-stand.json"), skeletonData);

		skeleton = new Skeleton(skeletonData);
//		if (name.equals("skeleton")) skeleton.setSkin("skeleton");
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
	public Action getActionOnCat(){
		return Actions.moveTo(this.getX() - GameConstants.BLOCK_WIDTH, this.getY());
		
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		time += Gdx.graphics.getDeltaTime();
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		skeleton.update(Gdx.graphics.getDeltaTime());
		skeleton.draw(batch);
	}
	
}
