package de.leonard.gabagoolcalc.sacks;

import de.leonard.gabagoolcalc.core.SackAmountParser;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Erkennt, ob gerade ein Sack-GUI offen ist. Hypixel nennt die Screens u.a.
 * "Sacks", "Sack of Sacks", "Mining Sack", "Enchanted Agronomy Sack" -
 * deshalb reicht der Teilstring "sack" im Titel.
 */
public final class SacksScreenDetector {

	public static boolean isSackScreen(Screen screen) {
		return screen instanceof AbstractContainerScreen<?> && matchesTitle(screen.getTitle().getString());
	}

	public static boolean matchesTitle(String rawTitle) {
		return SackAmountParser.strip(rawTitle).toLowerCase().contains("sack");
	}

	private SacksScreenDetector() {
	}
}
