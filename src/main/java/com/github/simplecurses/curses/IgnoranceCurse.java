package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

public class IgnoranceCurse extends Enchantment {
    public IgnoranceCurse() {
        super(Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        MinecraftForge.EVENT_BUS.addListener(this::onBlockBreak);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onBlockBreak(BlockEvent.BreakEvent event) {

    }
}
