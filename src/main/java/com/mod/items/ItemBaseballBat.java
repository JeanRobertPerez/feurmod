package com.mod.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

public class ItemBaseballBat extends Item
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
	
}
