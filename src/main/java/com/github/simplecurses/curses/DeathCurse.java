package com.github.simplecurses.curses;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static com.github.simplecurses.data.DeathCurseData.*;
import static net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DeathCurse extends Enchantment {
    private static final float MIN_MAX_HEALTH = 4.0f;
    private int hitsUntilHPLoss = 0;
    private int killsUntilHPRecover = 0;

    public DeathCurse() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity attacker, @NotNull Entity target, int level) {
        if (!target.isAlive()) this.killsUntilHPRecover++;


        if (this.killsUntilHPRecover == 3) {
            this.killsUntilHPRecover = 0;

            float healthLost = getLostHealth((Player) attacker) - 1;

            if (healthLost < 0) return;

            setLostHealth((Player) attacker, healthLost);

            AttributeInstance maxHP = attacker.getAttribute(MAX_HEALTH);
            assert maxHP != null;
            updateMaxHP(maxHP, healthLost, (Player) attacker);
        }
    }

    @Override
    public void doPostHurt(@NotNull LivingEntity attacker, @NotNull Entity target, int level) {
        this.hitsUntilHPLoss++;

        if (this.hitsUntilHPLoss == 5) {
            this.hitsUntilHPLoss = 0;

            float healthLost = getLostHealth((Player) attacker) + 1;

            if (attacker.getMaxHealth() < MIN_MAX_HEALTH) return;

            setLostHealth((Player) attacker, healthLost);

            AttributeInstance maxHP = attacker.getAttribute(MAX_HEALTH);
            assert maxHP != null;
            updateMaxHP(maxHP, healthLost, (Player) attacker);

            //attacker.playSound(net.minecraft.sounds.SoundEvents.GLASS_BREAK, 1.0f, 1.0f);
        }
    }

    private void updateMaxHP (@NotNull AttributeInstance maxHP, float healthLost, Player player) {
        maxHP.removeModifier(UUID.fromString("e281e03a-2e4b-4aa8-a7b2-eefb48aa8535"));
        maxHP.addPermanentModifier(new AttributeModifier(
                UUID.fromString("e281e03a-2e4b-4aa8-a7b2-eefb48aa8535"),
                "Curse of Death",
                -healthLost,
                AttributeModifier.Operation.ADDITION
        ));
    }
}
