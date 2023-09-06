package dotBlueShoes.atlas_lib.mixin.mixins.prismatic;

import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasItem;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import useless.prismaticlibe.IColored;

@Mixin( value = {ItemRenderer.class}, remap = false, priority = 1010)
public abstract class ItemRendererMixin {

	@Shadow
	private Minecraft mc;
	@Shadow
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	@Unique
	public void drawItem(int[] ints, float[] floats, boolean handheldTransform) {

		final int atlasResolution = ints[0];
		final float xo    = floats[0];
		final float xe    = floats[1];
		final float yo    = floats[2];
		final float ye    = floats[3];
		final float foon  = floats[4];
		final float goon  = floats[5];
		final float thickness = 0.0625f;
		final float one     = 1.0f;
		final float zero    = 0.0f;
		final float third   = 0.3f;

		Tessellator tessellator = Tessellator.instance;

		GL11.glEnable(32826);

		// I've drastically reduced .draw calls this might be an issue
		//  with the use of high-definition textures.

		if (handheldTransform) {
			final float scale = 1.5f;
			GL11.glTranslatef(-zero, -third, 0.0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glRotatef(50.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(335.0f, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
		}

		{ // front and back
			tessellator.startDrawingQuads();

			tessellator.setNormal(0.0f, 0.0f, 1.0f);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, xe, ye);
			tessellator.addVertexWithUV(one, 0.0, 0.0, xo, ye);
			tessellator.addVertexWithUV(one, 1.0, 0.0, xo, yo);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0, xe, yo);

			tessellator.setNormal(0.0f, 0.0f, -1.0f);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0f - thickness, xe, yo);
			tessellator.addVertexWithUV(one, 1.0, 0.0f - thickness, xo, yo);
			tessellator.addVertexWithUV(one, 0.0, 0.0f - thickness, xo, ye);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0f - thickness, xe, ye);

			tessellator.draw();
		}

		tessellator.startDrawingQuads();


		for (int i = 0; i < atlasResolution; ++i) {
			final float normalized = (float)i / (float)atlasResolution;
			final float u = xe + (xo - xe) * normalized - foon;
			final float v = ye + (yo - ye) * normalized - foon;
			final float x1 = one * normalized;
			final float x2 = one * normalized + goon;
			final float y1 = one * normalized;
			final float y2 = one * normalized + goon;

			tessellator.setNormal(-1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x1, 0.0, 0.0f - thickness, u, ye);
			tessellator.addVertexWithUV(x1, 0.0, 0.0, u, ye);
			tessellator.addVertexWithUV(x1, 1.0, 0.0, u, yo);
			tessellator.addVertexWithUV(x1, 1.0, 0.0f - thickness, u, yo);

			tessellator.setNormal(1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x2, 1.0, 0.0f - thickness, u, yo);
			tessellator.addVertexWithUV(x2, 1.0, 0.0, u, yo);
			tessellator.addVertexWithUV(x2, 0.0, 0.0, u, ye);
			tessellator.addVertexWithUV(x2, 0.0, 0.0f - thickness, u, ye);

			tessellator.setNormal(0.0f, -1.0f, 0.0f);

			tessellator.addVertexWithUV(one, y1, 0.0, xo, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0, xe, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0f - thickness, xe, v);
			tessellator.addVertexWithUV(one, y1, 0.0f - thickness, xo, v);

			tessellator.setNormal(0.0f, 1.0f, 0.0f);

			tessellator.addVertexWithUV(0.0, y2, 0.0, xe, v);
			tessellator.addVertexWithUV(one, y2, 0.0, xo, v);
			tessellator.addVertexWithUV(one, y2, 0.0f - thickness, xo, v);
			tessellator.addVertexWithUV(0.0, y2, 0.0f - thickness, xe, v);
		}

		tessellator.draw();

