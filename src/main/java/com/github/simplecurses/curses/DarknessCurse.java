package com.github.simplecurses.curses;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;


public class DarknessCurse extends Enchantment {
    public DarknessCurse() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::onUserTick);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onUserTick (LivingTickEvent event) {
        final LivingEntity user = event.getEntity();

        if (user != null && !user.level().isClientSide() && user.isAlive() && user.tickCount % 10 == 0) {
            final int level = EnchantmentHelper.getEnchantmentLevel(this, user);

            if (level > 0) {
                user.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 70, level-1, false, false));
            }
        }
    }
}
