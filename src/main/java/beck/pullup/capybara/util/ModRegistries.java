package beck.pullup.capybara.util;

import beck.pullup.capybara.entity.ModEntities;
import beck.pullup.capybara.entity.custom.CapybaraEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModRegistries {
    public static void registerModStuffs() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.CAPYBARA, CapybaraEntity.setAttributes());
    }
}
