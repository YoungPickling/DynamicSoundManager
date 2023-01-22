package net.denanu.dynamicsoundmanager.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.dynamicsoundmanager.gui.widgets.AudioPlayerWidgetGlobals;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
	@Inject(method="tick", at=@At("HEAD"), cancellable=true)
	private void tick(final CallbackInfo cir)
	{
		final SoundInstance current = ((IMusicTrackerAccessor)this).getCurrent();
		// Utils.test(current, cir);
		if (current != null && current.getId() == AudioPlayerWidgetGlobals.MUSIC_PREVIEW_ID) {
			cir.cancel();
		}
	}

}
