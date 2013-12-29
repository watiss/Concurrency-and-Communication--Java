
import org.omg.CORBA.*;
import java.lang.*;

class PeerImpl extends PeerPOA {

    private SemBoundedBuffer buffer;

    PeerImpl (SemBoundedBuffer buffer) {
        this.buffer = buffer;
    }

    public void send (int kind, int from) {
        try {
            buffer.put (new Message (kind, from));	    
        } catch (Exception e) {
            Context.panic ("PeerImpl.send()", e);
        }
    }
}
