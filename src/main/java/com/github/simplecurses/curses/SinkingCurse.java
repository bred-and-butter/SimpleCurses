package com.github.simplecurses.curses;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SinkingCurse extends Enchantment {
    public SinkingCurse() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
        MinecraftForge.EVENT_BUS.addListener(this::onUserTick);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onUserTick(LivingEvent.LivingTickEvent event) {
        final LivingEntity user = event.getEntity();

        if (user != null && user.isAlive() && user.isInWaterOrBubble()) {
            final int level = EnchantmentHelper.getEnchantmentLevel(this, user);

            if (level > 0) {
                final Vec3 motion = user.getDeltaMovement();
                user.setDeltaMovement(motion.x(), -0.05, motion.z());
            }
        }
    }
}
