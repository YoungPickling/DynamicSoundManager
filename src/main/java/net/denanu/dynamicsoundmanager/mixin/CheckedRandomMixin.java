package net.denanu.dynamicsoundmanager.mixin;

import java.util.concurrent.atomic.AtomicLong;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.math.random.CheckedRandom;

@Mixin(CheckedRandom.class)
public interface CheckedRandomMixin {
	@Accessor
	AtomicLong getSeed();
}
