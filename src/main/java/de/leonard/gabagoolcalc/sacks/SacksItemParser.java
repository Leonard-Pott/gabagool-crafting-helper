package de.leonard.gabagoolcalc.sacks;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import de.leonard.gabagoolcalc.GabagoolCalcClient;
import de.leonard.gabagoolcalc.core.SackAmountParser;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

/**
 * Liest die Enchanted-Coal-Menge aus einem offenen Container-Screen.
 * Nur Lesen von bereits sichtbaren Client-Daten, keine Interaktion.
 */
public final class SacksItemParser {

	/**
	 * Primaerweg: Item mit passender Skyblock-ID, Menge aus der
	 * "Stored:"-Lore (Stack-Count ist bei Sacks nur Deko).
	 * Fallback: Sack-Uebersicht, die den Bestand nur als Lore-Zeile des Sack-Items
	 * fuehrt - dort gibt es kein eigenes Item und damit keine NBT-ID.
	 */
	public static OptionalLong find(AbstractContainerScreen<?> screen, String skyblockId, String label) {
		List<Slot> slots = screen.getMenu().slots;

		for (Slot slot : slots) {
			ItemStack stack = slot.getItem();
			if (stack.isEmpty() || !skyblockId.equals(skyblockId(stack))) {
				continue;
			}
			// ponytail: erster Treffer gewinnt. Mehrere Enchanted-Coal-Stacks in einem
			// Screen aufsummieren, falls das im normalen Inventar mal gebraucht wird.
			OptionalLong stored = SackAmountParser.findAmount(lore(stack), label);
			return stored.isPresent() ? stored : OptionalLong.of(stack.getCount());
		}

		for (Slot slot : slots) {
			ItemStack stack = slot.getItem();
			if (stack.isEmpty()) {
				continue;
			}
			for (String line : lore(stack)) {
				OptionalLong amount = SackAmountParser.labelledAmount(line, label);
				if (amount.isPresent()) {
					return amount;
				}
			}
		}
		return OptionalLong.empty();
	}

	/**
	 * Skyblock-Item-ID, null wenn kein Skyblock-Item.
	 *
	 * Auf modernen Clients liefert Hypixel die ExtraAttributes flach im
	 * custom_data, die ID liegt also direkt unter "id" (so macht es auch
	 * Skyblocker). Der verschachtelte Pfad bleibt als Fallback drin.
	 */
	public static String skyblockId(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) {
			return null;
		}
		CompoundTag tag = data.copyTag();
		return tag.getString("id")
				.or(() -> tag.getCompoundOrEmpty("ExtraAttributes").getString("id"))
				.orElse(null);
	}

	/** Schreibt den kompletten Screen-Inhalt ins Log (config: debug=true). */
	public static void dump(AbstractContainerScreen<?> screen) {
		GabagoolCalcClient.LOGGER.info("[dump] Screen: '{}' mit {} Slots",
				screen.getTitle().getString(), screen.getMenu().slots.size());
		for (Slot slot : screen.getMenu().slots) {
			ItemStack stack = slot.getItem();
			if (stack.isEmpty()) {
				continue;
			}
			GabagoolCalcClient.LOGGER.info("[dump] slot {} id={} count={} name='{}' lore={}",
					slot.index, skyblockId(stack), stack.getCount(),
					stack.getHoverName().getString(), lore(stack));
		}
	}

	public static List<String> lore(ItemStack stack) {
		ItemLore itemLore = stack.get(DataComponents.LORE);
		if (itemLore == null) {
			return List.of();
		}
		List<String> lines = new ArrayList<>();
		for (Component line : itemLore.lines()) {
			lines.add(line.getString());
		}
		return lines;
	}

	private SacksItemParser() {
	}
}
