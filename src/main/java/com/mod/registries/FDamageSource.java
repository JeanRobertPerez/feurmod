package com.mod.registries;

import com.mod.FeurMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FDamageSource
{
    public static final RegistryKey<DamageType> SHOT_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(FeurMod.MOD_ID, "shot"));

    public static DamageSource shot(@Nullable LivingEntity shooter, World world)
    {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(SHOT_TYPE), null, shooter);
    }

    public static void registerDamageTypes()
    {

    }
}
