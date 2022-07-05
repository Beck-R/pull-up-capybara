package beck.pullup.capybara.item;

import beck.pullup.capybara.PullUpCapybara;
import beck.pullup.capybara.entity.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;

public class ModItems {
    public static final Item ORANGE = registerItem("orange",
            new Item(new Item.Settings().food(ModFoodComponents.ORANGE).group(ItemGroup.FOOD)));

    public static final Item CAPYBARA_SPAWN_EGG = registerItem("capybara_spawn_egg",
            new SpawnEggItem(ModEntities.CAPYBARA, 0x543400, 0x783f04, new Item.Settings().group(ItemGroup.MISC)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(PullUpCapybara.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PullUpCapybara.LOGGER.info("Registering items - " + PullUpCapybara.MOD_ID);
    }
}