package dotBlueShoes.atlas_lib.helper;

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
		"",
		"terrain.png",
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

	public static int getCustomTexture(RenderEngine renderEngine, String texturePath) {

		/// If it's already here just grab that.
		Integer id = renderEngine.getTextureMap().get(texturePath);
		if (id != null) {
			return id;
		}

		/// With this we're at the TOP e.g. resources root.
		//ClassLoader classLoader = RenderEngineHelper.class.getClassLoader();
		TexturePackBase texturePack = renderEngine.texturePacks.selectedTexturePack;
		//Global.LOGGER.info("TexturePack: " + texturePack.texturePackFileName);
		//Global.LOGGER.info("Resource: " + texturePack.getClass().getResource("/"));

		//String[] afiles = texturePack.getFilesInDirectory("");
		//for (String file : afiles) {
		//	Global.LOGGER.info("a " + file);
		//}
		//String[] bfiles = texturePack.getFilesInDirectory("/assets/");
		//for (String file : bfiles) {
		//	Global.LOGGER.info("b " + file);
		//}
		//String[] cfiles = texturePack.getFilesInDirectory("/assets/gems_mod/item/");
		//for (String file : cfiles) {
		//	Global.LOGGER.info("c " + file);
		//}

		id = GLAllocation.generateTexture();

		//Resource

		//LOGGER.info(TexturePackBase.class.getModule().getName());
		//System.out.println("PRINTING");
		//for (File file : getResourceFolderFiles(texturePack, "assets")) {
		//	System.out.println(file);
		//}
		//Global.LOGGER.info("1: " + "/assets/gems_mod/item/tilemap_gems.png");
		//Global.LOGGER.info("2: " + texturePath);

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

			/// When EXCEPTION setup "Missing Texture instead".
			renderEngine.setupTexture(Textures.missingTexture, id, false);
			renderEngine.getTextureMap().put(texturePath, id);
			return id;
		}

	}

}
