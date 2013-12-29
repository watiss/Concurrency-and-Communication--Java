/**
 * Generated from IDL interface "Peer".
 *
 * @author JacORB IDL compiler V 2.3.1, 27-May-2009
 * @version generated at 19 nov. 2013 18:59:31
 */

public final class PeerHelper
{
	public static void insert (final org.omg.CORBA.Any any, final Peer s)
	{
			any.insert_Object(s);
	}
	public static Peer extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:Peer:1.0", "Peer");
	}
	public static String id()
	{
		return "IDL:Peer:1.0";
	}
	public static Peer read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(_PeerStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final Peer s)
	{
		_out.write_Object(s);
	}
	public static Peer narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof Peer)
		{
			return (Peer)obj;
		}
		else if (obj._is_a("IDL:Peer:1.0"))
		{
			_PeerStub stub;
			stub = new _PeerStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static Peer unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof Peer)
		{
			return (Peer)obj;
		}
		else
		{
			_PeerStub stub;
			stub = new _PeerStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
