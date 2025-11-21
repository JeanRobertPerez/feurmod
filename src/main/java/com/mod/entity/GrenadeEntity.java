package com.mod.entity;

import com.mod.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GrenadeEntity extends Entity
{

	protected double prevMotionY;
	int rebondi;

	public GrenadeEntity(EntityType<?> type, World world) 
	{
		super(type, world);
		rebondi = 0;
	}

	@Override
	protected void initDataTracker() 
	{
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) 
	{
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt)
	{
	}

	public int getMaxExistTicks()
	{
		return 200;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(!this.isOnGround())
		{
			prevMotionY = this.getVelocity().y;
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.addVelocity(new Vec3d(0, -0.04, 0));
		if(!this.getWorld().isClient)
		{
			this.boing();
		}

		this.setVelocity(this.getVelocity().multiply(1.0, 0.98, 1.0));
	}

	public void onLanding()
	{
		super.onLanding();
	}

	protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition)
	{
		super.fall(heightDifference, onGround, state, landedPosition);
	}

	public void boing()
	{
		Vec3d vec = this.getPos().add(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
		Box box = new Box(vec.x - this.getWidth() / 2, vec.y, vec.z - this.getWidth() / 2, vec.x + this.getWidth() / 2, vec.y + this.getHeight(), vec.z + this.getWidth() / 2);
		if(Utils.checkBlockCollisionAdv(box, this.getWorld()))
		{
			Vec3d vecTest = this.getPos().add(-this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
			Box boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()))
			{
				this.setVelocity(this.getVelocity().multiply(-0.8, 1.0, 1.0));
				this.rebondi += 1;
			}

			vecTest = this.getPos().add(0, -this.getVelocity().y * 2, 0);
			boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()) && !this.isOnGround() && MathHelper.abs((float)this.getVelocity().y) > 0.1F)
			{
				this.setVelocity(this.getVelocity().multiply(1.0, -0.8, 1.0));
				this.rebondi += 1;
			}

			vecTest = this.getPos().add(this.getVelocity().x, this.getVelocity().y, -this.getVelocity().z);
			boxTest = new Box(vecTest.x - this.getWidth() / 2, vecTest.y, vecTest.z - this.getWidth() / 2, vecTest.x + this.getWidth() / 2, vecTest.y + this.getHeight(), vecTest.z + this.getWidth() / 2);

			if(!Utils.checkBlockCollisionAdv(boxTest, this.getWorld()) && !this.isOnGround())
			{
				this.setVelocity(this.getVelocity().multiply(1.0, 1.0, -0.8));
				this.rebondi += 1;
			}
		}
	}
}
