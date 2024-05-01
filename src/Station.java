import java.util.List;

public class Station {
    private int id;
    private String name;
    private List<String> lines;

    public Station(int id, String name, List<String> lines) {
        this.id = id;
        this.name = name;
        this.lines = lines;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return name;
    }
}
