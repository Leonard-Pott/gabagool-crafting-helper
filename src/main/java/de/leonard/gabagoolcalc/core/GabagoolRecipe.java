package de.leonard.gabagoolcalc.core;

/**
 * Rezept-Konstanten fuer Hypergolic Gabagool (Pfad ueber Very Crude Gabagool).
 * Gegengeprueft am NEU-Item-Repo.
 *
 * Kette:
 *   Hypergolic Gabagool = 12x Heavy Gabagool + 1x Sulphuric Coal
 *   Heavy Gabagool      = 24x Fuel Gabagool  + 1x Sulphuric Coal
 *   Fuel Gabagool (alt) = 8x Sulphuric Coal  + 1x Very Crude Gabagool -> 8x Fuel
 *   Sulphuric Coal      = 16x Enchanted Coal + 1x Enchanted Sulphur   -> 4x Sulphuric
 *
 *   Sulphuric Coal gesamt = 1 (top level) + 12 (Heavy) + 288 (Fuel) = 301
 *   Fuel gesamt = 12 * 24 = 288 -> 288 / 8 = 36 Very Crude Gabagool
 *
 * Wichtig: Sulphuric Coal entsteht nur in Vierergruppen. Fuer 301 Stueck sind
 * 76 Crafts noetig, die 304 ergeben und 1216 Enchanted Coal kosten - nicht 1204.
 * Die 3 uebrigen Sulphuric Coal wandern in den naechsten Hypergolic-Craft, der
 * deshalb nur noch 1200 Enchanted Coal kostet. Erst ueber 4 Hypergolic hinweg
 * geht die Rechnung glatt auf (4 * 1204 = 4816). Deswegen wird hier nicht mit
 * einer Pauschale pro Stueck gerechnet, sondern mit Batches.
 */
public final class GabagoolRecipe {
	public static final int SULPHURIC_COAL_PER_HYPERGOLIC = 301;
	public static final int VERY_CRUDE_PER_HYPERGOLIC = 36;

	/** Ein Sulphuric-Coal-Craft: 16 Enchanted Coal + 1 Enchanted Sulphur -> 4 Stueck. */
	public static final int SULPHURIC_COAL_PER_CRAFT = 4;
	public static final int ENCHANTED_COAL_PER_CRAFT = 16;
	public static final int ENCHANTED_SULPHUR_PER_CRAFT = 1;

	/** 1x Enchanted Sulphur = 5x 32 Sulphur. */
	public static final int SULPHUR_PER_ENCHANTED_SULPHUR = 160;

	/** Materialwert pro Stueck, wenn nichts uebrig bleibt: 301 * 4. */
	public static final int ENCHANTED_COAL_PER_HYPERGOLIC = 1204;
	public static final int SULPHUR_PER_HYPERGOLIC = 12040;

	/** Skyblock Item-IDs (custom_data.id), nicht Display-Namen. */
	public static final String ID_ENCHANTED_COAL = "ENCHANTED_COAL";
	public static final String ID_SULPHURIC_COAL = "SULPHURIC_COAL";

	private GabagoolRecipe() {
	}
}
