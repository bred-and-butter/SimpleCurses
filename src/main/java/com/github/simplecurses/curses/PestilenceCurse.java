package com.github.simplecurses.curses;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.List;

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

        Level level = entity.level();

        final int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, entity);
        if (enchantmentLevel <= 0) return;

        if (!entity.level().isClientSide() && entity.isAlive()) {
            FoodProperties food = event.getItem().getFoodProperties(entity);

            if (food != null && !entity.hasEffect(MobEffects.POISON)) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
            } else if (food != null && entity.hasEffect(MobEffects.POISON)) {
                int radius = 3; // 3,4,5 blocks
                AABB area = new AABB(
                        entity.getX() - radius, entity.getY() - radius, entity.getZ() - radius,
                        entity.getX() + radius, entity.getY() + radius, entity.getZ() + radius
                );
                List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, area,
                        closeEntities -> closeEntities != entity && closeEntities.isAlive());

                for (LivingEntity closeEntity : nearbyEntities) {
                    closeEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                }
            }
        }
    }
}
