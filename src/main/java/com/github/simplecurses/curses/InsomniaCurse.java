package com.github.simplecurses.curses;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;


public class InsomniaCurse extends Enchantment {
    private static boolean hasSpawnedPhantoms = false;

    public InsomniaCurse() {
        super(Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
        MinecraftForge.EVENT_BUS.addListener(this::preventSleepEvent);
        MinecraftForge.EVENT_BUS.addListener(this::phantomSpawnChecks);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void preventSleepEvent(PlayerSleepInBedEvent event) {
        final Player player = event.getEntity();
        final ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        final int level = EnchantmentHelper.getTagEnchantmentLevel(this, helmet);

        if (level > 0 && event.getResultStatus() == null) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("You feel restless and cannot sleep."), true);
        }
    }

    private void phantomSpawnChecks(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.level.isClientSide()) return;

        Level level = event.level;
        int time = (int) (level.getDayTime() % 24000);

        // Avoid spawning multiple times in same night, checks every 30 seconds
        if (time >= 13000 && time <= 23000 && time % 600 == 0 && !hasSpawnedPhantoms) {
            hasSpawnedPhantoms = true;

            // Spawn phantoms for all cursed players
            for (Player player : level.players()) {
                if (this.hasCurse(player)) {
                    this.spawnPhantoms(player, level);
                }
            }
        } else if (time >= 0 && time <= 12999 && time % 600 == 0 && hasSpawnedPhantoms) {
            hasSpawnedPhantoms = false;
        }
    }


    private void spawnPhantoms(Player player, Level level) {
        if (!player.isAlive()) return;

        BlockPos blockpos = player.blockPosition();
        DifficultyInstance difficultyInstance = level.getCurrentDifficultyAt(blockpos);
        RandomSource randomSource = level.getRandom();

        // Spawn 1-3 phantoms
        int phantomCount = 1 + randomSource.nextInt(difficultyInstance.getDifficulty().getId() + 1);

        for (int i = 0; i < phantomCount; i++) {
            Phantom phantom = EntityType.PHANTOM.create(level);
            if (phantom != null) {
                // Calculate spawn position (20-30 blocks above player)
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 10.0;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 10.0;

                phantom.moveTo(
                        player.getX()+ offsetX,
                        player.getY() + 20 + player.getRandom().nextInt(10),
                        player.getZ() + offsetZ,
                        player.getRandom().nextFloat() * 360.0F,
                        0.0F
                );

                // Target the cursed player
                phantom.setTarget(player);

                level.addFreshEntity(phantom);
            }
        }
    }

    private boolean hasCurse(Player player) {
        final ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        final int level = EnchantmentHelper.getTagEnchantmentLevel(this, helmet);

        return level > 0;
    }
}
