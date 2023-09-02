package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose:
///  injects initialization of SpriteAtlases
///

import dotBlueShoes.atlas_lib.Global;
import dotBlueShoes.atlas_lib.helper.SpriteAtlasHelper;
import dotBlueShoes.atlas_lib.initialization.SpriteAtlases;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;

import net.minecraft.client.render.RenderEngine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderEngine.class, remap = false)
public abstract class RenderEngineMixin {

	//@Shadow
	//private Map<String, Integer> textureMap;
	//@Shadow
	//private List<DynamicTexture> dynamicTextures;
	//@Shadow @Final
	//public Minecraft minecraft;
	//@Shadow
	//public TexturePackList texturePacks;
	//@Shadow
	//public abstract void setupTexture(BufferedImage img, int id, boolean mipmap);

	@Inject(method = "initDynamicTextures", at = @At("TAIL"), remap = false)
	public void initDynamicTextures(CallbackInfo ci) {
		Global.LOGGER.info("HeLLO");
		for (SpriteAtlas atlas : SpriteAtlases.atlases) {
			SpriteAtlasHelper.setCustomTexture((RenderEngine)(Object)this, atlas.getName());
		}
	}
}
