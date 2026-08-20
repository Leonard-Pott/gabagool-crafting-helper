# Hypergolic Gabagool Calculator

Client-Mod fuer Hypixel Skyblock (Minecraft 26.1.2, Fabric). Beim Oeffnen des
Enchanted Mining Sack blendet sie ein Overlay ein:

```
Hypergolic Gabagool
Enchanted Coal: 12,345
Craftbar: 10x
  Enchanted Sulphur: 753
    (= 120,400 Sulphur)
  Very Crude Gabagool: 360
  Rest-Coal: 305
Bis zum naechsten: 899 Coal
```

Reine Anzeige: liest nur die ohnehin sichtbaren Item-Daten des offenen GUIs und
rechnet. Kein Auto-Click, kein Auto-Craft, keine Packets.

## Rezept (pro 1x Hypergolic Gabagool, Pfad ueber Very Crude Gabagool)

| Zutat | 1. Craft | jeder weitere |
|---|---|---|
| Enchanted Coal | 1216 | 1200 |
| Enchanted Sulphur | 76 | 75 |
| Very Crude Gabagool | 36 | 36 |

Sulphuric Coal entsteht nur in Vierergruppen (16 Enchanted Coal + 1 Enchanted
Sulphur ergeben 4 Stueck). Fuer die 301 pro Hypergolic sind also 76 Crafts
noetig, die 304 ergeben - die 3 uebrigen wandern in den naechsten Craft. Ueber
4 Hypergolic hinweg mittelt sich das auf den Materialwert 1204 pro Stueck ein.

Herleitung siehe `core/GabagoolRecipe.java`.

## Bauen

```
./gradlew build
```

Ergebnis: `build/libs/gabagoolcalc-1.0.0.jar`. Das Jar in den `mods/`-Ordner der
Instanz legen (oder in der Modrinth App ueber "Add content" -> "From file").
Braucht nur Fabric Loader >= 0.19.3 und Fabric API.

Dev-Client zum Testen: `./gradlew runClient`

## Config

`config/gabagoolcalc.properties` (wird beim ersten Start angelegt, wird bei jedem
Oeffnen eines Sack-Screens neu gelesen - kein Neustart noetig):

```properties
overlay=true
x=6
y=6
screen=Enchanted Mining Sack
debug=false
```

`screen` ist der Teilstring des Screen-Titels, bei dem das Overlay erscheint.
`debug=true` schreibt beim Oeffnen jedes Sack-Screens dessen kompletten Inhalt
(Slot, Skyblock-ID, Count, Lore) ins Log - noetig, falls Hypixel das Format aendert.

## Aufbau

- `core/` - reine Rechen- und Parse-Logik, keine Minecraft-Imports, per JUnit getestet
- `sacks/` - Screen-Erkennung ueber den Titel aus der Config und Auslesen der
  Item-Daten ueber die Skyblock-ID `custom_data.id` = `ENCHANTED_COAL`
  (Hypixel liefert die ExtraAttributes auf modernen Clients flach im custom_data)
- `ui/` - Overlay-Rendering
