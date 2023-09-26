package dotBlueShoes.atlas_lib.mixin;

import com.google.common.collect.ImmutableMap;

import dotBlueShoes.atlas_lib.Global;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AtlasLibMixinPlugin implements IMixinConfigPlugin {

	private static final String PRISMATICLIBE_STRING = "prismaticlibe";

	private static final Supplier<Boolean> TRUE = () -> true;

	private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.<String, Supplier<Boolean>> builder()
		.put("dotBlueShoes.atlas_lib.mixin.mixins.ItemEntityRendererMixin",             () -> !FabricLoader.getInstance().isModLoaded(PRISMATICLIBE_STRING))
		.put("dotBlueShoes.atlas_lib.mixin.mixins.prismatic.ItemEntityRendererMixin",   () -> FabricLoader.getInstance().isModLoaded(PRISMATICLIBE_STRING))
		.put("dotBlueShoes.atlas_lib.mixin.mixins.ItemRendererMixin",                   () -> !FabricLoader.getInstance().isModLoaded(PRISMATICLIBE_STRING)            )
		.put("dotBlueShoes.atlas_lib.mixin.mixins.prismatic.ItemRendererMixin",         () -> FabricLoader.getInstance().isModLoaded(PRISMATICLIBE_STRING))
		.build();

	@Override
	public void onLoad(String mixinPackage) {
		Global.LOGGER.info(PRISMATICLIBE_STRING + " found, managing mixins.");
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		//boolean ret = CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
		//System.out.print(targetClassName);
		//System.out.print("SHOULD_MIXIN: " + ret + ", " + FabricLoader.getInstance().isModLoaded("prismaticlibe") + "\n");
		//return ret;
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
