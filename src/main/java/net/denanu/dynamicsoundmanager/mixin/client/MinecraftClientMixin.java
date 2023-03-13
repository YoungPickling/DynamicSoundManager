package net.denanu.dynamicsoundmanager.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.dynamicsoundmanager.networking.c2s.ReloadResourcesC2SPacket;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

@Mixin(targets = { "net.minecraft.client.sound.SoundManager$SoundList" })
public class MinecraftClientMixin {
	@Inject(method="reload", at=@At("RETURN"))
	private void reload(final Map<Identifier, WeightedSoundSet> sounds, final Map<Identifier, Resource> soundResources, final SoundSystem soundSystem, final CallbackInfo cir) {
		ReloadResourcesC2SPacket.send();
	}
}
