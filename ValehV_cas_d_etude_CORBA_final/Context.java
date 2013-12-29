

class Context {
    static boolean debugMode = false;

    static void debug (String s) {
        if (debugMode) System.out.println (s);
    }

    static void panic (String s, Exception e) {
        System.out.println ("*** PANIC ***");
        System.out.println (s);
        if (e != null) e.printStackTrace ();
        System.exit (-1);
    }

    static void panic (String s) {
        Context.panic (s, null);
    }
}
