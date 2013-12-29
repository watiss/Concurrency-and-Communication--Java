

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

import java.util.concurrent.Semaphore;

class Channel extends Thread {

    //  Channel aims at connecting all the nodes to the others using
    //  TCP sockets. As the TCP sockets mechanism is asymetric (one
    //  side is connecting, the other side is accepting), there are
    //  two possible ways to fully connect a set of nodes.
    //
    //  Global architecture and number of created sockets:
    //  --------------------------------------------------
    //
    //  1) Each node creates N threads, each of these threads being in
    //  charge of connecting to another node. The main thread on a
    //  given node is in charge of accepting the connections from
    //  other nodes. But in such a solution, to connect a node to
    //  another one, two sockets are created. One can use one socket
    //  to read and the other one to write. But if this architecture
    //  is easy to do, it is far from being efficient.
    //
    //  2) Node M can also create connections to the N other nodes by
    //  creating M threads in charge of connecting to another
    //  node. The main thread on a given node is in charge of
    //  accepting N - M connections from other nodes. In such an
    //  architecture, there is only one socket to connnect to another
    //  node. Channel implements this approach.
    //
    //  Intra-node communication and inter-node communication issues:
    //  -------------------------------------------------------------
    //
    //  Now there is a specific issue when a node want to send a
    //  message to itself (intra-node communication). In solution 1),
    //  there is no problem. A specific thread is in charge of
    //  connecting the node to itself and the main thread in charge of
    //  accepting the connection. But for efficency reasons, solution
    //  2) has been implemented. However, solution 2) raises an
    //  implementation issue because connecting and accepting an
    //  intra-node communication would result in the creation of 2
    //  socket end points (the 2 end points being on the same node)
    //  when inter-node communication results in 1 socket endpoint
    //  (the other endpoint being on the other node). The
    //  implementation would have to handle intra-node communication
    //  in a different manner it handles inter-node
    //  communication. Note that it is not possible to use only one of
    //  the two sockets. Because when a message is sent into one
    //  socket, the message is only available in the other socket.
    //
    //  Therefore, the intra-node communication is not handled with
    //  sockets. When an inter-node message is sent, the message is
    //  directly delivered to the node mailbox (internal buffer). Not
    //  only this solution makes the data and code structures simpler,
    //  but also more efficient (less ressources).
    //
    //  Summary:
    //  --------
    //
    //  On node M, the main thread creates M threads to connect to
    //  other nodes. The main thread accepts N - M - 1 connections (no
    //  intra-node socket). Then (N - M - 1) threads are created to
    //  handle the newly accepted sockets. An intra-node communication
    //  is handled by delivering directly the message to the node
    //  mailbox.

    static private Channel table [];
    static private SemBoundedBuffer buffer;
    //  In the Java sockets implementation, Channel defines one thread
    //  per socket to interact with another node. A buffer is also
    //  created to store the incoming messages.

    static private Semaphore initialized;
    //  This semaphore is used to suspend execution of main thread
    //  until Channel is fully initialized. That is all the sockets
    //  are created and initialized.

    //  These attributes belong to the thread in charge of a
    //  socket. self designates the id of the peer node, address and
    //  port its socket information.

    public int peer;
    public InetAddress address;
    public int port;
    public Socket socket;

    //  Constructor

    Channel (int peer, InetAddress address, int port) {
        this.peer = peer;
        this.address = address;
        this.port = port;
    }

    //  initialize () creates all the communication sockets in order
    //  for the actors to send and receive requests. Then, Agents,
    //  Proxies, Actors and Buffers are created and Channel is
    //  activated. This last step is a non-empty subprogram only in
    //  the CORBA implementation. As explained above, in the Java
    //  sockets version, node Node.self performs the following steps :
    //
    //  * create 0 .. Node.self - 1 threads in charge of connecting to
    //    node peers. When connected, send the node id to identify
    //    itself to peer node.
    //
    //  * accept Node.self + 1 .. Node.maxNode - 1 socket connections
    //    coming from peer nodes. Note that once the socket has been
    //    accepted, the accepting node does not know the id of the
    //    peer. Therefore, the peer sends its id (see above) and the
    //    accepting node reads it. Then Node.self + 1 .. Node.maxNode
    //    - 1 threads are created to deal with these accepted sockets.

