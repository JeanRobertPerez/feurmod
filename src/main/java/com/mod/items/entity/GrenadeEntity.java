package com.mod.items.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GrenadeEntity extends Entity
{

	public GrenadeEntity(EntityType<?> type, World world) 
	{
		super(type, world);
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
	
	@Override
	public void tick()
	{
		super.tick();
		this.move(MovementType.SELF, this.getVelocity());
		this.addVelocity(new Vec3d(0, -0.002, 0));
		Vec3d vel = this.getVelocity();
		vel.multiply(1.0F, 0.9999F, 1.0F);
		this.setVelocity(vel);
	}
	
}
