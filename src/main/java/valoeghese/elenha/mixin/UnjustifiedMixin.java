
package valoeghese.elenha.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.StructureAccessor;
import valoeghese.elenha.TerribleGenHack;

@Mixin(StructureAccessor.class)
public class UnjustifiedMixin {
	@Inject(at = @At("HEAD"), method = "forRegion", cancellable = true)
	private void forRegion(ChunkRegion region, CallbackInfoReturnable<StructureAccessor> cir) {
		if (region instanceof TerribleGenHack) {
			cir.setReturnValue(null);
		}
	}
}
