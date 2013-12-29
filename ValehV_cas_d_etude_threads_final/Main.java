

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
        for (int i = 0; i < Node.maxNodes; i++) {
            Proxy.table [i] = new Proxy (i, 0);
            Actor.table [i] = new Actor (i);
        }

        for (int i = 0; i < Node.maxNodes; i++) {
            Actor.table [i].start ();
        }

        Channel.activate ();
    }
}
