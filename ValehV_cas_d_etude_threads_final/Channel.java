

class Channel {

    static private SemBoundedBuffer table [];
    //  In the Java threads implementation, Channel shares all the
    //  communication buffers betwen nodes as all the actors are
    //  located in the same process.

    //  initialize () creates all the communication buffers in order
    //  for the actors to send and receive requests. Then, Agents,
    //  Proxies, Actors and Buffers are created and Channel is
    //  activated. This last step is a non-empty subprogram only in
    //  the CORBA implementation.

    static public final void initialize () {
        table = new SemBoundedBuffer [Node.maxNodes];
        for (int i = 0; i < Node.maxNodes; i++)
            table [i] = new SemBoundedBuffer (Node.maxNodes);
    }

    //  activate () is called when Actors, Agents, Proxies and Buffers
    //  have been created. It is a non-empty subprogram only in the
    //  CORBA implementation.

    static public final void activate () {
    }

    //  Send message msg to a node

    static public void send (int to, Message msg) throws InterruptedException {
        table [to].put (msg);
    }

    //  A node thread (a thread simulating a node) reads messages from
    //  its own buffer. In other implementations, parameter "from" is
    //  equal to "self" but for the Java threads implementation "self"
    //  has no meaning. Therefore, to read its own buffer, a node
    //  thread has to specifiy its node id.

    static public Message recv (int from) throws InterruptedException {
        return (Message) table [from].get ();
    }
}
