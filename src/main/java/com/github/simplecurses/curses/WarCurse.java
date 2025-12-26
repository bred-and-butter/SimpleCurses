package com.github.simplecurses.curses;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import java.util.List;

public class WarCurse extends Enchantment {
    private static final int AGGRO_RANGE = 20;
    private static final int AGGRO_DURATION = 600;

    public WarCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        // Only process on server side
        if (player.level().isClientSide()) return;
        
        int curseLevel = EnchantmentHelper.getEnchantmentLevel(this, player);
        if (curseLevel > 0 && player.tickCount % 40 == 0) {
            angerNeutralMobs(player);
        }
    }

    private void angerNeutralMobs (Player player) {
        int range = AGGRO_RANGE;

        AABB area = new AABB(
                player.getX() - range, player.getY() - range, player.getZ() - range,
                player.getX() + range, player.getY() + range, player.getZ() + range
        );

        List<Mob> mobsInRange = player.level().getEntitiesOfClass(Mob.class, area);

        for (Mob mob : mobsInRange) {
            if (isNeutralMob(mob) && canBecomeAggressive(mob)) {
                makeAggressive(mob, player);
            }
        }
    }

    private boolean isNeutralMob(Mob mob) {
        return mob instanceof NeutralMob || mob instanceof IronGolem || mob instanceof Wolf || mob instanceof Piglin || isCustomNeutralMob(mob);
    }

    //for custom mob types
    private boolean isCustomNeutralMob(Mob mob) {
        return false;
    }

    private boolean canBecomeAggressive(Mob mob) {
        if (mob instanceof TamableAnimal tamable && tamable.isTame()) {
            return false;
        }

        if (mob.getTarget() != null && mob.getTarget().isAlive()) {
            return false;
        }

        if (mob instanceof IronGolem golem) {
            return !golem.isPlayerCreated() || golem.getPersistentAngerTarget() != null;
        }

        if (mob instanceof Wolf wolf) {
            return !wolf.isTame();
        }

        if (mob instanceof Piglin piglin) {
            return !piglin.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT);
        }

        return true;
    }

    private void makeAggressive(Mob mob, Player player) {
        if (mob instanceof NeutralMob neutralMob) {
            if (neutralMob.getRemainingPersistentAngerTime() <= 0) {
                neutralMob.setRemainingPersistentAngerTime(AGGRO_DURATION);
                neutralMob.setLastHurtByMob(player);
                neutralMob.setTarget(player);
            }
        } else if (mob instanceof Piglin piglin) {
            makePiglinAggressive(piglin, player);
        } else if (mob instanceof IronGolem golem) {
            golem.setTarget(player);
        }
        else if (mob instanceof Wolf wolf) {
            wolf.setTarget(player);
            wolf.setRemainingPersistentAngerTime(AGGRO_DURATION);
        }
        else {
            // Generic mob targeting
            mob.setTarget(player);
        }
    }

    private void makePiglinAggressive(Piglin piglin, Player player) {
        final Brain<Piglin> brain = piglin.getBrain();

        // 1. Make piglin angry at player
        brain.setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, player.getUUID(), AGGRO_DURATION);

        // 2. Set player as attack target
        brain.setMemoryWithExpiry(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, player, AGGRO_DURATION);

        // 3. Force hunting behavior (bypasses gold armor check)
        brain.setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, AGGRO_DURATION);

        // 4. Clear pacification memories
        brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
        brain.eraseMemory(MemoryModuleType.ADMIRING_DISABLED);
    }
}
