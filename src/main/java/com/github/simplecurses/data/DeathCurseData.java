package com.github.simplecurses.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class DeathCurseData {
    private static final String DEATH_CURSE_DATA_KEY = "death_curse_data";
    private static final String LOST_HEALTH_KEY = "death_curse_lost_health";

    public static void setLostHealth(Player player, float healthReduction) {
        CompoundTag data = getOrCreateDeathCurseData(player);

        data.putFloat(LOST_HEALTH_KEY, healthReduction);

        saveData(player, data);
    }

    public static float getLostHealth(Player player) {
        CompoundTag data = getOrCreateDeathCurseData(player);

        if (!data.contains(LOST_HEALTH_KEY)) return 0.0f;

        return data.getFloat(LOST_HEALTH_KEY);
    }

    private static void saveData(Player player, CompoundTag data) {
        player.getPersistentData().put(DEATH_CURSE_DATA_KEY, data);
    }

    private static CompoundTag getOrCreateDeathCurseData(Player player) {
        CompoundTag playerData = player.getPersistentData();
        if (!playerData.contains(DEATH_CURSE_DATA_KEY)) {
            playerData.put(DEATH_CURSE_DATA_KEY, new CompoundTag());
        }

        return playerData.getCompound(DEATH_CURSE_DATA_KEY);
    }
}
