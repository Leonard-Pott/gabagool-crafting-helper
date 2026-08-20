package de.leonard.gabagoolcalc;

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

			if (GabagoolConfig.debug && SacksScreenDetector.isAnySackScreen(screen)) {
				SacksItemParser.dump((AbstractContainerScreen<?>) screen);
			}
			if (!SacksScreenDetector.isTargetScreen(screen)) {
				return;
			}

			AbstractContainerScreen<?> container = (AbstractContainerScreen<?>) screen;
			HypergolicOverlay overlay = new HypergolicOverlay();
			overlay.update(container);

			ScreenEvents.afterTick(screen).register(s -> overlay.update(container));
			ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, delta) -> overlay.render(graphics));
		});
	}
}
