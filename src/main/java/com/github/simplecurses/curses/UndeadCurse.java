package com.github.simplecurses.curses;

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

public class UndeadCurse extends Enchantment {
    public UndeadCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::checkIfUnderSunlight);
        MinecraftForge.EVENT_BUS.addListener(this::onApplicablePotion);
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

        if (time % 200 == 0 && time > 300 && time < 13000) {
            for (Player player : level.players()) {
                if (this.hasCurse(player) && level.canSeeSky(player.blockPosition())) {
                    player.setRemainingFireTicks(60);
                }
            }
        }
    }

    private void onApplicablePotion(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (hasCurse(player)) {
            // 1. Instant Healing damages undead
            if (event.getEffectInstance().getEffect() == MobEffects.HEAL) {
                player.hurt(player.damageSources().magic(), 3.0F * (event.getEffectInstance().getAmplifier() + 1));
                event.setResult(Event.Result.DENY);
            }

            // 2. Instant Damage heals undead
            if (event.getEffectInstance().getEffect() == MobEffects.HARM) {
                player.heal(3.0F * (event.getEffectInstance().getAmplifier() + 1));
                event.setResult(Event.Result.DENY);
            }
        }

    }

}
