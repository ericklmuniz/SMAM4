public class Event implements Comparable<Event> {
    double time;
    EventType type;
    QueueSystem queue;
    Customer customer;

    public Event(double time, EventType type, QueueSystem queue, Customer customer) {
        this.time = time;
        this.type = type;
        this.queue = queue;
        this.customer = customer;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}