		GL11.glDisable(32826);
	}

	@Unique
	public void drawColoredItem(int[] ints, float[] floats, boolean handheldTransform, int colorB, int colorO) {

		final int atlasResolution = ints[0];

		final float xob    = floats[0];
		final float xeb    = floats[1];
		final float yob    = floats[2];
		final float yeb    = floats[3];
		final float xoo    = floats[6];
		final float xeo    = floats[7];
		final float yoo    = floats[8];
		final float yeo    = floats[9];

		final float foon  = floats[4];
		final float goon  = floats[5];

		float rcb = (float)(colorB >> 16 & 255) / 255.0F;
		float gcb = (float)(colorB >> 8 & 255) / 255.0F;
		float bcb = (float)(colorB & 255) / 255.0F;
		float rco = (float)(colorO >> 16 & 255) / 255.0F;
		float gco = (float)(colorO >> 8 & 255) / 255.0F;
		float bco = (float)(colorO & 255) / 255.0F;

		final float thickness = 0.0625f;
		final float one     = 1.0f;
		final float zero    = 0.0f;
		final float third   = 0.3f;

		Tessellator tessellator = Tessellator.instance;

		GL11.glEnable(32826);

		// I've drastically reduced .draw calls this might be an issue
		//  with the use of high-definition textures.

		if (handheldTransform) {
			final float scale = 1.5f;
			GL11.glTranslatef(-zero, -third, 0.0f);
			GL11.glScalef(scale, scale, scale);
			GL11.glRotatef(50.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(335.0f, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
		}

		{ // front and back base
			tessellator.startDrawingQuads();

			tessellator.setColorOpaque_F(rcb, gcb, bcb);

			tessellator.setNormal(0.0f, 0.0f, 1.0f);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, xeb, yeb);
			tessellator.addVertexWithUV(one, 0.0, 0.0, xob, yeb);
			tessellator.addVertexWithUV(one, 1.0, 0.0, xob, yob);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0, xeb, yob);

			tessellator.setNormal(0.0f, 0.0f, -1.0f);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0f - thickness, xeb, yob);
			tessellator.addVertexWithUV(one, 1.0, 0.0f - thickness, xob, yob);
			tessellator.addVertexWithUV(one, 0.0, 0.0f - thickness, xob, yeb);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0f - thickness, xeb, yeb);

			tessellator.setColorOpaque_F(rco, gco, bco);

			tessellator.setNormal(0.0f, 0.0f, 1.0f);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, xeo, yeo);
			tessellator.addVertexWithUV(one, 0.0, 0.0, xoo, yeo);
			tessellator.addVertexWithUV(one, 1.0, 0.0, xoo, yoo);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0, xeo, yoo);

			tessellator.setNormal(0.0f, 0.0f, -1.0f);
			tessellator.addVertexWithUV(0.0, 1.0, 0.0f - thickness, xeo, yoo);
			tessellator.addVertexWithUV(one, 1.0, 0.0f - thickness, xoo, yoo);
			tessellator.addVertexWithUV(one, 0.0, 0.0f - thickness, xoo, yeo);
			tessellator.addVertexWithUV(0.0, 0.0, 0.0f - thickness, xeo, yeo);

			tessellator.draw();
		}

		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_F(rcb, gcb, bcb);

		for (int i = 0; i < atlasResolution; ++i) {
			final float normalized = (float)i / (float)atlasResolution;
			final float u = xeb + (xob - xeb) * normalized - foon;
			final float v = yeb + (yob - yeb) * normalized - foon;
			final float x1 = one * normalized;
			final float x2 = one * normalized + goon;
			final float y1 = one * normalized;
			final float y2 = one * normalized + goon;

			tessellator.setNormal(-1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x1, 0.0, 0.0f - thickness, u, yeb);
			tessellator.addVertexWithUV(x1, 0.0, 0.0, u, yeb);
			tessellator.addVertexWithUV(x1, 1.0, 0.0, u, yob);
			tessellator.addVertexWithUV(x1, 1.0, 0.0f - thickness, u, yob);

			tessellator.setNormal(1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x2, 1.0, 0.0f - thickness, u, yob);
			tessellator.addVertexWithUV(x2, 1.0, 0.0, u, yob);
			tessellator.addVertexWithUV(x2, 0.0, 0.0, u, yeb);
			tessellator.addVertexWithUV(x2, 0.0, 0.0f - thickness, u, yeb);

			tessellator.setNormal(0.0f, -1.0f, 0.0f);

			tessellator.addVertexWithUV(one, y1, 0.0, xob, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0, xeb, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0f - thickness, xeb, v);
			tessellator.addVertexWithUV(one, y1, 0.0f - thickness, xob, v);

			tessellator.setNormal(0.0f, 1.0f, 0.0f);

			tessellator.addVertexWithUV(0.0, y2, 0.0, xeb, v);
			tessellator.addVertexWithUV(one, y2, 0.0, xob, v);
			tessellator.addVertexWithUV(one, y2, 0.0f - thickness, xob, v);
			tessellator.addVertexWithUV(0.0, y2, 0.0f - thickness, xeb, v);
		}

		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_F(rco, gco, bco);

		for (int i = 0; i < atlasResolution; ++i) {
			final float normalized = (float)i / (float)atlasResolution;
			final float u = xeo + (xoo - xeo) * normalized - foon;
			final float v = yeo + (yoo - yeo) * normalized - foon;
			final float x1 = one * normalized;
			final float x2 = one * normalized + goon;
			final float y1 = one * normalized;
			final float y2 = one * normalized + goon;

			tessellator.setNormal(-1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x1, 0.0, 0.0f - thickness, u, yeo);
			tessellator.addVertexWithUV(x1, 0.0, 0.0, u, yeo);
			tessellator.addVertexWithUV(x1, 1.0, 0.0, u, yoo);
			tessellator.addVertexWithUV(x1, 1.0, 0.0f - thickness, u, yoo);

			tessellator.setNormal(1.0f, 0.0f, 0.0f);

			tessellator.addVertexWithUV(x2, 1.0, 0.0f - thickness, u, yoo);
			tessellator.addVertexWithUV(x2, 1.0, 0.0, u, yoo);
			tessellator.addVertexWithUV(x2, 0.0, 0.0, u, yeo);
			tessellator.addVertexWithUV(x2, 0.0, 0.0f - thickness, u, yeo);

			tessellator.setNormal(0.0f, -1.0f, 0.0f);

			tessellator.addVertexWithUV(one, y1, 0.0, xoo, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0, xeo, v);
			tessellator.addVertexWithUV(0.0, y1, 0.0f - thickness, xeo, v);
			tessellator.addVertexWithUV(one, y1, 0.0f - thickness, xoo, v);

			tessellator.setNormal(0.0f, 1.0f, 0.0f);

			tessellator.addVertexWithUV(0.0, y2, 0.0, xeo, v);
			tessellator.addVertexWithUV(one, y2, 0.0, xoo, v);
			tessellator.addVertexWithUV(one, y2, 0.0f - thickness, xoo, v);
			tessellator.addVertexWithUV(0.0, y2, 0.0f - thickness, xeo, v);
		}

		tessellator.draw();

		GL11.glDisable(32826);
	}

	@Unique
	public void getColoredItem(Entity entity, ItemStack itemstack, int[] ints, float[] floats) {

			final IColored colored = (IColored)itemstack.getItem();
			//int spriteIndex = itemstack.getItem().getIconIndex(itemstack);
			//if (entity instanceof EntityLiving) {
			//	spriteIndex = ((EntityLiving) entity).getItemIcon(itemstack);
			//}

			if (itemstack.itemID < Block.blocksList.length) {
				final SpriteAtlas spriteAtlas = SpriteAtlasHelper.vanillaBlockAtlas;
				final int sbi = spriteAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
				final int soi = spriteAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);

				GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));

				ints[0] = spriteAtlas.resolution;

				floats[0] = ((float) (sbi % spriteAtlas.elements.x) / spriteAtlas.elements.x);
				floats[1] = floats[0] + (1f / spriteAtlas.elements.x);
				floats[2] = ((float) (sbi / spriteAtlas.elements.x) / spriteAtlas.elements.y);
				floats[3] = floats[2] + (1f / spriteAtlas.elements.y);
				floats[6] = ((float) (soi % spriteAtlas.elements.x) / spriteAtlas.elements.x);
				floats[7] = floats[6] + (1f / spriteAtlas.elements.x);
				floats[8] = ((float) (soi / spriteAtlas.elements.x) / spriteAtlas.elements.y);
				floats[9] = floats[8] + (1f / spriteAtlas.elements.y);

				floats[4] = 0.5f / (float) spriteAtlas.resolution / (float) spriteAtlas.elements.x;
				floats[5] = 0.0625f * (16.0f / (float) spriteAtlas.resolution);

			} else {
				if (itemstack.getItem() instanceof ISpriteAtlasItem) {
					final ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemstack.getItem();
					final SpriteAtlas spriteAtlas = spriteAtlasItem.getSpriteAtlas();
					final int sbi = spriteAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
					final int soi = spriteAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, spriteAtlas.getName()));

					ints[0] = spriteAtlas.resolution;

					floats[0] = ((float) (sbi % spriteAtlas.elements.x) / spriteAtlas.elements.x);
					floats[1] = floats[0] + (1f / spriteAtlas.elements.x);
					floats[2] = ((float) (sbi / spriteAtlas.elements.x) / spriteAtlas.elements.y);
					floats[3] = floats[2] + (1f / spriteAtlas.elements.y);
					floats[6] = ((float) (soi % spriteAtlas.elements.x) / spriteAtlas.elements.x);
					floats[7] = floats[6] + (1f / spriteAtlas.elements.x);
					floats[8] = ((float) (soi / spriteAtlas.elements.x) / spriteAtlas.elements.y);
					floats[9] = floats[8] + (1f / spriteAtlas.elements.y);

					floats[4] = 0.5f / (float) spriteAtlas.resolution / (float) spriteAtlas.elements.x;
					floats[5] = 0.0625f * (16.0f / (float) spriteAtlas.resolution);

				} else {
					final SpriteAtlas spriteAtlas = SpriteAtlasHelper.vanillaItemAtlas;
					final int sbi = spriteAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
					final int soi = spriteAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));

					ints[0] = spriteAtlas.resolution;

					floats[0] = ((float) (sbi % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[1] = floats[0] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[2] = ((float) (sbi / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					floats[3] = floats[2] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					floats[6] = ((float) (soi % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[7] = floats[6] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[8] = ((float) (soi / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					floats[9] = floats[8] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);

					floats[4] = 0.5f / (float) spriteAtlas.resolution / (float) SpriteAtlasHelper.vanillaItemAtlas.elements.x;
					floats[5] = 0.0625f * (16.0f / (float) spriteAtlas.resolution);
				}
			}
	}

	@Unique
	public void getItem(Entity entity, ItemStack itemstack, int[] ints, float[] floats) {
			int spriteIndex = itemstack.getItem().getIconIndex(itemstack);
			if (entity instanceof EntityLiving) {
				spriteIndex = ((EntityLiving) entity).getItemIcon(itemstack);
			}

			if (itemstack.itemID < Block.blocksList.length) {
				final int atlasResolution = SpriteAtlasHelper.vanillaBlockAtlas.resolution;

				GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));

				ints[0] = atlasResolution;
				floats[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
				floats[1] = floats[0] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
				floats[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
				floats[3] = floats[2] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
				floats[4] = 0.5f / (float) atlasResolution / (float) SpriteAtlasHelper.vanillaBlockAtlas.elements.x;
				floats[5] = 0.0625f * (16.0f / (float) atlasResolution);

			} else {
				if (itemstack.getItem() instanceof ISpriteAtlasItem) {
					final ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemstack.getItem();
					final SpriteAtlas spriteAtlas = spriteAtlasItem.getSpriteAtlas();
					final int atlasResolution = spriteAtlas.resolution;

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, spriteAtlas.getName()));

					ints[0] = atlasResolution;
					floats[0] = ((float) (spriteAtlasItem.getSpriteIndex() % spriteAtlas.elements.x) / spriteAtlas.elements.x);
					floats[1] = floats[0] + (1f / spriteAtlas.elements.x);
					floats[2] = ((float) (spriteAtlasItem.getSpriteIndex() / spriteAtlas.elements.x) / spriteAtlas.elements.y);
					floats[3] = floats[2] + (1f / spriteAtlas.elements.y);
					floats[4] = 0.5f / (float) atlasResolution / (float) spriteAtlas.elements.x;
					floats[5] = 0.0625f * (16.0f / (float) atlasResolution);

				} else {
					final int atlasResolution = SpriteAtlasHelper.vanillaItemAtlas.resolution;

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));

					ints[0] = atlasResolution;
					floats[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[1] = floats[0] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					floats[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					floats[3] = floats[2] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					floats[4] = 0.5f / (float) atlasResolution / (float) SpriteAtlasHelper.vanillaItemAtlas.elements.x;
					floats[5] = 0.0625f * (16.0f / (float) atlasResolution);
				}
			}
	}

	@Unique
	public boolean isBlock(int id) {
		return id < Block.blocksList.length &&
			BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[id]).shouldItemRender3d();
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void renderItem(Entity entity, ItemStack itemstack, boolean handheldTransform) {
		GL11.glPushMatrix();
		BlockModelRenderBlocks.setRenderBlocks(this.renderBlocksInstance);

		if (isBlock(itemstack.itemID)) {

			float brightness = entity.getBrightness(1.0f);
			if (this.mc.fullbright) {
				brightness = 1.0f;
			}

			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			SpriteAtlasHelper.handleBlock(Block.blocksList[itemstack.itemID]);
			GL11.glBindTexture(3553, this.mc.renderEngine.getTexture(SpriteAtlasHelper.currentAtlas.getName()));

			this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getMetadata(), brightness);
			GL11.glDisable(3042);

		} else {

			float[] floats = new float[7 + 4];
			int[] ints = new int[1];

			if (itemstack.getItem() instanceof IColored) {
				final IColored colored = (IColored) itemstack.getItem();
				final int colorB = colored.baseColor();
				final int colorO = colored.overlayColor();

				this.getColoredItem(entity, itemstack, ints, floats);
				this.drawColoredItem(ints, floats, handheldTransform, colorB, colorO);
			} else {
				this.getItem(entity, itemstack, ints, floats);
				this.drawItem(ints, floats, handheldTransform);
			}

		}

		GL11.glPopMatrix();
	}

}
