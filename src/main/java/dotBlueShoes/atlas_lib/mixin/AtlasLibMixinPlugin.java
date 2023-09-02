package dotBlueShoes.atlas_lib.mixin;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AtlasLibMixinPlugin implements IMixinConfigPlugin {

	// This makes it not to load a mixin if a mod is enabled.
	// Todo: make it so theres a specific mixin that only loads when mod is enabled

	private static final Supplier<Boolean> TRUE = () -> true; // default value for ones not specified.

	private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
		"dotBlueShoes.atlas_lib.mixin.mixins.RenderEngineMixin", () -> !FabricLoader.getInstance().isModLoaded("prismaticlibe")
	);

	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		//boolean ret = CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
		//System.out.print("SHOULD_MIXIN: " + ret + ", " + FabricLoader.getInstance().isModLoaded("prismaticlibe") + "\n");
		return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

}
