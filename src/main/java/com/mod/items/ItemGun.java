package com.mod.items;

import com.mod.registries.FDamageSource;
import com.mod.utils.Utils;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemGun extends Item implements ISpecialLeftClickItem, IRightClickItem
{

    public ItemGun(Settings settings)
    {
        super(settings);
    }

    public abstract float damages();

    public abstract int cooldown();

    public abstract float range();

    public abstract int capacity();

    public abstract int maxReloadTime();

    public abstract boolean reloadAll();

    public abstract boolean isShotgun();

    public abstract float inaccuracy();

    public abstract float dispertion();

    public abstract SoundEvent sound();

    public abstract float soundPitch();

    public abstract float soundVolume();

    public abstract boolean reloadPassively();

    public static int getMagazine(ItemStack stack)
    {
        if(stack.hasNbt())
        {
            if(stack.getNbt().contains("magazine"))
            {
                return stack.getNbt().getInt("magazine");
            }
            else
            {
                stack.getNbt().putInt("magazine", 0);
                return 0;
            }
        }
        else
        {
            stack.setNbt(new NbtCompound());
            return 0;
        }
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        if(this.reloadPassively() || selected)
        {
            if(reloadTime(stack) >= 0)
            {
                this.reload(stack);
            }
        }
    }

    public static void setMagazine(ItemStack stack, int i)
    {
        if(stack.hasNbt())
        {
            stack.getNbt().putInt("magazine", i);
        }
        else
        {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("magazine", i);
        }
    }

    public static int reloadTime(ItemStack stack)
    {
        if(stack.hasNbt())
        {
            if(stack.getNbt().contains("reloadTime"))
            {
                return stack.getNbt().getInt("reloadTime");
            }
            else
            {
                stack.getNbt().putInt("reloadTime", -1);
                return -1;
            }
        }
        else
        {
            stack.setNbt(new NbtCompound());
            return -1;
        }
    }

    public static void setReloadTime(ItemStack stack, int i)
    {
        if(stack.hasNbt())
        {
            stack.getNbt().putInt("reloadTime", i);
        }
        else
        {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putInt("reloadTime", i);
        }
    }

    public void reload(ItemStack stack)
    {
        if(reloadTime(stack) == 0)
        {
            if(this.reloadAll())
            {
                setMagazine(stack, this.capacity());
                setReloadTime(stack,-1);
            }
            else
            {
                if(getMagazine(stack) < this.capacity() - 1)
                {
                    setMagazine(stack, getMagazine(stack) + 1);
                    setReloadTime(stack,this.maxReloadTime());
                }
                else
                {
                    setMagazine(stack, getMagazine(stack) + 1);
                    setReloadTime(stack, -1);
                }
            }
        }
        else if(reloadTime(stack) > 0)
        {
            setReloadTime(stack, reloadTime(stack) - 1);
        }
    }

    public void tryReload(ItemStack stack)
    {
        if(getMagazine(stack) < this.capacity())
        {
            setReloadTime(stack, maxReloadTime());
        }
    }

    public void onHitEntity(int i, LivingEntity user, Entity shot, World world, ItemStack stack)
    {
        if(shot instanceof LivingEntity)
        {
            Utils.damageEntity((LivingEntity) shot, FDamageSource.shot(user, world), this.damages() * i);
        }
        else
        {
            shot.damage(FDamageSource.shot(user, world), this.damages() * i);
        }
    }

    public void onHitBlock(World world, ItemStack stack, BlockPos pos)
    {
        //if(world.getBlockState(pos).)
    }

    @Override
    public void rightClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {
    }

    @Override
    public void leftClick(PlayerEntity player, ItemStack stack, World world, int ticks)
    {

    }

    @Override
    @Environment(EnvType.CLIENT)
    public void leftClickClientOnly(PlayerEntity player, ItemStack stack, World world, int ticks)
    {
        if(!player.getItemCooldownManager().isCoolingDown(this))
        {
            Utils.shootClient(player, world, player.getPitch(), player.getYaw(), stack);
        }
    }
}
