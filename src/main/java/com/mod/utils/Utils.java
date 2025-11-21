package com.mod.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Utils
{
    public static boolean checkBlockCollisionAdv(Box bb, World world)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    BlockPos pos = new BlockPos(l3, i4, j4);
                    BlockState iblockstate1 = world.getBlockState(pos);

                    if (iblockstate1 != null && iblockstate1.getCollisionShape(world, pos) != null && !iblockstate1.getCollisionShape(world, pos).isEmpty() && bb.intersects(iblockstate1.getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ()).getBoundingBox()))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
