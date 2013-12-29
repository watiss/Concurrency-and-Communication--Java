

class Main {
    public static void main (String args[]) {
        String n = "nodes";

        for (int i = 0; i < args.length; i++) {
            if (args [i].equals ("-v"))
                Context.debugMode = true;
            else
                try {
                    Node.self = Integer.parseInt (args [i]);
                } catch (Exception e) {
                    n = args [i];
                }
        }

        Node.readConfFile (n);
        Channel.initialize ();

        Proxy.table = new Proxy [Node.maxNodes];
        Actor.table = new Actor [Node.maxNodes];
        Proxy.table [Node.self] = new Proxy (Node.self, 0);
        Actor.table [Node.self] = new Actor (Node.self);
        Actor.table [Node.self].start ();

        Channel.activate ();
    }
}
