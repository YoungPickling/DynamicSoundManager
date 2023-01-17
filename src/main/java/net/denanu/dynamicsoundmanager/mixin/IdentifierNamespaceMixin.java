package net.denanu.dynamicsoundmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Identifier;

@Mixin(Identifier.class)
public abstract class IdentifierNamespaceMixin {
	@Inject(method="isNamespaceValid", at=@At("HEAD"), cancellable=true)
	private static void inject(final String character, final CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
		cir.cancel();
	}
	/*
	@Inject(method="isPathCharacterValid", at=@At("HEAD"), cancellable=true)
	private static void inject2(final char character, final CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
		cir.cancel();
	}*/
}
