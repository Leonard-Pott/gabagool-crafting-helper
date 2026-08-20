package de.leonard.gabagoolcalc.sacks;

import java.util.OptionalLong;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.GabagoolConfig;
import de.leonard.gabagoolcalc.core.GabagoolRecipe;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Gemerkte Bestaende aus den Sacks. Noetig, weil immer nur ein GUI offen ist:
 * Enchanted Coal steht im Enchanted Mining Sack, Sulphuric Coal im Nether Sack.
 * Jeder geoeffnete Sack aktualisiert, was er hergibt, der Rest bleibt stehen.
 *
 * Die Werte liegen in der Config und ueberleben damit den Neustart: der Nether
 * Sack muss nur einmal aufgemacht werden, danach rechnet der Mining Sack mit
 * dem gemerkten Stand weiter. Gespeichert wird nur, wenn sich wirklich etwas
 * geaendert hat.
 */
public final class SackStock {

	public static void update(AbstractContainerScreen<?> screen) {
		try {
			scan(screen);
		} catch (RuntimeException e) {
			// Hypixel hat die GUI geaendert -> alte Werte behalten statt crashen
			GabagoolCalcClient.LOGGER.debug("Sack-Parsing fehlgeschlagen", e);
		}
	}

	private static void scan(AbstractContainerScreen<?> screen) {
		boolean changed = false;
		OptionalLong coal = SacksItemParser.find(screen, GabagoolRecipe.ID_ENCHANTED_COAL, "Enchanted Coal");
		if (coal.isPresent() && coal.getAsLong() != GabagoolConfig.stockEnchantedCoal) {
			GabagoolConfig.stockEnchantedCoal = coal.getAsLong();
			changed = true;
		}
		OptionalLong sulphuric = SacksItemParser.find(screen, GabagoolRecipe.ID_SULPHURIC_COAL, "Sulphuric Coal");
		if (sulphuric.isPresent() && sulphuric.getAsLong() != GabagoolConfig.stockSulphuricCoal) {
			GabagoolConfig.stockSulphuricCoal = sulphuric.getAsLong();
			changed = true;
		}
		if (changed) {
			GabagoolConfig.save();
		}
	}

	public static OptionalLong enchantedCoal() {
		return known(GabagoolConfig.stockEnchantedCoal);
	}

	public static OptionalLong sulphuricCoal() {
		return known(GabagoolConfig.stockSulphuricCoal);
	}

	private static OptionalLong known(long value) {
		return value < 0 ? OptionalLong.empty() : OptionalLong.of(value);
	}

	private SackStock() {
	}
}
