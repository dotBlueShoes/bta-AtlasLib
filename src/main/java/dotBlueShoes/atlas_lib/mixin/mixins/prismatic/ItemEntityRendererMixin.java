package dotBlueShoes.atlas_lib.mixin.mixins.prismatic;

import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasItem;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;
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
import useless.prismaticlibe.IColored;

import java.util.Random;

@Mixin(value = ItemEntityRenderer.class, remap = false, priority = 1020)
public abstract class ItemEntityRendererMixin extends EntityRenderer<EntityItem> {

	@Shadow
	@Final
	private Random random;
	@Shadow @Final
	private RenderBlocks renderBlocks;
	@Shadow
	public boolean field_27004_a;

	@Unique
	public void drawColoredGroundItem(
		EntityItem entity, ItemStack itemStack, IColored colored,
		int[] spriteIndexes, int[] atlasElements,
		float angle, float f1,
		int renderCount
	) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glScalef(0.5f, 0.5f, 0.5f);

		if (this.field_27004_a) {
			int color = Item.itemsList[itemStack.itemID].getColorFromDamage(itemStack.getMetadata());
			float red = (float)(color >> 16 & 255) / 255.0F;
			float green = (float)(color >> 8 & 255) / 255.0F;
			float blue = (float)(color & 255) / 255.0F;
			float brightness = entity.getBrightness(f1);

			if (Minecraft.getMinecraft(this).fullbright || entity.item.getItem().hasTag(ItemTags.renderFullbright))
				brightness = 1.0F;

			GL11.glColor4f(red * brightness, green * brightness, blue * brightness, 1.0F);
		}

