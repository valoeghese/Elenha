package valoeghese.elenha;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class HackCG extends ElenhaCG {
	public HackCG(BiomeSource biomeSource, long worldSeed, StructuresConfig structuresConfig) {
		super(biomeSource, worldSeed, structuresConfig);
	}

	public static final Codec<HackCG> CODEC = RecordCodecBuilder.create(
			i -> i.group(
					BiomeSource.CODEC.fieldOf("biome_source")
						.forGetter(HackCG::getBiomeSource),
					Codec.LONG.fieldOf("seed").stable()
						.forGetter(cg -> cg.seed),
					StructuresConfig.CODEC.fieldOf("structures")
						.forGetter(HackCG::getStructuresConfig))
			.apply(i, i.stable(HackCG::new))
			);

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new HackCG(this.biomeSource, this.seed, this.getStructuresConfig());
	}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
		ChunkPos chunkPos = region.getCenterPos();
		int i = chunkPos.getStartX();
		int j = chunkPos.getStartZ();
		BlockPos blockPos = new BlockPos(i, region.getBottomY(), j);
		Biome biome = this.populationSource.getBiomeForNoiseGen(chunkPos);
		ChunkRandom chunkRandom = new ChunkRandom();
		long l = chunkRandom.setPopulationSeed(region.getSeed(), i, j);

		try {
			biome.generateFeatureStep(accessor, this, region, l, chunkRandom, blockPos);
		} catch (Exception var13) {
			CrashReport crashReport = CrashReport.create(var13, "Biome decoration");
			crashReport.addElement("Generation").add("CenterX", chunkPos.x).add("CenterZ", chunkPos.z).add("Seed", l).add("Biome", biome);
			throw new CrashException(crashReport);
		}
	}
}
