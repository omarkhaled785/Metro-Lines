import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class App extends Application {

    private MetroGraph metroGraph;
    private Label outputLabel;

    // Method to find the common line between two lists of lines
    private String findCommonLine(List<String> lines1, List<String> lines2) {
        for (String line : lines1) {
            if (lines2.contains(line)) {
                return line;
            }
        }
        // If no common line is found, return the first line of the second list
        return lines2.get(0);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the MetroGraph
        metroGraph = MetroLoader.loadMetro();
        // Create labels
        Label projectLabel = new Label("Metro Line Stations");
        projectLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;-fx-text-weight:bold");
        Label welcomeLabel = new Label("Good Morning!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        Label welcomeLabel2 = new Label("Wish you an easy trip!");
        welcomeLabel2.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        Label trip = new Label("Know your trip details");
        trip
                .setStyle("-fx-font-size: 13px; -fx-text-fill: white; -fx-padding: 1px 0 0 0;");
        Label guide = new Label("Choose from here");
        guide
                .setStyle("-fx-font-size: 16px; -fx-text-fill: white;  -fx-padding: 1px 0 0 0;");
        // Create selectors
        ComboBox<String> fromComboBox = new ComboBox<>();
        ComboBox<String> toComboBox = new ComboBox<>();
        // Populate the ComboBoxes with station names
        for (Station station : metroGraph.getStations().values()) {
            fromComboBox.getItems().add(station.getName());
            toComboBox.getItems().add(station.getName());
        }

        Image homeImage = new Image("Home.png");
        ImageView imageView = new ImageView(homeImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Button homeButton = new Button();
        homeButton.setGraphic(imageView);

        HBox homeBox = new HBox();
        homeBox.getChildren().add(homeButton);
        homeBox.setAlignment(Pos.TOP_RIGHT);
        homeBox.setPadding(new Insets(10));
        homeBox.setSpacing(10);

        // Create Proceed button
        Button proceedButton = new Button("Proceed");

        // Add action for Proceed button (handle button click)
        proceedButton.setOnAction(e -> {
            String fromStationName = fromComboBox.getValue();
            String toStationName = toComboBox.getValue();
            int fromStationId = getStationIdByName(fromStationName);
            int toStationId = getStationIdByName(toStationName);

            if (fromStationId != -1 && toStationId != -1) {
                if (fromStationId == toStationId) {
                    outputLabel.setText("Please change your values: From and To stations cannot be the same.");
                } else {
                    Pair<Long, List<Station>> result = metroGraph.shortestPath(fromStationId, toStationId);
                    long minCost = result.getKey() + 10;
                    List<Station> path = result.getValue();
                    long numofStations = minCost / 10;
                    long minPrice;
                    if (numofStations < 10)
                        minPrice = 6;
                    else if (numofStations < 17)
                        minPrice = 8;
                    else if (numofStations < 24)
                        minPrice = 12;
                    else
                        minPrice = 15;
                    StringBuilder outputText = new StringBuilder(
                            "Time: " + String.format("%.3f", (double) minCost / 800 * 60) + " Min" + "\n"
                                    + "Num Of Stations: " + numofStations + "\n" + "The Cost is : " + minPrice + "\n");
                    String currentLine = "";
                    for (int i = 0; i < path.size() - 1; i++) {
                        Station currentStation = path.get(i);
                        Station nextStation = path.get(i + 1);
                        String commonLine = findCommonLine(currentStation.getLines(), nextStation.getLines());
                        if (!commonLine.equals(currentLine)) {
                            if (!currentLine.equals("")) {
                                outputText.append(currentStation);
                                outputText.append(" Now change and go to line ");
                                outputText.append(commonLine);
                                outputText.append("\n");
                            }
                            outputText.append(commonLine).append(": ");
                            currentLine = commonLine;
                        }
                        outputText.append(currentStation.getName()).append(", ");
                    }
                    outputText.append(path.get(path.size() - 1).getName());

                    // Split the outputText into lines based on the new line character '\n'
                    String[] lines = outputText.toString().split("\n");

                    // Create a ListView to display the output
                    ListView<String> outputListView = new ListView<>();

                    // Iterate through each line
                    for (String line : lines) {
                        // Split the line into stations based on the comma delimiter
                        String[] stations = line.split(", ");

                        // Add the line name as the first item in the stations array
                        String firstLine = stations[0];
                        outputListView.getItems().add(firstLine);

                        // Add the remaining stations to the ListView
                        for (int i = 1; i < stations.length; i++) {
                            outputListView.getItems().add("\t" + stations[i]); // Indent stations for clarity
                        }
                    }

                    // Create a VBox for the output page
                    VBox outputPage = new VBox(10);
                    outputPage.setAlignment(Pos.CENTER);
                    outputPage.setStyle("-fx-background-color: rgb(38, 38, 38)");
                    outputPage.setPadding(new Insets(20));

                    // Add the output ListView to the VBox
                    Label PathSen = new Label("Hope You A Good Trip!");
                    PathSen.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
                    outputPage.getChildren().addAll(homeBox, PathSen, outputListView);

                    // Create a new scene for the output page
                    Scene outputScene = new Scene(outputPage, 370, 600);

                    // Set the scene to the stage
                    primaryStage.setScene(outputScene);
                }
            } else {
                outputLabel.setText("Invalid station names");
            }
        });

        Line line = new Line(0, 0, 350, 0);
        line.setStyle("-fx-stroke: #eee;");

        fromComboBox.setPrefSize(300, 5);
        toComboBox.setPrefSize(300, 5);
        proceedButton.setPrefSize(300, 5);
        fromComboBox.setPromptText("From");

        toComboBox.setPromptText("To");
        proceedButton.setStyle("-fx-background-radius: 8px; -fx-margin:10 10 10 10;");
        toComboBox.setStyle(
                "-fx-background-radius: 8px;");
        fromComboBox.setStyle("-fx-background-radius: 8px;");
        VBox sen = new VBox(2);
        sen.setAlignment(Pos.TOP_LEFT);
        sen.getChildren().addAll(welcomeLabel, welcomeLabel2, line);
        sen.setPadding(new Insets(10)); // Add padding around the VBox
        VBox comp = new VBox(4);
        comp.getChildren().addAll(fromComboBox, toComboBox);
        VBox form = new VBox(13);
        form.getChildren().addAll(trip, comp, proceedButton);
        form.setPadding(new Insets(20)); // Add padding around the VBox
        form.setStyle("-fx-background-color:black; -fx-background-radius: 15px;");
        // Set preferred size for the form
        sen.setPrefSize(300, 150);

        // Set preferred size for the form
        form.setMargin(form, new Insets(10));
        // Create StackPane to center the form

        primaryStage.setResizable(false);

        Button LinesDetails = new Button("Lines Details!");
        Button EnterTrip = new Button("Enter a Trip!");
        EnterTrip.setPrefSize(300, 5);
        EnterTrip.setStyle("-fx-background-radius: 8px; -fx-margin:10 10 10 10;");

        LinesDetails.setPrefSize(300, 5);
        LinesDetails.setStyle("-fx-background-radius: 8px; -fx-margin:10 10 10 10;");

        VBox main = new VBox(10);
        main.setAlignment(Pos.CENTER);
        main.setMargin(main, new Insets(10));
        main.setStyle("-fx-background-color:black; -fx-background-radius: 15px;");
        main.setPadding(new Insets(20)); // Add padding around the VBox
        main.setStyle("-fx-background-color:black; -fx-background-radius: 15px;");
        main.setPrefSize(300, 150);
        main.getChildren().addAll(EnterTrip, LinesDetails);

        VBox Title = new VBox(5);
        Title.setAlignment(Pos.CENTER); // Center alignment for the VBox
        Label L = new Label("Metro Stations");
        L.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: fantasy;");
        Title.getChildren().addAll(L);
        VBox Bp = new VBox(20);
        Bp.setAlignment(Pos.CENTER);
        Bp.getChildren().addAll(Title, main);

        Bp.setStyle("-fx-background-color: rgb(38, 38, 38)");
        Scene scene = new Scene(Bp, 370, 600);

        // Set the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Metro Line Stations");

        VBox root = new VBox(homeBox, sen, form);
        homeButton.setOnAction(e -> primaryStage.setScene(scene));

        root.setStyle("-fx-background-color: rgb(38, 38, 38)");
        Scene pr = new Scene(root, 370, 600);
        EnterTrip.setOnAction(e -> {
            primaryStage.setScene(pr);
        });

        LinesDetails.setOnAction(e -> {
            // Create a VBox for the lines details page
            VBox linesDetailsPage = new VBox(10);
            linesDetailsPage.setAlignment(Pos.CENTER);
            linesDetailsPage.setStyle("-fx-background-color: rgb(38, 38, 38)");
            linesDetailsPage.setPadding(new Insets(20));

            // Add a label for selecting a metro line
            Label lineLabel = new Label("Select a Metro Line:");
            lineLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

            // Create a ComboBox to select metro lines
            ComboBox<String> lineComboBox = new ComboBox<>();
            // Populate the ComboBox with unique names of metro lines retrieved from the
            // database
            List<String> uniqueLineNames = MetroDBHandler.getUniqueLineNames();
            lineComboBox.getItems().addAll(uniqueLineNames);

            // Create a VBox to hold the line selection components
            VBox lineSelectionBox = new VBox(10);
            lineSelectionBox.setAlignment(Pos.CENTER);
            lineSelectionBox.getChildren().addAll(lineLabel, lineComboBox);

            // Create a ListView to display stations of the selected line
            ListView<String> stationListView = new ListView<>();
            stationListView.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

            // Add a listener to the ComboBox to load stations of the selected line when a
            // line is chosen
            lineComboBox.setOnAction(event -> {
                String selectedLine = lineComboBox.getValue();
                if (selectedLine != null) {
                    // Fetch stations of the selected line from the database
                    List<String> stationsOfSelectedLine = MetroDBHandler.getStationsOfLine(selectedLine);
                    // Clear previous items and add stations of the selected line to the ListView
                    stationListView.getItems().clear();
                    stationListView.getItems().addAll(stationsOfSelectedLine);
                }
            });

            // Add components to the lines details page VBox
            linesDetailsPage.getChildren().addAll(homeBox, lineSelectionBox, stationListView);

            // Create a new scene for the lines details page
            Scene linesDetailsScene = new Scene(linesDetailsPage, 370, 600);

            // Set the scene to the stage
            primaryStage.setScene(linesDetailsScene);
        });

        // Create output label
        outputLabel = new Label();
        outputLabel.setStyle("-fx-font-size: 14px; -fx-text-fill:white;");
        outputLabel.setWrapText(true);

        // Add output label to the elementsBox
        form.getChildren().add(outputLabel);

        primaryStage.show();
    }

    private int getStationIdByName(String stationName) {
        for (Station station : metroGraph.getStations().values()) {
            if (station.getName().equals(stationName)) {
                return station.getId();
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
