package ftb.lib.api.gui.callback;

public class FieldSelected
{
	public final Object ID;
	public final boolean set;
	private final String result;
	public final boolean closeGui;
	
	public FieldSelected(Object id, boolean s, String r, boolean g)
	{
		ID = id;
		set = s;
		result = r;
		closeGui = g;
	}
	
	public String getS()
	{ return result; }
	
	public int getI()
	{ return Integer.parseInt(getS()); }
	
	public double getD()
	{ return Double.parseDouble(getS()); }
}