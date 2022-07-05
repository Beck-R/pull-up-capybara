package beck.pullup.capybara.util;

import net.minecraft.util.Identifier;
import beck.pullup.capybara.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.condition.RandomChanceLootCondition;

public class ModLootTableModifiers {
    private static final Identifier SHIPWRECK_SUPPLY = new Identifier("minecraft", "chests/shipwreck_supply");
    private static final Identifier ACACIA_LEAVES = new Identifier("minecraft", "blocks/acacia_leaves");

    public static void modifyLootTables() {
        // modify shipwreck supply loot table to add oranges
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && SHIPWRECK_SUPPLY.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2))
                        .conditionally(RandomChanceLootCondition.builder(0.7f)) // Drops 70% of the time
                        .with(ItemEntry.builder(ModItems.ORANGE));

                tableBuilder.pool(poolBuilder);
            }
        });

        // modify acacia leaves loot table to add oranges
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && ACACIA_LEAVES.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f)) // Drops 0.05% of the time
                        .with(ItemEntry.builder(ModItems.ORANGE));

                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
