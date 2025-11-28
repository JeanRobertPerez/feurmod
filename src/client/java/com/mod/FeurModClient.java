package com.mod;

import com.mod.events.ClientEvents;
import com.mod.networking.NetworkingConstants;
import com.mod.registries.RegisterEntities;
import com.mod.rendermobs.GrappleEntityRender;
import com.mod.rendermobs.GrenadeEntityRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Random;

import static net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry.*;

@Environment(EnvType.CLIENT)
public class FeurModClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientEvents.register();

		INSTANCE.register(RegisterEntities.GRENADE, (context) -> {
            return new GrenadeEntityRender(context);
		});

		INSTANCE.register(RegisterEntities.GRAPPLE, (context) -> {
			return new GrappleEntityRender(context);
		});

		ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.BASIC_PARTICLES_MESSAGE, (client, handler, buf, responseSender) -> {

			int i = buf.readInt();
			Vector3f vec = buf.readVector3f();

			client.execute(() -> {
				if(i == 0 && client.world != null)
				{
					Random rand = new Random();
					for(int j = 0; j < 10; ++j)
					{
						client.world.addParticle(ParticleTypes.FLAME, true, vec.x, vec.y + 0.1F, vec.z, (rand.nextFloat() - 0.5F) * 2, (rand.nextFloat() - 0.5F) * 2, (rand.nextFloat() - 0.5F) * 2);
						client.world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, vec.x, vec.y + 0.1F, vec.z, (rand.nextFloat() - 0.5F) * 2, (rand.nextFloat() - 0.5F) * 2, (rand.nextFloat() - 0.5F) * 2);
					}
				}
			});
		});
	}
}