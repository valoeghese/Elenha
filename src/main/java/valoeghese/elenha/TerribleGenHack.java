package valoeghese.elenha;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TickScheduler;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.StructureFeature;

public class TerribleGenHack extends ChunkRegion implements StructureWorldAccess {
	public TerribleGenHack(ChunkRegion region) {
		super(region.toServerWorld(), ImmutableList.of(new EmptyChunk(region.toServerWorld(), region.getCenterPos())),
				ChunkStatus.FEATURES, -1);
		this.parentRegion = region;
	}

	private final ChunkRegion parentRegion;
	private Set<BlockPos> NOT_HACKED = new HashSet<>();

	public ChunkPos getCenterPos() {
		return this.parentRegion.getCenterPos();
	}

	public void method_36972(@Nullable Supplier<String> supplier) {
		this.parentRegion.method_36972(supplier);
	}

	public Chunk getChunk(int chunkX, int chunkZ) {
		return this.parentRegion.getChunk(chunkX, chunkZ);
	}

	@Nullable
	public Chunk getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
		return this.parentRegion.getChunk(chunkX, chunkZ, leastStatus, create);
	}

	public boolean isChunkLoaded(int chunkX, int chunkZ) {
		/* 138 */     return this.parentRegion.isChunkLoaded(chunkX, chunkZ);
	}

	// MODIFIED
	public BlockState getBlockState(BlockPos pos) {
		if (NOT_HACKED.contains(pos)) {
			return this.parentRegion.getBlockState(pos);
		} else {
			Biome biome = this.parentRegion.getBiome(pos);
			int y = pos.getY();
			int height = biome.getCategory() == Biome.Category.OCEAN ? 40 : 64;
			return y < height ?
					(y < (height - 3) ? Blocks.STONE.getDefaultState() : biome.getGenerationSettings().getSurfaceConfig().getTopMaterial()) : 
						(y < 64 ? Blocks.WATER : Blocks.AIR).getDefaultState();
		}
	}

	// MODIFIED
	public FluidState getFluidState(BlockPos pos) {
		if (NOT_HACKED.contains(pos)) {
			return this.parentRegion.getFluidState(pos);
		} else {
			Biome biome = this.parentRegion.getBiome(pos);
			int y = pos.getY();
			int height = biome.getCategory() == Biome.Category.OCEAN ? 40 : 64;
			return y < height ?
					(y < (height - 3) ? Fluids.EMPTY.getDefaultState() : biome.getGenerationSettings().getSurfaceConfig().getTopMaterial().getFluidState()) : 
						(y < 64 ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
		}
	}

	@Nullable
	public PlayerEntity getClosestPlayer(double x, double y, double z, double maxDistance, Predicate<Entity> targetPredicate) {
		/* 151 */       return null;
	}

	public int getAmbientDarkness() {
		/* 155 */       return 0;
	}

	public BiomeAccess getBiomeAccess() {
		/* 159 */       return this.parentRegion.getBiomeAccess();
	}

	public Biome getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
		/* 163 */       return this.parentRegion.getGeneratorStoredBiome(biomeX, biomeY, biomeZ);
	}

	public float getBrightness(Direction direction, boolean shaded) {
		/* 167 */       return 1.0F;
	}

	public LightingProvider getLightingProvider() {
		/* 171 */       return this.parentRegion.getLightingProvider();
	}

	public boolean breakBlock(BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth) {
		boolean result = this.parentRegion.breakBlock(pos, drop, breakingEntity, maxUpdateDepth);
		if (result) NOT_HACKED.add(pos);
		return result;
	}

	@Nullable
	public BlockEntity getBlockEntity(BlockPos pos) {
		return this.parentRegion.getBlockEntity(pos);
	}

	public boolean method_37368(BlockPos blockPos) {
		return this.parentRegion.method_37368(blockPos);
	}

	// MODIFIED
	public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
		if (this.parentRegion.setBlockState(pos, state, flags, maxUpdateDepth)) {
			NOT_HACKED.add(pos);
			return true;
		} else {
			return false;
		}
	}

	public boolean spawnEntity(Entity entity) {
		return this.parentRegion.spawnEntity(entity);
	}

	public boolean removeBlock(BlockPos pos, boolean move) {
		boolean result = this.parentRegion.removeBlock(pos, move);
		if (result) NOT_HACKED.add(pos);
		return result;
	}

	public WorldBorder getWorldBorder() {
		/* 289 */       return this.parentRegion.getWorldBorder();
	}

	public boolean isClient() {
		/* 293 */       return false;
	}

	// MODIFIED to fix hack
	@Deprecated
	public ServerWorld toServerWorld() {
		return this.parentRegion == null ? null : this.parentRegion.toServerWorld();
	}

	public DynamicRegistryManager getRegistryManager() {
		/* 302 */       return this.parentRegion.getRegistryManager();
	}

	public WorldProperties getLevelProperties() {
		/* 306 */       return this.parentRegion.getLevelProperties();
	}

	public LocalDifficulty getLocalDifficulty(BlockPos pos) {
		return this.parentRegion.getLocalDifficulty(pos);
	}

	@Nullable
	public MinecraftServer getServer() {
		/* 319 */       return this.parentRegion.getServer();
	}

	public ChunkManager getChunkManager() {
		/* 323 */       return this.parentRegion.getChunkManager();
	}

	public long getSeed() {
		/* 327 */       return this.parentRegion.getSeed();
	}

	public TickScheduler<Block> getBlockTickScheduler() {
		/* 331 */       return this.parentRegion.getBlockTickScheduler();
	}

	public TickScheduler<Fluid> getFluidTickScheduler() {
		/* 335 */       return this.parentRegion.getFluidTickScheduler();
	}

	public int getSeaLevel() {
		/* 339 */       return this.parentRegion.getSeaLevel();
	}

	public Random getRandom() {
		/* 343 */       return this.parentRegion.getRandom();
	}

	public int getTopY(Type heightmap, int x, int z) {
		/* 347 */       return this.parentRegion.getTopY();
	}

	public void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
	/* 351 */    }

	public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
	/* 354 */    }

	public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) {
	/* 357 */    }

	public void emitGameEvent(@Nullable Entity entity, GameEvent event, BlockPos pos) {
	/* 360 */    }

	public DimensionType getDimension() {
		/* 363 */       return this.parentRegion.getDimension();
	}

	public boolean testBlockState(BlockPos pos, Predicate<BlockState> state) {
		/* 367 */       return state.test(this.getBlockState(pos));
	}

	public boolean testFluidState(BlockPos pos, Predicate<FluidState> state) {
		/* 371 */       return state.test(this.parentRegion.getFluidState(pos));
	}

	public <T extends Entity> List<T> getEntitiesByType(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate) {
		/* 375 */       return Collections.emptyList();
	}

	public List<Entity> getOtherEntities(@Nullable Entity except, Box box, @Nullable Predicate<? super Entity> predicate) {
		/* 379 */       return Collections.emptyList();
	}

	public List<PlayerEntity> getPlayers() {
		/* 383 */       return Collections.emptyList();
	}

	public Stream<? extends StructureStart<?>> getStructures(ChunkSectionPos pos, StructureFeature<?> feature) {
		/* 387 */       return this.parentRegion.getStructures(pos, feature);
	}

	public int getBottomY() {
		/* 391 */       return this.parentRegion.getBottomY();
	}

	public int getHeight() {
		/* 395 */       return this.parentRegion.getHeight();
	}
}
