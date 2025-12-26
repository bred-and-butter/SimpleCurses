package com.github.simplecurses.curses;

//import net.minecraftforge.event.entity.player.
import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.slf4j.Logger;

public class PestilenceCurse extends Enchantment {
    //private static final Logger LOGGER = LogUtils.getLogger();

    public PestilenceCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
        MinecraftForge.EVENT_BUS.addListener(this::onFoodOrDrinkConsumed);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onFoodOrDrinkConsumed(LivingEntityUseItemEvent event) {
        Player player = (Player) event.getEntity();

        if (player != null && !player.level().isClientSide() && player.isAlive()) {
            FoodProperties food = event.getItem().getFoodProperties(player);

            if (food != null) {
                final int level = EnchantmentHelper.getEnchantmentLevel(this, player);

                if (level > 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0, false, false));
                }
            }
        }
    }
}
