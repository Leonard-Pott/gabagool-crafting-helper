package de.leonard.gabagoolcalc.core;

/**
 * Bazaar-Stueckpreise in Coins, jeweils von der Buchseite, die zur eigenen
 * Handelsart passt. Die API-Feldnamen sind dabei invertiert:
 *
 *   hypergolic       = quick_status.buyPrice  (Kopf des Sell-Offer-Buchs)
 *                      -> Preis, zu dem das eigene Sell Offer gefuellt wird
 *   veryCrude        = quick_status.sellPrice (Kopf des Kauforder-Buchs)
 *   enchantedSulphur = quick_status.sellPrice -> Preis, zu dem die eigene
 *                      Buy Order gefuellt wird, guenstiger als Sofortkauf
 */
public record Prices(double hypergolic, double veryCrude, double enchantedSulphur) {
}
