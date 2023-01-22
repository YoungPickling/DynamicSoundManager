package net.denanu.dynamicsoundmanager.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class Utils {
	public static int buildNextX(final int x, final WidgetBase widget) {
		return x + widget.getWidth() + 2;
	}

	public static int buildNextY(final WidgetBase widget) {
		return widget.getY() + widget.getHeight() + 2;
	}

	public static boolean hasModificationPermission(final PlayerEntity player) {
		return player.hasPermissionLevel(2);
	}

	public static boolean hasModificationPermission() {
		final MinecraftClient client = MinecraftClient.getInstance();
		return Utils.hasModificationPermission(client.player);
	}
}
