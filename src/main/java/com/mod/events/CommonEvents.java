package com.mod.events;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.mod.FeurMod;
import com.mod.events.custom_event.EntityUpdateCallback;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;

public class CommonEvents 
{
	public static void register()
	{
		//TODO here is the update entity event
		EntityUpdateCallback.EVENT.register((entity) -> 
		{
			if(entity instanceof PlayerEntity)
			{
				PlayerEntity player = (PlayerEntity) entity;
				System.out.print(player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).getBaseValue() + "\n");
				if(player.getMainHandStack().getItem() instanceof AxeItem)
				{
					player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(FeurMod.AXE_ATT_SPEED);
				}
				else
				{
					player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(Float.POSITIVE_INFINITY);
				}
			}
		});
	}
}
