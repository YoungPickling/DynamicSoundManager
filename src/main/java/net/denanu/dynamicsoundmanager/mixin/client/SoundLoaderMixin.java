package net.denanu.dynamicsoundmanager.mixin.client;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.denanu.dynamicsoundmanager.player_api.DynamicSound;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.StaticSound;
import net.minecraft.util.Identifier;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin {
	@Inject(method="loadStatic", at=@At("HEAD"), cancellable=true)
	public void loadStaticInjector(final Identifier id, final CallbackInfoReturnable<CompletableFuture<StaticSound>> cir) {
		if (id.getPath() == DynamicSound.SHOULD_LOAD_DYNAMICLY) {
			cir.setReturnValue(DynamicSound.loadDynamicSound(id));
			cir.cancel();
		}
	}
}
