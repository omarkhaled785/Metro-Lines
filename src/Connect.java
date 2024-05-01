public class Connect {
    private int fromStationId;
    private int toStationId;
    private int weight; // Weight of the connection

    public Connect(int fromStationId, int toStationId, int weight2) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
        this.weight = weight2;
    }

    public int getFromStationId() {
        return fromStationId;
    }

    public int getToStationId() {
        return toStationId;
    }

    public int getWeight() {
        return weight;
    }
}
