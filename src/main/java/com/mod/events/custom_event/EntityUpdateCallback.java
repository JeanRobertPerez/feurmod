package com.mod.events.custom_event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

public interface EntityUpdateCallback 
{
	Event<EntityUpdateCallback> EVENT = EventFactory.createArrayBacked(EntityUpdateCallback.class,
	        (listeners) -> (entity) ->
	        {
	            for (EntityUpdateCallback listener : listeners)
	            {
	                listener.update(entity);
	            }
	    });
	 
	    void update(Entity entity);
}
