package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose:
///  injects initialization of SpriteAtlases
///

import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.initialization.SpriteAtlases;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texturepack.TexturePackList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

@Mixin(RenderEngine.class)
public abstract class RenderEngineMixin {
	@Shadow(remap = false)
	private Map<String, Integer> textureMap;
	@Shadow(remap = false)
	private List<DynamicTexture> dynamicTextures;

	@Shadow(remap = false) @Final
	public Minecraft minecraft;
	@Shadow(remap = false)
	public TexturePackList texturePacks;
	@Shadow(remap = false)
	public abstract void setupTexture(BufferedImage img, int id, boolean mipmap);

	@Inject(method = "initDynamicTextures", at = @At("TAIL"), remap = false)
	public void initDynamicTextures(CallbackInfo ci) {

		//String spriteAtlas = "/assets/" + Global.MOD_ID + "/" + Global.TILEMAP_GEMS_IMAGE;
		//String spriteAtlas = Global.MOD_ID + "/" + Global.TILEMAP_GEMS_IMAGE;

		for (SpriteAtlas atlas : SpriteAtlases.atlases) {
			SpriteAtlasHelper.getCustomTexture((RenderEngine)(Object)this, atlas.getName());
		}

		//// I believe we're loading a texture only to read its size for now.
		//GL11.glBindTexture(Global.OPENGL_VERSION, RenderEngineHelper.getCustomTexture((RenderEngine)(Object)this, spriteAtlas));
		//int atlasResolution = GL11.glGetTexLevelParameteri(Global.OPENGL_VERSION, 0, 4096) / Global.TILEMAP_GEMS_ELEMENTS_X;
		//
		//LOGGER.info("Custom Atlas Resolution: " + Integer.toString(atlasResolution));
		//
		//final int[] coord = { 1, 1 };
		//final int textureIndex = coord[0] + (coord[1] * Global.TILEMAP_GEMS_ELEMENTS_X);
		//
		//// And here we're loading a texture to use it.
		//this.dynamicTextures.add(
		//	new TextureAtlasHandler(
		//		this.minecraft,
		//		spriteAtlas,
		//		coord[0], coord[1],
		//		atlasResolution,
		//		textureIndex
		//	)
		//);
	}
}
