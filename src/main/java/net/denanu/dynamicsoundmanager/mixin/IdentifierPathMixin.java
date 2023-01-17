package net.denanu.dynamicsoundmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Identifier;

@Mixin(Identifier.class)
public abstract class IdentifierPathMixin {
	@Inject(method="isPathValid", at=@At("HEAD"), cancellable=true)
	private static void injectPathValid(final String character, final CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
		cir.cancel();
	}
}
