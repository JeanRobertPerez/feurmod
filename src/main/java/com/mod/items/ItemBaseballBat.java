package com.mod.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import com.mod.entity.GrenadeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBaseballBat extends Item implements ISpecialLeftClickItem
{
	private static final java.util.UUID ID = java.util.UUID.randomUUID();
	
	public ItemBaseballBat(Settings settings) 
	{
		super(settings);
		settings.maxCount(1).maxDamage(0);
	}
	
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot)
	{
		if(slot == EquipmentSlot.MAINHAND)
		{
			Multimap<EntityAttribute, EntityAttributeModifier> map = super.getAttributeModifiers(slot);
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = new Builder<EntityAttribute, EntityAttributeModifier>();
			builder.putAll(map);
			builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ID, "Attack Damage", 6.5F, EntityAttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getAttributeModifiers(slot);
	}
	
	public boolean postHit(net.minecraft.item.ItemStack stack, net.minecraft.entity.LivingEntity target, net.minecraft.entity.LivingEntity attacker)
	{
		if(target.getMaxHealth() <= 100.0f)
		{
			Vec3d vec = attacker.getRotationVec(1.0F).multiply(1.3f);
			target.addVelocity(vec.x, vec.y, vec.z);
		}
		return true;
	}

	@Override
	public void leftClick(PlayerEntity player, ItemStack stack, World world, int ticks)
	{
		for(Entity entity : world.getOtherEntities(player, new Box(player.getPos().x - 4, player.getPos().y - 2.5, player.getPos().z - 4, player.getPos().x + 4, player.getPos().y + 5.5, player.getPos().z + 4)))
		{
			if(entity instanceof GrenadeEntity && player.getEyePos().distanceTo(entity.getPos()) < 3.5)
			{
				GrenadeEntity grenade = (GrenadeEntity) entity;
				double M = 3.5;
				double d = player.getEyePos().distanceTo(entity.getPos());
				//double l = 30, cos(30) is around 0.85;

				Vec3d vecPosRel = grenade.getPos().subtract(player.getEyePos());
				double dot = player.getRotationVector().dotProduct(vecPosRel.normalize());
				if(dot >= 0.85)
				{
					grenade.damageMultiplier += 0.5F;
					grenade.ticks = 0;
					grenade.throwGrenade(player, 1.6F);
				}
			}
		}
	}
}
