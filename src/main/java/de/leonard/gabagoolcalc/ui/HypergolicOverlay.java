package de.leonard.gabagoolcalc.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalLong;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.GabagoolConfig;
import de.leonard.gabagoolcalc.core.CraftResult;
import de.leonard.gabagoolcalc.core.GabagoolCalculator;
import de.leonard.gabagoolcalc.sacks.SacksItemParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Text-Overlay fuer einen offenen Sack-Screen. Reine Anzeige.
 * Berechnet einmal pro Client-Tick, gerendert wird nur der zwischengespeicherte Text.
 */
public final class HypergolicOverlay {

	private static final int COLOR_TITLE = 0xFFFFAA00;
	private static final int COLOR_TEXT = 0xFFFFFFFF;
	private static final int COLOR_HINT = 0xFFAAAAAA;
	private static final int COLOR_BACKDROP = 0xC0101010;
	private static final int PADDING = 4;

	private List<Line> lines = List.of();

	public void update(AbstractContainerScreen<?> screen) {
		try {
			OptionalLong coal = SacksItemParser.findEnchantedCoal(screen);
			lines = coal.isPresent() ? build(coal.getAsLong()) : List.of();
		} catch (RuntimeException e) {
			// Parsing kaputt (Hypixel aendert die GUI) -> Overlay ausblenden statt crashen
			GabagoolCalcClient.LOGGER.debug("Sack-Parsing fehlgeschlagen", e);
			lines = List.of();
		}
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

	private static List<Line> build(long coal) {
		CraftResult result = GabagoolCalculator.calculate(coal);
		List<Line> out = new ArrayList<>();
		out.add(new Line("Hypergolic Gabagool", COLOR_TITLE));
		out.add(new Line("Enchanted Coal: " + num(coal), COLOR_TEXT));
		out.add(new Line("Craftbar: " + num(result.craftableAmount()) + "x", COLOR_TITLE));
		if (result.craftableAmount() > 0) {
			out.add(new Line("  Sulphur: " + num(result.neededSulphur()), COLOR_TEXT));
			out.add(new Line("  Very Crude Gabagool: " + num(result.neededVeryCrude()), COLOR_TEXT));
			out.add(new Line("  Rest-Coal: " + num(result.remainingCoal()), COLOR_HINT));
		}
		out.add(new Line("Bis zum naechsten: " + num(result.coalToNextCraft()) + " Coal", COLOR_HINT));
		return List.copyOf(out);
	}

	private static String num(long value) {
		return String.format(Locale.US, "%,d", value);
	}

	private record Line(String text, int color) {
	}
}
