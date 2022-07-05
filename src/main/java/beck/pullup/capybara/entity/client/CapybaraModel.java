package beck.pullup.capybara.entity.client;

import beck.pullup.capybara.PullUpCapybara;
import beck.pullup.capybara.entity.custom.CapybaraEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CapybaraModel extends AnimatedGeoModel<CapybaraEntity> {
    // public Identifier getModelLocation(CapybaraEntity object) {
    // return new Identifier(PullUpCapybara.MOD_ID, "geo/capybara.geo.json");
    // }

    // public Identifier getTextureLocation(CapybaraEntity object) {
    // return new Identifier(PullUpCapybara.MOD_ID,
    // "textures/item/entity/capybara/capybara.png");
    // }

    // public Identifier getAnimationFileLocation(CapybaraEntity animatable) {
    // return new Identifier(PullUpCapybara.MOD_ID,
    // "animations/capybara.animation.json");
    // }

    public Identifier getModelResource(CapybaraEntity object) {
        return new Identifier(PullUpCapybara.MOD_ID, "geo/capybara.geo.json");
    }

    public Identifier getTextureResource(CapybaraEntity object) {
        return new Identifier(PullUpCapybara.MOD_ID, "textures/item/entity/capybara/capybara.png");
    }

    public Identifier getAnimationResource(CapybaraEntity animatable) {
        return new Identifier(PullUpCapybara.MOD_ID, "animations/capybara.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(CapybaraEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
