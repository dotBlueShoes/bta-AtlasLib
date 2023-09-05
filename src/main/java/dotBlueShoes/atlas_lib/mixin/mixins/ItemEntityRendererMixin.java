package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose:
///  - Renders items on ground.
///  - Renders items in hot-bar and drawItemIntoGui is needed for GuiRenderItem to work.
///

import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;

import java.util.Random;

import static net.minecraft.core.Global.TEXTURE_ATLAS_WIDTH_TILES;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public abstract class ItemEntityRendererMixin extends EntityRenderer<EntityItem> {

	@Shadow @Final
	private Random random;
	@Shadow @Final
	private RenderBlocks renderBlocks;
	@Shadow
	public boolean field_27004_a;
	@Shadow
	public abstract void renderTexturedQuad(int l, int i1, int i, int i2, int tileWidth, int tileWidth1);


	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void doRenderItem(EntityItem entity, double d, double d1, double d2, float f, float f1) {

		this.random.setSeed(187L);
		ItemStack itemstack = entity.item;
		Item item = itemstack.getItem();

		if (item == null) {
			return;
		}

		GL11.glPushMatrix();

		float f2 = MathHelper.sin(((float)entity.age + f1) / 10.0f + entity.field_804_d) * 0.1f + 0.1f;
		float f3 = (((float)entity.age + f1) / 20.0f + entity.field_804_d) * 57.29578f;
		int renderCount = 1;

		// the following code makes same-type blocks render as piles of that block.
		if (entity.item.stackSize > 1) {
			renderCount = 2;
		}

		if (entity.item.stackSize > 5) {
			renderCount = 3;
		}

		if (entity.item.stackSize > 20) {
			renderCount = 4;
		}

		GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
		GL11.glEnable(32826);

		if (itemstack.itemID < Block.blocksList.length && Block.blocksList[itemstack.itemID] != null && BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID]).shouldItemRender3d()) {

			GL11.glRotatef(f3, 0.0f, 1.0f, 0.0f);

			SpriteAtlasHelper.handleBlock(Block.blocksList[itemstack.itemID]);
			RenderEngine renderEngine = this.renderDispatcher.renderEngine;
			renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.currentAtlas.getName()));

			BlockModelRenderBlocks.setRenderBlocks(this.renderBlocks);
			BlockModel model = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID]);

			float itemSize = model.getItemRenderScale();
			GL11.glScalef(itemSize, itemSize, itemSize);

			for (int j = 0; j < renderCount; ++j) {
				GL11.glPushMatrix();

				if (j > 0) {
					float f5 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					float f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					GL11.glTranslatef(f5, f7, f9);
				}

				float f4 = entity.getBrightness(f1);

				if (Minecraft.getMinecraft(this).fullbright) {
					f4 = 1.0f;
				}

				this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getMetadata(), f4);
				GL11.glPopMatrix();
			}

		} else {

			RenderEngine renderEngine = this.renderDispatcher.renderEngine;
			float xo, xe, yo, ye;

			if (itemstack.itemID < Block.blocksList.length) { // Grass, mushrooms are from terrain.png.
				int spriteIndex = itemstack.getIconIndex();

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
				xo  = ((float) (spriteIndex % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				xe  = xo + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				yo = ((float) (spriteIndex / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
				ye = yo + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);

			} else {
				if (itemstack.getItem() instanceof ISpriteAtlasItem) {
					ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemstack.getItem();
					int spriteIndex = spriteAtlasItem.getSpriteIndex();

					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));
					xo  = ((float) (spriteIndex % spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.x);
					xe  = xo + (1f / spriteAtlasItem.getSpriteAtlas().elements.x);
					yo = ((float) (spriteIndex / spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.y);
					ye = yo + (1f / spriteAtlasItem.getSpriteAtlas().elements.y);

				} else {
					int spriteIndex = itemstack.getIconIndex();

					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
					xo  = ((float) (spriteIndex % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					xe  = xo + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
					yo = ((float) (spriteIndex / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
					ye = yo + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
				}

			}

			Tessellator tessellator = Tessellator.instance;

			GL11.glScalef(0.5f, 0.5f, 0.5f);

			if (this.field_27004_a) {
				int color = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getMetadata());
				float red = (float)(color >> 16 & 0xFF) / 255.0f;
				float green = (float)(color >> 8 & 0xFF) / 255.0f;
				float blue = (float)(color & 0xFF) / 255.0f;
				float brightness = entity.getBrightness(f1);

				if (Minecraft.getMinecraft(this).fullbright || entity.item.getItem().hasTag(ItemTags.renderFullbright))
					brightness = 1.0f;

				GL11.glColor4f(red * brightness, green * brightness, blue * brightness, 1.0f);
			}

			if (Minecraft.getMinecraft(this).gameSettings.items3D.value) {

				GL11.glPushMatrix();
				GL11.glScaled(1.0, 1.0, 1.0);
				GL11.glRotated(f3, 0.0, 1.0, 0.0);
				GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));

				for (int j = 0; j < renderCount; ++j) {
					GL11.glPushMatrix();
					GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
					EntityRenderDispatcher.instance.itemRenderer.renderItem(entity, itemstack, false);
					GL11.glPopMatrix();
				}

				GL11.glPopMatrix();

			} else {

				for (int l = 0; l < renderCount; ++l) {
					GL11.glPushMatrix();

					if (l > 0) {
						float f16 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
						float f18 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
						float f20 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
						GL11.glTranslatef(f16, f18, f20);
					}

					GL11.glRotatef(180.0f - this.renderDispatcher.viewLerpYaw, 0.0f, 1.0f, 0.0f);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0f, 1.0f, 0.0f);
					tessellator.addVertexWithUV(-0.5, -0.25, 0.0, xo, ye);
					tessellator.addVertexWithUV(0.5, -0.25, 0.0, xe, ye);
					tessellator.addVertexWithUV(0.5, 0.75, 0.0, xe, yo);
					tessellator.addVertexWithUV(-0.5, 0.75, 0.0, xo, yo);
					tessellator.draw();
					GL11.glPopMatrix();
				}
			}
		}

		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Unique
	private void defaultRenderer(int color, int spriteIndex, int l, int i1, float brightness, float alpha, int tileWidth, int tileHeight) {
		float red = (float)(color >> 16 & 0xFF) / 255.0f;
		float green = (float)(color >> 8 & 0xFF) / 255.0f;
		float blue = (float)(color & 0xFF) / 255.0f;

		if (this.field_27004_a)
			GL11.glColor4f(red * brightness, green * brightness, blue * brightness, alpha);
		else
			GL11.glColor4f(brightness, brightness, brightness, alpha);

		this.renderTexturedQuad(l, i1, spriteIndex % TEXTURE_ATLAS_WIDTH_TILES * tileWidth, spriteIndex / TEXTURE_ATLAS_WIDTH_TILES * tileHeight, tileWidth, tileHeight);
		GL11.glEnable(2896);
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderEngine, int itemId, int j, int spriteIndex, int x, int y, float brightness, float alpha) {
		if (itemId < Block.blocksList.length && BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemId]).shouldItemRender3d()) {

			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			SpriteAtlasHelper.handleBlock(Block.blocksList[itemId]);
			renderEngine.bindTexture(renderEngine.getTexture(SpriteAtlasHelper.currentAtlas.getName()));

			Block block = Block.blocksList[itemId];

			GL11.glPushMatrix();
			GL11.glTranslatef(x - 2, y + 3, -3.0f);
			GL11.glScalef(10.0f, 10.0f, 10.0f);
			GL11.glTranslatef(1.0f, 0.5f, 1.0f);
			GL11.glScalef(1.0f, 1.0f, -1.0f);
			GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);

			int color = Item.itemsList[itemId].getColorFromDamage(j);
			float red = (float)(color >> 16 & 0xFF) / 255.0f;
			float green = (float)(color >> 8 & 0xFF) / 255.0f;
			float blue = (float)(color & 0xFF) / 255.0f;

			if (this.field_27004_a) {
				GL11.glColor4f(red * brightness, green * brightness, blue * brightness, alpha);
			} else {
				GL11.glColor4f(brightness, brightness, brightness, alpha);
			}

			GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			this.renderBlocks.useInventoryTint = this.field_27004_a;
			this.renderBlocks.renderBlockOnInventory(block, j, brightness);
			this.renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
			GL11.glDisable(3042);

		} else if (spriteIndex >= 0) {

			GL11.glDisable(2896);

			if (itemId < Block.blocksList.length) {

				int tileWidth = TextureFX.tileWidthTerrain;
				int color = Item.itemsList[itemId].getColorFromDamage(j);

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
				defaultRenderer(color, spriteIndex, x, y, brightness, alpha, tileWidth, tileWidth);

			} else {

				if (Item.itemsList[itemId] instanceof ISpriteAtlasItem) {
					ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) Item.itemsList[itemId];

					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));

					{
						int color = ((Item)spriteAtlasItem).getColorFromDamage(j);
						float red = (float) (color >> 16 & 0xFF) / 255.0f;
						float green = (float) (color >> 8 & 0xFF) / 255.0f;
						float blue = (float) (color & 0xFF) / 255.0f;

						if (this.field_27004_a) {
							GL11.glColor4f(red * brightness, green * brightness, blue * brightness, alpha);
						} else {
							GL11.glColor4f(brightness, brightness, brightness, alpha);
						}

						float xo  = ((float) (spriteIndex % spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.x);
						float xe  = xo + (1f / spriteAtlasItem.getSpriteAtlas().elements.x);
						float yo = ((float) (spriteIndex / spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.y);
						float ye = yo + (1f / spriteAtlasItem.getSpriteAtlas().elements.y);

						Tessellator tessellator = Tessellator.instance;
						tessellator.startDrawingQuads();

						// 1->2
						//    v
						// 4<-3

						tessellator.addVertexWithUV(
							x + 0, y + 16,
							0.0,
							xo,
							ye
						);

						tessellator.addVertexWithUV(
							x + 16, y + 16,
							0.0,
							xe,
							ye
						);

						tessellator.addVertexWithUV(
							x + 16, y + 0,
							0.0,
							xe,
							yo
						);

						tessellator.addVertexWithUV(
							x + 0, y + 0,
							0.0,
							xo,
							yo
						);

						tessellator.draw();

						GL11.glEnable(2896);
					}

				} else {
					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
					int tileWidth = TextureFX.tileWidthItems;
					int color = Item.itemsList[itemId].getColorFromDamage(j);
					defaultRenderer(color, spriteIndex, x, y, brightness, alpha, tileWidth, tileWidth);
				}

			}
		}

		GL11.glEnable(2884);
	}

}
