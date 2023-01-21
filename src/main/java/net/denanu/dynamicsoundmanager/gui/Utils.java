package net.denanu.dynamicsoundmanager.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;

public class Utils {
	public static int buildNextX(final int x, final WidgetBase widget) {
		return x + widget.getWidth() + 2;
	}

	public static int buildNextY(final WidgetBase widget) {
		return widget.getY() + widget.getHeight() + 2;
	}
}
