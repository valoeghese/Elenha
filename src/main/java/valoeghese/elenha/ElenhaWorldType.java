package valoeghese.elenha;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import valoeghese.elenha.Elenha.ChunkGenFactory;

class ElenhaWorldType extends GeneratorType {
	public ElenhaWorldType(String name, boolean haxed) {
		super(name);
		VALUES.add(this);
		this.generator = haxed ? HackCG::new : ElenhaCG::new;
	}

	private final ChunkGenFactory generator;

	@Override
	protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
		return this.generator.create(
				new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry),
				seed,
				chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD).getStructuresConfig());
	}
}
