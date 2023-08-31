package dotBlueShoes.atlas_lib.items;

import dotBlueShoes.atlas_lib.utility.ISpriteAtlasItem;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;

import net.minecraft.core.item.Item;

public class SampleItem extends Item implements ISpriteAtlasItem {

	private SpriteAtlas spriteAtlas;
	public SampleItem(String key, int id, SpriteAtlas spriteAtlas) {
		super(id);
		this.spriteAtlas = spriteAtlas;
		this.setKey(key);
	}

	public int spriteCoordToIndex(int x, int y) {
		return x + y * spriteAtlas.elements.x;
	}



	@Override
	public Item setIconCoord(int x, int y) {
		return this.setSpriteCoord(x, y);
	}

	@Override
	public SampleItem setSpriteCoord(int x, int y) {
		this.iconIndex = spriteCoordToIndex(x, y);
		return this;
	}

	@Override
	public int getSpriteIndex() {
		return this.iconIndex;
	}

	@Override
	public SpriteAtlas getSpriteAtlas() {
		return spriteAtlas;
	}

	@Override
	public void setSpriteAtlas(SpriteAtlas spriteAtlas) {
		this.spriteAtlas = spriteAtlas;
	}

}
