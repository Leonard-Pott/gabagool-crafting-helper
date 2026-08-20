package de.leonard.gabagoolcalc.core;

/** Reine Rechenlogik, keine Minecraft-Imports. */
public final class GabagoolCalculator {

	public static CraftResult calculate(long enchantedCoal) {
		long coal = Math.max(0, enchantedCoal);

		// Enchanted Coal reicht fuer so viele Sulphuric-Coal-Crafts a 4 Stueck.
		long affordableCrafts = coal / GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;
		long affordableSulphuric = affordableCrafts * GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT;
		long craftable = affordableSulphuric / GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC;

		long sulphuricNeeded = craftable * GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC;
		long crafts = Math.ceilDiv(sulphuricNeeded, GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT);
		long coalUsed = crafts * GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;

		return new CraftResult(
				craftable,
				crafts * GabagoolRecipe.ENCHANTED_SULPHUR_PER_CRAFT * GabagoolRecipe.SULPHUR_PER_ENCHANTED_SULPHUR,
				crafts * GabagoolRecipe.ENCHANTED_SULPHUR_PER_CRAFT,
				craftable * GabagoolRecipe.VERY_CRUDE_PER_HYPERGOLIC,
				coal - coalUsed,
				enchantedCoalFor(craftable + 1) - coal,
				crafts * GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT - sulphuricNeeded);
	}

	/**
	 * Enchanted Coal fuer genau so viele Hypergolic Gabagool am Stueck, inklusive
	 * der Vierergruppen-Rundung beim Sulphuric Coal.
	 */
	public static long enchantedCoalFor(long hypergolicAmount) {
		long sulphuric = Math.max(0, hypergolicAmount) * GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC;
		return Math.ceilDiv(sulphuric, GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT)
				* GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;
	}

	private GabagoolCalculator() {
	}
}
