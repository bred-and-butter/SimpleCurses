package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class FleshArmorCurse extends Enchantment {

    private final EquipmentSlot[] slots = EquipmentSlot.values();
    private static final UUID[] UUIDSlots = {
            UUID.fromString("a04eebbc-f63d-4885-8e5b-f431e152307f"), //head
            UUID.fromString("8126b90d-e3a6-41bf-bdba-64ccf4581caa"), //chest
            UUID.fromString("24545cf0-55fe-4ebd-8236-6d093204725b"), //legs
            UUID.fromString("b50fa1dc-2c99-4403-b7a6-c2a63c0bb666"), //feet
    };

    public FleshArmorCurse() {
        super(
                Rarity.UNCOMMON,
                EnchantmentCategory.ARMOR,
                new EquipmentSlot[]{
                        EquipmentSlot.HEAD,
                        EquipmentSlot.CHEST,
                        EquipmentSlot.LEGS,
                        EquipmentSlot.FEET
                }
        );
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
                default -> 0;
            };

            this.applyModifiers(level, slotIndex, event);
        }
    }

    public void applyModifiers(int level, int slotIndex, ItemAttributeModifierEvent event) {
        if (level > 0) {
            AttributeModifier modifierArmor = new AttributeModifier(
                    UUIDSlots[slotIndex],
                    "Curse of Flesh Armor",
                    1f,
                    AttributeModifier.Operation.MULTIPLY_BASE);

            //max health lost only takes effect when the player takes damage for some reason, research how to deal with that later
            AttributeModifier modifierHealth = new AttributeModifier(
                    UUIDSlots[slotIndex],
                    "Curse of Flesh Armor",
                    -0.2f,
                    AttributeModifier.Operation.MULTIPLY_BASE);

            event.addModifier(Attributes.ARMOR, modifierArmor);
            event.addModifier(Attributes.MAX_HEALTH, modifierHealth);
        }
    }
}
