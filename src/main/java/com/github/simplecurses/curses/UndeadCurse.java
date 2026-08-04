package com.github.simplecurses.curses;

import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.slf4j.Logger;

public class UndeadCurse extends Enchantment {
    private static final Logger LOGGER = LogUtils.getLogger();

    public UndeadCurse() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::checkIfUnderSunlight);
        //MinecraftForge.EVENT_BUS.addListener(this::onApplicablePotion);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private boolean hasCurse(Player player) {
        final int level = EnchantmentHelper.getEnchantmentLevel(this, player);
        return level > 0;
    }

    private void checkIfUnderSunlight(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.level.isClientSide()) return;

        Level level = event.level;
        int time = (int) (level.getDayTime() % 24000);

        if (time % 60 == 0 && time > 300 && time < 13000) {
            for (Player player : level.players()) {
                if (this.hasCurse(player) && level.canSeeSky(player.blockPosition())) {
                    player.setRemainingFireTicks(60);
                }
            }
        }
    }

    private void onApplicablePotion(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        //LOGGER.info("entered event");

        if (hasCurse(player)) {
            // 1. Instant Healing damages undead
            if (event.getEffectInstance().getEffect() == MobEffects.HEAL) {
                //LOGGER.info("entered heal handler");
                player.hurt(player.damageSources().magic(), 3.0F * (event.getEffectInstance().getAmplifier() + 1));
                event.setResult(Event.Result.DENY);
            }

            // 2. Instant Damage heals undead
            if (event.getEffectInstance().getEffect() == MobEffects.HARM) {
                //LOGGER.info("entered harm handler");
                player.heal(3.0F * (event.getEffectInstance().getAmplifier() + 1));
                event.setResult(Event.Result.DENY);
            }
        }

    }

}
