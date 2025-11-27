package com.mod.entity;

import com.mod.networking.NetworkingConstants;
import com.mod.utils.Utils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3f;

import java.util.Random;

public class GrenadeEntity extends Entity
{

	protected double prevMotionY;
	public int ticks;
    private static final TrackedData<Integer> USER_ID = DataTracker.registerData(GrenadeEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> REBONDI = DataTracker.registerData(GrenadeEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public float damageMultiplier;
    private LivingEntity thrower;

	public GrenadeEntity(EntityType<?> type, World world) 
	{
		super(type, world);
		damageMultiplier = 1.0F;
	}

	public void setRebondi(int rebondi)
	{
		this.dataTracker.set(REBONDI, rebondi);
	}

	public int getRebondi()
	{
		return this.dataTracker.get(REBONDI);
	}

	@Override
	protected void initDataTracker() 
	{
        this.dataTracker.startTracking(USER_ID, -1);
		this.dataTracker.startTracking(REBONDI, 0);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) 
	{
		this.dataTracker.set(USER_ID, nbt.getInt("userId"));
		this.dataTracker.set(REBONDI, nbt.getInt("rebondi"));
		this.damageMultiplier = nbt.getFloat("damageMultiplier");
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
		nbt.putInt("userId", this.dataTracker.get(USER_ID));
		nbt.putInt("rebondi", this.dataTracker.get(REBONDI));
		nbt.putFloat("damageMultiplier", this.damageMultiplier);
	}

	public int getMaxExistTicks()
	{
		return 100;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(!this.isOnGround())
		{
			prevMotionY = this.getVelocity().y;
		}

		if(this.isInLava() || this.isSubmergedInWater())
		{
			this.setVelocity(this.getVelocity().multiply(0.96, 0.96, 0.96));
		}

		if(this.isOnGround())
		{
			this.setVelocity(this.getVelocity().multiply(0.7, 1, 0.7));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.addVelocity(new Vec3d(0, -0.04, 0));

		if(!this.getWorld().isClient)
		{
			this.boing();
		}

		this.setVelocity(this.getVelocity().multiply(1.0, 0.98, 1.0));

		if(ticks >= this.getMaxExistTicks())
		{
			this.explode();
		}

		if(!this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.3), EntityPredicates.VALID_LIVING_ENTITY).isEmpty())
		{
			if(this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.3), EntityPredicates.VALID_LIVING_ENTITY).size() > 1 || this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.3), EntityPredicates.VALID_LIVING_ENTITY).get(0) != this.getUser() || ticks > 10)
			{
				this.explode();
			}
		}
		++ticks;
	}

    public void explode()
    {
		if(!this.getWorld().isClient)
		{
			this.getWorld().playSound((PlayerEntity) null,this.getPos().x, this.getPos().y, this.getPos().z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 2.0F, 1.0F, 0L);

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(0);
			buf.writeVector3f(new Vector3f((float)this.getPos().getX(), (float)this.getPos().getY(), (float)this.getPos().getZ()));

			for(PlayerEntity entity : this.getWorld().getPlayers())
			{
				if(entity instanceof ServerPlayerEntity)
				{
					ServerPlayNetworking.send((ServerPlayerEntity) entity, NetworkingConstants.BASIC_PARTICLES_MESSAGE, buf);
				}
			}

			for(Entity entity : this.getWorld().getOtherEntities(this, new Box(this.getPos().x - 3, this.getPos().y - 3, this.getPos().z - 3, this.getPos().x + 3, this.getPos().y + 3, this.getPos().z + 3)))
			{
				entity.damage(this.getWorld().getDamageSources().explosion(this, this.getUser()), 10.0F * this.damageMultiplier);
			}

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

    public void throwGrenade(Entity user, float power)
    {
        this.setPosition(user.getEyePos());
        this.setVelocity(user.getRotationVec(1.0F).multiply(power));
		this.dataTracker.set(USER_ID, user.getId());
    }

	public void boing()
	{
		Vec3d vec = this.getPos().add(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
		Box box = new Box(vec.x - this.getWidth() / 2, vec.y, vec.z - this.getWidth() / 2, vec.x + this.getWidth() / 2, vec.y + this.getHeight(), vec.z + this.getWidth() / 2);
		if(Utils.checkBlockCollisionAdv(box, this.getWorld()))
		{
			Vec3d vecTest = this.getPos().add(-this.getVelocity().x, 0, this.getVelocity().z);
			Box boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()))
			{
				//this.setVelocity(this.getVelocity().multiply(-0.5, 1.0, 1.0));
				this.setRebondi(this.getRebondi() + 1);

			}

			vecTest = this.getPos().add(this.getVelocity().x, -this.getVelocity().y, this.getVelocity().z);
			boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()))
			{
				if(MathHelper.abs((float)this.getVelocity().y) > 0.1F)
				{
					this.setVelocity(this.getVelocity().multiply(0.6, -0.5, 0.6));
					this.setRebondi(this.getRebondi() + 1);
				}
			}

			vecTest = this.getPos().add(this.getVelocity().x, 0, -this.getVelocity().z);
			boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()))
			{
				//this.setVelocity(this.getVelocity().multiply(1.0, 1.0, -0.5));
				this.setRebondi(this.getRebondi() + 1);
			}
		}
	}
}
