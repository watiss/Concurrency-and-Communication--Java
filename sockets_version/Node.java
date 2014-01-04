

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Node {
    static public Node table[];
    static public int maxNodes = 0;
    static public final int none = -1;
    static public int self = none;

    String name;
    String info;

    Node (String name, String info) {
        this.name = name;
        this.info = info;
    }

    static public void readConfFile (String n) {
        Scanner s = null;

        try {
            s = new Scanner (new BufferedReader (new FileReader (n)));
            while (s.hasNextLine ()) {
                Node.maxNodes = Node.maxNodes + 1;
                String l = s.nextLine ();
            }

            Node.table = new Node [Node.maxNodes];

            s = new Scanner (new BufferedReader (new FileReader (n)));
            for (int i = 0; i < Node.maxNodes; i++) {
                String name = s.next ();
                String info = s.next ();
                Node.table [i] = new Node (name, info);
            }
        } catch (Exception e) {
            Context.panic ("readConfFile ()", e);
        } finally {
            s.close();
        }
    }
}
