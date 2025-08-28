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
    Random random;
    QueueSystem nextQueue = null;

    public QueueSystem(int servers, int capacity, double serviceMin, double serviceMax, Random random) {
        this.servers = servers;
        this.capacity = capacity;
        this.stateTimes = new double[capacity + 1];
        this.serviceMin = serviceMin;
        this.serviceMax = serviceMax;
        this.random = random;
    }

    public void setNextQueue(QueueSystem nextQueue) {
        this.nextQueue = nextQueue;
    }

    public void updateState(double timeNow) {
        int state = waitingLine.size();
        stateTimes[state] += timeNow - lastTime;
        lastTime = timeNow;
        this.currentTime = timeNow;
    }

    public boolean canAccept() {
        return waitingLine.size() + busyServers.size() < capacity;
    }

    public void addCustomer(Customer customer, double timeNow, PriorityQueue<Event> scheduler) {
        updateState(timeNow);

        if (!canAccept()) {
            lostCustomers++;
            return;
        }

        if (busyServers.size() < servers) {
            double duration = serviceMin + (serviceMax - serviceMin) * random.nextDouble();
            double endService = timeNow + duration;
            busyServers.add(endService);
            scheduler.add(new Event(endService, EventType.DEPARTURE, this, customer));
        } else {
            waitingLine.add(customer);
        }
    }

    public void finishService(double timeNow, PriorityQueue<Event> scheduler) {
        updateState(timeNow);
        busyServers.poll();

        if (!waitingLine.isEmpty()) {
            Customer next = waitingLine.poll();
            double duration = serviceMin + (serviceMax - serviceMin) * random.nextDouble();
            double endService = timeNow + duration;
            busyServers.add(endService);
            scheduler.add(new Event(endService, EventType.DEPARTURE, this, next));
        }
    }

    public void printStats() {
        System.out.println("Clientes perdidos: " + lostCustomers);
        System.out.println("Distribuição de estados:");
        for (int i = 0; i < stateTimes.length; i++) {
            System.out.printf("Estado %d: %.4f\n", i, stateTimes[i] / currentTime);
        }
    }
}
