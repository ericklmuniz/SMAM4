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
    
    // Novos campos para métricas T2
    int arrivals = 0;
    int departures = 0;
    double busyTime = 0.0;
    double collectFrom = 0.0;  // timestamp para início da coleta (warm-up)

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

    public void setCollectFrom(double t) {
        this.collectFrom = t;
    }

    public void updateState(double timeNow) {
        int inService = busyServers.size();
        int inQueue = waitingLine.size();
        int state = inService + inQueue;
        if (capacity != -1 && state > capacity) state = capacity;

        // Acumular apenas a partir do warm-up
        double start = Math.max(lastTime, collectFrom);
        if (timeNow > start) {
            double dt = timeNow - start;
            stateTimes[state] += dt;
            busyTime += Math.min(servers, inService) * dt;
        }
        lastTime = timeNow;
        this.currentTime = timeNow;
    }

    public boolean canAccept() {
        if (capacity == -1) return true; // Infinite capacity
        return (waitingLine.size() + busyServers.size()) < capacity;
    }

    public void addCustomer(Customer customer, double timeNow, PriorityQueue<Event> scheduler) {
        updateState(timeNow);
        arrivals++;  // Incrementar contador de chegadas

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
        departures++;  // Incrementar contador de partidas

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

    // Classe para métricas da estação
    public static class StationMetrics {
        public int arrivals;
        public int departures;
        public int lost;
        public double L;
        public double Lq;
        public double X;
        public double R;
        public double Wq;
        public double rho;
        public double Tobs;
    }

    public StationMetrics toMetrics(double Tobs) {
        StationMetrics m = new StationMetrics();
        m.arrivals = this.arrivals;
        m.departures = this.departures;
        m.lost = this.lostCustomers;
        m.Tobs = Tobs;
        
        double L = 0.0, Lq = 0.0;
        for (int n = 0; n < stateTimes.length; n++) {
            double p = Tobs > 0 ? stateTimes[n] / Tobs : 0.0;
            L += n * p;
            Lq += Math.max(0, n - servers) * p;
        }
        
        double X = Tobs > 0 ? (double) m.departures / Tobs : 0.0;
        m.L = L; 
        m.Lq = Lq; 
        m.X = X;
        m.R = X > 0 ? L / X : 0.0;
        m.Wq = X > 0 ? Lq / X : 0.0;
        m.rho = (servers > 0 && Tobs > 0) ? (busyTime / (servers * Tobs)) : 0.0;
        
        return m;
    }
}
