package de.leonard.gabagoolcalc.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.OptionalLong;

import org.junit.jupiter.api.Test;

class SackAmountParserTest {

	@Test
	void storedMitFarbcodesUndKappe() {
		assertEquals(OptionalLong.of(1234),
				SackAmountParser.storedAmount("\u00a77Stored: \u00a7a1,234\u00a77/\u00a7a10M"));
	}

	@Test
	void storedOhneFormatierung() {
		assertEquals(OptionalLong.of(48000), SackAmountParser.storedAmount("Stored: 48,000"));
	}

	@Test
	void labelZeileAusSackUebersicht() {
		assertEquals(OptionalLong.of(9001),
				SackAmountParser.labelledAmount("\u00a77Enchanted Coal: \u00a7a9,001", "Enchanted Coal"));
	}

	@Test
	void storedGewinntGegenLabel() {
		List<String> lore = List.of("\u00a77Enchanted Coal", "\u00a77Stored: \u00a7a500\u00a77/\u00a7a10M");
		assertEquals(OptionalLong.of(500), SackAmountParser.findAmount(lore, "Enchanted Coal"));
	}

	@Test
	void nichtsGefunden() {
		assertTrue(SackAmountParser.findAmount(List.of("\u00a77Click to open!"), "Enchanted Coal").isEmpty());
	}

	@Test
	void abkuerzungen() {
		assertEquals(OptionalLong.of(12_000), SackAmountParser.parseNumber("12k"));
		assertEquals(OptionalLong.of(3_000_000), SackAmountParser.parseNumber("3M"));
	}

	@Test
	void muellIstLeer() {
		assertTrue(SackAmountParser.parseNumber("abc").isEmpty());
	}
}
