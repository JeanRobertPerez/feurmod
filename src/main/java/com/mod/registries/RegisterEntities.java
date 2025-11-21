package com.mod.registries;

import com.mod.FeurMod;
import com.mod.entity.GrenadeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterEntities
{
    public static final EntityType<GrenadeEntity> GRENADE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(FeurMod.MOD_ID, "grenade"),
            EntityType.Builder.create(GrenadeEntity::new, SpawnGroup.MISC).setDimensions(0.3f, 0.3f).build("grenade")
    );

    public static void initialize()
    {
    }
}
