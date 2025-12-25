package com.github.simplecurses.init;

import com.github.simplecurses.SimpleCurses;
import com.github.simplecurses.curses.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SimpleCurses.MODID);

    public static final RegistryObject<Enchantment> DARKNESS = ENCHANTMENTS.register("darkness", DarknessCurse::new);
    public static final RegistryObject<Enchantment> MISFORTUNE = ENCHANTMENTS.register("misfortune", MisfortuneCurse::new);
    public static final RegistryObject<Enchantment> FLESHARMOR = ENCHANTMENTS.register("flesh_armor", FleshArmorCurse::new);
    public static final RegistryObject<Enchantment> INSOMNIA = ENCHANTMENTS.register("insomnia", InsomniaCurse::new);
    public static final RegistryObject<Enchantment> ECHOES = ENCHANTMENTS.register("echoes", EchoesCurse::new);
    public static final RegistryObject<Enchantment> SINKING = ENCHANTMENTS.register("sinking", SinkingCurse::new);
    public static final RegistryObject<Enchantment> RUIN = ENCHANTMENTS.register("ruin", RuinCurse::new);
    public static final RegistryObject<Enchantment> IGNORANCE = ENCHANTMENTS.register("ignorance", IgnoranceCurse::new);
    public static final RegistryObject<Enchantment> WAR = ENCHANTMENTS.register("war", WarCurse::new);
}
