

final class Message {
    final static public int requestLock = 1;
    final static public int receiveLock = 2;

    int kind;
    int from;

    Message (int kind, int from) {
        this.kind = kind;
        this.from = from;
    }

    public String toString () {
        if (this.kind == requestLock)
            return "from " + from + " request lock";
        else
            return "from " + from + " receive lock";
    }
}
