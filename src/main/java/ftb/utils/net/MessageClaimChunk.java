package ftb.utils.net;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.LMAccessToken;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.utils.world.*;
import ftb.utils.world.claims.ClaimedChunk;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageClaimChunk extends MessageFTBU
{
	public static final int ID_CLAIM = 0;
	public static final int ID_UNCLAIM = 1;
	public static final int ID_UNCLAIM_ALL = 2;
	public static final int ID_UNCLAIM_ALL_DIMS = 3;
	public static final int ID_LOAD = 4;
	public static final int ID_UNLOAD = 5;
	
	public MessageClaimChunk() { super(ByteCount.BYTE); }
	
	public MessageClaimChunk(int d, long t, int x, int z, int c)
	{
		this();
		io.writeByte(c);
		io.writeLong(t);
		io.writeInt(d);
		io.writeInt(x);
		io.writeInt(z);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBUNetHandler.NET_WORLD; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP entityPlayer = ctx.getServerHandler().playerEntity;
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(entityPlayer.getGameProfile()))
			return null;
	    
		int type = io.readUnsignedByte();
		long token = io.readLong();
		int dim = io.readInt();
		int cx = io.readInt();
		int cz = io.readInt();
		
		LMPlayerServer p = LMWorldServer.inst.getPlayer(ctx.getServerHandler().playerEntity);
		if(type == ID_CLAIM)
		{
			p.claimChunk(dim, cx, cz, true);
			return new MessageAreaUpdate(p, cx, cz, dim, 1, 1);
		}
		else if(type == ID_UNCLAIM)
		{
			if(token != 0L && LMAccessToken.equals(p.getPlayer(), token, false))
			{
				ClaimedChunk c = LMWorldServer.inst.claimedChunks.getChunk(dim, cx, cz);
				if(c != null)
				{
					LMPlayerServer p1 = LMWorldServer.inst.getPlayer(c.ownerID);
					p1.unclaimChunk(dim, cx, cz);
				}
			}
			else p.unclaimChunk(dim, cx, cz, true);
			return new MessageAreaUpdate(p, cx, cz, dim, 1, 1);
		}
		else if(type == ID_UNCLAIM_ALL) p.unclaimAllChunks(Integer.valueOf(dim));
		else if(type == ID_UNCLAIM_ALL_DIMS) p.unclaimAllChunks(null);
		else if(type == ID_LOAD) p.setLoaded(dim, cx, cz, true);
		else if(type == ID_UNLOAD) p.setLoaded(dim, cx, cz, false);
		
		return null;
		
	}
}