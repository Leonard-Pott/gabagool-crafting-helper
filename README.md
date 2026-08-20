# Hypergolic Gabagool Calculator

Client-Mod fuer Hypixel Skyblock (Minecraft 26.1.2, Fabric). Beim Oeffnen eines
Sack-Screens (`/sacks`, "Mining Sack", ...) blendet sie ein Overlay ein:

```
Hypergolic Gabagool
Enchanted Coal: 12,345
Craftbar: 10x
  Sulphur: 120,400
  Very Crude Gabagool: 360
  Rest-Coal: 305
Bis zum naechsten: 899 Coal
```

Reine Anzeige: liest nur die ohnehin sichtbaren Item-Daten des offenen GUIs und
rechnet. Kein Auto-Click, kein Auto-Craft, keine Packets.

## Rezept (pro 1x Hypergolic Gabagool, Pfad ueber Very Crude Gabagool)

| Zutat | Menge |
|---|---|
| Enchanted Coal | 1204 |
| Sulphur (roh) | 12040 |
| Very Crude Gabagool | 36 |

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
```

## Aufbau

- `core/` - reine Rechen- und Parse-Logik, keine Minecraft-Imports, per JUnit getestet
- `sacks/` - Screen-Erkennung (Titel enthaelt "sack") und Auslesen der Item-Daten
  ueber die Skyblock-ID `ExtraAttributes.id` = `ENCHANTED_COAL`
- `ui/` - Overlay-Rendering
