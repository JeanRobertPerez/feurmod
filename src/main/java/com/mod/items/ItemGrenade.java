package com.mod.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mod.entity.GrenadeEntity;
import com.mod.registries.RegisterEntities;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemGrenade extends Item
{

    public ItemGrenade(Settings settings)
    {
        super(settings);
        settings.maxCount(16).maxDamage(0);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        GrenadeEntity entity = new GrenadeEntity(RegisterEntities.GRENADE, world);
        entity.throwGrenade(user, 0.8F);
        if(!world.isClient())
        {
            world.spawnEntity(entity);
        }
        user.getItemCooldownManager().set(this, 30);
        user.getStackInHand(hand).decrement(1);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
