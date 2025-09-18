import java.util.List;

public class SimulationConfig {
    private int maxRandomDraws;
    private double firstArrivalTime;
    private long randomSeed;
    private double interArrivalMin;
    private double interArrivalMax;
    private int destinationStation;
    private List<StationConfig> stations;

    public SimulationConfig() {}

    public int getMaxRandomDraws() {
        return maxRandomDraws;
    }

    public void setMaxRandomDraws(int maxRandomDraws) {
        this.maxRandomDraws = maxRandomDraws;
    }

    public double getFirstArrivalTime() {
        return firstArrivalTime;
    }

    public void setFirstArrivalTime(double firstArrivalTime) {
        this.firstArrivalTime = firstArrivalTime;
    }

    public long getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(long randomSeed) {
        this.randomSeed = randomSeed;
    }

    public double getInterArrivalMin() {
        return interArrivalMin;
    }

    public void setInterArrivalMin(double interArrivalMin) {
        this.interArrivalMin = interArrivalMin;
    }

    public double getInterArrivalMax() {
        return interArrivalMax;
    }

    public void setInterArrivalMax(double interArrivalMax) {
        this.interArrivalMax = interArrivalMax;
    }

    public int getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(int destinationStation) {
        this.destinationStation = destinationStation;
    }

    public List<StationConfig> getStations() {
        return stations;
    }

    public void setStations(List<StationConfig> stations) {
        this.stations = stations;
    }
}
