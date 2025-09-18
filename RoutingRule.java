public class RoutingRule {
    private int destination;
    private double probability;

    public RoutingRule() {}

    public RoutingRule(int destination, double probability) {
        this.destination = destination;
        this.probability = probability;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
