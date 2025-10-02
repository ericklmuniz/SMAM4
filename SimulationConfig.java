import java.util.List;

public class SimulationConfig {
    private int maxRandomDraws;
    private double firstArrivalTime;
    private long randomSeed;
    private double interArrivalMin;
    private double interArrivalMax;
    private int destinationStation;
    private List<StationConfig> stations;
    
    // Novos campos para T2
    private Double timeMax;        // tempo máximo em minutos (opcional)
    private Double warmupFrac;     // fração de warm-up 0-1 (opcional)
    private String outputDir;      // diretório de saída (opcional)
    private Integer logEvery;      // log a cada N eventos (opcional)

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

    // Getters e setters para novos campos
    public Double getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(Double timeMax) {
        this.timeMax = timeMax;
    }

    public Double getWarmupFrac() {
        return warmupFrac;
    }

    public void setWarmupFrac(Double warmupFrac) {
        this.warmupFrac = warmupFrac;
    }

    public String getOutputDir() {
        return outputDir != null ? outputDir : "out";  // default se ausente
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public Integer getLogEvery() {
        return logEvery;
    }

    public void setLogEvery(Integer logEvery) {
        this.logEvery = logEvery;
    }
}
