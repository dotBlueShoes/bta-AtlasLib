package dotBlueShoes.atlas_lib.blocks;

import dotBlueShoes.atlas_lib.utility.ISpriteAtlas;
import dotBlueShoes.atlas_lib.utility.ISpriteAtlasBlock;
import dotBlueShoes.atlas_lib.utility.SpriteAtlas;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;

public class SampleBlock extends Block implements ISpriteAtlasBlock {

	private SpriteAtlas spriteAtlas;

	public SampleBlock(String key, int id, Material material, SpriteAtlas spriteAtlas) {
		super(key, id, material);
		this.spriteAtlas = spriteAtlas;
	}

	public Block setFullBlockCoord(int x, int y) {
		atlasIndices[Side.TOP.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		atlasIndices[Side.BOTTOM.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		atlasIndices[Side.NORTH.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		atlasIndices[Side.EAST.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		atlasIndices[Side.SOUTH.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		atlasIndices[Side.WEST.getId()] = this.spriteAtlas.spriteCoordToIndex(x, y);
		return this;
	}

	@Override
	public ISpriteAtlas setSpriteCoord(int side, int x, int y) {
		atlasIndices[side] = this.spriteAtlas.spriteCoordToIndex(x, y);
		return this;
	}

	@Override
	public int getSpriteIndex(int side) {
		return atlasIndices[side];
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
