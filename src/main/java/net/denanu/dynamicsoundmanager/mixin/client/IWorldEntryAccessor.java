package net.denanu.dynamicsoundmanager.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.storage.LevelSummary;

@Mixin(WorldListWidget.WorldEntry.class)
public interface IWorldEntryAccessor {
	@Accessor LevelSummary getLevel();
}
