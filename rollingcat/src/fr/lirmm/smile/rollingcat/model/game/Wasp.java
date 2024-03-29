package fr.lirmm.smile.rollingcat.model.game;

import static fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter.getWaspAtlas;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;
import fr.lirmm.smile.rollingcat.spine.Animation;
import fr.lirmm.smile.rollingcat.spine.Bone;
import fr.lirmm.smile.rollingcat.spine.Skeleton;
import fr.lirmm.smile.rollingcat.spine.SkeletonBinary;
import fr.lirmm.smile.rollingcat.spine.SkeletonData;

public class Wasp extends Entity {

	/**
	 * Touchable enabled
	 * bloque le passage du chat
	 * @param x
	 * @param y
	 */

	private SkeletonData skeletonData;
	private Skeleton skeleton;
	private Animation animation;
	private float time;
	private boolean declenche;

	public Wasp(float x, float y) {
		super(x, y, GameConstants.TEXTURE_WASP);

		TextureAtlas atlas = getWaspAtlas();

		SkeletonBinary binary = new SkeletonBinary(atlas);
		skeletonData = binary.readSkeletonData(Gdx.files.internal("data/wasp/guepe-skeleton.skel"));
		animation = binary.readAnimation(Gdx.files.internal("data/wasp/wasp-fly.anim"), skeletonData);

		skeleton = new Skeleton(skeletonData);
		try {
			skeleton.setSkin("s" + RollingCat.skin);
		} catch (Exception e) {
			skeleton.setSkin("s1");
		}
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.setScaleX(0.20f * GameConstants.SCALE);
		root.setScaleY(0.20f * GameConstants.SCALE);
		root.setX(getX() + GameConstants.BLOCK_WIDTH *0.4f);
		root.setY(getY() - GameConstants.BLOCK_WIDTH * 0.2f);

		time = new Random().nextFloat();
	}

	@Override
	public Action getAction(){
		return Actions.visible(false);
	}

	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(this.getEtage() == Cat.getInstance().getEtage() && this.getSegment() == Cat.getInstance().getSegment())
		{
			batch.end();
			drawAllowedArea();
			batch.begin();

			highlight(batch);
			if(declenche)
				time += Gdx.graphics.getDeltaTime();
			animation.apply(skeleton, time, true);
			skeleton.updateWorldTransform();
			skeleton.update(deltaParent);
			skeleton.draw(batch);
		}
	}

	@Override
	public int getItemToAct() {
		return Box.SWATTER;
	}

	public void declencher(boolean b){
		declenche = b;
	}

}
