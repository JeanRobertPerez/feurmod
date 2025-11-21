package com.mod;

import com.mod.registries.RegisterEntities;
import com.mod.rendermobs.GrenadeEntityRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import static net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry.*;

@Environment(EnvType.CLIENT)
public class FeurModClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		INSTANCE.register(RegisterEntities.GRENADE, (context) -> {
			return new GrenadeEntityRender(context);
		});
	}
}