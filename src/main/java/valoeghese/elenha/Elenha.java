package valoeghese.elenha;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class Elenha implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Elenha");

	@Override
	public void onInitialize() {
		LOGGER.info("Elenha is Initialising!");

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			new ElenhaWorldType("elenha", false);
			new ElenhaWorldType("elenhax", true);
		}

		LOGGER.info("Elenha Initialised!");
	}


	@FunctionalInterface
	public interface ChunkGenFactory {
		ElenhaCG create(BiomeSource source, long seed, StructuresConfig config);
	}
}
