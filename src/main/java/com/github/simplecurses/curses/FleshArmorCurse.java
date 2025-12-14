package com.github.simplecurses.curses;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;

import java.util.UUID;

public class FleshArmorCurse extends Enchantment {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final EquipmentSlot[] slots = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };
    private static final UUID[] UUIDSlots = {
            UUID.fromString("a17222c2-ac5a-4939-b7ee-da8f375bcb07"), //head
            UUID.fromString("a74d2944-0cd4-4456-b066-d159a70ec2ba"), //chest
            UUID.fromString("ffb657e4-02b7-4079-9522-4a77f7e87814"), //legs
            UUID.fromString("73b255a6-8968-4d7f-83d2-27aea19fafca"), //feet
    };

    public FleshArmorCurse() {
        super(
                Rarity.UNCOMMON,
                EnchantmentCategory.ARMOR,
                new EquipmentSlot[]{
                        EquipmentSlot.HEAD,
                        EquipmentSlot.CHEST,
                        EquipmentSlot.LEGS,
                        EquipmentSlot.FEET
                }
        );
        MinecraftForge.EVENT_BUS.addListener(this::onEquipmentChange);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    public void onEquipmentChange(LivingEquipmentChangeEvent event){
        LivingEntity entity = event.getEntity();
        ItemStack equipment = event.getTo();
        EquipmentSlot slot = event.getSlot();

        if (entity instanceof Player && (
                slot == EquipmentSlot.HEAD ||
                slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS ||
                slot == EquipmentSlot.FEET
        )
        ) {
            int slotIndex = switch (slot) {
                case HEAD -> 0;
                case CHEST -> 1;
                case LEGS -> 2;
                case FEET -> 3;
                default -> 0;
            };

            final int level = EnchantmentHelper.getTagEnchantmentLevel(this, equipment);

            AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
            AttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
            assert (maxHealth != null) && (armor != null);

            maxHealth.removeModifier(UUIDSlots[slotIndex]);
            armor.removeModifier(UUIDSlots[slotIndex]);

            if (level > 0) {
                maxHealth.addTransientModifier(new AttributeModifier(
                        UUIDSlots[slotIndex],
                        "Curse of Flesh Armor",
                        -2f * level,
                        AttributeModifier.Operation.ADDITION
                ));

                armor.addTransientModifier(new AttributeModifier(
                        UUIDSlots[slotIndex],
                        "Curse of Flesh Armor",
                        2f * level,
                        AttributeModifier.Operation.ADDITION
                ));

                LOGGER.info(String.format("%f %f", entity.getMaxHealth(), entity.getHealth()));
                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth()));
            }
        }
    }
}
