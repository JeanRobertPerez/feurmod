package com.mod.entity;

import com.mod.networking.NetworkingConstants;
import com.mod.utils.Utils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class GrappleEntity extends Entity
{
    private static final TrackedData<Integer> USER_ID = DataTracker.registerData(GrappleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CAN_ATTRACT = DataTracker.registerData(GrappleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private LivingEntity thrower;

    public GrappleEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void initDataTracker()
    {
        this.dataTracker.startTracking(USER_ID, -1);
        this.dataTracker.startTracking(CAN_ATTRACT, false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt)
    {
        this.dataTracker.set(USER_ID, nbt.getInt("userId"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt)
    {
        nbt.putInt("userId", this.dataTracker.get(USER_ID));
    }

    public boolean canAttract()
    {
        return this.dataTracker.get(CAN_ATTRACT);
    }

    public void setCanAttract(boolean b)
    {
        this.dataTracker.set(CAN_ATTRACT, b);
    }

    public int getMaxExistTicks()
    {
        return 100;
    }

    @Override
    public void tick()
    {
        super.tick();
        //Vec3d vecTest = this.getPos().add(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
        //Box boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

        if(!this.getWorld().isClient)
        {
            if(this.getUser() == null || this.getUser().getPos().add(0, this.getUser().getHeight() / 2, 0).subtract(this.getPos().add(0, this.getHeight() / 2, 0)).length() > 16)
            {
                this.discard();
            }
        }


        if(Utils.checkBlockCollisionAdv(this.getBoundingBox().expand(0.1), this.getWorld()))
        {
            this.setCanAttract(true);
        }
        else
        {
            this.setCanAttract(false);
            this.move(MovementType.SELF, this.getVelocity());
        }
    }

    public void attractUser()
    {
        if(this.getUser() != null)
        {
            Vec3d attractVec = this.getUser().getPos().add(0, this.getUser().getHeight() / 2, 0).subtract(this.getPos().add(0, this.getHeight() / 2, 0));
            if(attractVec != Vec3d.ZERO)
            {
                attractVec = attractVec.normalize();
                this.getUser().addVelocity(attractVec.multiply(-0.2));
            }
        }
        else
        {
            this.discard();
        }
    }

    public LivingEntity getUser()
    {
        if(this.thrower != null)
        {
            return this.thrower;
        }
        else
        {
            if(this.dataTracker.get(USER_ID) != -1 && this.getWorld().getEntityById(this.dataTracker.get(USER_ID)) instanceof LivingEntity)
            {
                this.thrower = (LivingEntity) this.getWorld().getEntityById(this.dataTracker.get(USER_ID));
                return thrower;
            }
        }
        return null;
    }

    public void throwGrapple(Entity user, float power)
    {
        this.setPosition(user.getEyePos());
        this.setVelocity(user.getRotationVec(1.0F).multiply(power));
        this.dataTracker.set(USER_ID, user.getId());
    }
}