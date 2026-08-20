package de.leonard.gabagoolcalc.bazaar;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.core.Prices;

/**
 * Holt die Bazaar-Preise vom oeffentlichen Hypixel-Endpoint. Kein API-Key noetig,
 * kein Login, reines Lesen - die Daten sind fuer alle gleich.
 *
 * Laeuft immer asynchron: der Render-Thread fragt nur den Cache ab und wartet nie
 * auf das Netz. Solange noch nichts da ist, zeigt das Overlay einfach keine Preise.
 */
public final class BazaarApi {

	private static final String URL = "https://api.hypixel.net/v2/skyblock/bazaar";
	private static final Duration MAX_AGE = Duration.ofMinutes(2);
	private static final HttpClient CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.followRedirects(HttpClient.Redirect.NORMAL)
			.build();

	private static final AtomicBoolean FETCHING = new AtomicBoolean();
	private static volatile Prices cached;
	private static volatile long fetchedAt;

	/** Cache-Stand, ohne zu blockieren. Leer, solange noch nichts geladen wurde. */
	public static Optional<Prices> prices() {
		return Optional.ofNullable(cached);
	}

	/** Stoesst bei Bedarf einen Refresh im Hintergrund an. */
	public static void refreshIfStale() {
		if (System.currentTimeMillis() - fetchedAt < MAX_AGE.toMillis()) {
			return;
		}
		if (!FETCHING.compareAndSet(false, true)) {
			return;
		}
		HttpRequest request = HttpRequest.newBuilder(URI.create(URL))
				.header("User-Agent", "gabagoolcalc/1.0 (Fabric mod)")
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(20))
				.GET()
				.build();

		CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(BazaarApi::handle)
				.exceptionally(error -> {
					GabagoolCalcClient.LOGGER.warn("Bazaar-Preise nicht abrufbar: {}", error.getMessage());
					return null;
				})
				.whenComplete((ignored, error) -> {
					fetchedAt = System.currentTimeMillis();
					FETCHING.set(false);
				});
	}

	private static void handle(HttpResponse<String> response) {
		if (response.statusCode() != 200) {
			GabagoolCalcClient.LOGGER.warn("Bazaar-Endpoint antwortete mit HTTP {}", response.statusCode());
			return;
		}
		JsonObject products = JsonParser.parseString(response.body())
				.getAsJsonObject()
				.getAsJsonObject("products");

		cached = new Prices(
				buyPrice(products, "HYPERGOLIC_GABAGOOL"),
				buyPrice(products, "VERY_CRUDE_GABAGOOL"),
				buyPrice(products, "ENCHANTED_SULPHUR"));
	}

	private static double buyPrice(JsonObject products, String productId) {
		JsonObject product = products.getAsJsonObject(productId);
		if (product == null) {
			throw new IllegalStateException("Bazaar kennt " + productId + " nicht");
		}
		return product.getAsJsonObject("quick_status").get("buyPrice").getAsDouble();
	}

	private BazaarApi() {
	}
}
