package ftb.lib.api.notification;

import com.google.gson.JsonElement;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.GuiScreenRegistry;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.LMUtils;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.gui.*;

import java.io.File;
import java.net.URI;

public abstract class ClickActionType extends FinalIDObject
{
	public ClickActionType(String s)
	{
		super(s);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void onClicked(JsonElement data);
	
	public String getDisplayName()
	{ return FTBLibMod.proxy.translate("click_action." + getID()); }
	
	// Static //
	
	public static final ClickActionType CMD = new ClickActionType("cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.execClientCommand("/" + data.getAsString()); }
	};
	
	public static final ClickActionType SHOW_CMD = new ClickActionType("show_cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.openGui(new GuiChat(data.getAsString())); }
	};
	
	public static final ClickActionType URL = new ClickActionType("url")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new URI(data.getAsString())); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType FILE = new ClickActionType("file")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new File(data.getAsString()).toURI()); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType GUI = new ClickActionType("gui")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			GuiScreen gui = GuiScreenRegistry.openGui(FTBLibClient.mc.thePlayer, data.getAsString());
			if(gui != null) FTBLibClient.openGui(gui);
		}
	};
}