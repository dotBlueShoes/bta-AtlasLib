package dotBlueShoes.atlas_lib.mixin.mixins;

///
/// Purpose of this mixin is to inject custom blocks rendering into the rendering pipeline.
///

import net.minecraft.client.render.ChunkRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkRenderer.class)
public abstract class ChunkRendererMixin {
}
