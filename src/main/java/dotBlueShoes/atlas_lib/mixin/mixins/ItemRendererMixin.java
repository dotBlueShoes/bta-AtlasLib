package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose
/// - Renders item in player hand.
///

import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow(remap = false)
	private RenderBlocks renderBlocksInstance;
	@Shadow(remap = false)
	private Minecraft mc;

	@Inject(
		method = "renderItem(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/item/ItemStack;Z)V",
		at = @At(
			value = "HEAD"
		),
		cancellable = true, remap = false
	)
	public void renderItem(Entity entity, ItemStack itemstack, boolean handheldTransform, CallbackInfo ci) {
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


			int iconIndex = itemstack.getItem().getIconIndex(itemstack);
			if (entity instanceof EntityLiving) {
				iconIndex = ((EntityLiving)entity).getItemIcon(itemstack);
			}

			int tileWidth;
			float xo;
			float xe;
			float yo;
			float ye;
			float foon;
			float goon;

			if (itemstack.itemID < Block.blocksList.length) {
				//dotBlueShoes.GemsMod.Global.LOGGER.info("id:" + itemstack.itemID);
				//if (Block.blocksList[itemstack.itemID] instanceof AtlasSpriteBlock) {
				//	dotBlueShoes.GemsMod.Global.LOGGER.info("yup");
				//}
				GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
				tileWidth = TextureFX.tileWidthTerrain;

				xo = ((float)(iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + 0.0f) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
				xe = ((float)(iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + ((float)tileWidth - 0.01f)) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
				yo = ((float)(iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + 0.0f) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
				ye = ((float)(iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + ((float)tileWidth - 0.01f)) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
				foon = 0.5f / (float)tileWidth / (float)Global.TEXTURE_ATLAS_WIDTH_TILES;
				goon = 0.0625f * (16.0f / (float)tileWidth);

			} else {
				if (itemstack.getItem() instanceof ISpriteAtlasItem) {
					ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemstack.getItem();

					GL11.glBindTexture(3553, SpriteAtlasHelper.getCustomTexture(this.mc.renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));
					tileWidth = spriteAtlasItem.getSpriteAtlas().resolution;

					xo  = ((float) (spriteAtlasItem.getSpriteIndex() % spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.x);
					xe  = xo + (1f / spriteAtlasItem.getSpriteAtlas().elements.x);
					yo = ((float) (spriteAtlasItem.getSpriteIndex() / spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.y);
					ye = yo + (1f / spriteAtlasItem.getSpriteAtlas().elements.y);

					foon = 0.5f / (float)tileWidth / (float)spriteAtlasItem.getSpriteAtlas().elements.x;
					goon = 0.0625f * (16.0f / (float)tileWidth);

				} else {
					GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/items.png"));
					tileWidth = TextureFX.tileWidthItems;

					xo = ((float)(iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + 0.0f) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
					xe = ((float)(iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + ((float)tileWidth - 0.01f)) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
					yo = ((float)(iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + 0.0f) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
					ye = ((float)(iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) + ((float)tileWidth - 0.01f)) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
					foon = 0.5f / (float)tileWidth / (float)Global.TEXTURE_ATLAS_WIDTH_TILES;
					goon = 0.0625f * (16.0f / (float)tileWidth);
				}
			}

			Tessellator tessellator = Tessellator.instance;
			float f4 = 1.0f;
			float f5 = 0.0f;
			float f6 = 0.3f;



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

			for (int j = 0; j < tileWidth; ++j) {
				float f9 = (float)j / (float)tileWidth;
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

			for (int k = 0; k < tileWidth; ++k) {
				float f10 = (float)k / (float)tileWidth;
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

			for (int l = 0; l < tileWidth; ++l) {
				float f11 = (float)l / (float)tileWidth;
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

			for (int i1 = 0; i1 < tileWidth; ++i1) {
				float f12 = (float)i1 / (float)tileWidth;
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

		GL11.glPopMatrix();

		// We're overriding a whole method this way.
		ci.cancel();
	}

}
