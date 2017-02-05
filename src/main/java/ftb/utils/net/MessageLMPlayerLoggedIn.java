package ftb.utils.net;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import ftb.utils.api.EventLMPlayerClient;
import ftb.utils.world.*;
import latmod.lib.ByteCount;

import java.util.UUID;

public class MessageLMPlayerLoggedIn extends MessageFTBU
{
	public MessageLMPlayerLoggedIn() { super(ByteCount.INT); }
	
	public MessageLMPlayerLoggedIn(LMPlayerServer p, boolean first, boolean self)
	{
		this();
		
		io.writeInt(p.getPlayerID());
		io.writeUUID(p.getProfile().getId());
		io.writeUTF(p.getProfile().getName());
		io.writeBoolean(FTBLib.isOP(p.getProfile()));
		io.writeBoolean(first);
		p.writeToNet(io, self);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		if(LMWorldClient.inst == null) return null;
		
		int playerID = io.readInt();
		UUID uuid = io.readUUID();
		String username = io.readUTF();
		boolean isOp = io.readBoolean();
		boolean firstTime = io.readBoolean();
		
		LMPlayerClient p = LMWorldClient.inst.getPlayer(playerID);
		boolean add = p == null;
		if(add) p = new LMPlayerClient(LMWorldClient.inst, playerID, new GameProfile(uuid, username));
		p.readFromNet(io, p.getPlayerID() == LMWorldClient.inst.clientPlayerID);
		LMWorldClient.inst.playerMap.put(p.getPlayerID(), p);
		p.isOp = isOp;
		new EventLMPlayerClient.LoggedIn(p, firstTime).post();
		new EventLMPlayerClient.DataLoaded(p).post();
		return null;
	}
}