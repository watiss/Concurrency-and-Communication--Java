import org.omg.CORBA.*;
import java.util.*;
import java.io.*;
import java.lang.*;

class Channel {

    static private Peer table [];
    static private SemBoundedBuffer buffer;
    //  In the Java corba version, Channel creates its own CORBA
    //  object and makes its IOR available in a file. Then all these
    //  files are read back to create all the stubs needed to
    //  communicate with the other nodes. A buffer is also created for
    //  the CORBA object to store the incoming messages.

    private static org.omg.CORBA.ORB orb;

    //  Initialize creates all the communication channels in order for
    //  the actors to send and receive requests. Then, Agents,
    //  Proxies, Actors and Buffers have to be created. At last,
    //  Channel is activated to resume all the threads waiting for the
    //  starting time. In the Java corba version, node n does the
    //  following :
    //
    //  * create its own Peer (CORBA object) and register its IOR in a
    //    file named <node name> + ".ref"
    //
    //  * read all the ref files (even its own one) and their IOR to
    //    create all the needed stubs
    //
    //  * activate the POA and its own Peer.

    static void initialize () {
        org.omg.CORBA.Object objPoa = null;
        org.omg.PortableServer.POA rootPOA = null;
        org.omg.CORBA.Object obj;
        String ref;
	System.out.println("node_"+Node.self+".ref");
        //  Initialize ORB and Root POA
		orb = org.omg.CORBA.ORB.init((String[])null, null);
        try
	    {
			objPoa = orb.resolve_initial_references ("RootPOA");
	    }
        catch (org.omg.CORBA.ORBPackage.InvalidName ex)
	    {
			System.out.println ("RootPOA est introuvable!");
			System.exit (1);
	    }
	rootPOA = org.omg.PortableServer.POAHelper.narrow (objPoa);
	// VALEH : fin initialisation ORB et RootPOA
		
        buffer = new SemBoundedBuffer (Node.maxNodes);
        table = new Peer [Node.maxNodes];
        PeerImpl peer = new PeerImpl (buffer);
        //  Save IOR in a file named <node name> + ".ref"
	try
	    {
		obj = peer._this(orb); /*ou obj = rootPOA.servant_to_reference(peer); */  		
		ref = orb.object_to_string(obj);
		try
		{
		    java.io.FileOutputStream file = new java.io.FileOutputStream ("node_"+Node.self+".ref");
		    java.io.PrintStream pfile = new java.io.PrintStream (file);
		    pfile.println (ref);
		    file.close ();
		}
        catch (java.io.IOException ex)
		{
		    System.out.println ("Erreur lors de l'ouverture du fichier");
		}
        for (int i = 0; i < Node.maxNodes; i++) {
            //  For each node, try to read a file named <node name> +
            //  ".ref" to get its IOR. Then transform the IOR into a
            //  Peer object and store it into peer table. If the file
            //  does not exist yet, catch the exception and retry
            //  200ms later.
            ref = Node.table [i].name + ".ref";
            String ior = null;
            while (true) {
                try {
                    FileInputStream file = new FileInputStream (ref);
                    BufferedReader in =
                        new BufferedReader (new InputStreamReader (file));
                    ior = in.readLine ();
                    file.close ();
                    break;
                } catch (Exception e) {
                    Context.debug ("File " + ref + " not found");
                    try {
                        Thread.sleep (200);
                    } catch (Exception ignore) {};
                }
            }		           				
            //  Create the stub object from the IOR and register it
            //  into the table.
			org.omg.CORBA.Object peer_obj = orb.string_to_object(ior);
			Peer one_peer = PeerHelper.narrow(peer_obj);
			table[i] = one_peer;
			
            Context.debug (Node.table [i].name + " ior ready");
        }
        //  Activate POA
		rootPOA.the_POAManager().activate();
	    } 
	catch (Exception e)
	    {
		e.printStackTrace();
	    }
    }
    //  activate () is supposed to run the ORB that is the event loop for
    //  CORBA events.
    static public final void activate () {
	orb.run();
    }
    //  Send a message
    public static void send (int to, Message msg){
        //  Send msg to the peer. Use stub to do that.
	table[to].send(msg.kind, msg.from);
    }
    public static Message recv (int from) throws InterruptedException {
        return (Message) buffer.get ();
    }
}
