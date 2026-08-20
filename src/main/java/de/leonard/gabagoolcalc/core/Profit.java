package de.leonard.gabagoolcalc.core;

/** Alle Werte in Coins, schon gerundet. */
public record Profit(
		long revenue,
		long tax,
		long costVeryCrude,
		long costEnchantedSulphur,
		long netProfit) {

	public long totalCost() {
		return costVeryCrude + costEnchantedSulphur;
	}
}
