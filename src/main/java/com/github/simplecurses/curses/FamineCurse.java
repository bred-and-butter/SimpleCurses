package com.github.simplecurses.curses;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;

public class FamineCurse extends Enchantment {
    private static final Logger LOGGER = LogUtils.getLogger();
    public FamineCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        if (player.level().getGameTime() % 20 != 0) return;
        if (player.isCreative() || player.isSpectator()) return;

        final int level = EnchantmentHelper.getEnchantmentLevel(this, player);
        if (level > 0) {
            FoodData foodData = player.getFoodData();
            float exhaustion = foodData.getExhaustionLevel();

            LOGGER.info(String.valueOf(exhaustion));
            foodData.addExhaustion((float) (0.5));
        }
    }
}
