import org.omg.PortableServer.POA;
/**
 * Generated from IDL interface "Peer".
 *
 * @author JacORB IDL compiler V 2.3.1, 27-May-2009
 * @version generated at 19 nov. 2013 18:59:31
 */

public class PeerPOATie
	extends PeerPOA
{
	private PeerOperations _delegate;

	private POA _poa;
	public PeerPOATie(PeerOperations delegate)
	{
		_delegate = delegate;
	}
	public PeerPOATie(PeerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public Peer _this()
	{
		return PeerHelper.narrow(_this_object());
	}
	public Peer _this(org.omg.CORBA.ORB orb)
	{
		return PeerHelper.narrow(_this_object(orb));
	}
	public PeerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(PeerOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		return super._default_POA();
	}
	public void send(int kind, int from)
	{
_delegate.send(kind,from);
	}

}
