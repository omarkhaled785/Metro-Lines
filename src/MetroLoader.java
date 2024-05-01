import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetroLoader {

    // Method to load metro stations and connections from the database
    public static MetroGraph loadMetro() {
        MetroGraph metroGraph = new MetroGraph();
        try {
            // Connect to the database
            Connection connection = DatabaseConnector.connect();

            // Query to retrieve station information
            String query = "SELECT stationName, Line, distance FROM stations";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Temporary variables to track the previous station on the same line
            Station prevStation = null;
            String prevLine = null;

            // Iterate over the result set
            // Iterate over the result set
            while (resultSet.next()) {
                String name = resultSet.getString("stationName");
                String line = resultSet.getString("Line");
                int distance = resultSet.getInt("distance");

                // If the line has changed or it's the first station on the line
                if (prevLine == null || !prevLine.equals(line)) {
                    prevStation = null;
                }
                if (name.equals("El-Tawfikeya")) {
                    prevStation = metroGraph.getStationByNameOrCreate("Kit Kat");
                }
                // Create a new station object or get the existing one
                Station station = metroGraph.getStationByNameOrCreate(name);

                // Add the line to the station's lines list
                if (!station.getLines().contains(line)) {
                    station.getLines().add(line);
                }

                // Create connection with previous station on the same line
                if (prevStation != null) {
                    metroGraph.addConnection(new Connect(prevStation.getId(), station.getId(), distance));
                    metroGraph.addConnection(new Connect(station.getId(), prevStation.getId(), distance));
                }

                // Update previous station and line
                prevStation = station;
                prevLine = line;
            }

            // Close resources
            resultSet.close();
            statement.close();
            DatabaseConnector.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metroGraph;
    }

    public static void main(String[] args) {

    }
}
