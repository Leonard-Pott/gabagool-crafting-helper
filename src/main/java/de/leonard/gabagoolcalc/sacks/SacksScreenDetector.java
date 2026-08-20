package de.leonard.gabagoolcalc.sacks;

import de.leonard.gabagoolcalc.GabagoolConfig;
import de.leonard.gabagoolcalc.core.SackAmountParser;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Erkennt, ob der gesuchte Sack-Screen offen ist. Welcher das ist, steht in der
 * Config (Default "Enchanted Mining Sack") - falls Hypixel den Titel mal
 * umbenennt, reicht eine Zeile in der .properties statt eines neuen Builds.
 */
public final class SacksScreenDetector {

	public static boolean isTargetScreen(Screen screen) {
		return screen instanceof AbstractContainerScreen<?> && matchesTitle(screen.getTitle().getString());
	}

	public static boolean matchesTitle(String rawTitle) {
		String wanted = GabagoolConfig.screenTitle.trim().toLowerCase();
		return !wanted.isEmpty() && SackAmountParser.strip(rawTitle).toLowerCase().contains(wanted);
	}

	/** Nur fuer den Debug-Dump: irgendein Sack-Screen. */
	public static boolean isAnySackScreen(Screen screen) {
		return screen instanceof AbstractContainerScreen<?>
				&& SackAmountParser.strip(screen.getTitle().getString()).toLowerCase().contains("sack");
	}

	private SacksScreenDetector() {
	}
}
