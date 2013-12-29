

class Proxy {
    static public Proxy table [];

    // Several constants to represent the lock status.
    //
    // NOTE: this implementation of a distributed lock must work when
    // there are SEVERAL threads on a node. There is only one actor in
    // this case study. However, this implementation should also work
    // in presence of several local threads.
    //
    // * locked : the lock is available on the node and locked by one
    // local thread. 
    //
    // * unlocked : the lock is available on the node and not locked
    // by any local thread.
    //
    // * removed : the lock is not locally available and has been
    // transfered to another node. No request to get it back has been
    // performed.
    //
    // * delivered : the lock was not locally available. A request to
    // get it back has been performed and has just been completed.
    //
    // * requested :  the lock was not locally available. A request to
    // get it back has been performed but has not been completed yet.

    final static private int locked = 0;
    final static private int unlocked = 1;
    final static private int removed = 2;
    final static private int delivered = 3;
    final static private int requested = 4;

    private String name;

    private int self;
    private int status;

    // Variable release is used to let the proxy know that the agent
    // wants it to release the lock as soon as it is no longer locked.
    private boolean release = false;

    private Agent agent;

    Proxy (int self, int owner) {
        this.name = Node.table [self].name + " (proxy)";
        this.self = self;
        if (self == owner) {
            this.status = unlocked;
        } else {
            this.status = removed;
        }
        this.agent = new Agent (self, owner, this);
        this.agent.start ();
    }

    public String toString () {
        String n = this.name;
        n = n + "(" + release + ")";
        switch (status) {
        case locked : n = n + " [locked]"; break;
        case unlocked : n = n + " [unlocked]"; break;
        case removed : n = n + " [removed]"; break;
        case delivered : n = n + " [delivered]"; break;
        case requested : n = n + " [requested]"; break;
        default : n = n + " [invalid]"; break;
        }
        return n;
    }

    synchronized void lock () throws InterruptedException {
        //  When lock is not available, request it to agent        
		while (status==removed) {
			Channel.send(self, new Message(Message.requestLock, self));			
			status = requested;
		}
        //  Stay in wait loop until the lock is : unlocked if the lock
        //  is locally available or delivered if it is unavailable
		//Context.debug (this + " lock methid, gonna wait");		
		while (status==requested || status==locked) wait();		
		status = locked;
    }
    
    synchronized void unlock () throws InterruptedException {
        //  Check whether the agent wants the proxy to release the
        //  lock.  If so, send the lock to the agent in order to wake
        //  it up and set the status to "removed".

		if (release){			
			Channel.send(self, new Message(Message.receiveLock, self));						
			status = removed;
			//Context.debug (this + "I'm in unlock method, release=true");			
			release=false;
		}
		else {

            status = unlocked;
			Context.debug (this + "I'm in unlock method, release=false");
            notifyAll();
		}
    }
	
    //  Request the proxy to release the lock. If the lock is
    //  released, return true. In this case, the proxy state is
    //  "removed". Otherwise, set release to true and return false as
    //  request () has not successfully completed.

    synchronized boolean request () {
        //  When the lock is unlocked, remove it from the proxy
        //  Otherwise, let the proxy know through variable release
        //  that the lock must be removed from proxy as soon as the
        //  lock is unlocked.

		if (status==unlocked) {			
			status=removed;
		}
		else {
			release=true;						
			Context.debug (this + "I'm in request method, release=true");
		}

        //  Return true when the lock has been succesfully removed
        return (status == removed);
    }
	
    //  Deliver the lock to the proxy as a request was emitted to get
    //  the lock back. Change proxy status and resume pending thread.

    synchronized void deliver () {
        //  Let the proxy know that the lock is available by changing
        //  the state and notifying an actor waiting for it.		
			status = delivered;
			notifyAll();		
    }
}
