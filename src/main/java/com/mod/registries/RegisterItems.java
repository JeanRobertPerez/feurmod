package com.mod.registries;

import java.util.function.Function;

import com.mod.FeurMod;
import com.mod.items.ItemBaseballBat;

import com.mod.items.ItemGrapple;
import com.mod.items.ItemGrenade;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RegisterItems {

	public static final Item BASEBALL_BAT = register("baseball_bat", ItemBaseballBat::new, new Item.Settings().maxCount(1));
	public static final Item GRENADE = register("grenade", ItemGrenade::new, new Item.Settings().maxCount(8));
	public static final Item GRAPPLE = register("grapple", ItemGrapple::new, new Item.Settings().maxCount(1));

	public static void initialize() 
	{
	}
	
	public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FeurMod.MOD_ID, name));
		Item item = itemFactory.apply(settings);
		Registry.register(Registries.ITEM, itemKey, item);
		return item;
	}
}
