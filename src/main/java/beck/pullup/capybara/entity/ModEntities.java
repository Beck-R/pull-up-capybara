package beck.pullup.capybara.entity;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import beck.pullup.capybara.PullUpCapybara;
import beck.pullup.capybara.entity.custom.CapybaraEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

public class ModEntities {
        public static final EntityType<CapybaraEntity> CAPYBARA = Registry.register(Registry.ENTITY_TYPE,
                        new Identifier(PullUpCapybara.MOD_ID, "capybara"),
                        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CapybaraEntity::new)
                                        .dimensions(EntityDimensions.fixed(0.8f, 1f)).build());
}