package com.github.simplecurses.curses;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class EchoesCurse extends Enchantment {
    private final List<SoundEvent> sounds = new ArrayList<>();

    public EchoesCurse() {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});

        this.sounds.add(SoundEvents.AMBIENT_CAVE.value());
        this.sounds.add(SoundEvents.ZOMBIE_AMBIENT);
        this.sounds.add(SoundEvents.SKELETON_SHOOT);
        this.sounds.add(SoundEvents.CREEPER_PRIMED);
        this.sounds.add(SoundEvents.PHANTOM_SWOOP);
        this.sounds.add(SoundEvents.ELDER_GUARDIAN_CURSE);
        this.sounds.add(SoundEvents.SPIDER_AMBIENT);
        this.sounds.add(SoundEvents.BLAZE_SHOOT);
        this.sounds.add(SoundEvents.TNT_PRIMED);
        this.sounds.add(SoundEvents.ENDERMAN_SCREAM);
        this.sounds.add(SoundEvents.GHAST_SHOOT);
        this.sounds.add(SoundEvents.SLIME_JUMP);

        MinecraftForge.EVENT_BUS.addListener(this::onUserTick);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onUserTick(PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        final Player player = event.player;

        if (player.level().isClientSide() && player.isAlive() && player.tickCount % 1200 == 0 && player.getRandom().nextDouble() < 0.2) {
            final int level = EnchantmentHelper.getEnchantmentLevel(this, player);

            if (level > 0) {
                player.playSound(this.sounds.get(player.getRandom().nextInt(this.sounds.size())));
            }
        }
    }
}
