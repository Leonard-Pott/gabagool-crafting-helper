# Hypergolic Gabagool Calculator

Client-Mod fuer **Hypixel Skyblock** auf **Minecraft 26.1.2 / Fabric**.

Beim Oeffnen des Enchanted Mining Sack rechnet sie aus, wie viele Hypergolic
Gabagool du mit deiner vorhandenen Enchanted Coal craften kannst, und was du
dafuer noch am Bazaar brauchst:

```
Hypergolic Gabagool
Enchanted Coal: 12,500
Sulphuric Coal: 3
Craftbar: 10x
  Enchanted Sulphur: 752
    (= 120,320 Sulphur)
  Very Crude Gabagool: 360
  Rest: 468 Enchanted Coal
  Uebrig: 2 Sulphuric Coal
Bazaar (Buy Order / Sell Offer)
  Very Crude: -26.32M
  Ench. Sulphur: -1.26M
  Verkauf: +55.99M (-0.70M Steuer)
  Gewinn: 27.71M
Bis zum naechsten: 732 Enchanted Coal
```

## Keine Automation

Die Mod liest ausschliesslich die Item-Daten des GUIs, das du selbst geoeffnet
hast, und zeigt eine Rechnung an. Kein Auto-Click, kein Auto-Craft, keine
Packets an den Server. Sie stellt nur anders dar, was ohnehin auf deinem
Bildschirm steht.

Einzige Ausnahme: fuer die Preise fragt sie alle 2 Minuten den oeffentlichen
Bazaar-Endpoint `api.hypixel.net/v2/skyblock/bazaar` ab. Das ist reines Lesen
oeffentlicher Marktdaten, ohne API-Key und ohne dass irgendetwas ueber dich
oder deinen Account uebertragen wird. Abschaltbar mit `bazaar=false`.

## Installation

Braucht **nur Fabric Loader >= 0.19.3 und Fabric API** auf Minecraft 26.1.2 -
kein bestimmtes Modpack, keine anderen Mods. Das Jar in den `mods/`-Ordner der
Instanz legen und Minecraft neu starten.

Fertige Jars gibt es unter [Releases](../../releases), oder selbst bauen:

```
./gradlew build
```

Ergebnis: `build/libs/gabagoolcalc-1.0.0.jar`. Ein JDK musst du nicht
installieren, Gradle laedt das noetige JDK 25 selbst nach.

## Wie gerechnet wird

Pfad ueber Very Crude Gabagool:

```
Hypergolic Gabagool = 12x Heavy Gabagool + 1x Sulphuric Coal
Heavy Gabagool      = 24x Fuel Gabagool  + 1x Sulphuric Coal
Fuel Gabagool       = 8x Sulphuric Coal  + 1x Very Crude Gabagool -> 8x Fuel
Sulphuric Coal      = 16x Enchanted Coal + 1x Enchanted Sulphur   -> 4x Sulphuric
```

Macht **301 Sulphuric Coal** und **36 Very Crude Gabagool** pro Hypergolic.

Sulphuric Coal entsteht aber nur in Vierergruppen. Fuer 301 Stueck sind 76
Crafts noetig, die 304 ergeben - der erste Hypergolic kostet damit **1216**
Enchanted Coal, nicht die oft genannten 1204. Die 3 uebrigen Sulphuric Coal
wandern in den naechsten Craft, der dadurch nur noch 1200 kostet. Ueber vier
Stueck hinweg mittelt es sich auf 1204 pro Stueck ein.

| | 1. Craft | 2. | 3. | 4. |
|---|---|---|---|---|
| Enchanted Coal | 1216 | 1200 | 1200 | 1200 |
| Enchanted Sulphur | 76 | 75 | 75 | 75 |
| Very Crude Gabagool | 36 | 36 | 36 | 36 |

Angezeigt wird Enchanted Sulphur statt rohem Sulphur, weil man es so am Bazaar
kauft (1x Enchanted Sulphur = 160x Sulphur). Alle Rezepte sind gegen das
NEU-Item-Repo geprueft.

## Nether Sack

Sulphuric Coal, die schon im Nether Sack liegt, muss nicht mehr gecraftet
werden und senkt den Bedarf entsprechend. Da immer nur ein GUI offen ist,
merkt sich die Mod den Bestand: Nether Sack einmal aufmachen, danach rechnet
der Mining Sack damit weiter - auch nach einem Neustart.

Solange der Nether Sack noch nie offen war, steht `Sulphuric Coal: ?` statt
einer stillen 0. Gerechnet wird dann mit 0, angezeigt wird also nie zu viel.

Die Zeile `Uebrig: N Sulphuric Coal` ist der Bestand nach allen moeglichen
Crafts - der Teil, den man verkaufen kann, ohne sich den naechsten Craft zu
zerschiessen.

## Bazaar-Rechnung

Zeigt, was die Zutaten kosten und was am Ende haengen bleibt:

Gerechnet wird durchgehend mit geduldigem Handel, also mit dem Preis, zu dem
die eigene Order gefuellt wird - nicht mit Sofortkauf/Sofortverkauf:

- **Verkauf** als **Sell Offer**: rund 590k mehr pro Stueck als Sofortverkauf.
- **Zutaten** per **Buy Order**: rund 320k guenstiger pro Craft als Sofortkauf.
- **Steuer**: 1.25% auf den Verkauf, per `bazaar.tax` anpassbar. Mit maximalem
  Bazaar-Flipper-Perk sind es 1.0%.
- **Enchanted Coal taucht nicht auf**, weil sie selbst abgebaut und nicht
  gekauft wird - sie waere in einer Gewinnrechnung ein fiktiver Posten.

## Config

`config/gabagoolcalc.properties`, wird beim ersten Start angelegt und bei jedem
Oeffnen eines Sacks neu gelesen. Kein Neustart noetig.

```properties
overlay=true
x=6
y=6
screen=Enchanted Mining Sack
bazaar=true
bazaar.tax=1.25
debug=false
```

- `screen` - Teilstring des Screen-Titels, bei dem das Overlay erscheint
- `debug=true` - schreibt beim Oeffnen jedes Sacks dessen kompletten Inhalt
  (Slot, Skyblock-ID, Count, Lore) ins Log. Noetig, falls Hypixel das Format
  aendert und Zahlen fehlen.
- die `stock.*`-Werte schreibt die Mod selbst, `-1` heisst "noch nie gesehen"

## Aufbau

- `core/` - Rezept, Rechen- und Gewinnlogik, Lore-Parsing; ohne
  Minecraft-Imports und per JUnit getestet (`./gradlew test`)
- `bazaar/` - Preisabruf, asynchron und gecacht, blockiert nie den Render-Thread
- `sacks/` - Screen-Erkennung, Auslesen der Item-Daten ueber die Skyblock-ID
  (`custom_data.id`, nicht ueber Display-Namen) und der gemerkte Bestand
- `ui/` - Overlay-Rendering ueber `ScreenEvents`, ohne Mixin

## Lizenz

MIT, siehe [LICENSE](LICENSE).
