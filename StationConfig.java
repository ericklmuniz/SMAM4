import java.util.List;

public class StationConfig {
    private int id;
    private String name;
    private int servers;
    private int capacity;
    private double serviceTimeMin;
    private double serviceTimeMax;
    private List<RoutingRule> routing;

    public StationConfig() {}

    public StationConfig(int id, String name, int servers, int capacity, 
                        double serviceTimeMin, double serviceTimeMax, 
                        List<RoutingRule> routing) {
        this.id = id;
        this.name = name;
        this.servers = servers;
        this.capacity = capacity;
        this.serviceTimeMin = serviceTimeMin;
        this.serviceTimeMax = serviceTimeMax;
        this.routing = routing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServers() {
        return servers;
    }

    public void setServers(int servers) {
        this.servers = servers;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getServiceTimeMin() {
        return serviceTimeMin;
    }

    public void setServiceTimeMin(double serviceTimeMin) {
        this.serviceTimeMin = serviceTimeMin;
    }

    public double getServiceTimeMax() {
        return serviceTimeMax;
    }

    public void setServiceTimeMax(double serviceTimeMax) {
        this.serviceTimeMax = serviceTimeMax;
    }

    public List<RoutingRule> getRouting() {
        return routing;
    }

    public void setRouting(List<RoutingRule> routing) {
        this.routing = routing;
    }
}
