package de.leonard.gabagoolcalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProfitCalculatorTest {

	private static final Prices PRICES = new Prices(10_000_000, 100_000, 1_000);

	@Test
	void einStueckMitSteuer() {
		Profit p = ProfitCalculator.calculate(1, 36, 76, PRICES, 1.25);
		assertEquals(10_000_000, p.revenue());
		assertEquals(125_000, p.tax());
		assertEquals(3_600_000, p.costVeryCrude());
		assertEquals(76_000, p.costEnchantedSulphur());
		assertEquals(3_676_000, p.totalCost());
		assertEquals(10_000_000 - 125_000 - 3_676_000, p.netProfit());
	}

	@Test
	void ohneSteuerBleibtMehr() {
		Profit mit = ProfitCalculator.calculate(1, 36, 76, PRICES, 1.25);
		Profit ohne = ProfitCalculator.calculate(1, 36, 76, PRICES, 0);
		assertEquals(0, ohne.tax());
		assertEquals(mit.netProfit() + 125_000, ohne.netProfit());
	}

	@Test
	void skaliertLinearMitDerMenge() {
		Profit eins = ProfitCalculator.calculate(1, 36, 76, PRICES, 1.25);
		Profit vier = ProfitCalculator.calculate(4, 144, 301, PRICES, 1.25);
		assertEquals(4 * eins.revenue(), vier.revenue());
		assertEquals(4 * eins.tax(), vier.tax());
		assertEquals(4 * eins.costVeryCrude(), vier.costVeryCrude());
	}

	@Test
	void teureZutatenErgebenVerlust() {
		Profit p = ProfitCalculator.calculate(1, 36, 76, new Prices(1_000_000, 100_000, 1_000), 1.25);
		assertTrue(p.netProfit() < 0, "36x 100k Zutaten koennen sich bei 1M Verkaufspreis nicht lohnen");
	}

	@Test
	void negativeSteuerWirdIgnoriert() {
		assertEquals(0, ProfitCalculator.calculate(1, 36, 76, PRICES, -5).tax());
	}
}
