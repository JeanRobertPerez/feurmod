package com.mod.events;

import com.mod.items.IRightClickItem;
import com.mod.items.ISpecialLeftClickItem;
import com.mod.networking.NetworkingConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;

public class ClientEvents
{

    private static int rightClickticksPressed = 0;
    private static int leftClickticksPressed = 0;

    public static void register()
    {
        ClientTickEvents.START_CLIENT_TICK.register((MinecraftClient client) -> {
            if(client.player != null)
            {
                ClientPlayerEntity player = client.player;
                if(player.getMainHandStack().getItem() instanceof IRightClickItem item)
                {
                    if(client.options.useKey.isPressed())
                    {
                        item.rightClickClientOnly(client.player, player.getMainHandStack(), client.world, rightClickticksPressed);
                        item.rightClick(client.player, player.getMainHandStack(), client.world, rightClickticksPressed);

                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(rightClickticksPressed);
                        buf.writeBoolean(false);
                        buf.writeInt(0);

                        ClientPlayNetworking.send(NetworkingConstants.SPECIAL_USE_ITEM_MESSAGE, buf);
                        ++rightClickticksPressed;
                    }
                    else if(rightClickticksPressed > 0)
                    {
                        item.releaseClient(client.player, player.getMainHandStack(), client.world, rightClickticksPressed);
                        item.release(client.player, player.getMainHandStack(), client.world, rightClickticksPressed);

                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(rightClickticksPressed);
                        buf.writeBoolean(true);
                        buf.writeInt(0);

                        ClientPlayNetworking.send(NetworkingConstants.SPECIAL_USE_ITEM_MESSAGE, buf);
                        rightClickticksPressed = 0;
                    }
                }
                else
                {
                    rightClickticksPressed = 0;
                }

                if(player.getMainHandStack().getItem() instanceof ISpecialLeftClickItem item)
                {
                    if(client.options.attackKey.isPressed())
                    {
                        item.leftClickClientOnly(client.player, player.getMainHandStack(), client.world, leftClickticksPressed);
                        item.leftClick(client.player, player.getMainHandStack(), client.world, leftClickticksPressed);

                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(leftClickticksPressed);
                        buf.writeBoolean(false);
                        buf.writeInt(1);

                        ClientPlayNetworking.send(NetworkingConstants.SPECIAL_USE_ITEM_MESSAGE, buf);
                        ++leftClickticksPressed;
                    }
                    else if(leftClickticksPressed > 0)
                    {
                        item.releaseLeftClickClient(client.player, player.getMainHandStack(), client.world, leftClickticksPressed);
                        item.releaseLeftClick(client.player, player.getMainHandStack(), client.world, leftClickticksPressed);

                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(leftClickticksPressed);
                        buf.writeBoolean(true);
                        buf.writeInt(1);

                        ClientPlayNetworking.send(NetworkingConstants.SPECIAL_USE_ITEM_MESSAGE, buf);
                        leftClickticksPressed = 0;
                    }
                }
                else
                {
                    leftClickticksPressed = 0;
                }
            }
        });
    }
}
