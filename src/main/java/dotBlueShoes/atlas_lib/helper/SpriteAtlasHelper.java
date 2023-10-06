package dotBlueShoes.atlas_lib.helper;

import dotBlueShoes.atlas_lib.AtlasLib;
import dotBlueShoes.atlas_lib.Global;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasBlock;
import dotBlueShoes.atlas_lib.utility.Pair;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;

import net.minecraft.client.GLAllocation;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.texturepack.TexturePackBase;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Textures;

import java.io.IOException;
import java.io.InputStream;

public class SpriteAtlasHelper {

	public static final SpriteAtlas vanillaBlockAtlas = new SpriteAtlas(
		null,
		"terrain.png",
		new Pair<Integer>(32, 32),
		16
	);

	public static final SpriteAtlas vanillaItemAtlas = new SpriteAtlas(
		null,
		"gui/items.png",
		new Pair<Integer>(32, 32),
		16
	);

	public static SpriteAtlas currentAtlas = vanillaBlockAtlas;

	public static void handleBlock(Block block) {
		if (block instanceof ISpriteAtlasBlock) {
			ISpriteAtlasBlock atlasSpriteBlock = (ISpriteAtlasBlock) block;
			currentAtlas = atlasSpriteBlock.getSpriteAtlas();
		} else {
			currentAtlas = vanillaBlockAtlas;
		}
	}

	//public void handleItem(Item item) {
	//	if (item instanceof SpriteAtlasItem) {
	//		SpriteAtlasItem atlasSpriteItem = (SpriteAtlasItem) item;
	//	} else {
	//	}
	//}

	public static int setCustomTexture(RenderEngine renderEngine, String texturePath) {
		TexturePackBase texturePack = renderEngine.texturePacks.selectedTexturePack;



		int id = GLAllocation.generateTexture();

		//Global.LOGGER.info("1: " + "/assets/gems_mod/item/tilemap_gems.png");
		//Global.LOGGER.info("2: " + "/gems_mod/item/tilemap_gems.png");
		//Global.LOGGER.info("3: " + texturePath);

		try (InputStream inputStream = texturePack.getResourceAsStream(texturePath)) {

			/// When NULL setup "Missing Texture instead".
			if (inputStream == null) {

				Global.LOGGER.info("STREAM_ERROR");
				renderEngine.setupTexture(Textures.missingTexture, id, false);

			} else {

				Global.LOGGER.info("Sprite Found!");
				renderEngine.setupTexture(Textures.readImage(inputStream), id, false);

			}

			renderEngine.getTextureMap().put(texturePath, id);
			return id;

		} catch (IOException exception) {
			exception.printStackTrace();


			renderEngine.setupTexture(Textures.missingTexture, id, false);
			renderEngine.getTextureMap().put(texturePath, id);
			return id;
		}
	}

	public static int getCustomTexture(RenderEngine renderEngine, String texturePath) {
		//Global.LOGGER.info(texturePath);
		return renderEngine.getTextureMap().get(texturePath);
	}

}
