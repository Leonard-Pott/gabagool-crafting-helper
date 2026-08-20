package de.leonard.gabagoolcalc.core;

/** Reine Rechenlogik, keine Minecraft-Imports. */
public final class GabagoolCalculator {

	public static CraftResult calculate(long enchantedCoal) {
		long coal = Math.max(0, enchantedCoal);
		long craftable = coal / GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC;
		long remaining = coal % GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC;
		return new CraftResult(
				craftable,
				craftable * GabagoolRecipe.SULPHUR_PER_HYPERGOLIC,
				craftable * GabagoolRecipe.VERY_CRUDE_PER_HYPERGOLIC,
				remaining,
				GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC - remaining);
	}

	private GabagoolCalculator() {
	}
}
