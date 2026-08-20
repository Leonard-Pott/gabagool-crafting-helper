package de.leonard.gabagoolcalc;

import de.leonard.gabagoolcalc.sacks.SackStock;
import de.leonard.gabagoolcalc.sacks.SacksItemParser;
import de.leonard.gabagoolcalc.sacks.SacksScreenDetector;
import de.leonard.gabagoolcalc.ui.HypergolicOverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GabagoolCalcClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("gabagoolcalc");

	@Override
	public void onInitializeClient() {
		GabagoolConfig.load();

		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			GabagoolConfig.load();

			if (!SacksScreenDetector.isAnySackScreen(screen)) {
				return;
			}
			AbstractContainerScreen<?> container = (AbstractContainerScreen<?>) screen;

			if (GabagoolConfig.debug) {
				SacksItemParser.dump(container);
			}

			// Jeder Sack aktualisiert, was er hergibt: Enchanted Coal steckt im
			// Mining Sack, Sulphuric Coal im Nether Sack.
			SackStock.update(container);
			ScreenEvents.afterTick(screen).register(s -> SackStock.update(container));

			if (!SacksScreenDetector.isTargetScreen(screen)) {
				return;
			}
			HypergolicOverlay overlay = new HypergolicOverlay();
			overlay.update();
			ScreenEvents.afterTick(screen).register(s -> overlay.update());
			ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> overlay.render(graphics));
		});
	}
}
