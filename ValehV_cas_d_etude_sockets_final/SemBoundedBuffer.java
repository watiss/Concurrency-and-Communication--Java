

class SemBoundedBuffer extends BoundedBuffer {
    Semaphore emptySlots, fullSlots;

    SemBoundedBuffer (int maxSize) {
        super(maxSize);
	// Initialize semaphores.
	emptySlots = new Semaphore(maxSize);
	fullSlots = new Semaphore(0); 
    }

    // This method must be protected against simultaneous accesses
    // from consumers. But **DO NOT CHANGE** the signature of this method.

    Object get() throws InterruptedException {
        Object value;
			// Suspend until a full slot is available
			fullSlots.acquire();
			synchronized (this){
            value = super.get();
			}
        // Release an empty slot
		emptySlots.release();

        return value;
    }

    // This method must be protected against simultaneous accesses
    // from producers. But **DO NOT CHANGE** the signature of this method.

    void put(Object value) throws InterruptedException {
        // Suspend until an empty slot is available
			emptySlots.acquire();
			synchronized (this){
            super.put(value);
			}
        // Release a full slot
        fullSlots.release();
    }
}
