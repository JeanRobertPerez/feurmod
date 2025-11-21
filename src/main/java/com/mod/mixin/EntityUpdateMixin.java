package com.mod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mod.events.custom_event.EntityUpdateCallback;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityUpdateMixin 
{
 
    @Inject(at = @At(value = "INVOKE"), method = "tick", cancellable = false)
    private void onUpdate(CallbackInfo info)
    {
        EntityUpdateCallback.EVENT.invoker().update((Entity) (Object) this);
    }
}