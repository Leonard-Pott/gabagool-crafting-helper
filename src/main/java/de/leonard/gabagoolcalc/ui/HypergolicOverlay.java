package de.leonard.gabagoolcalc.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalLong;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.GabagoolConfig;
import de.leonard.gabagoolcalc.core.CraftResult;
import de.leonard.gabagoolcalc.bazaar.BazaarApi;
import de.leonard.gabagoolcalc.core.GabagoolCalculator;
import de.leonard.gabagoolcalc.core.Prices;
import de.leonard.gabagoolcalc.core.Profit;
import de.leonard.gabagoolcalc.core.ProfitCalculator;
import de.leonard.gabagoolcalc.sacks.SackStock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Text-Overlay fuer einen offenen Sack-Screen. Reine Anzeige.
 * Berechnet einmal pro Client-Tick, gerendert wird nur der zwischengespeicherte Text.
 */
public final class HypergolicOverlay {

	private static final int COLOR_TITLE = 0xFFFFAA00;
	private static final int COLOR_TEXT = 0xFFFFFFFF;
	private static final int COLOR_HINT = 0xFFAAAAAA;
	private static final int COLOR_PROFIT = 0xFF55FF55;
	private static final int COLOR_LOSS = 0xFFFF5555;
	private static final int COLOR_BACKDROP = 0xC0101010;
	private static final int PADDING = 4;

	private List<Line> lines = List.of();

	public void update() {
		lines = build(SackStock.enchantedCoal(), SackStock.sulphuricCoal());
	}

	public void render(GuiGraphicsExtractor graphics) {
		if (!GabagoolConfig.overlayEnabled || lines.isEmpty()) {
			return;
		}
		Font font = Minecraft.getInstance().font;
		int width = 0;
		for (Line line : lines) {
			width = Math.max(width, font.width(line.text()));
		}
		int x = GabagoolConfig.overlayX;
		int y = GabagoolConfig.overlayY;
		int height = lines.size() * font.lineHeight;

		graphics.fill(x - PADDING, y - PADDING, x + width + PADDING, y + height + PADDING, COLOR_BACKDROP);
		int lineY = y;
		for (Line line : lines) {
			graphics.text(font, line.text(), x, lineY, line.color());
			lineY += font.lineHeight;
		}
	}

	private static List<Line> build(OptionalLong coal, OptionalLong sulphuric) {
		if (coal.isEmpty()) {
			return List.of(
					new Line("Hypergolic Gabagool", COLOR_TITLE),
					new Line("Keine Enchanted Coal in diesem Sack gefunden", COLOR_HINT));
		}
		return craftLines(coal.getAsLong(), sulphuric);
	}

	private static List<Line> craftLines(long coal, OptionalLong sulphuric) {
		CraftResult result = GabagoolCalculator.calculate(coal, sulphuric.orElse(0));
		List<Line> out = new ArrayList<>();
		out.add(new Line("Hypergolic Gabagool", COLOR_TITLE));
		out.add(new Line("Enchanted Coal: " + num(coal), COLOR_TEXT));
		if (sulphuric.isPresent()) {
			out.add(new Line("Sulphuric Coal: " + num(sulphuric.getAsLong()), COLOR_TEXT));
		} else {
			out.add(new Line("Sulphuric Coal: ? (Nether Sack oeffnen)", COLOR_HINT));
		}
		out.add(new Line("Craftbar: " + num(result.craftableAmount()) + "x", COLOR_TITLE));
		if (result.craftableAmount() > 0) {
			out.add(new Line("  Enchanted Sulphur: " + num(result.neededEnchantedSulphur()), COLOR_TEXT));
			out.add(new Line("    (= " + num(result.neededSulphur()) + " Sulphur)", COLOR_HINT));
			out.add(new Line("  Very Crude Gabagool: " + num(result.neededVeryCrude()), COLOR_TEXT));
			out.add(new Line("  Rest: " + num(result.remainingCoal()) + " Enchanted Coal", COLOR_HINT));
			if (result.leftoverSulphuricCoal() > 0) {
				out.add(new Line("  Uebrig: " + num(result.leftoverSulphuricCoal()) + " Sulphuric Coal", COLOR_HINT));
			}
		}
		appendBazaar(out, result);
		out.add(new Line("Bis zum naechsten: " + num(result.coalToNextCraft()) + " Enchanted Coal", COLOR_HINT));
		return List.copyOf(out);
	}

	/**
	 * Beide Seiten geduldig gehandelt: Zutaten per Buy Order, Verkauf per Sell
	 * Offer. Gerechnet wird jeweils mit dem Kopf des Buchs, an dem die eigene
	 * Order gefuellt wird.
	 */
	private static void appendBazaar(List<Line> out, CraftResult result) {
		if (!GabagoolConfig.bazaar) {
			return;
		}
		BazaarApi.refreshIfStale();
		Optional<Prices> prices = BazaarApi.prices();
		if (prices.isEmpty()) {
			out.add(new Line("Bazaar: laedt...", COLOR_HINT));
			return;
		}

		boolean preview = result.craftableAmount() == 0;
		CraftResult basis = preview
				? GabagoolCalculator.calculate(GabagoolCalculator.enchantedCoalFor(1))
				: result;
		Profit profit = ProfitCalculator.calculate(basis.craftableAmount(), basis.neededVeryCrude(),
				basis.neededEnchantedSulphur(), prices.get(), GabagoolConfig.bazaarTax);

		out.add(new Line(preview
				? "Bazaar (Buy Order / Sell Offer, pro 1x)"
				: "Bazaar (Buy Order / Sell Offer)", COLOR_TITLE));
		out.add(new Line("  Very Crude: -" + coins(profit.costVeryCrude()), COLOR_TEXT));
		out.add(new Line("  Ench. Sulphur: -" + coins(profit.costEnchantedSulphur()), COLOR_TEXT));
		out.add(new Line("  Verkauf: +" + coins(profit.revenue())
				+ " (-" + coins(profit.tax()) + " Steuer)", COLOR_TEXT));
		out.add(new Line("  Gewinn: " + coins(profit.netProfit()),
				profit.netProfit() >= 0 ? COLOR_PROFIT : COLOR_LOSS));
	}

	/** 5598610 -> "5.60M", damit die Zeile nicht ausfranst. */
	private static String coins(long value) {
		double abs = Math.abs(value);
		String text;
		if (abs >= 1_000_000) {
			text = String.format(Locale.US, "%.2fM", abs / 1_000_000);
		} else if (abs >= 1_000) {
			text = String.format(Locale.US, "%.1fk", abs / 1_000);
		} else {
			text = String.format(Locale.US, "%.0f", abs);
		}
		return (value < 0 ? "-" : "") + text;
	}

	private static String num(long value) {
		return String.format(Locale.US, "%,d", value);
	}

	private record Line(String text, int color) {
	}
}
