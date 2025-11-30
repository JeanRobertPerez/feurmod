package com.mod.utils;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class Utils
{
    public static boolean checkBlockCollisionAdv(Box bb, World world)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    BlockPos pos = new BlockPos(l3, i4, j4);
                    BlockState iblockstate1 = world.getBlockState(pos);

                    if (iblockstate1 != null && iblockstate1.getCollisionShape(world, pos) != null && !iblockstate1.getCollisionShape(world, pos).isEmpty() && bb.intersects(iblockstate1.getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ()).getBoundingBox()))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void damageEntity(LivingEntity entityLiving, DamageSource source, float amount) {
        Entity entity;
        if (entityLiving.isInvulnerableTo(source)) {
            return;
        }

        float abs = Math.max(entityLiving.getAbsorptionAmount() - amount, 0.0f);

        float g = Math.max(0.0F, amount - abs);

        if (g > 0.0f && g < 3.4028235E37f && (entity = source.getAttacker()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
            serverPlayerEntity.increaseStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(g * 10.0f));
        }
        if (g < 0.0f)
        {
            return;
        }
        entityLiving.getDamageTracker().onDamage(source, g);
        entityLiving.setHealth(entityLiving.getHealth() - g);
        entityLiving.setAbsorptionAmount(abs);
        entityLiving.emitGameEvent(GameEvent.ENTITY_DAMAGE);
        //entityLiving.onDamaged(source);

        if (entityLiving.isDead())
        {
            if (!Utils.tryUseTotem(entityLiving, source))
            {
                SoundEvent soundEvent = SoundEvents.ENTITY_GENERIC_DEATH;
                if (soundEvent != null) {
                    entityLiving.playSound(soundEvent, 1.0F, entityLiving.getSoundPitch());
                }

                if(!entityLiving.getWorld().isClient)
                {
                    entityLiving.onDeath(source);
                }
            }
        }
        else
        {
            SoundEvent soundEvent = SoundEvents.ENTITY_GENERIC_HURT;
            if (soundEvent != null) {
                entityLiving.playSound(soundEvent, 1.0F, entityLiving.getSoundPitch());
            }
        }
    }

    public static boolean tryUseTotem(LivingEntity entityLiving, DamageSource source) {
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        ItemStack itemStack = null;
        for (Hand hand : Hand.values()) {
            ItemStack itemStack2 = entityLiving.getStackInHand(hand);
            if (!itemStack2.isOf(Items.TOTEM_OF_UNDYING)) continue;
            itemStack = itemStack2.copy();
            itemStack2.decrement(1);
            break;
        }
        if (itemStack != null) {
            if (entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entityLiving;
                serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
            }
            entityLiving.setHealth(1.0f);
            entityLiving.clearStatusEffects();
            entityLiving.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            entityLiving.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            entityLiving.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            entityLiving.getWorld().sendEntityStatus(entityLiving, EntityStatuses.USE_TOTEM_OF_UNDYING);
        }
        return itemStack != null;
    }

    public static Vec3d lookVecWithInaccuracy(Entity entity, float inaccuracy, float range)
    {
        Random rand = new Random();

        double f = (entity.getPitch() + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);
    double g = -(entity.getYaw() + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);

        double h = MathHelper.cos((float) g);
        double i = MathHelper.sin((float) g);
        double j = MathHelper.cos((float) f);
        double k = MathHelper.sin((float) f);
        return new Vec3d(range * i * j,-range * k, range * h * j);
    }

    public static Vec3d lookVecWithInaccuracy(float pitch, float yaw, float inaccuracy, float range)
    {
        Random rand = new Random();

        double f = (pitch + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);
        double g = -(yaw + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);

        double h = MathHelper.cos((float) g);
        double i = MathHelper.sin((float) g);
        double j = MathHelper.cos((float) f);
        double k = MathHelper.sin((float) f);
        return new Vec3d(range * i * j,-range * k, range * h * j);
    }

    public static Vec3d lookVecWithInaccuracy(Entity entity, float inaccuracy, float range, float partialTick)
    {
        Random rand = new Random();

        double f = (entity.getPitch(partialTick) + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);
        double g = -(entity.getYaw(partialTick) + rand.nextGaussian() * inaccuracy) * ((float)Math.PI / 180);

        double h = MathHelper.cos((float) g);
        double i = MathHelper.sin((float) g);
        double j = MathHelper.cos((float) f);
        double k = MathHelper.sin((float) f);
        return new Vec3d(range * i * j,-range * k, range * h * j);
    }
}
