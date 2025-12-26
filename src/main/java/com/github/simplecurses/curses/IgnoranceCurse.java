package com.github.simplecurses.curses;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

public class IgnoranceCurse extends Enchantment {
    public IgnoranceCurse() {
        super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        MinecraftForge.EVENT_BUS.addListener(this::onBlockBreak);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getLevel().isClientSide()) {
            final ItemStack item = event.getPlayer().getMainHandItem();
            final int level = item.getEnchantmentLevel(this);

            if (level > 0 && !item.isCorrectToolForDrops(event.getState())) {
                final ServerPlayer damagerEntity = event.getPlayer() instanceof ServerPlayer ? (ServerPlayer) event.getPlayer() : null;
                item.hurt((int) Math.ceil(item.getMaxDamage() * 0.1), event.getLevel().getRandom(), damagerEntity);

                final Player user = event.getPlayer();
                //event.getLevel().playSound(user, user.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS);
                user.playSound(SoundEvents.ITEM_BREAK); //doesnt work
            }
        }
    }
}
