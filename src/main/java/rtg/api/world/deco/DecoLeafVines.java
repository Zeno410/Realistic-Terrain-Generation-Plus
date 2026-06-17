package rtg.api.world.deco;

import java.util.Optional;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.minecraftforge.event.terraingen.TerrainGen;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;

import rtg.api.util.ChunkInfo;
import rtg.api.world.RTGWorld;
import rtg.api.world.biome.IRealisticBiome;
import rtg.api.world.biome.RealisticBiomeBase;
import rtg.api.world.gen.feature.WorldGenVinesRTG;

/**
 * @author WhichOnesPink
 */
public class DecoLeafVines extends DecoBase {

    private int loops;
    private float strengthFactor;
    private Block vineBlock;
    private int minY;
    private int maxY;
    private PropertyBool propNorth;
    private PropertyBool propEast;
    private PropertyBool propSouth;
    private PropertyBool propWest;

    protected WorldGenerator worldGenerator;

    public DecoLeafVines() {

        super();

        this.setLoops(1);
        this.setStrengthFactor(0f);
        this.setMinY(63);
        this.setMaxY(200);
        this.vineBlock = Blocks.VINE;
        this.propNorth = BlockVine.NORTH;
        this.propEast = BlockVine.EAST;
        this.propSouth = BlockVine.SOUTH;
        this.propWest = BlockVine.WEST;
    }

    @Override
    public boolean properlyDefined() {

        try {
            IBlockState vineTest = this.vineBlock.getDefaultState()
                .withProperty(this.propNorth, true)
                .withProperty(this.propEast, false)
                .withProperty(this.propSouth, false)
                .withProperty(this.propWest, false);
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

   // @Override
    public void generate(RealisticBiomeBase biome, RTGWorld rtgWorld, Random rand, int worldX, int worldZ, float strength, float river, boolean hasPlacedVillageBlocks) {

        if (true) {//TODO: config option

            if (true) {//(
            	//TerrainGen.decorate(rtgWorld.world(), rand, new BlockPos(worldX, 0, worldZ), GRASS)) {

                this.worldGenerator = new WorldGenVinesRTG(this.vineBlock, this.maxY, this.propNorth, this.propEast, this.propSouth, this.propWest);

                this.setLoops((this.strengthFactor > 0f) ? (int) (this.strengthFactor * loops) : this.loops);
                for (int i = 0; i < this.loops; i++) {

                    int intX = worldX + rand.nextInt(16);// + 8;
                    int intZ = worldZ + rand.nextInt(16);// + 8;
                    int intY = this.minY;

                    worldGenerator.generate(rtgWorld.world(), rand, new BlockPos(intX, intY, intZ));
                }
            }
        }
    }
    
    
    

    public int getLoops() {

        return loops;
    }

    public DecoLeafVines setLoops(int loops) {

        this.loops = loops;
        return this;
    }

    public float getStrengthFactor() {

        return strengthFactor;
    }

    public DecoLeafVines setStrengthFactor(float strengthFactor) {

        this.strengthFactor = strengthFactor;
        return this;
    }

    public Block getVineBlock() {

        return vineBlock;
    }

    public DecoLeafVines setVineBlock(Block vineBlock) {

        this.vineBlock = vineBlock;
        return this;
    }

    public int getMinY() {

        return minY;
    }

    public DecoLeafVines setMinY(int minY) {

        this.minY = minY;
        return this;
    }

    public int getMaxY() {

        return maxY;
    }

    public DecoLeafVines setMaxY(int maxY) {

        this.maxY = maxY;
        return this;
    }

    public PropertyBool getPropNorth() {

        return propNorth;
    }

    public DecoLeafVines setPropNorth(PropertyBool propNorth) {

        this.propNorth = propNorth;
        return this;
    }

    public PropertyBool getPropEast() {

        return propEast;
    }

    public DecoLeafVines setPropEast(PropertyBool propEast) {

        this.propEast = propEast;
        return this;
    }

    public PropertyBool getPropSouth() {

        return propSouth;
    }

    public DecoLeafVines setPropSouth(PropertyBool propSouth) {

        this.propSouth = propSouth;
        return this;
    }

    public PropertyBool getPropWest() {

        return propWest;
    }

    public DecoLeafVines setPropWest(PropertyBool propWest) {

        this.propWest = propWest;
        return this;
    }

	@Override
	public void generate(IRealisticBiome biome, RTGWorld rtgWorld, Random rand, ChunkPos chunkPos, float river,
			boolean hasVillage, ChunkInfo chunkInfo) {
        if (true) {//TODO: config option

            if (true ) {//if (TerrainGen.decorate(rtgWorld.world(), rand, chunkPos.getBlock(0,64,0), GRASS)) {

                this.worldGenerator = new WorldGenVinesRTG(this.vineBlock, this.maxY, this.propNorth, this.propEast, this.propSouth, this.propWest);

                this.setLoops((this.strengthFactor > 0f) ? (int) (this.strengthFactor) : this.loops);
                for (int i = 0; i < this.loops; i++) {

                	// random ground location
                	BlockPos searchPos = chunkPos.getBlock(rand.nextInt(16), 65, rand.nextInt(16));
                	Optional<BlockPos> placement = findVinePlacement(rtgWorld.world(),searchPos);
                	placement.ifPresent(location ->{worldGenerator.generate(rtgWorld.world(), rand, location);});
                }
            }
        }
		
	}
	
	private Optional<BlockPos> findVinePlacement(World world, BlockPos start) {
		while(start.getY()<200) {
			IBlockState blockState = world.getBlockState(start);
			if (blockState.getBlock().isAir(blockState, world, start)) { 
				if (leavesAt(world,start.east())) return Optional.of(start);
				if (leavesAt(world,start.north())) return Optional.of(start);
				if (leavesAt(world,start.west())) return Optional.of(start);
				if (leavesAt(world,start.south())) return Optional.of(start);
			}	
			start = start.up();
		}
		return Optional.empty();
	}
	
	private boolean leavesAt(World world, BlockPos location) {

		IBlockState eastState = world.getBlockState(location);
		if (eastState.isTopSolid()) return true;
		return (eastState.getBlock().isLeaves(eastState, world, location));
	}
}