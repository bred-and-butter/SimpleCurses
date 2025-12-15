package com.github.simplecurses.curses;

import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EchoesCurse extends Enchantment {
    private final List<SoundEvent> sounds = new ArrayList<>();

    private static final Logger LOGGER = LogUtils.getLogger();

    public EchoesCurse() {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});

        //this.sounds.add(SoundEvents.AMBIENT_CAVE);
        this.sounds.add(SoundEvents.ZOMBIE_AMBIENT);
        this.sounds.add(SoundEvents.SKELETON_SHOOT);
        this.sounds.add(SoundEvents.CREEPER_PRIMED);
        this.sounds.add(SoundEvents.PHANTOM_SWOOP);
        this.sounds.add(SoundEvents.ELDER_GUARDIAN_CURSE);
        this.sounds.add(SoundEvents.SPIDER_AMBIENT);
        this.sounds.add(SoundEvents.BLAZE_SHOOT);
        this.sounds.add(SoundEvents.TNT_PRIMED);
        this.sounds.add(SoundEvents.ENDERMAN_SCREAM);
        this.sounds.add(SoundEvents.GHAST_SCREAM);
        this.sounds.add(SoundEvents.SLIME_JUMP);

        MinecraftForge.EVENT_BUS.addListener(this::onUserTick);
    }

    private void onUserTick(PlayerTickEvent event) {
        final Player user = event.player;

        if (user.level().isClientSide() && user.isAlive() && user.tickCount % 200 == 0 && user.getRandom().nextDouble() < 1.0) {
            final int level = EnchantmentHelper.getEnchantmentLevel(this, user);
            LOGGER.info("got enchantment");

            if (level > 0) {
                //plays 2 sounds at once for some reason, research this later
                user.playSound(this.sounds.get(user.getRandom().nextInt(this.sounds.size())));
                LOGGER.info("playing sound");
            }
        }
    }
}
