import java.util.*;

import javafx.util.Pair;

public class MetroGraph {
    private Map<Integer, Station> stations; // Map of station ID to Station object
    private List<Connect> connections; // List of connections between stations
    private int nextStatId = 1;

    public MetroGraph() {
        stations = new HashMap<>();
        connections = new ArrayList<>();
    }

    public boolean exists(String name) {
        for (Station station : stations.values()) {
            if (station.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Station getStationByNameOrCreate(String name) {
        // Check if the station already exists
        for (Station station : stations.values()) {
            if (station.getName().equals(name)) {
                return station; // Station already exists, return it
            }
        }
        // If the station doesn't exist, create a new one and add it to the graph
        Station newStation = new Station(nextStatId++, name, new ArrayList<>());
        addStation(newStation);
        return newStation;
    }

    public void addStation(Station station) {
        stations.put(station.getId(), station);
    }

    public void addConnection(Connect connection) {
        connections.add(connection);
    }

    public Map<Integer, Station> getStations() {
        return stations;
    }

    public Pair<Long, List<Station>> shortestPath(int fromStationId, int toStationId) {
        Map<Integer, Long> dist = new HashMap<>(); // Map to store distances
        Map<Integer, Integer> prev = new HashMap<>(); // Map to store previous station in the shortest path
        PriorityQueue<Pair<Integer, Long>> pq = new PriorityQueue<>(Comparator.comparingLong(p -> p.getValue()));

        // Initialize distances
        for (int stationId : stations.keySet()) {
            dist.put(stationId, Long.MAX_VALUE);
            prev.put(stationId, -1);
        }
        dist.put(fromStationId, 0L);
        pq.add(new Pair<>(fromStationId, 0L));

        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            int currentStationId = pq.poll().getKey();
            if (currentStationId == toStationId)
                break; // Stop when reaching the destination

            for (Connect connection : connections) {
                if (connection.getFromStationId() == currentStationId) {
                    int nextStationId = connection.getToStationId();
                    long weight = connection.getWeight();
                    long newDist = dist.get(currentStationId) + weight;
                    if (newDist < dist.get(nextStationId)) {
                        dist.put(nextStationId, newDist);
                        prev.put(nextStationId, currentStationId);
                        pq.add(new Pair<>(nextStationId, newDist));
                    }
                }
            }
        }

        // Reconstruct the shortest path
        List<Station> path = new ArrayList<>();
        long minCost = dist.get(toStationId);
        int temp = toStationId;
        while (temp != -1) {
            path.add(stations.get(temp));
            temp = prev.get(temp);
        }
        Collections.reverse(path);

        return new Pair<>(minCost, path);
    }

}
