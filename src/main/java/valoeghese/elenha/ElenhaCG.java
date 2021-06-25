package valoeghese.elenha;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class ElenhaCG extends ChunkGenerator {
	public ElenhaCG(BiomeSource biomeSource, long worldSeed, StructuresConfig structuresConfig) {
		super(biomeSource, biomeSource, structuresConfig, worldSeed);
		this.seed = worldSeed;
	}

	private final long seed;

	public static final Codec<ElenhaCG> CODEC = RecordCodecBuilder.create(
			i -> i.group(
					BiomeSource.CODEC.fieldOf("biome_source")
					.forGetter(ElenhaCG::getBiomeSource),
					Codec.LONG.fieldOf("seed").stable()
					.forGetter(cg -> cg.seed),
					StructuresConfig.CODEC.fieldOf("structures")
					.forGetter(ElenhaCG::getStructuresConfig))
			.apply(i, i.stable(ElenhaCG::new))
			);

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new ElenhaCG(this.biomeSource, this.seed, this.getStructuresConfig());
	}

	@Override
	public void carve(long seed, BiomeAccess access, Chunk chunk, Carver carver) {
		// no
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		// Nothing
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
		// Screw with heightmaps
		long[] heightmap = new long[16 * 16];
		Arrays.fill(heightmap, this.getHeight(0, 0, null, null)); // null bc we ignore these parameters
		chunk.setHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, heightmap);
		chunk.setHeightmap(Heightmap.Type.WORLD_SURFACE_WG, heightmap);
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getHeight(int x, int z, Type heightmap, HeightLimitView world) {
		return 64;
	}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
		super.generateFeatures(new TerribleGenHack(region), accessor);
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		BlockState states[] = new BlockState[world.getHeight()];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(world.getBottomY(), states);
	}

}
