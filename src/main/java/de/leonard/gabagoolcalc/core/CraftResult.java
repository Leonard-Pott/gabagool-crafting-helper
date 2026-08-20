package de.leonard.gabagoolcalc.core;

/**
 * @param craftableAmount  wie viele Hypergolic Gabagool mit der vorhandenen Enchanted Coal gehen
 * @param neededSulphur    roher Sulphur fuer craftableAmount Stueck
 * @param neededEnchantedSulphur  dasselbe in Enchanted Sulphur, aufgerundet (Bazaar-Menge)
 * @param neededVeryCrude  Very Crude Gabagool fuer craftableAmount Stueck
 * @param remainingCoal    Enchanted Coal, die uebrig bleibt
 * @param coalToNextCraft  fehlende Enchanted Coal bis zum naechsten Craft
 * @param leftoverSulphuricCoal  Sulphuric Coal, die durch die Vierergruppen-Rundung
 *                               uebrig bleiben und den naechsten Craft billiger machen
 */
public record CraftResult(
		long craftableAmount,
		long neededSulphur,
		long neededEnchantedSulphur,
		long neededVeryCrude,
		long remainingCoal,
		long coalToNextCraft,
		long leftoverSulphuricCoal) {
}
