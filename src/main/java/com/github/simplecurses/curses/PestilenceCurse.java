package com.github.simplecurses.curses;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class PestilenceCurse extends Enchantment {

    public PestilenceCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
        MinecraftForge.EVENT_BUS.addListener(this::onFoodOrDrinkConsumed);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onFoodOrDrinkConsumed(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();

        if (entity == null) return;

        final int level = EnchantmentHelper.getEnchantmentLevel(this, entity);
        if (level <= 0) return;

        if (!entity.level().isClientSide() && entity.isAlive()) {
            FoodProperties food = event.getItem().getFoodProperties(entity);

            if (food != null) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0, false, false));
            }
        }
    }
}
