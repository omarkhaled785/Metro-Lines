import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetroDBHandler {

    // Method to retrieve unique names of metro lines from the database
    public static List<String> getUniqueLineNames() {
        List<String> uniqueLineNames = new ArrayList<>();
        try {
            Connection connection = DatabaseConnector.connect();
            String query = "SELECT DISTINCT Line FROM stations"; // Assuming "Line" is the column name for metro lines
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                uniqueLineNames.add(resultSet.getString("Line"));
            }
            resultSet.close();
            statement.close();
            DatabaseConnector.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uniqueLineNames;
    }

    // Method to retrieve stations of a specific metro line from the database
    public static List<String> getStationsOfLine(String lineName) {
        List<String> stationsOfLine = new ArrayList<>();
        try {
            Connection connection = DatabaseConnector.connect();
            String query = "SELECT stationName FROM stations WHERE Line = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lineName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stationsOfLine.add(resultSet.getString("stationName"));
            }
            resultSet.close();
            statement.close();
            DatabaseConnector.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stationsOfLine;
    }
}
