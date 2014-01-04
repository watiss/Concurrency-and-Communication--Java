
import java.util.Random;

class Actor extends Thread {
    static public Actor table [];

    private String name;

    private int self;
    private Proxy proxy;

    Actor (int self) {
        this.name = Node.table [self].name + " (actor)";
        this.self = self;
        this.proxy = Proxy.table [self];
        Context.debug ("create " + this);
    }

    public String toString () {
        return this.name;
    }

    public void run () {
        Random r = new Random ();
        int count = 0;

        try {
            while (true) {
                Context.debug (this + " request lock");
                proxy.lock ();
                Context.debug (this + " enter lock");
                sleep (r.nextInt (500));
                count = count + 1;
                System.out.println (name + " count = " + count);
                proxy.unlock ();
                Context.debug (this + " leave lock");
                sleep (r.nextInt (1000));
            }

        } catch (Exception e) {
            Context.panic ("Actor.run ()", e);
        }
    }
}
