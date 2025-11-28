package com.github.simplecurses.init;

import com.github.simplecurses.SimpleCurses;
import com.github.simplecurses.curses.DarknessCurse;
import com.github.simplecurses.curses.MisfortuneCurse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SimpleCurses.MODID);

    public static final RegistryObject<Enchantment> DARKNESS = ENCHANTMENTS.register("darkness", DarknessCurse::new);
    public static final RegistryObject<Enchantment> MISFORTUNE = ENCHANTMENTS.register("misfortune", MisfortuneCurse::new);
}
