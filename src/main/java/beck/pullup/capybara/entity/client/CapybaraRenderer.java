package beck.pullup.capybara.entity.client;

import beck.pullup.capybara.PullUpCapybara;
import beck.pullup.capybara.entity.custom.CapybaraEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class CapybaraRenderer extends GeoEntityRenderer<CapybaraEntity> {
    public CapybaraRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CapybaraModel());
    }

    @Override
    public Identifier getTextureResource(CapybaraEntity instance) {
        return new Identifier(PullUpCapybara.MOD_ID, "textures/item/entity/capybara/capybara.png");
    }

    @Override
    public RenderLayer getRenderType(CapybaraEntity animatable, float partialTicks, MatrixStack stack,
            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
            int packedLightIn, Identifier textureLocation) {
        if (animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder,
                packedLightIn, textureLocation);
    }
}