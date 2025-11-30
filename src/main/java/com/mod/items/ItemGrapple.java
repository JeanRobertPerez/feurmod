package com.mod.items;

import com.mod.entity.GrappleEntity;
import com.mod.entity.GrenadeEntity;
import com.mod.registries.RegisterEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ItemGrapple extends Item implements ISpecialLeftClickItem, IRightClickItem
{

    public ItemGrapple(Settings settings)
    {
        super(settings);
        settings.maxCount(1).maxDamage(0);
    }

    @Override
    public void leftClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {
        if(ticks == 0)
        {
            boolean b = false;
            Box box = new Box(player.getX() - 16, player.getY() + player.getHeight() / 2 - 16, player.getZ() - 16, player.getX() + 16, player.getY() + player.getHeight() / 2 + 16, player.getZ() + 16);
            for(Entity entity : world.getOtherEntities(player, box))
            {
                if(entity instanceof GrappleEntity && !world.isClient)
                {
                    b = true;
                    entity.discard();
                }
            }

            if(!b && !world.isClient)
            {
                GrappleEntity entity = new GrappleEntity(RegisterEntities.GRAPPLE, world);
                entity.throwGrapple(player, 1F);
                entity.setCanAttract(false);
                if(!world.isClient())
                {
                    world.spawnEntity(entity);
                }
            }
        }
    }

    @Override
    public void rightClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {
        Box box = new Box(player.getX() - 16, player.getY() + player.getHeight() / 2 - 16, player.getZ() - 16, player.getX() + 16, player.getY() + player.getHeight() / 2 + 16, player.getZ() + 16);
        for(Entity entity : world.getOtherEntities(player, box))
        {
            if(entity instanceof GrappleEntity)
            {
                if(((GrappleEntity) entity).canAttract())
                {
                    ((GrappleEntity) entity).attractUser();
                }
            }
        }
    }
}