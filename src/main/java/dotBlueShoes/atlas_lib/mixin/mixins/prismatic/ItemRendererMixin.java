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
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin( value = {ItemRenderer.class}, remap = false)
public abstract class ItemRendererMixin {

	@Shadow
	private Minecraft mc;
	@Shadow
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	@Unique
	public void drawItem(int atlasResolution, float[] params, boolean handheldTransform) {

		float xo    = params[0];
		float xe    = params[1];
		float yo    = params[2];
		float ye    = params[3];
		float foon  = params[4];
		float goon  = params[5];

		Tessellator tessellator = Tessellator.instance;

		final float f4 = 1.0f;
		final float f5 = 0.0f;
		final float f6 = 0.3f;

		GL11.glEnable(32826);

		if (handheldTransform) {
			GL11.glTranslatef(-f5, -f6, 0.0f);
			float f7 = 1.5f;
			GL11.glScalef(f7, f7, f7);
			GL11.glRotatef(50.0f, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(335.0f, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(-0.9375f, -0.0625f, 0.0f);
		}

		float thickness = 0.0625f;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0f, 0.0f, 1.0f);
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, xe, ye);
		tessellator.addVertexWithUV(f4, 0.0, 0.0, xo, ye);
		tessellator.addVertexWithUV(f4, 1.0, 0.0, xo, yo);
		tessellator.addVertexWithUV(0.0, 1.0, 0.0, xe, yo);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0f, 0.0f, -1.0f);
		tessellator.addVertexWithUV(0.0, 1.0, 0.0f - thickness, xe, yo);
		tessellator.addVertexWithUV(f4, 1.0, 0.0f - thickness, xo, yo);
		tessellator.addVertexWithUV(f4, 0.0, 0.0f - thickness, xo, ye);
		tessellator.addVertexWithUV(0.0, 0.0, 0.0f - thickness, xe, ye);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0f, 0.0f, 0.0f);

		for (int j = 0; j < atlasResolution; ++j) {
			float f9 = (float)j / (float)atlasResolution;
			float f13 = xe + (xo - xe) * f9 - foon;
			float f17 = f4 * f9;
			tessellator.addVertexWithUV(f17, 0.0, 0.0f - thickness, f13, ye);
			tessellator.addVertexWithUV(f17, 0.0, 0.0, f13, ye);
			tessellator.addVertexWithUV(f17, 1.0, 0.0, f13, yo);
			tessellator.addVertexWithUV(f17, 1.0, 0.0f - thickness, f13, yo);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0f, 0.0f, 0.0f);

		for (int k = 0; k < atlasResolution; ++k) {
			float f10 = (float)k / (float)atlasResolution;
			float f14 = xe + (xo - xe) * f10 - foon;
			float f18 = f4 * f10 + goon;
			tessellator.addVertexWithUV(f18, 1.0, 0.0f - thickness, f14, yo);
			tessellator.addVertexWithUV(f18, 1.0, 0.0, f14, yo);
			tessellator.addVertexWithUV(f18, 0.0, 0.0, f14, ye);
			tessellator.addVertexWithUV(f18, 0.0, 0.0f - thickness, f14, ye);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0f, 1.0f, 0.0f);

		for (int l = 0; l < atlasResolution; ++l) {
			float f11 = (float)l / (float)atlasResolution;
			float f15 = ye + (yo - ye) * f11 - foon;
			float f19 = f4 * f11 + goon;
			tessellator.addVertexWithUV(0.0, f19, 0.0, xe, f15);
			tessellator.addVertexWithUV(f4, f19, 0.0, xo, f15);
			tessellator.addVertexWithUV(f4, f19, 0.0f - thickness, xo, f15);
			tessellator.addVertexWithUV(0.0, f19, 0.0f - thickness, xe, f15);
		}

		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0f, -1.0f, 0.0f);

		for (int i1 = 0; i1 < atlasResolution; ++i1) {
			float f12 = (float)i1 / (float)atlasResolution;
			float f16 = ye + (yo - ye) * f12 - foon;
			float f20 = f4 * f12;
			tessellator.addVertexWithUV(f4, f20, 0.0, xo, f16);
			tessellator.addVertexWithUV(0.0, f20, 0.0, xe, f16);
			tessellator.addVertexWithUV(0.0, f20, 0.0f - thickness, xe, f16);
			tessellator.addVertexWithUV(f4, f20, 0.0f - thickness, xo, f16);
		}

		tessellator.draw();
		GL11.glDisable(32826);
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void renderItem(Entity entity, ItemStack itemstack, boolean handheldTransform) {
		GL11.glPushMatrix();
		BlockModelRenderBlocks.setRenderBlocks(this.renderBlocksInstance);

		if (itemstack.itemID < Block.blocksList.length && BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID]).shouldItemRender3d()) {

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

			int spriteIndex = itemstack.getItem().getIconIndex(itemstack);
			if (entity instanceof EntityLiving) {
				spriteIndex = ((EntityLiving)entity).getItemIcon(itemstack);
			}

			int atlasResolution;
			float[] params = new float[6];

			if (itemstack.itemID < Block.blocksList.length) {
				GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
				atlasResolution = SpriteAtlasHelper.vanillaBlockAtlas.resolution;

				params[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
				params[1] = params[0] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
				params[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
				params[3] = params[2] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
				params[4] = 0.5f / (float)atlasResolution / (float)SpriteAtlasHelper.vanillaBlockAtlas.elements.x;
				params[5] = 0.0625f * (16.0f / (float)atlasResolution);

			} else {
				if (itemstack.getItem() instanceof ISpriteAtlasItem) {
					final ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemstack.getItem();
					final SpriteAtlas spriteAtlas = spriteAtlasItem.getSpriteAtlas();

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, spriteAtlas.getName()));
					atlasResolution = spriteAtlas.resolution;

					params[0] = ((float) (spriteAtlasItem.getSpriteIndex() % spriteAtlas.elements.x) / spriteAtlas.elements.x);
					params[1] = params[0] + (1f / spriteAtlas.elements.x);
					params[2] = ((float) (spriteAtlasItem.getSpriteIndex() / spriteAtlas.elements.x) / spriteAtlas.elements.y);
					params[3] = params[2] + (1f / spriteAtlas.elements.y);
					params[4] = 0.5f / (float)atlasResolution / (float)spriteAtlas.elements.x;
					params[5] = 0.0625f * (16.0f / (float)atlasResolution);

				} else {
					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
					atlasResolution = SpriteAtlasHelper.vanillaItemAtlas.resolution;

					params[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					params[1] = params[0] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					params[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					params[3] = params[2] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					params[4] = 0.5f / (float)atlasResolution / (float)SpriteAtlasHelper.vanillaItemAtlas.elements.x;
					params[5] = 0.0625f * (16.0f / (float)atlasResolution);
				}
			}

			//ItemRendererCommon common = (ItemRendererCommon)this;
			this.drawItem(atlasResolution, params, handheldTransform);

		}

		GL11.glPopMatrix();
	}

}
