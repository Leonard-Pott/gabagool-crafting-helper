package de.leonard.gabagoolcalc.core;

/**
 * Rezept-Konstanten fuer 1x Hypergolic Gabagool (Pfad ueber Very Crude Gabagool).
 *
 * Herleitung:
 *   Hypergolic Gabagool = 12x Heavy Gabagool + 1x Sulphuric Coal
 *   Heavy Gabagool      = 24x Fuel Gabagool  + 1x Sulphuric Coal
 *   Fuel Gabagool (alt) = 8x Sulphuric Coal  + 1x Very Crude Gabagool -> ergibt 8x Fuel
 *   Sulphuric Coal      = 4x Enchanted Coal  + 40x Sulphur
 *
 *   Sulphuric Coal gesamt = 1 (top level) + 12 (Heavy) + 288 (Fuel) = 301
 *     -> 301 * 4  = 1204 Enchanted Coal
 *     -> 301 * 40 = 12040 Sulphur
 *   Fuel Gabagool gesamt = 12 * 24 = 288 -> 288 / 8 = 36 Very Crude Gabagool
 */
public final class GabagoolRecipe {
	public static final int SULPHURIC_COAL_PER_HYPERGOLIC = 301;
	public static final int ENCHANTED_COAL_PER_HYPERGOLIC = 1204;
	public static final int SULPHUR_PER_HYPERGOLIC = 12040;
	public static final int VERY_CRUDE_PER_HYPERGOLIC = 36;

	/**
	 * 1x Enchanted Sulphur = 5x 32 Sulphur (Craft-Rezept), also 160 Sulphur.
	 * 12040 / 160 = 75.25 -> aufgerundet 76 Enchanted Sulphur pro Hypergolic,
	 * denn am Bazaar gibt es nur ganze Enchanted Sulphur.
	 */
	public static final int SULPHUR_PER_ENCHANTED_SULPHUR = 160;

	/** Skyblock Item-IDs (ExtraAttributes.id), nicht Display-Namen. */
	public static final String ID_ENCHANTED_COAL = "ENCHANTED_COAL";

	private GabagoolRecipe() {
	}
}
