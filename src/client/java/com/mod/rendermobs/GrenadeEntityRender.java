package com.mod.rendermobs;

import com.mod.entity.GrenadeEntity;
import com.mod.registries.RegisterItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class GrenadeEntityRender extends EntityRenderer<GrenadeEntity>
{
    private final ItemRenderer itemRenderer;

    public GrenadeEntityRender(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        itemRenderer = context.getItemRenderer();
    }

    public void render(GrenadeEntity tntEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        ItemStack itemStack = new ItemStack(RegisterItems.GRENADE);
        int j = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + itemStack.getDamage();
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, tntEntity.getWorld(), (LivingEntity)null, tntEntity.getId());
        boolean bl = bakedModel.hasDepth();
        float h = 0.25F;
        float n = 0;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(n));

        this.itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);

        matrixStack.pop();
        super.render(tntEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Deprecated
    public Identifier getTexture(GrenadeEntity tntEntity)
    {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}