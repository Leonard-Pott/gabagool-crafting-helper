package de.leonard.gabagoolcalc.core;

/**
 * Was bleibt haengen, wenn man die fertigen Hypergolic Gabagool per Sell Offer
 * verkauft. Enchanted Coal taucht nicht auf: die wird selbst abgebaut, nicht
 * gekauft, und waere in einer Gewinnrechnung nur ein fiktiver Posten.
 */
public final class ProfitCalculator {

	public static Profit calculate(long hypergolicAmount, long veryCrudeAmount, long enchantedSulphurAmount,
			Prices prices, double taxPercent) {
		double revenue = hypergolicAmount * prices.hypergolic();
		double tax = revenue * Math.max(0, taxPercent) / 100.0;
		double veryCrude = veryCrudeAmount * prices.veryCrude();
		double sulphur = enchantedSulphurAmount * prices.enchantedSulphur();

		return new Profit(
				Math.round(revenue),
				Math.round(tax),
				Math.round(veryCrude),
				Math.round(sulphur),
				Math.round(revenue - tax - veryCrude - sulphur));
	}

	private ProfitCalculator() {
	}
}
