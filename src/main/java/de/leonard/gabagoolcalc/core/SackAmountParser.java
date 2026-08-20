package de.leonard.gabagoolcalc.core;

import java.util.List;
import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reines Text-Parsing der Sack-Lore. Keine Minecraft-Imports, damit testbar.
 *
 * Zwei Formate kommen im Spiel vor:
 *   1. Item liegt einzeln im Sack-GUI  -> Lore-Zeile "Stored: 1,234"
 *   2. Sack-Uebersicht (/sacks)        -> Lore-Zeile "Enchanted Coal: 1,234"
 */
public final class SackAmountParser {

	private static final Pattern FORMATTING = Pattern.compile("(?i)\u00a7[0-9a-fklmnor]");
	private static final Pattern STORED = Pattern.compile("(?i)stored:? *([0-9][0-9,. ]*[kmb]?)");

	/** Entfernt Minecraft-Farbcodes (§a etc.). */
	public static String strip(String line) {
		return line == null ? "" : FORMATTING.matcher(line).replaceAll("").trim();
	}

	/** "§7Stored: §a1,234§7/§a10M" -> 1234 */
	public static OptionalLong storedAmount(String loreLine) {
		Matcher m = STORED.matcher(strip(loreLine));
		return m.find() ? parseNumber(m.group(1)) : OptionalLong.empty();
	}

	/** "§7Enchanted Coal: §a1,234" mit label "Enchanted Coal" -> 1234 */
	public static OptionalLong labelledAmount(String loreLine, String label) {
		String clean = strip(loreLine);
		Matcher m = Pattern.compile("(?i)" + Pattern.quote(label) + " *:? *x? *([0-9][0-9,. ]*[kmb]?)")
				.matcher(clean);
		return m.find() ? parseNumber(m.group(1)) : OptionalLong.empty();
	}

	/**
	 * Sucht in einer kompletten Lore erst nach "Stored:", dann nach dem Label.
	 */
	public static OptionalLong findAmount(List<String> lore, String label) {
		for (String line : lore) {
			OptionalLong stored = storedAmount(line);
			if (stored.isPresent()) {
				return stored;
			}
		}
		for (String line : lore) {
			OptionalLong labelled = labelledAmount(line, label);
			if (labelled.isPresent()) {
				return labelled;
			}
		}
		return OptionalLong.empty();
	}

	/** "1,234" -> 1234, "12k" -> 12000. Nur was Hypixel wirklich anzeigt. */
	public static OptionalLong parseNumber(String raw) {
		String s = raw.replaceAll("[,. ]", "").toLowerCase();
		long factor = 1;
		if (s.endsWith("k")) {
			factor = 1_000L;
		} else if (s.endsWith("m")) {
			factor = 1_000_000L;
		} else if (s.endsWith("b")) {
			factor = 1_000_000_000L;
		}
		if (factor > 1) {
			s = s.substring(0, s.length() - 1);
		}
		if (s.isEmpty()) {
			return OptionalLong.empty();
		}
		try {
			return OptionalLong.of(Long.parseLong(s) * factor);
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	private SackAmountParser() {
	}
}
