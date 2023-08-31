package dotBlueShoes.atlas_lib.items;

import dotBlueShoes.atlas_lib.utility.SpriteAtlas;
import net.minecraft.core.item.Item;

public class SpriteAtlasItem extends Item{

	public SpriteAtlas spriteAtlas;
	public SpriteAtlasItem(String key, int id, SpriteAtlas spriteAtlas) {
		super(id);
		this.spriteAtlas = spriteAtlas;
		this.setKey(key);
	}

	public int spriteCoordToIndex(int x, int y) {
		return x + y * spriteAtlas.elements.x;
	}

	public SpriteAtlasItem setSpriteCoord(int x, int y) {
		this.iconIndex = spriteCoordToIndex(x, y);
		return this;
	}

	public int getSpriteIndex() {
		return this.iconIndex;
	}

	@Override
	public Item setIconCoord(int x, int y) {
		return this.setSpriteCoord(x, y);
	}
}
