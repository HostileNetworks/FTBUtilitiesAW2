package ftb.lib;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class EventBusHelper
{
	public static void register(Object o)
	{
		if(o == null) return;
		MinecraftForge.EVENT_BUS.register(o);
		FMLCommonHandler.instance().bus().register(o);
	}
	
	public static void unregister(Object o)
	{
		if(o == null) return;
		MinecraftForge.EVENT_BUS.unregister(o);
		FMLCommonHandler.instance().bus().unregister(o);
	}
}