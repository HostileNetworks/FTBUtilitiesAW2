package latmod.lib.util;

import java.lang.reflect.Array;
import java.util.HashSet;

import cpw.mods.fml.common.registry.GameRegistry;
import ftb.lib.api.config.ConfigEntryStringArray;
import ftb.utils.mod.FTBU;
import net.minecraft.block.Block;

/**
 * Wrapper class used for the break/interact whitelists.
 * It's better to use block instances instead of parsing the String
 * names to blocks every time (this is better for performance).
 * @author CosmicDan
 *
 */
public class BlockAndMeta {
	public final Block block;
	public final int meta;
	
	public BlockAndMeta(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }
	
	public static HashSet<BlockAndMeta> buildList(String listName, ConfigEntryStringArray blockListRaw) {
		HashSet<BlockAndMeta> blockList = new HashSet<BlockAndMeta>();

		FTBU.logger.info("Building " + listName + "...");
		
        for (String blockName : blockListRaw.get()) {
            blockName = blockName.trim();
            if (!blockName.equals("")) {
                String[] blockId = blockName.split(":");
                if (Array.getLength(blockId) != 2 && Array.getLength(blockId) != 3 ) {
                	FTBU.logger.warn(" - Invalid block (bad length of " + Array.getLength(blockId) + "): " + blockName);
                    continue;
                }
                if (blockId[0] == null || blockId[1] == null) {
                	FTBU.logger.warn(" - Invalid block (parse/format error): " + blockName);
                    continue;
                }
                Block block = GameRegistry.findBlock(blockId[0], blockId[1]);
                if (block == null) {
                	FTBU.logger.warn(" - Skipping missing block: " + blockName);
                    continue;
                }
                int meta = -1;
                if (Array.getLength(blockId) == 3) {
                    try {
                        meta = Integer.parseInt(blockId[2]);
                        if (meta < 0 || meta > 15)
                            throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                    	FTBU.logger.warn(" - Meta value invalid : '" + blockId[2] + "', must be a number between 0 and 15");
                        continue;
                    }
                }
                blockList.add(new BlockAndMeta(block, meta));
            }
        }

        FTBU.logger.info("...added " + blockList.size() + " blocks to " + listName);
		
		
		return blockList;
	}
}
