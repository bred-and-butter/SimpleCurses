package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerSpawnPhantomsEvent;


public class InsomniaCurse extends Enchantment {
    public InsomniaCurse() {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::preventSleepEvent);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void preventSleepEvent(PlayerSleepInBedEvent event) {
        final Player player = event.getEntity();
        final ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        final int level = EnchantmentHelper.getTagEnchantmentLevel(this, helmet);

        if (level > 0 && event.getResultStatus() == null) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("You feel restless and cannot sleep."), true);
        }
    }
}
