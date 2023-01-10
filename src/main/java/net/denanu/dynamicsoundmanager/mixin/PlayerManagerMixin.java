package net.denanu.dynamicsoundmanager.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.dynamicsoundmanager.networking.s2c.RequiredSoundsS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(method="onPlayerConnect", at=@At("TAIL"))
	public void onPlayerConnect(final ClientConnection connection, final ServerPlayerEntity player, final CallbackInfo ci) {
		RequiredSoundsS2CPacket.send(player);
	}
}
