package com.mod.networking;

import com.mod.items.IRightClickItem;
import com.mod.items.ISpecialLeftClickItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;

public class MessageHandler {
    public static void registerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SPECIAL_USE_ITEM_MESSAGE, (server, player, handler, buf, responseSender) -> {
            int ticks = buf.readInt();
            boolean release = buf.readBoolean();
            int inputId = buf.readInt();

            server.execute(() -> {
                if(inputId == 0 && player.getMainHandStack().getItem() instanceof IRightClickItem)
                {
                    IRightClickItem item = (IRightClickItem) player.getMainHandStack().getItem();
                    if(release)
                    {
                        item.release(player, player.getMainHandStack(), player.getWorld(), ticks);
                    }
                    else
                    {
                        item.rightClick(player, player.getMainHandStack(), player.getWorld(), ticks);
                    }
                }

                if(inputId == 1 && player.getMainHandStack().getItem() instanceof ISpecialLeftClickItem)
                {
                    ISpecialLeftClickItem item = (ISpecialLeftClickItem) player.getMainHandStack().getItem();
                    if(release)
                    {
                        item.release(player, player.getMainHandStack(), player.getWorld(), ticks);
                    }
                    else
                    {
                        item.leftClick(player, player.getMainHandStack(), player.getWorld(), ticks);
                    }
                }
            });
        });
    }
}