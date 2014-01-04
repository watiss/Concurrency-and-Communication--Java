/**
 * Generated from IDL interface "Peer".
 *
 * @author JacORB IDL compiler V 2.3.1, 27-May-2009
 * @version generated at 19 nov. 2013 18:59:31
 */

public final class PeerHolder	implements org.omg.CORBA.portable.Streamable{
	 public Peer value;
	public PeerHolder()
	{
	}
	public PeerHolder (final Peer initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return PeerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = PeerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		PeerHelper.write (_out,value);
	}
}
