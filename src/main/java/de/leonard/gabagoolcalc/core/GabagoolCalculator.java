package de.leonard.gabagoolcalc.core;

/** Reine Rechenlogik, keine Minecraft-Imports. */
public final class GabagoolCalculator {

	public static CraftResult calculate(long enchantedCoal) {
		return calculate(enchantedCoal, 0);
	}

	/**
	 * @param sulphuricCoalStock schon vorhandene Sulphuric Coal (Nether Sack),
	 *                           die nicht mehr gecraftet werden muessen
	 */
	public static CraftResult calculate(long enchantedCoal, long sulphuricCoalStock) {
		long coal = Math.max(0, enchantedCoal);
		long stock = Math.max(0, sulphuricCoalStock);

		// Enchanted Coal reicht fuer so viele Sulphuric-Coal-Crafts a 4 Stueck.
		long affordableCrafts = coal / GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;
		long available = stock + affordableCrafts * GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT;
		long craftable = available / GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC;

		long sulphuricNeeded = craftable * GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC;
		long crafts = Math.ceilDiv(Math.max(0, sulphuricNeeded - stock),
				GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT);
		long coalUsed = crafts * GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;

		return new CraftResult(
				craftable,
				crafts * GabagoolRecipe.ENCHANTED_SULPHUR_PER_CRAFT * GabagoolRecipe.SULPHUR_PER_ENCHANTED_SULPHUR,
				crafts * GabagoolRecipe.ENCHANTED_SULPHUR_PER_CRAFT,
				craftable * GabagoolRecipe.VERY_CRUDE_PER_HYPERGOLIC,
				coal - coalUsed,
				enchantedCoalFor(craftable + 1, stock) - coal,
				stock + crafts * GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT - sulphuricNeeded);
	}

	/**
	 * Enchanted Coal fuer genau so viele Hypergolic Gabagool am Stueck, inklusive
	 * der Vierergruppen-Rundung beim Sulphuric Coal.
	 */
	public static long enchantedCoalFor(long hypergolicAmount) {
		return enchantedCoalFor(hypergolicAmount, 0);
	}

	public static long enchantedCoalFor(long hypergolicAmount, long sulphuricCoalStock) {
		long missing = Math.max(0, hypergolicAmount) * GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC
				- Math.max(0, sulphuricCoalStock);
		return Math.ceilDiv(Math.max(0, missing), GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT)
				* GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT;
	}

	private GabagoolCalculator() {
	}
}
