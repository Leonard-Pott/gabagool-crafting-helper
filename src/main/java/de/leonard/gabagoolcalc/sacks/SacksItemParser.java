package de.leonard.gabagoolcalc.sacks;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import de.leonard.gabagoolcalc.core.GabagoolRecipe;
import de.leonard.gabagoolcalc.core.SackAmountParser;

import net.minecraft.core.component.DataComponents;
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

	private static final String LABEL = "Enchanted Coal";

	/**
	 * Primaerweg: Item mit ExtraAttributes.id == ENCHANTED_COAL, Menge aus der
	 * "Stored:"-Lore (Stack-Count ist bei Sacks nur Deko).
	 * Fallback: Sack-Uebersicht, die den Bestand nur als Lore-Zeile des Sack-Items
	 * fuehrt - dort gibt es kein eigenes Item und damit keine NBT-ID.
	 */
	public static OptionalLong findEnchantedCoal(AbstractContainerScreen<?> screen) {
		List<Slot> slots = screen.getMenu().slots;

		for (Slot slot : slots) {
			ItemStack stack = slot.getItem();
			if (stack.isEmpty() || !GabagoolRecipe.ID_ENCHANTED_COAL.equals(skyblockId(stack))) {
				continue;
			}
			// ponytail: erster Treffer gewinnt. Mehrere Enchanted-Coal-Stacks in einem
			// Screen aufsummieren, falls das im normalen Inventar mal gebraucht wird.
			OptionalLong stored = SackAmountParser.findAmount(lore(stack), LABEL);
			return stored.isPresent() ? stored : OptionalLong.of(stack.getCount());
		}

		for (Slot slot : slots) {
			ItemStack stack = slot.getItem();
			if (stack.isEmpty()) {
				continue;
			}
			for (String line : lore(stack)) {
				OptionalLong amount = SackAmountParser.labelledAmount(line, LABEL);
				if (amount.isPresent()) {
					return amount;
				}
			}
		}
		return OptionalLong.empty();
	}

	/** Skyblock-Item-ID aus ExtraAttributes, null wenn kein Skyblock-Item. */
	public static String skyblockId(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null) {
			return null;
		}
		return data.copyTag().getCompoundOrEmpty("ExtraAttributes").getString("id").orElse(null);
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
