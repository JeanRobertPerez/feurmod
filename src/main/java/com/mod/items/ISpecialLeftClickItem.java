package com.mod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpecialLeftClickItem
{
    public default void leftClickClientOnly(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }

    public void leftClick(PlayerEntity player, ItemStack stack, World world, int ticks);

    public default void releaseLeftClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }

    public default void releaseLeftClickClient(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }
}
