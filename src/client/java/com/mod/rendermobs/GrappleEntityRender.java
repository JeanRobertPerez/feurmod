package com.mod.rendermobs;

import com.mod.FeurMod;
import com.mod.entity.GrappleEntity;
import com.mod.registries.RegisterItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GrappleEntityRender extends EntityRenderer<GrappleEntity>
{
    private final ItemRenderer itemRenderer;
    private static final Identifier TAMERELAPUTE = Identifier.of(FeurMod.MOD_ID, "textures/entity/test.png");
    private static final RenderLayer NTM;

    public GrappleEntityRender(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        itemRenderer = context.getItemRenderer();
    }

    public void render(GrappleEntity tntEntity, float t, float partialTick, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int c) {
        matrixStack.push();
        ItemStack itemStack = new ItemStack(Blocks.SLIME_BLOCK);
        int pute = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + itemStack.getDamage();
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, tntEntity.getWorld(), (LivingEntity)null, tntEntity.getId());
        boolean bl = bakedModel.hasDepth();
        float salope = 0.25F;
        float encule = 0;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(encule));

        this.itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, c, OverlayTexture.DEFAULT_UV, bakedModel);

        matrixStack.pop();

        if(MinecraftClient.getInstance() != null)
        {
            Entity renderEntity = MinecraftClient.getInstance().cameraEntity;

            if(renderEntity != null && tntEntity.getUser() != null)
            {
                Vec3d entityPos = new Vec3d(MathHelper.lerp((double)partialTick, tntEntity.prevX, tntEntity.getX()), MathHelper.lerp((double)partialTick, tntEntity.prevY, tntEntity.getY()), MathHelper.lerp((double)partialTick, tntEntity.prevZ, tntEntity.getZ())).add(0, 0.2, 0);
                Vec3d userPos = new Vec3d(MathHelper.lerp((double)partialTick, tntEntity.getUser().prevX, tntEntity.getUser().getX()), MathHelper.lerp((double)partialTick, tntEntity.getUser().prevY, tntEntity.getUser().getY()), MathHelper.lerp((double)partialTick, tntEntity.getUser().prevZ, tntEntity.getUser().getZ())).add(0, 1.4, 0);
                Vec3d cameraPos = new Vec3d(MathHelper.lerp((double)partialTick, renderEntity.prevX, renderEntity.getX()), MathHelper.lerp((double)partialTick, renderEntity.prevY, renderEntity.getY()), MathHelper.lerp((double)partialTick, renderEntity.prevZ, renderEntity.getZ()));

                Vec3d vec = userPos.subtract(entityPos);
                double dx = vec.x;
                double dy = vec.y;
                double dz = vec.z;

                float f = MathHelper.sqrt((float) (dx * dx + dz * dz));
                float g = MathHelper.sqrt((float) (dx * dx + dy * dy + dz * dz));
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2((double)dz, (double)dx)) - ((float)Math.PI / 2F)));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float)(-Math.atan2((double)f, (double)dy)) - ((float)Math.PI / 2F)));
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(NTM);
                float h = 0.0F - ((float)t + partialTick) * 0.01F;
                float i = MathHelper.sqrt((float) (dx * dx + dy * dy + dz * dz)) / 32.0F - ((float)t + partialTick) * 0.01F;
                int j = 8;
                float k = 0.0F;
                float l = 0.75F;
                float m = 0.0F;
                MatrixStack.Entry entry = matrixStack.peek();
                Matrix4f matrix4f = entry.getPositionMatrix();
                Matrix3f matrix3f = entry.getNormalMatrix();

                for(int n = 0; n < 4; ++n)
                {
                    float o = MathHelper.sin((float)n * ((float)Math.PI * 2F) / 4.0F) * 0.75F;
                    float p = MathHelper.cos((float)n * ((float)Math.PI * 2F) / 4.0F) * 0.75F;
                    float q = (float)n / 4.0F;
                    vertexConsumer.vertex(matrix4f, k * 0.02F, l * 0.02F, 0.0F).color(255, 255, 255, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(c).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
                    vertexConsumer.vertex(matrix4f, k * 0.02F, l * 0.02F, g).color(255, 255, 255, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(c).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
                    vertexConsumer.vertex(matrix4f, o * 0.02F, p * 0.02F, g).color(255, 255, 255, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(c).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
                    vertexConsumer.vertex(matrix4f, o * 0.02F, p * 0.02F, 0.0F).color(255, 255, 255, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(c).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
                }

                matrixStack.pop();
            }
        }


        super.render(tntEntity, t, partialTick, matrixStack, vertexConsumerProvider, c);
    }

    static
    {
        NTM = RenderLayer.getEntitySmoothCutout(TAMERELAPUTE);
    }

    @Deprecated
    public Identifier getTexture(GrappleEntity tntEntity)
    {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}