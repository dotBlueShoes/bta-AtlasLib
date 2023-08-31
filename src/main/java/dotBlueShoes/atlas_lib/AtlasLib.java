package dotBlueShoes.atlas_lib;

import net.fabricmc.api.ClientModInitializer;


public class AtlasLib implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Global.LOGGER.info("AtlasLib initialized.");
	}
}
