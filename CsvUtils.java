import java.io.*;
import java.util.*;

public class CsvUtils {
    
    public static void writeMetrics(String outputDir, String runId, 
                                  Map<Integer, QueueSystem> stations, double Tobs) throws IOException {
        File dir = new File(outputDir, runId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, "metrics.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Cabeçalho
            writer.println("station_id,station_name,servers,capacity,arrivals,departures,lost,L,Lq,X,R,Wq,rho,Tobs");
            
            // Dados de cada estação
            for (QueueSystem station : stations.values()) {
                QueueSystem.StationMetrics metrics = station.toMetrics(Tobs);
                writer.printf("%d,\"%s\",%d,%d,%d,%d,%d,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f%n",
                            station.getId(),
                            station.getName(),
                            station.servers,
                            station.capacity,
                            metrics.arrivals,
                            metrics.departures,
                            metrics.lost,
                            metrics.L,
                            metrics.Lq,
                            metrics.X,
                            metrics.R,
                            metrics.Wq,
                            metrics.rho,
                            metrics.Tobs);
            }
        }
    }
    
    public static void writeStates(String outputDir, String runId, 
                                 Map<Integer, QueueSystem> stations, double Tobs) throws IOException {
        File dir = new File(outputDir, runId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        for (QueueSystem station : stations.values()) {
            File file = new File(dir, "states_station_" + station.getId() + ".csv");
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("state,p");
                
                for (int state = 0; state < station.stateTimes.length; state++) {
                    double p = Tobs > 0 ? station.stateTimes[state] / Tobs : 0.0;
                    writer.printf("%d,%.6f%n", state, p);
                }
            }
        }
    }
    
    public static void writeSummary(String outputDir, String runId, 
                                  Map<Integer, QueueSystem> stations, double Tobs, 
                                  String stopReason, double currentTime, int rngUsed, int rngMax) throws IOException {
        File dir = new File(outputDir, runId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, "summary.csv");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("metric,value");
            writer.printf("stop_reason,\"%s\"%n", stopReason);
            writer.printf("current_time,%.6f%n", currentTime);
            writer.printf("observation_time,%.6f%n", Tobs);
            writer.printf("rng_used,%d%n", rngUsed);
            writer.printf("rng_max,%d%n", rngMax);
            
            int totalArrivals = 0, totalDepartures = 0, totalLost = 0;
            for (QueueSystem station : stations.values()) {
                totalArrivals += station.arrivals;
                totalDepartures += station.departures;
                totalLost += station.lostCustomers;
            }
            
            writer.printf("total_arrivals,%d%n", totalArrivals);
            writer.printf("total_departures,%d%n", totalDepartures);
            writer.printf("total_lost,%d%n", totalLost);
        }
    }
    
    public static String generateRunId(String configFile) {
        // Usar timestamp ou nome do arquivo sem extensão
        if (configFile != null && !configFile.isEmpty()) {
            String name = new File(configFile).getName();
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf('.'));
            }
            return name + "_" + System.currentTimeMillis();
        }
        return "run_" + System.currentTimeMillis();
    }
}
