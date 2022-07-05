package beck.pullup.capybara.world.gen;

import beck.pullup.capybara.entity.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.Heightmap;
import beck.pullup.capybara.PullUpCapybara;

public class ModEntitySpawn {
    public static void addEntitySpawn() {
        PullUpCapybara.LOGGER.info("Modifying capybara spawns - " + PullUpCapybara.MOD_ID);

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.RIVER, BiomeKeys.MANGROVE_SWAMP),
                SpawnGroup.CREATURE, ModEntities.CAPYBARA, 10, 4, 20);

        SpawnRestrictionAccessor.callRegister(ModEntities.CAPYBARA, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn);
    }
}
