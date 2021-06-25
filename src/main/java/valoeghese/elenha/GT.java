package valoeghese.elenha;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

class ElenhaWorldType extends GeneratorType {
	public ElenhaWorldType(String name) {
		super(name);
		VALUES.add(this);
	}

	@Override
	protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
		return new ElenhaCG(
				new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry),
				seed,
				chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD).getStructuresConfig());
	}
}