package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getCarpetAtlas;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;



public class Carpet extends Entity {
	
	private SkeletonData skeletonData;
	private Skeleton skeleton;
	private Animation animation;
	private float time;

	public Carpet(float x, float y) {
		super(x, y, GameConstants.TEXTURE_CARPET);
		
		TextureAtlas atlas = getCarpetAtlas();
		
		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/carpet/carpet.skel"));
		animation = binary.readAnimation(Gdx.files.internal("data/carpet/carpet-fly.anim"), skeletonData);

		skeleton = new Skeleton(skeletonData);
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.setScaleX(0.15f * GameConstants.SCALE);
		root.setScaleY(0.15f * GameConstants.SCALE);
		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.6f);
		root.setY(getY() + GameConstants.BLOCK_HEIGHT * 0.5f);
		
		time = new Random().nextFloat();
	}
	
	@Override
	public Action getAction(){
		return Actions.visible(false);
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		time += deltaParent / 150;
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		skeleton.update(deltaParent);
		skeleton.draw(batch);
	}
}
