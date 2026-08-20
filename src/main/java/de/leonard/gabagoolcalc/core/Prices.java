package de.leonard.gabagoolcalc.core;

/**
 * Bazaar-Stueckpreise in Coins. Alle drei kommen aus dem Feld quick_status.buyPrice,
 * also vom Kopf des Sell-Offer-Buchs:
 *   - was man zahlt, wenn man Very Crude Gabagool / Enchanted Sulphur sofort kauft
 *   - was man bekommt, wenn das eigene Sell Offer fuer Hypergolic gefuellt wird
 * (Das API-Feld heisst "buyPrice", weil es die Seite ist, von der man kauft -
 * sellPrice waere der Sofortverkauf und damit deutlich weniger.)
 */
public record Prices(double hypergolic, double veryCrude, double enchantedSulphur) {
}
