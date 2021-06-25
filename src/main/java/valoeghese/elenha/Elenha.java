package valoeghese.elenha;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Elenha implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Elenha");

	@Override
	public void onInitialize() {
		LOGGER.info("Elenha is Initialising!");

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			new ElenhaWorldType("elenha");
		}

		LOGGER.info("Elenha Initialised!");
	}
}
