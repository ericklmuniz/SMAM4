import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlConfigLoader {
    
    public static SimulationConfig loadConfig(String filename) throws IOException {
        SimulationConfig config = new SimulationConfig();
        List<StationConfig> stations = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            StationConfig currentStation = null;
            List<RoutingRule> currentRouting = null;
            boolean inSimulation = false;
            boolean inArrivals = false;
            boolean inStation = false;
            boolean inRouting = false;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                if (line.equals("simulation:")) {
                    inSimulation = true;
                    inArrivals = false;
                } else if (line.equals("arrivals:")) {
                    inArrivals = true;
                    inSimulation = false;
                } else if (line.equals("stations:")) {
                    inSimulation = false;
                    inArrivals = false;
                } else if (line.startsWith("- id:")) {
                    if (currentStation != null) {
                        currentStation.setRouting(currentRouting);
                        stations.add(currentStation);
                    }
                    currentStation = new StationConfig();
                    currentRouting = new ArrayList<>();
                    inStation = true;
                    inRouting = false;
                    currentStation.setId(extractInt(line));
                } else if (line.startsWith("routing:")) {
                    inRouting = true;
                } else if (inSimulation) {
                    parseSimulationConfig(config, line);
                } else if (inArrivals) {
                    parseArrivalsConfig(config, line);
                } else if (inStation && !inRouting) {
                    parseStationConfig(currentStation, line);
                } else if (inRouting) {
                    parseRoutingRule(currentRouting, line);
                }
            }
            
            if (currentStation != null) {
                currentStation.setRouting(currentRouting);
                stations.add(currentStation);
            }
        }
        
        config.setStations(stations);
        return config;
    }
    
    private static void parseSimulationConfig(SimulationConfig config, String line) {
        if (line.contains("max_random_draws:")) {
            config.setMaxRandomDraws(extractInt(line));
        } else if (line.contains("first_arrival_time:")) {
            config.setFirstArrivalTime(extractDouble(line));
        } else if (line.contains("random_seed:")) {
            config.setRandomSeed(extractLong(line));
        }
    }
    
    private static void parseArrivalsConfig(SimulationConfig config, String line) {
        if (line.contains("inter_arrival_min:")) {
            config.setInterArrivalMin(extractDouble(line));
        } else if (line.contains("inter_arrival_max:")) {
            config.setInterArrivalMax(extractDouble(line));
        } else if (line.contains("destination_station:")) {
            config.setDestinationStation(extractInt(line));
        }
    }
    
    private static void parseStationConfig(StationConfig station, String line) {
        if (line.contains("name:")) {
            station.setName(extractString(line));
        } else if (line.contains("servers:")) {
            station.setServers(extractInt(line));
        } else if (line.contains("capacity:")) {
            station.setCapacity(extractInt(line));
        } else if (line.contains("service_time_min:")) {
            station.setServiceTimeMin(extractDouble(line));
        } else if (line.contains("service_time_max:")) {
            station.setServiceTimeMax(extractDouble(line));
        }
    }
    
    private static void parseRoutingRule(List<RoutingRule> routing, String line) {
        if (line.startsWith("- destination:")) {
            RoutingRule rule = new RoutingRule();
            rule.setDestination(extractInt(line));
            routing.add(rule);
        } else if (line.contains("probability:")) {
            if (!routing.isEmpty()) {
                routing.get(routing.size() - 1).setProbability(extractDouble(line));
            }
        }
    }
    
    private static int extractInt(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }
    
    private static long extractLong(String line) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Long.parseLong(matcher.group());
        }
        return 0;
    }
    
    private static double extractDouble(String line) {
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return 0.0;
    }
    
    private static String extractString(String line) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
