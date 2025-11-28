package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
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
    private final AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "Curse of Misfortune", -10f, AttributeModifier.Operation.ADDITION);

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
        if (event.getSlotType() == LivingEntity.getEquipmentSlotForItem(event.getItemStack()) && this.isValidSlot(event.getSlotType()) && event.getItemStack().hasTag()) {
            final int level = EnchantmentHelper.getTagEnchantmentLevel(this, event.getItemStack());
            this.applyModifiers(level, event);
        }
    }

    public void applyModifiers(int level, ItemAttributeModifierEvent event) {
        if (level > 0) {
            event.addModifier(Attributes.LUCK, this.modifier);
        }
    }
    
}
