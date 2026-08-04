package com.github.simplecurses.curses;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;

public class WiltingCurse extends Enchantment {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int BASE_RADIUS = 3;

    public WiltingCurse() {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide) return;

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.isEmpty()) return;

        final int curseLevel = EnchantmentHelper.getEnchantmentLevel(this, player);

        if (curseLevel > 0) {
            if (level.getGameTime() % 200 == 0 && level.getRandom().nextDouble() < 0.5) { //mudar isso pra 0.2 dps
                LOGGER.info("initiating plant wilting");

                regressCropsAroundPlayer(player);
            }
        }
    }

    private void regressCropsAroundPlayer(Player player) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        int radius = BASE_RADIUS;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -1; y <= 1; y++) { // Check one block below and above
                    BlockPos checkPos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    if (isCrop(state) && level.getRandom().nextDouble() < 0.5) {
                        LOGGER.info("crop found, wilting it");
                        regressCrop(level, checkPos, state);
                    }
                }
            }
        }
    }

    private boolean isCrop(BlockState state) {
        Block block = state.getBlock();
        return block instanceof CropBlock ||
               block instanceof StemBlock ||
               block instanceof AttachedStemBlock;

    }

    private static void regressCrop(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();

        if (block instanceof CropBlock) {
            int age = state.getValue(CropBlock.AGE);
            if (age > 0) {
                int newAge = Math.max(0, age - level.random.nextInt(2) - 1);
                level.setBlock(pos, state.setValue(CropBlock.AGE, newAge), 3);

                // Optional: Add particle effect
                level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                     pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                     0, 0, 0);
            }
        } else if (block instanceof StemBlock) {
            int age = state.getValue(StemBlock.AGE);
            if (age > 0) {
                int newAge = Math.max(0, age - level.random.nextInt(2) - 1);
                level.setBlock(pos, state.setValue(StemBlock.AGE, newAge), 3);
            }
        }
    }
}
