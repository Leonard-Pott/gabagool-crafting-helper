package de.leonard.gabagoolcalc.sacks;

import java.util.OptionalLong;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.core.GabagoolRecipe;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Gemerkte Bestaende aus den Sacks. Noetig, weil immer nur ein GUI offen ist:
 * Enchanted Coal steht im Enchanted Mining Sack, Sulphuric Coal im Nether Sack.
 * Jeder geoeffnete Sack aktualisiert, was er hergibt, der Rest bleibt stehen.
 *
 * ponytail: bewusst nur im Speicher. Nach einem Neustart ist der Wert wieder
 * unbekannt, bis der Nether Sack einmal offen war - reicht, weil man die Sacks
 * ohnehin staendig aufmacht.
 */
public final class SackStock {

	private static OptionalLong enchantedCoal = OptionalLong.empty();
	private static OptionalLong sulphuricCoal = OptionalLong.empty();

	public static void update(AbstractContainerScreen<?> screen) {
		try {
			scan(screen);
		} catch (RuntimeException e) {
			// Hypixel hat die GUI geaendert -> alte Werte behalten statt crashen
			GabagoolCalcClient.LOGGER.debug("Sack-Parsing fehlgeschlagen", e);
		}
	}

	private static void scan(AbstractContainerScreen<?> screen) {
		SacksItemParser.find(screen, GabagoolRecipe.ID_ENCHANTED_COAL, "Enchanted Coal")
				.ifPresent(amount -> enchantedCoal = OptionalLong.of(amount));
		SacksItemParser.find(screen, GabagoolRecipe.ID_SULPHURIC_COAL, "Sulphuric Coal")
				.ifPresent(amount -> sulphuricCoal = OptionalLong.of(amount));
	}

	public static OptionalLong enchantedCoal() {
		return enchantedCoal;
	}

	public static OptionalLong sulphuricCoal() {
		return sulphuricCoal;
	}

	private SackStock() {
	}
}
