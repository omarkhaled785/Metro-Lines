# Metro Line Application

The Metro Line Stations Application is a Java application developed using JavaFX for the user interface and MySQL database for storing metro station data. The application's purpose is to find the shortest and least costly path between any two metro stations within a metro network.

## Features

- **Station Selection**: Users can select the origin and destination stations from dropdown menus.
- **Path Finding**: The application uses Dijkstra's algorithm to find the shortest path between the selected origin and destination stations.
- **Output Display**: The application displays the shortest path, including the time and number of stations, in a user-friendly format.

## Getting Started

To run the Metro Line Stations Application, follow these steps:

1. Clone the repository to your local machine.
2. Ensure you have Java and MySQL installed on your system.
3. Import the provided SQL script to create the database schema and populate it with metro station data.
4. Update the database connection details in the `DatabaseConnector.java` file if necessary.
5. Compile and run the `App.java` file to launch the application.

## Usage

1. Launch the application.
2. Select the origin and destination stations from the dropdown menus.
3. Click the "Proceed" button to find the shortest path.
4. View the output, which includes the shortest path, time, and number of stations.
5. Optionally, click the "Lines Details" button to view details about metro lines and stations.

## Compatibility

- **Java**: The application is written in Java and is compatible with Java versions 8 and above.
- **JavaFX**: The user interface is developed using JavaFX, which requires JavaFX libraries.
- **MySQL**: The application uses MySQL for database operations and is compatible with MySQL databases.
- **Operating System**: The application should run on any operating system that supports Java and MySQL.

## Contributing

Contributions to the Metro Line Stations Application are welcome! If you find any bugs or have suggestions for improvements, please open an issue or submit a pull request.

## License
