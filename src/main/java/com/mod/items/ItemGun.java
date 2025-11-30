package com.mod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemGun extends Item implements ISpecialLeftClickItem, IRightClickItem
{

    public ItemGun(Settings settings)
    {
        super(settings);
    }

    public abstract float damage();

    public abstract int cooldown();

    public abstract float range();

    public abstract int capacity();

    public abstract int rechargeTime();

    public abstract boolean rechargeAll();

    public abstract boolean isShotgun();

    @Override
    public void rightClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {
    }

    @Override
    public void leftClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }
}
