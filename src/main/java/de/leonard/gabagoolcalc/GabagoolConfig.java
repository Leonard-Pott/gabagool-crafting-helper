package de.leonard.gabagoolcalc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Minimal-Config als .properties in config/gabagoolcalc.properties.
 * Wird beim Oeffnen eines Sack-Screens neu gelesen, also ohne Neustart wirksam.
 */
public final class GabagoolConfig {

	public static boolean overlayEnabled = true;
	public static int overlayX = 6;
	public static int overlayY = 6;

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("gabagoolcalc.properties");
	}

	public static void load() {
		Path path = file();
		Properties props = new Properties();
		try {
			if (Files.exists(path)) {
				try (InputStream in = Files.newInputStream(path)) {
					props.load(in);
				}
			} else {
				save();
				return;
			}
			overlayEnabled = Boolean.parseBoolean(props.getProperty("overlay", "true"));
			overlayX = Integer.parseInt(props.getProperty("x", "6").trim());
			overlayY = Integer.parseInt(props.getProperty("y", "6").trim());
		} catch (IOException | NumberFormatException e) {
			GabagoolCalcClient.LOGGER.warn("Config nicht lesbar, nutze Defaults", e);
		}
	}

	public static void save() {
		Properties props = new Properties();
		props.setProperty("overlay", Boolean.toString(overlayEnabled));
		props.setProperty("x", Integer.toString(overlayX));
		props.setProperty("y", Integer.toString(overlayY));
		try {
			Files.createDirectories(file().getParent());
			try (OutputStream out = Files.newOutputStream(file())) {
				props.store(out, "Hypergolic Gabagool Calculator");
			}
		} catch (IOException e) {
			GabagoolCalcClient.LOGGER.warn("Config nicht schreibbar", e);
		}
	}

	private GabagoolConfig() {
	}
}
