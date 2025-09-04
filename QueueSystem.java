import java.util.*;

public class QueueSystem {
    int servers;
    int capacity;
    Queue<Customer> waitingLine = new LinkedList<>();
    PriorityQueue<Double> busyServers = new PriorityQueue<>();
    double[] stateTimes;
    int lostCustomers = 0;
    double lastTime = 0;
    double currentTime = 0;
    double serviceMin, serviceMax;
    // Random random;
    QueueSystem nextQueue = null;
    CountingRandom countingRandom;

    public QueueSystem(int servers, int capacity, double serviceMin, double serviceMax, CountingRandom countingRandom) {
        this.servers = servers;
        this.capacity = capacity;
        this.stateTimes = new double[capacity + 1];
        this.serviceMin = serviceMin;
        this.serviceMax = serviceMax;
        // this.random = random;
        this.countingRandom = countingRandom;
    }

    public void setNextQueue(QueueSystem nextQueue) {
        this.nextQueue = nextQueue;
    }

    private int currentState() {
        int inService = busyServers.size();
        int inQueue = waitingLine.size();
        int state = inService + inQueue;
        if (state > capacity) state = capacity;
        return state;
    }

    public void updateState(double timeNow) {
        int state = currentState();
        stateTimes[state] += timeNow - lastTime;
        lastTime = timeNow;
        this.currentTime = timeNow;
    }

    public boolean canAccept() {
        return (waitingLine.size() + busyServers.size()) < capacity;
    }

    public void addCustomer(Customer customer, double timeNow, PriorityQueue<Event> scheduler) {
        updateState(timeNow);

        if (!canAccept()) {
            lostCustomers++;
            return;
        }

        if (busyServers.size() < servers) {
            if (countingRandom.canDraw()) {
                double duration = serviceMin + (serviceMax - serviceMin) * countingRandom.nextDouble();
                double endService = timeNow + duration;
                busyServers.add(endService);
                scheduler.add(new Event(endService, EventType.DEPARTURE, this, customer));
            } else {
                waitingLine.add(customer);
            }
        } else {
            waitingLine.add(customer);
        }
    }

    public void finishService(double timeNow, PriorityQueue<Event> scheduler) {
        updateState(timeNow);
        busyServers.poll();

        if (!waitingLine.isEmpty()) {
            Customer next = waitingLine.poll();
            if (countingRandom.canDraw()) {
                double duration = serviceMin + (serviceMax - serviceMin) * countingRandom.nextDouble();
                double endService = timeNow + duration;
                busyServers.add(endService);
                scheduler.add(new Event(endService, EventType.DEPARTURE, this, next));
            } else {
                waitingLine.add(next);
            }
        }
    }

    public void printStats(double currentTime) {
        System.out.println("Clientes perdidos: " + lostCustomers);
        System.out.println("Tempos acumulados por estado:");
        for (int i = 0; i < stateTimes.length; i++) {
            System.out.printf("Estado %d: %.6f\n", i, stateTimes[i]);
        }

        System.out.println("Distribuição de estados:");
        for (int i = 0; i < stateTimes.length; i++) {
            double p = (currentTime > 0.0) ? (stateTimes[i] / currentTime) : 0.0;
            System.out.printf("Estado %d: %.6f\n", i, p);
        }
    }
}
