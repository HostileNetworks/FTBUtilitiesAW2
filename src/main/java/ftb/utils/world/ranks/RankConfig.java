package ftb.utils.world.ranks;

import java.util.HashSet;

import ftb.lib.PrivacyLevel;
import ftb.lib.api.config.*;
import latmod.lib.IntList;
import latmod.lib.annotations.*;
import latmod.lib.util.BlockAndMeta;
import latmod.lib.util.EnumEnabled;

public class RankConfig
{
	private HashSet<BlockAndMeta> break_whitelist = new HashSet<BlockAndMeta>();
	private static boolean break_whitelist_built = false;
	
	public final ConfigGroup custom = new ConfigGroup("custom_config");
	
	@NumberBounds(min = 0, max = 30000)
	@Info({"Max amount of chunks that player can claim", "0 - Disabled"})
	public final ConfigEntryInt max_claims = new ConfigEntryInt("max_claims", 100);
	
	@NumberBounds(min = 0, max = 30000)
	@Info("Max home count")
	public final ConfigEntryInt max_homes = new ConfigEntryInt("max_homes", 1);
	
	@Info("Can use /home to teleport to/from another dimension")
	public final ConfigEntryBool cross_dim_homes = new ConfigEntryBool("cross_dim_homes", true);
	
	@Flags(Flags.SYNC)
	@Info({"'-' - Player setting", "'disabled' - Explosions will never happen in claimed chunks\", \"'enabled' - Explosions will always happen in claimed chunks"})
	public final ConfigEntryEnum<EnumEnabled> forced_explosions = new ConfigEntryEnum<>("forced_explosions", EnumEnabled.VALUES, null, true);
	
	@Flags(Flags.SYNC)
	@Info({ })
	public final ConfigEntryEnum<PrivacyLevel> forced_chunk_security = new ConfigEntryEnum<>("forced_chunk_security", PrivacyLevel.VALUES_3, null, true);
	
	@Info("Block IDs that you can break in claimed chunks")
	private final ConfigEntryStringArray break_whitelist_raw = new ConfigEntryStringArray("break_whitelist", "OpenBlocks:grave");
	
	@Flags(Flags.SYNC)
	@Info("Dimensions where players can't claim")
	public final ConfigEntryIntArray dimension_blacklist = new ConfigEntryIntArray("dimension_blacklist", IntList.asList());
	
	@Flags(Flags.SYNC)
	@Info("Allow creative players access protected chests / chunks")
	public final ConfigEntryBool allow_creative_interact_secure = new ConfigEntryBool("allow_creative_interact_secure", false);
	
	@Info("If set to false, players won't be able to see others Rank in FriendsGUI")
	public final ConfigEntryBool show_rank = new ConfigEntryBool("show_rank", true);
	
	@Info("Badge ID")
	public final ConfigEntryString badge = new ConfigEntryString("badge", "");
	
	public ConfigGroup getAsGroup(String id, boolean copy)
	{
		ConfigGroup g = new ConfigGroup(id);
		g.addAll(RankConfig.class, this, copy);
		return g;
	}
	
	public HashSet<BlockAndMeta> getBreakWhitelist() {
		if (!break_whitelist_built) {
			break_whitelist = BlockAndMeta.buildList("Break block whitelist", break_whitelist_raw);
			break_whitelist_built = true;
		}
		return break_whitelist;
	}
}