package dotBlueShoes.atlas_lib.utility;

public interface ISpriteAtlasBlock extends ISpriteAtlas {

	public int getSpriteIndex(int side);
	public ISpriteAtlas setSpriteCoord(int side, int x, int y);
}
