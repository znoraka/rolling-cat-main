
package fr.lirmm.smile.rollingcat.spine.attachments;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import fr.lirmm.smile.rollingcat.spine.Attachment;
import fr.lirmm.smile.rollingcat.spine.AttachmentResolver;

public class TextureAtlasAttachmentResolver implements AttachmentResolver {
	private TextureAtlas atlas;

	public TextureAtlasAttachmentResolver (TextureAtlas atlas) {
		if (atlas == null) throw new IllegalArgumentException("atlas cannot be null.");
		this.atlas = atlas;
	}

	public void resolve (Attachment attachment) {
		if (attachment instanceof RegionAttachment) {
			AtlasRegion region = atlas.findRegion(attachment.getName());
			if (region == null) throw new RuntimeException("Region not found in atlas: " + attachment);
			((RegionAttachment)attachment).setRegion(region);
			attachment.setResolved(true);
			return;
		}

		throw new IllegalArgumentException("Unable to resolve attachment of type: " + attachment.getClass().getName());
	}
}