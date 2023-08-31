package dotBlueShoes.atlas_lib.utility;

public class SpriteAtlas {

	private final String pathname;
	private final String modid;
	public final Pair<Integer> elements;
	public final int resolution;

	public SpriteAtlas(String modid, String pathname, Pair<Integer> elements, int resolution) {
		this.modid = modid;
		this.pathname = pathname;
		this.elements = elements;
		this.resolution = resolution;
	}

	public String getName() {
		return modid + "/" + pathname;
	}
}