		if (Minecraft.getMinecraft(this).gameSettings.items3D.value) {
			GL11.glPushMatrix();
			GL11.glScaled(1.0, 1.0, 1.0);
			GL11.glRotated(angle, 0.0, 1.0, 0.0);
			GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));

			for(int j = 0; j < renderCount; ++j) {
				GL11.glPushMatrix();
				GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
				EntityRenderDispatcher.instance.itemRenderer.renderItem(entity, itemStack, false);
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
		} else {

			final int overlayTextureIndex = spriteIndexes[1];
			final int baseTextureIndex = spriteIndexes[0];
			int overlayColor = colored.overlayColor();
			int baseColor = colored.baseColor();

			float brightness = 1.0F;

			if (this.field_27004_a) {
				int color = Item.itemsList[itemStack.itemID].getColorFromDamage(itemStack.getMetadata());
				float red = (float)(color >> 16 & 255) / 255.0F;
				float green = (float)(color >> 8 & 255) / 255.0F;
				float blue = (float)(color & 255) / 255.0F;
				brightness = entity.getBrightness(f1);

				if (Minecraft.getMinecraft(this).fullbright || entity.item.getItem().hasTag(ItemTags.renderFullbright))
					brightness = 1.0F;

				GL11.glColor4f(red * brightness, green * brightness, blue * brightness, 1.0F);
			}

			for (int l = 0; l < renderCount; ++l) {
				GL11.glPushMatrix();
				if (l > 0) {
					float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(x, y, z);
				}

				GL11.glRotatef(180.0F - this.renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
				this.renderColorQuadSpace(tessellator, atlasElements[0], atlasElements[1], baseColor, brightness, baseTextureIndex);
				this.renderColorQuadSpace(tessellator, atlasElements[0], atlasElements[1], overlayColor, brightness, overlayTextureIndex);
				GL11.glPopMatrix();
			}
		}
	}

	@Unique
	public void drawVanillaGroundItem(
		EntityItem entity, ItemStack itemStack,
		float[] points,
		float angle, float f1,
		int renderCount
	) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glScalef(0.5f, 0.5f, 0.5f);

		final float xo = points[0], xe = points[1], yo = points[2], ye = points[3];

		if (this.field_27004_a) {
			int color = Item.itemsList[itemStack.itemID].getColorFromDamage(itemStack.getMetadata());
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
			GL11.glRotated(angle, 0.0, 1.0, 0.0);
			GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));

			for (int j = 0; j < renderCount; ++j) {
				GL11.glPushMatrix();
				GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
				EntityRenderDispatcher.instance.itemRenderer.renderItem(entity, itemStack, false);
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

	@Unique
	public void renderColorQuadSpace(Tessellator tessellator, int atlasElementsX, int atlasElementsY, int color, float brightness, int spriteIndex) {
		float xo = ((float) (spriteIndex % atlasElementsX) / atlasElementsX);
		float xe = xo + (1f / atlasElementsX);
		float yo = ((float) (spriteIndex / atlasElementsX) / atlasElementsY);
		float ye = yo + (1f / atlasElementsY);

		float[] baseColorRGB = new float[]{
			(float)(color >> 16 & 255) / 255.0F,
			(float)(color >> 8 & 255) / 255.0F,
			(float)(color & 255) / 255.0F
		};

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.setColorOpaque_F(baseColorRGB[0] * brightness, baseColorRGB[1] * brightness, baseColorRGB[2] * brightness);
		tessellator.addVertexWithUV(-0.5, -0.25, 0.0, xo, ye);
		tessellator.setColorOpaque_F(baseColorRGB[0] * brightness, baseColorRGB[1] * brightness, baseColorRGB[2] * brightness);
		tessellator.addVertexWithUV(0.5, -0.25, 0.0, xe, ye);
		tessellator.setColorOpaque_F(baseColorRGB[0] * brightness, baseColorRGB[1] * brightness, baseColorRGB[2] * brightness);
		tessellator.addVertexWithUV(0.5, 0.75, 0.0, xe, yo);
		tessellator.setColorOpaque_F(baseColorRGB[0] * brightness, baseColorRGB[1] * brightness, baseColorRGB[2] * brightness);
		tessellator.addVertexWithUV(-0.5, 0.75, 0.0, xo, yo);
		tessellator.draw();
	}

	@Unique
	public void calculateColoredItem(RenderEngine renderEngine, int id, IColored colored, float[] points, int[] atlasElements, int[] spriteIndexes) {
		if (id < Block.blocksList.length) { // Grass, mushrooms are from terrain.png.

			// COLORED Setup for later.
			spriteIndexes[0] = SpriteAtlasHelper.vanillaBlockAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
			spriteIndexes[1] = SpriteAtlasHelper.vanillaBlockAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);
			atlasElements[0] = SpriteAtlasHelper.vanillaBlockAtlas.elements.x;
			atlasElements[1] = SpriteAtlasHelper.vanillaBlockAtlas.elements.y;

			renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
			points[0] = ((float) (spriteIndexes[0] % SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
			points[1] = points[0] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
			points[2] = ((float) (spriteIndexes[0] / SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
			points[3] = points[2] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);

		} else {
			if (colored instanceof ISpriteAtlasItem) {

				ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) colored;
				final SpriteAtlas spriteAtlas = spriteAtlasItem.getSpriteAtlas();

				// COLORED Setup for later.
				spriteIndexes[0] = spriteAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
				spriteIndexes[1] = spriteAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);
				atlasElements[0] = spriteAtlas.elements.x;
				atlasElements[1] = spriteAtlas.elements.y;

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));
				points[0] = ((float) (spriteIndexes[0] % spriteAtlas.elements.x) / spriteAtlas.elements.x);
				points[1] = points[0] + (1f / spriteAtlasItem.getSpriteAtlas().elements.x);
				points[2] = ((float) (spriteIndexes[0] / spriteAtlas.elements.x) / spriteAtlas.elements.y);
				points[3] = points[2] + (1f / spriteAtlas.elements.y);

			} else {

				// COLORED Setup for later.
				spriteIndexes[0] = SpriteAtlasHelper.vanillaItemAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
				spriteIndexes[1] = SpriteAtlasHelper.vanillaItemAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);
				atlasElements[0] = SpriteAtlasHelper.vanillaItemAtlas.elements.x;
				atlasElements[1] = SpriteAtlasHelper.vanillaItemAtlas.elements.y;

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
				points[0] = ((float) (spriteIndexes[0] % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				points[1] = points[0] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				points[2] = ((float) (spriteIndexes[0] / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
				points[3] = points[2] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);

			}
		}
	}

	@Unique
	public void calculateItem(RenderEngine renderEngine, ItemStack itemStack, float[] points) {
		if (itemStack.itemID < Block.blocksList.length) { // Grass, mushrooms are from terrain.png.
			int spriteIndex = itemStack.getIconIndex();

			renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
			points[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
			points[1] = points[0] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.x);
			points[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaBlockAtlas.elements.x) / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);
			points[3] = points[2] + (1f / SpriteAtlasHelper.vanillaBlockAtlas.elements.y);

		} else {
			if (itemStack.getItem() instanceof ISpriteAtlasItem) {
				ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) itemStack.getItem();
				int spriteIndex = spriteAtlasItem.getSpriteIndex();

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));
				points[0] = ((float) (spriteIndex % spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.x);
				points[1] = points[0] + (1f / spriteAtlasItem.getSpriteAtlas().elements.x);
				points[2] = ((float) (spriteIndex / spriteAtlasItem.getSpriteAtlas().elements.x) / spriteAtlasItem.getSpriteAtlas().elements.y);
				points[3] = points[2] + (1f / spriteAtlasItem.getSpriteAtlas().elements.y);

			} else {
				int spriteIndex = itemStack.getIconIndex();

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
				points[0] = ((float) (spriteIndex % SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				points[1] = points[0] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.x);
				points[2] = ((float) (spriteIndex / SpriteAtlasHelper.vanillaItemAtlas.elements.x) / SpriteAtlasHelper.vanillaItemAtlas.elements.y);
				points[3] = points[2] + (1f / SpriteAtlasHelper.vanillaItemAtlas.elements.y);

			}
		}
	}

	@Unique
	public boolean isBlock(final int id) {
		return id < Block.blocksList.length &&
			//Block.blocksList[id] != null &&
			BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[id]).shouldItemRender3d();
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void doRenderItem(EntityItem entity, double d, double d1, double d2, float f, float f1) {

		this.random.setSeed(187L);
		ItemStack itemStack = entity.item;
		Item item = itemStack.getItem();

		if (item == null) {
			return;
		}

		GL11.glPushMatrix();

		float f2 = MathHelper.sin(((float)entity.age + f1) / 10.0f + entity.field_804_d) * 0.1f + 0.1f;
		float angle = (((float)entity.age + f1) / 20.0f + entity.field_804_d) * 57.29578f;
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

		if (isBlock(itemStack.itemID)) {

			GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);

			SpriteAtlasHelper.handleBlock(Block.blocksList[itemStack.itemID]);
			RenderEngine renderEngine = this.renderDispatcher.renderEngine;
			renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.currentAtlas.getName()));

			BlockModelRenderBlocks.setRenderBlocks(this.renderBlocks);
			BlockModel model = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemStack.itemID]);

			float itemSize = model.getItemRenderScale();
			GL11.glScalef(itemSize, itemSize, itemSize);

			for (int i = 0; i < renderCount; ++i) {
				GL11.glPushMatrix();

				if (i > 0) {
					float ix = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					float iy = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					float iz = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
					GL11.glTranslatef(ix, iy, iz);
				}

				float brightness = entity.getBrightness(f1);

				if (Minecraft.getMinecraft(this).fullbright) {
					brightness = 1.0f;
				}

				this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemStack.itemID], itemStack.getMetadata(), brightness);
				GL11.glPopMatrix();
			}

		} else {

			RenderEngine renderEngine = this.renderDispatcher.renderEngine;

			float[] points = new float[4];

			if (itemStack.getItem() instanceof IColored) {
				IColored colored = (IColored) item;
				int[] spriteIndexes = new int[2];
				int[] atlasElements = new int[2];

				this.calculateColoredItem(renderEngine, itemStack.itemID, colored, points, atlasElements, spriteIndexes);
				this.drawColoredGroundItem(entity, itemStack, colored, spriteIndexes, atlasElements, angle, f1, renderCount);
			} else {
				this.calculateItem(renderEngine, itemStack, points);
				this.drawVanillaGroundItem(entity, itemStack, points, angle, f1, renderCount);
			}
		}

		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}


	@Unique
	private void setColor(int color, float brightness, float alpha) {
		float red = (float)(color >> 16 & 0xFF) / 255.0f;
		float green = (float)(color >> 8 & 0xFF) / 255.0f;
		float blue = (float)(color & 0xFF) / 255.0f;

		if (this.field_27004_a)
			GL11.glColor4f(red * brightness, green * brightness, blue * brightness, alpha);
		else
			GL11.glColor4f(brightness, brightness, brightness, alpha);
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void renderItemIntoGUI(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float brightness, float alpha) {
		if (itemStack != null) {
			if (itemStack.getItem() instanceof IColored) {
				this.drawColoredItemIntoGui(renderEngine, itemStack.getItem(), itemStack.getMetadata(), x, y, brightness, alpha);
			} else {
				this.drawItemIntoGui(fontRenderer, renderEngine, itemStack.itemID, itemStack.getMetadata(), itemStack.getIconIndex(), x, y, brightness, alpha);
			}
		}
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void renderItemIntoGUI(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float alpha) {
		renderItemIntoGUI(fontRenderer, renderEngine, itemStack, x, y, 1f, alpha);
	}

	/**
	 * @author dotBlueShoes
	 * @reason Maybe it was stupid.
	 */
	@Overwrite
	public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderEngine, int itemId, int metadata, int spriteIndex, int x, int y, float brightness, float alpha) {
		if (this.isBlock(itemId)) {

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

			int color = Item.itemsList[itemId].getColorFromDamage(metadata);
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
			this.renderBlocks.renderBlockOnInventory(block, metadata, brightness);
			this.renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
			GL11.glDisable(3042);

		} else {

			GL11.glDisable(2896);

			if (itemId < Block.blocksList.length) {
				final int color = Item.itemsList[itemId].getColorFromDamage(metadata);

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));
				this.setColor(color, brightness, alpha);
				this.renderTexturedQuad(x, y, spriteIndex, SpriteAtlasHelper.vanillaBlockAtlas);

				GL11.glEnable(2896);

			} else {

				if (Item.itemsList[itemId] instanceof ISpriteAtlasItem) {
					ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) Item.itemsList[itemId];

					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));

					final int color = ((Item)spriteAtlasItem).getColorFromDamage(metadata);
					this.setColor(color, brightness, alpha);
					this.renderTexturedQuad(x, y, spriteIndex, spriteAtlasItem.getSpriteAtlas());
					GL11.glEnable(2896);

				} else {
					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));
					int color = Item.itemsList[itemId].getColorFromDamage(metadata);
					this.setColor(color, brightness, alpha);
					this.renderTexturedQuad(x, y, spriteIndex, SpriteAtlasHelper.vanillaItemAtlas);

					GL11.glEnable(2896);
				}

			}
		}

		GL11.glEnable(2884);
	}

	@Unique
	public void drawColoredItemIntoGui(RenderEngine renderEngine, Item item, int metadata, int x, int y, float brightness, float alpha) {
		IColored colored = (IColored)item;

		int[] spriteIndexes = new int[2];

		if (this.isBlock(item.id)) {
			Block block = Block.blocksList[item.id];

			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			SpriteAtlasHelper.handleBlock(Block.blocksList[item.id]);
			renderEngine.bindTexture(renderEngine.getTexture(SpriteAtlasHelper.currentAtlas.getName()));

			GL11.glPushMatrix();
			GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

			final int color = Item.itemsList[item.id].getColorFromDamage(metadata);
			this.setColor(color, brightness, alpha);

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

			this.renderBlocks.useInventoryTint = this.field_27004_a;
			this.renderBlocks.renderBlockOnInventory(block, metadata, brightness);
			this.renderBlocks.useInventoryTint = true;

			GL11.glPopMatrix();
			GL11.glDisable(3042);

		} else {

			GL11.glDisable(2896);

			if (item.id < Block.blocksList.length) {
				spriteIndexes[0] = SpriteAtlasHelper.vanillaBlockAtlas.spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
				spriteIndexes[1] = SpriteAtlasHelper.vanillaBlockAtlas.spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);

				renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaBlockAtlas.getName()));

				this.renderColoredQuadPlane(x, y, spriteIndexes[0], SpriteAtlasHelper.vanillaBlockAtlas, colored.baseColor());
				this.renderColoredQuadPlane(x, y, spriteIndexes[1], SpriteAtlasHelper.vanillaBlockAtlas, colored.overlayColor());
				GL11.glEnable(2896);

			} else {

				if (item instanceof ISpriteAtlasItem) {
					ISpriteAtlasItem spriteAtlasItem = (ISpriteAtlasItem) item;

					spriteIndexes[0] = spriteAtlasItem.getSpriteAtlas().spriteCoordToIndex(colored.baseTexture()[0], colored.baseTexture()[1]);
					spriteIndexes[1] = spriteAtlasItem.getSpriteAtlas().spriteCoordToIndex(colored.overlayTexture()[0], colored.overlayTexture()[1]);

					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, spriteAtlasItem.getSpriteAtlas().getName()));

					this.renderColoredQuadPlane(x, y, spriteIndexes[0], spriteAtlasItem.getSpriteAtlas(), colored.baseColor());
					this.renderColoredQuadPlane(x, y, spriteIndexes[1], spriteAtlasItem.getSpriteAtlas(), colored.overlayColor());
					GL11.glEnable(2896);

				} else {
					renderEngine.bindTexture(SpriteAtlasHelper.getCustomTexture(renderEngine, SpriteAtlasHelper.vanillaItemAtlas.getName()));

					this.renderColoredQuadPlane(x, y, spriteIndexes[0], SpriteAtlasHelper.vanillaItemAtlas, colored.baseColor());
					this.renderColoredQuadPlane(x, y, spriteIndexes[1], SpriteAtlasHelper.vanillaItemAtlas, colored.overlayColor());
					GL11.glEnable(2896);
				}

			}
		}

		GL11.glEnable(2884);
	}

	@Unique
	public void renderTexturedQuad(int x, int y, int spriteIndex, SpriteAtlas spriteAtlas) {
		float xo  = ((float) (spriteIndex % spriteAtlas.elements.x) / spriteAtlas.elements.x);
		float xe  = xo + (1f / spriteAtlas.elements.x);
		float yo = ((float) (spriteIndex / spriteAtlas.elements.x) / spriteAtlas.elements.y);
		float ye = yo + (1f / spriteAtlas.elements.y);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		// 1->2
		//    v
		// 4<-3

		tessellator.addVertexWithUV(
			x, y + 16, 0.0,
			xo, ye
		);

		tessellator.addVertexWithUV(
			x + 16, y + 16, 0.0,
			xe, ye
		);

		tessellator.addVertexWithUV(
			x + 16, y, 0.0,
			xe, yo
		);

		tessellator.addVertexWithUV(
			x, y, 0.0,
			xo, yo
		);

		tessellator.draw();

		GL11.glEnable(2896);
	}

	@Unique
	public void renderColoredQuadPlane(int x, int y, int spriteIndex, SpriteAtlas spriteAtlas, int color) {

		float red = (float)(color >> 16 & 255) / 255.0F;
		float green = (float)(color >> 8 & 255) / 255.0F;
		float blue = (float)(color & 255) / 255.0F;

		float xo  = ((float) (spriteIndex % spriteAtlas.elements.x) / spriteAtlas.elements.x);
		float xe  = xo + (1f / spriteAtlas.elements.x);
		float yo = ((float) (spriteIndex / spriteAtlas.elements.x) / spriteAtlas.elements.y);
		float ye = yo + (1f / spriteAtlas.elements.y);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.setColorOpaque_F(red, green, blue);
		tessellator.addVertexWithUV(
			x, y + 16, 0.0,
			xo, ye
		);

		tessellator.setColorOpaque_F(red, green, blue);
		tessellator.addVertexWithUV(
			x + 16, y + 16, 0.0,
			xe, ye
		);

		tessellator.setColorOpaque_F(red, green, blue);
		tessellator.addVertexWithUV(
			x + 16, y, 0.0,
			xe, yo
		);

		tessellator.setColorOpaque_F(red, green, blue);
		tessellator.addVertexWithUV(
			x, y, 0.0,
			xo, yo
		);

		tessellator.draw();
	}

}
