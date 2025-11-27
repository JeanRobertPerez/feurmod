package com.mod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRightClickItem
{
    public default void rightClickClientOnly(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }

    public void rightClick(PlayerEntity player, ItemStack stack, World world, int ticks);

    public default void release(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }

    public default void releaseClient(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }
}
