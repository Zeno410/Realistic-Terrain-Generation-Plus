package rtg.api.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

// this class keeps a limited list of world-specific chunks
// intended for tracking whether some action has been done

public class ChunkTracker {
	
	private final WeakHashMap<World,LinkedHashSet<ChunkPos>> remembered = new WeakHashMap<>();
	private final int toRemember;

	public ChunkTracker(int chunksPerWorld) {
		toRemember = chunksPerWorld;
	}
	
	public synchronized boolean addIfNeeded(World world, ChunkPos chunkPos) {
		if (!remembered.containsKey(world)) remembered.put(world, new LinkedHashSet<ChunkPos>());
		LinkedHashSet<ChunkPos> rememberedChunks = remembered.get(world);
		if (rememberedChunks.contains(chunkPos)) return false;
		rememberedChunks.add(chunkPos);
		if (rememberedChunks.size()> toRemember) {
			Iterator<ChunkPos> list = rememberedChunks.iterator();
			if (list.hasNext()) {
				rememberedChunks.remove(list.next());
			}
		}
		return true;
	}

}
