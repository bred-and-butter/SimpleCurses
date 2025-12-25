package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class RuinCurse extends Enchantment {
    public RuinCurse() {
        super(Rarity.RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onAnvilUpdate);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onAnvilUpdate(AnvilUpdateEvent event) {
        if (EnchantmentHelper.getTagEnchantmentLevel(this, event.getLeft()) > 0 || EnchantmentHelper.getTagEnchantmentLevel(this, event.getRight()) > 0) {
            event.setOutput(ItemStack.EMPTY);
            event.setCanceled(true);
        }
    }
}
