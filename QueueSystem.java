import java.util.*;

public class QueueSystem {
    int id;
    String name;
    int servers;
    int capacity;
    Queue<Customer> waitingLine = new LinkedList<>();
    PriorityQueue<Double> busyServers = new PriorityQueue<>();
    double[] stateTimes;
    int lostCustomers = 0;
    double lastTime = 0;
    double currentTime = 0;
    double serviceMin, serviceMax;
    List<RoutingRule> routingRules;
    Map<Integer, QueueSystem> stationMap;
    CountingRandom countingRandom;

    public QueueSystem(int id, String name, int servers, int capacity, double serviceMin, double serviceMax, 
                      List<RoutingRule> routingRules, CountingRandom countingRandom) {
        this.id = id;
        this.name = name;
        this.servers = servers;
        this.capacity = capacity;
        this.serviceMin = serviceMin;
        this.serviceMax = serviceMax;
        this.routingRules = routingRules;
        this.countingRandom = countingRandom;
        
        int stateArraySize = (capacity == -1) ? 1000 : capacity + 1;
        this.stateTimes = new double[stateArraySize];
    }

    public void setStationMap(Map<Integer, QueueSystem> stationMap) {
        this.stationMap = stationMap;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private int currentState() {
        int inService = busyServers.size();
        int inQueue = waitingLine.size();
        int state = inService + inQueue;
        if (capacity != -1 && state > capacity) state = capacity;
        return state;
    }

    public void updateState(double timeNow) {
        int state = currentState();
        stateTimes[state] += timeNow - lastTime;
        lastTime = timeNow;
        this.currentTime = timeNow;
    }

    public boolean canAccept() {
        if (capacity == -1) return true; // Infinite capacity
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

        if (routingRules != null && !routingRules.isEmpty()) {
            int destination = routeCustomer();
            if (destination != -1 && stationMap != null && stationMap.containsKey(destination)) {
                QueueSystem nextStation = stationMap.get(destination);
                nextStation.addCustomer(new Customer(timeNow), timeNow, scheduler);
            }
        }

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

    private int routeCustomer() {
        if (!countingRandom.canDraw()) {
            return -1; // Exit system if no random numbers available
        }
        
        double random = countingRandom.nextDouble();
        double cumulativeProbability = 0.0;
        
        for (RoutingRule rule : routingRules) {
            cumulativeProbability += rule.getProbability();
            if (random <= cumulativeProbability) {
                return rule.getDestination();
            }
        }
        
        return -1; // Default to exit if no rule matches
    }

    public void printStats(double currentTime) {
        System.out.println("=== " + name + " (ID: " + id + ") ===");
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
        System.out.println();
    }
}
