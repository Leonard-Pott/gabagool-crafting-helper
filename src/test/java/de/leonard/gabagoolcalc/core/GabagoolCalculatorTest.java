package de.leonard.gabagoolcalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GabagoolCalculatorTest {

	@Test
	void keinCoal() {
		CraftResult r = GabagoolCalculator.calculate(0);
		assertEquals(0, r.craftableAmount());
		assertEquals(0, r.neededSulphur());
		assertEquals(0, r.neededEnchantedSulphur());
		assertEquals(0, r.neededVeryCrude());
		assertEquals(0, r.remainingCoal());
		assertEquals(0, r.leftoverSulphuricCoal());
		assertEquals(1216, r.coalToNextCraft());
	}

	@Test
	void materialwertAlleineReichtNicht() {
		// 1204 ist der reine Materialwert. Sulphuric Coal entsteht aber nur in
		// Vierergruppen: 75 Crafts = 300 Stueck, das 301. fehlt.
		CraftResult r = GabagoolCalculator.calculate(1204);
		assertEquals(0, r.craftableAmount());
		assertEquals(12, r.coalToNextCraft());
	}

	@Test
	void ersterCraftKostet1216() {
		CraftResult r = GabagoolCalculator.calculate(1216);
		assertEquals(1, r.craftableAmount());
		assertEquals(76, r.neededEnchantedSulphur());
		assertEquals(76 * 160, r.neededSulphur());
		assertEquals(36, r.neededVeryCrude());
		assertEquals(0, r.remainingCoal());
		// 76 Crafts ergeben 304, gebraucht werden 301
		assertEquals(3, r.leftoverSulphuricCoal());
		// die 3 uebrigen machen den zweiten Craft billiger als den ersten
		assertEquals(1200, r.coalToNextCraft());
	}

	@Test
	void einsZuWenig() {
		CraftResult r = GabagoolCalculator.calculate(1215);
		assertEquals(0, r.craftableAmount());
		assertEquals(1, r.coalToNextCraft());
	}

	@Test
	void zweiterCraftNutztDenRest() {
		CraftResult r = GabagoolCalculator.calculate(2416);
		assertEquals(2, r.craftableAmount());
		assertEquals(0, r.remainingCoal());
		assertEquals(151, r.neededEnchantedSulphur());
		assertEquals(2, r.leftoverSulphuricCoal());
	}

	@Test
	void vierCraftsGehenGlattAuf() {
		// 4 * 301 = 1204 Sulphuric Coal = genau 301 Crafts, kein Rest
		CraftResult r = GabagoolCalculator.calculate(4 * 1204);
		assertEquals(4, r.craftableAmount());
		assertEquals(301, r.neededEnchantedSulphur());
		assertEquals(0, r.leftoverSulphuricCoal());
		assertEquals(0, r.remainingCoal());
	}

	@Test
	void fuenftausend() {
		CraftResult r = GabagoolCalculator.calculate(5000);
		assertEquals(4, r.craftableAmount());
		assertEquals(5000 - 4816, r.remainingCoal());
		assertEquals(4 * 36, r.neededVeryCrude());
	}

	@Test
	void grosseZahlenOhneOverflow() {
		CraftResult r = GabagoolCalculator.calculate(1_000_000_000L);
		assertEquals(830564, r.craftableAmount());
		assertEquals(62_499_941L, r.neededEnchantedSulphur());
	}

	@Test
	void negativWirdWieNullBehandelt() {
		assertEquals(0, GabagoolCalculator.calculate(-42).craftableAmount());
	}

	@Test
	void kostenProStueckMittelnSichAufDenMaterialwertEin() {
		// Ueber 4 Stueck hinweg ist der Schnitt exakt der Materialwert 1204.
		assertEquals(4L * GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC,
				GabagoolCalculator.enchantedCoalFor(4));
		assertEquals(1216, GabagoolCalculator.enchantedCoalFor(1));
		assertEquals(2416, GabagoolCalculator.enchantedCoalFor(2));
	}

	@Test
	void rezeptKonstantenPassenZusammen() {
		assertEquals(GabagoolRecipe.ENCHANTED_COAL_PER_HYPERGOLIC,
				GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC
						* (GabagoolRecipe.ENCHANTED_COAL_PER_CRAFT / GabagoolRecipe.SULPHURIC_COAL_PER_CRAFT));
		assertEquals(GabagoolRecipe.SULPHUR_PER_HYPERGOLIC,
				GabagoolRecipe.SULPHURIC_COAL_PER_HYPERGOLIC * 40);
	}
}
