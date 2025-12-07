package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class MisfortuneCurse extends Enchantment {

    private final EquipmentSlot[] slots = EquipmentSlot.values();

    private static final UUID[] UUIDSlots = {
            UUID.fromString("a04eebbc-f63d-4885-8e5b-f431e152307f"), //head
            UUID.fromString("8126b90d-e3a6-41bf-bdba-64ccf4581caa"), //chest
            UUID.fromString("24545cf0-55fe-4ebd-8236-6d093204725b"), //legs
            UUID.fromString("b50fa1dc-2c99-4403-b7a6-c2a63c0bb666"), //feet
            UUID.fromString("5892abe8-6883-4686-9d78-0658ee40c800"), //mainhand tools
            UUID.fromString("e4e96284-9a02-430d-8563-71af43090c08"), //offhand tools
    };

    public MisfortuneCurse() {
		super(Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
        MinecraftForge.EVENT_BUS.addListener(this::checkModifiers);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private boolean isValidSlot (EquipmentSlot slot) {
        for (final EquipmentSlot validSlot : this.slots) {
            if (validSlot == slot) {
                return true;
            }
        }

        return false;
    }

    private void checkModifiers(ItemAttributeModifierEvent event) {
        EquipmentSlot slot = event.getSlotType();

        if (slot == LivingEntity.getEquipmentSlotForItem(event.getItemStack()) && this.isValidSlot(slot) && event.getItemStack().hasTag()) {
            final int level = EnchantmentHelper.getTagEnchantmentLevel(this, event.getItemStack());

            int slotIndex = switch (slot) {
                case HEAD -> 0;
                case CHEST -> 1;
                case LEGS -> 2;
                case FEET -> 3;
                case MAINHAND -> 4;
                case OFFHAND -> 5;
            };

            this.applyModifiers(level, slotIndex, event);
        }
    }

    public void applyModifiers(int level, int slotIndex, ItemAttributeModifierEvent event) {
        if (level > 0) {
            AttributeModifier modifier = new AttributeModifier(
                    UUIDSlots[slotIndex],
                    "Curse of Misfortune",
                    -5f,
                    AttributeModifier.Operation.ADDITION);

            event.addModifier(Attributes.LUCK, modifier);
        }
    }
}
