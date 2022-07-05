package beck.pullup.capybara;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import beck.pullup.capybara.entity.ModEntities;
import beck.pullup.capybara.entity.client.CapybaraRenderer;

public class PullUpCapybaraClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CAPYBARA, CapybaraRenderer::new);
    }
}