    static void initialize () {

        //  These variables are used to bind the local socket to a
        //  given port and to accept socket connections from peer nodes.

        InetAddress addr;
        int port;
        ServerSocket serverSocket;
        Socket clientSocket;

        // We do not want to exit the initialize method until all the
        // sockets have been initialized. As initialize must wait until
        // Node.maxNodes - 1 sockets are initialized, we have to
        // create the semaphore with 2 - Node.maxNodes initial resources.

        initialized = new Semaphore (2 - Node.maxNodes);

        //  Initialize serverSocket and clientSocket

        try {
            serverSocket = new ServerSocket ();
        } catch (Exception e) {
		//VALEH:added printStackTrace
			e.printStackTrace();
		};
        clientSocket = new Socket ();		

        table = new Channel [Node.maxNodes];
        buffer = new SemBoundedBuffer (Node.maxNodes);
        try {
            //  Read address and port for each node. Then, create a
            //  correspondig thread. However, this thread is not yet
            //  activated.

            for (int n = 0; n < Node.maxNodes; n++) {
                String s = Node.table [n].info;
                int i = s.indexOf (':');
                addr = InetAddress.getByName (s.substring (0, i));
                port = Integer.parseInt (s.substring (i+1, s.length ()));
                table [n] = new Channel (n, addr, port);
            }
			Context.debug("Node.self = "+Node.self+" Node.maxNodes = "+Node.maxNodes);
            addr = table [Node.self].address;
            port = table [Node.self].port;
						
            //  Start threads which have to connect to peer node.				
				if (Node.self != 0)
				{
					for (int j=0; j < Node.self; j++)
					{	
						//Context.debug("here");
						table[j].start();						
					}	
				}
			
            //  Bind server socket and configure server socket to
            //  reuse address.
				serverSocket = new ServerSocket(port);				
				try {
					serverSocket.setReuseAddress(true);
				}
				catch (Exception se) {
					se.printStackTrace();
				}
				
            // Accept connections from the threads created above on
            // other nodes.
			if (Node.self!=Node.maxNodes) 
			{
				int peer = 0;
				for (int i = Node.self + 1; i < Node.maxNodes; i++) {

					//  Let server socket accept incoming connection. Then
					//  read id to identify peer node 
					Context.debug("node "+Node.self+" going to wait on accept()");
					clientSocket = serverSocket.accept();
					DataInputStream peer_id = new DataInputStream (clientSocket.getInputStream ());
					try 
					{
						peer = peer_id.readInt();
					} 
					catch (Exception io_e) {
						io_e.printStackTrace();
					}
					
					Context.debug ("ACCEPT from " + peer);

					//  Update socket attribute of the corresponding thread.
					//  Start thread once accept() performed.
					table[peer].socket = clientSocket;
					table[peer].start();
				}
			}
            // Do not exit until all the sockets have been
            // initialized. This is done in the run () method.

            initialized.acquire ();
            Context.debug ("Channel initialized");

        } catch (Exception e) {
            Context.panic ("initialize ()", e);
        }
    }

    //  activate () is called when Actors, Agents, Proxies and Buffers
    //  have been created. It is a non-empty subprogram only in the
    //  CORBA implementation.

    static public final void activate () {
    }

    //  Run thread Receiver

    public void run () {
        DataOutputStream DOS = null;
        DataInputStream DIS = null;
		
		Context.debug("Thread created to handle connection to peer node " + peer);
		 
        try {
            if (socket == null) {
                //  When socket has not been initialized, the thread
                //  is responsible to start the connection step.
                //  However, the peer node may not be ready to
                //  accept. The thread tries to connect. On failure,
                //  after a 200ms timeout, it retries the operation.
				while (true) {
					try {
						socket = new Socket(address, port);	
						break;
					} catch (Exception e) {
						Context.debug ("Connection to peer node failed. Waiting 200ms before retry");
						try {
							Thread.sleep (200);
						} catch (Exception ignore) {};
					}
				}					
                DOS = new DataOutputStream (socket.getOutputStream ());
				//  Once connected, the thread sends its id
                //  to identify itself.
				DOS.writeInt(Node.self);
            }
            //  At this point, the socket is fully initialized. So let
            //  this know to the main initialize () method.
            Context.debug ("socket " + peer + " is " + socket);
            initialized.release ();
            DIS = new DataInputStream (socket.getInputStream ());
            while (true) {
                Context.debug ("RECEIVE 2 ints from " + peer);
                int kind;
                int from; 
				//VALEH:receive via socket then send via send() to buffer				
				kind = DIS.readInt();
				from = DIS.readInt();
				Context.debug("kind: "+kind+" from: "+from);
				this.send(Node.self, new Message(kind, from));
            }
        } catch (Exception e) {
            Context.panic ("Channel.run ()", e);
        }
    }
    //  Send a message to node "to"
    static public void send (int to, Message msg) {
        //  Send in a raw the two ints stored in msg (kind and from)
        try {
            //  For intra-node communication, deliver the msg directly
            //  to the local buffer.
            if (to == Node.self)
                buffer.put (msg);
            else { 
				//VALEH:send via socket
				Socket to_socket = table[to].socket;
				DataOutputStream msg_to_send = new DataOutputStream(to_socket.getOutputStream());
				msg_to_send.writeInt(msg.kind);
				msg_to_send.writeInt(msg.from);
            }
        } catch (Exception e) {
           Context.panic ("send ()", e);
        }
    }
    static public final Message recv (int from) throws InterruptedException {
        return (Message) buffer.get ();
    }
}
