package de.leonard.gabagoolcalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GabagoolCalculatorTest {

	@Test
	void keinCoal() {
		CraftResult r = GabagoolCalculator.calculate(0);
		assertEquals(0, r.craftableAmount());
		assertEquals(0, r.neededSulphur());
		assertEquals(0, r.neededVeryCrude());
		assertEquals(0, r.remainingCoal());
		assertEquals(1204, r.coalToNextCraft());
	}

	@Test
	void genauEinCraft() {
		CraftResult r = GabagoolCalculator.calculate(1204);
		assertEquals(1, r.craftableAmount());
		assertEquals(12040, r.neededSulphur());
		assertEquals(36, r.neededVeryCrude());
		assertEquals(0, r.remainingCoal());
		assertEquals(1204, r.coalToNextCraft());
	}

	@Test
	void einsZuWenig() {
		CraftResult r = GabagoolCalculator.calculate(1203);
		assertEquals(0, r.craftableAmount());
		assertEquals(1203, r.remainingCoal());
		assertEquals(1, r.coalToNextCraft());
	}

	@Test
	void fuenftausend() {
		CraftResult r = GabagoolCalculator.calculate(5000);
		assertEquals(4, r.craftableAmount());
		assertEquals(4 * 12040, r.neededSulphur());
		assertEquals(4 * 36, r.neededVeryCrude());
		assertEquals(5000 - 4 * 1204, r.remainingCoal());
	}

	@Test
	void grosseZahlenOhneOverflow() {
		CraftResult r = GabagoolCalculator.calculate(1_000_000_000L);
		assertEquals(830564, r.craftableAmount());
		assertEquals(830564L * 12040L, r.neededSulphur());
	}

	@Test
	void negativWirdWieNullBehandelt() {
		assertEquals(0, GabagoolCalculator.calculate(-42).craftableAmount());
	}

	@Test
	void rezeptKonstantenPassenZuSulphuricCoal() {
		assertEquals(GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC,
				GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC * 4);
		assertEquals(GabagoolRecipe.SULPHUR_PER_HYPERGOLIC,
				GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC * 40);
	}
}
