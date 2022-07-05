package beck.pullup.capybara;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import beck.pullup.capybara.util.ModRegistries;
import beck.pullup.capybara.world.gen.ModWorldGen;
import beck.pullup.capybara.item.ModItems;
import beck.pullup.capybara.util.ModLootTableModifiers;
import software.bernie.geckolib3.GeckoLib;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class PullUpCapybara implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("pullupcapybara");
	public static final String MOD_ID = "pullupcapybara";

	public static final Identifier ID = new Identifier(MOD_ID, "capybara_ambient");
	public static SoundEvent CAPYBARA_AMBIENT = new SoundEvent(ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		ModLootTableModifiers.modifyLootTables();
		ModRegistries.registerModStuffs();

		GeckoLib.initialize();

		ModWorldGen.generateModWorldGen();

		Registry.register(Registry.SOUND_EVENT, PullUpCapybara.ID, CAPYBARA_AMBIENT);
	}
}
