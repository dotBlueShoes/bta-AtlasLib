package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose of this mixin is to inject custom blocks rendering into the rendering pipeline.
///

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCache;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Mixin(
	value = ChunkRenderer.class,
	remap = false
)
public abstract class ChunkRendererMixin {

	@Shadow
	public boolean needsUpdate;
	@Shadow
	public static int chunksUpdated = 0;
	@Shadow
	public int posX;
	@Shadow
	public int posY;
	@Shadow
	public int posZ;
	@Shadow
	public int sizeWidth;
	@Shadow
	public int sizeHeight;
	@Shadow
	public int sizeDepth;
	@Shadow
	public boolean[] skipRenderPass;
	@Shadow
	public List<TileEntity> specialTileEntities;
	@Shadow
	public World worldObj;
	@Shadow @Final
	private int glRenderList;
	@Shadow @Final
	private static Tessellator tessellator;
	@Shadow @Final
	private List<TileEntity> tileEntities;
	@Shadow
	public boolean isChunkLit;
	@Shadow
	private boolean isInitialized;

	@Shadow
	protected abstract void setupGLTranslation();

	// Mixin apply for mod prismaticlibe failed prismaticlibe.mixins.json:ItemEntityRendererMixin from mod prismaticlibe ->
	// net.minecraft.client.render.entity.ItemEntityRenderer:
	// org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException @At("INVOKE") on
	// net/minecraft/client/render/entity/ItemEntityRenderer::doRenderColoredItem with priority 1000
	// cannot inject into net/minecraft/client/render/entity/ItemEntityRenderer::doRenderItem(Lnet/minecraft/core/entity/EntityItem;DDDFF)V merged by
	// dotBlueShoes.atlas_lib.mixin.mixins.prismatic.ItemEntityRendererMixin with priority 1010
	// [PREINJECT Applicator Phase -> prismaticlibe.mixins.json:ItemEntityRendererMixin from mod prismaticlibe ->
	// Prepare Injections ->  ->
	// handler$zzf000$prismaticlibe$doRenderColoredItem(Lnet/minecraft/core/entity/EntityItem;DDDFFLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V -> Prepare]

	/**
	 * @author dotBlueShoes
	 * @reason This might be stupid.
	 */
	@Overwrite
	public void updateRenderer() {
		if (this.needsUpdate) {
			++chunksUpdated;
			int minX = this.posX;
			int minY = this.posY;
			int minZ = this.posZ;
			int maxX = this.posX + this.sizeWidth;
			int maxY = this.posY + this.sizeHeight;
			int maxZ = this.posZ + this.sizeDepth;

			for(int i = 0; i < 2; ++i) {
				this.skipRenderPass[i] = true;
			}

			Chunk.isLit = false;
			HashSet<TileEntity> lastSpecialTileEntities = new HashSet<>(this.specialTileEntities);
			this.specialTileEntities.clear();
			int cacheRadius = 1;
			ChunkCache chunkcache = new ChunkCache(this.worldObj, minX - cacheRadius, minY - cacheRadius, minZ - cacheRadius, maxX + cacheRadius, maxY + cacheRadius, maxZ + cacheRadius);
			RenderBlocks renderblocks = new RenderBlocks(this.worldObj, chunkcache);
			BlockModelRenderBlocks.setRenderBlocks(renderblocks);

			for(int renderPass = 0; renderPass < 2; ++renderPass) {
				boolean needsMoreRenderPasses = false;
				boolean hasRenderedBlock = false;
				boolean hasStartedDrawing = false;

				for(int y = minY; y < maxY; ++y) {
					for(int z = minZ; z < maxZ; ++z) {
						for(int x = minX; x < maxX; ++x) {
							int blockId = chunkcache.getBlockId(x, y, z);
							if (blockId > 0) {
								if (!hasStartedDrawing) {
									hasStartedDrawing = true;
									GL11.glNewList(this.glRenderList + renderPass, 4864);
									GL11.glPushMatrix();
									this.setupGLTranslation();
									float scale = 1.000001F;
									GL11.glTranslatef((float)(-this.sizeDepth) / 2.0F, (float)(-this.sizeHeight) / 2.0F, (float)(-this.sizeDepth) / 2.0F);
									GL11.glScalef(scale, scale, scale);
									GL11.glTranslatef((float)this.sizeDepth / 2.0F, (float)this.sizeHeight / 2.0F, (float)this.sizeDepth / 2.0F);
									tessellator.startDrawingQuads();
									tessellator.setTranslation(-this.posX, -this.posY, -this.posZ);
								}

								if (renderPass == 0 && Block.isBlockContainer[blockId]) {
									TileEntity tileentity = chunkcache.getBlockTileEntity(x, y, z);
									if (TileEntityRenderDispatcher.instance.hasRenderer(tileentity)) {
										this.specialTileEntities.add(tileentity);
									}
								}

								Block block = Block.blocksList[blockId];
								int blockRenderPass = block.getRenderBlockPass();
								if (blockRenderPass != renderPass) {
									needsMoreRenderPasses = true;
								} else {
									BlockModel model = BlockModelDispatcher.getInstance().getDispatch(block);
									hasRenderedBlock |= model.render(block, x, y, z);
									if (block.hasOverbright) {
										renderblocks.overbright = true;
										hasRenderedBlock |= model.render(block, x, y, z);
										renderblocks.overbright = false;
									}
								}
							}
						}
					}
				}

				if (hasStartedDrawing) {
					tessellator.draw();
					GL11.glPopMatrix();
					GL11.glEndList();
					tessellator.setTranslation(0.0, 0.0, 0.0);
				} else {
					hasRenderedBlock = false;
				}

				if (hasRenderedBlock) {
					this.skipRenderPass[renderPass] = false;
				}

				if (!needsMoreRenderPasses) {
					break;
				}
			}

			HashSet<TileEntity> newSpecialTileEntities = new HashSet<>(this.specialTileEntities);
			newSpecialTileEntities.removeAll(lastSpecialTileEntities);
			this.tileEntities.addAll(newSpecialTileEntities);
			List<TileEntity> var10000 = this.specialTileEntities;
			Objects.requireNonNull(lastSpecialTileEntities);
			var10000.forEach(lastSpecialTileEntities::remove);
			this.tileEntities.removeAll(lastSpecialTileEntities);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
		}
	}
}
