package net.denanu.dynamicsoundmanager.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.dynamicsoundmanager.groups.client.ClientSoundGroupManager;
import net.denanu.dynamicsoundmanager.gui.Utils;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.screen.world.WorldListWidget.Entry;

@Mixin(WorldListWidget.WorldEntry.class)
public abstract class WorldEntryMixin extends Entry implements AutoCloseable {
	@Inject(method="delete", at=@At("HEAD"))
	public void delete(final CallbackInfo cir) {
		final IWorldEntryAccessor world = (IWorldEntryAccessor)this;
		Utils.deleteDirectory(ClientSoundGroupManager.getDeleteFile(world.getLevel()).toFile());
	}

}
