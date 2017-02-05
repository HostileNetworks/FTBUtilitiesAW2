package ftb.utils.mod.config;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.config.*;
import latmod.lib.annotations.*;
import net.minecraft.entity.*;

import java.util.*;

public class FTBUConfigGeneral
{
	@NumberBounds(min = 0D, max = 720D)
	@Info({"Server will automatically shut down after X hours", "0 - Disabled", "0.5 - 30 minutes", "1 - 1 Hour", "24 - 1 Day", "168 - 1 Week", "720 - 1 Month"})
	public static final ConfigEntryDouble restart_timer = new ConfigEntryDouble("restart_timer", 0D);
	
	@Info("If set to true, explosions in spawn area will be allowed")
	public static final ConfigEntryBool spawn_or_commonwealth_explosions = new ConfigEntryBool("spawn_or_commonwealth_explosions", false);
	
	@Info("If set to true, mobs in spawn area will be allowed")
	public static final ConfigEntryBool spawn_or_commonwealth_mobs = new ConfigEntryBool("spawn_or_commonwealth_mobs", false);
	
	@Info("If set to false, players won't be able to attack each other in spawn area")
	public static final ConfigEntryBool spawn_or_commonwealth_pvp = new ConfigEntryBool("spawn_or_commonwealth_pvp", true);
	
	@Info("Entity IDs that are banned from world. They will not spawn and existing ones will be destroyed")
	private static final ConfigEntryStringArray blocked_entities = new ConfigEntryStringArray("blocked_entities");
	
	@Info("Enable spawn area in singleplayer")
	public static final ConfigEntryBool spawn_area_in_sp = new ConfigEntryBool("spawn_area_in_sp", false);
	
	public static final ConfigEntryBool server_info_difficulty = new ConfigEntryBool("server_info_difficulty", true);
	
	public static final ConfigEntryBool server_info_mode = new ConfigEntryBool("server_info_mode", true);
	
	@Info("Block IDs that any player can interact with in the spawn/commonwealth zones")
	public static final ConfigEntryStringArray spawn_and_commonwealth_interact_whitelist = new ConfigEntryStringArray("spawn_and_commonwealth_interact_whitelist", "minecraft:wooden_door");
	
	private static final List<Class<?>> blockedEntitiesL = new ArrayList<>();
	
	public static void onReloaded(Side side)
	{
		if(side.isServer())
		{
			blockedEntitiesL.clear();
			
			for(String s : blocked_entities.get())
			{
				try
				{
					Class<?> c = (Class<?>) EntityList.stringToClassMapping.get(s);
					if(c != null && Entity.class.isAssignableFrom(c)) blockedEntitiesL.add(c);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		/*
		blockedItemsL.removeAll();
		
		list = blockedItems.get();
		
		if(list != null && list.length > 0)
		{
			for(String s : list)
			{
				ItemStack is = ItemStackTypeAdapter.parseItem(s);
				if(is != null && !LMInvUtils.isAir(is.getItem())) blockedItemsL.add(is);
			}
		}
		*/
	}
	
	public static boolean isEntityBanned(Class<?> c)
	{
		for(int i = 0; i < blockedEntitiesL.size(); i++)
		{
			Class<?> c1 = blockedEntitiesL.get(i);
			if(c1.isAssignableFrom(c)) return true;
		}
		
		return false;
	}
}