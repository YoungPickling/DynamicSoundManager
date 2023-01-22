package net.denanu.dynamicsoundmanager.utils;

import net.minecraft.entity.player.PlayerEntity;

public class Permission {
	public static boolean hasModificationPermission(final PlayerEntity player) {
		return player == null ? false : player.hasPermissionLevel(2);
	}
}
