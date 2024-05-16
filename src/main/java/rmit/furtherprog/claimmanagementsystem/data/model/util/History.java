package rmit.furtherprog.claimmanagementsystem.data.model.util;

public class History {
    private final int id;
    private final String timeStampt;
    private final String type;
    private final String event;

    public History(int id, String timeStampt, String type, String event) {
        this.id = id;
        this.timeStampt = timeStampt;
        this.type = type;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public String getTimeStampt() {
        return timeStampt;
    }

    public String getType() {
        return type;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", timeStampt='" + timeStampt + '\'' +
                ", type='" + type + '\'' +
                ", event='" + event + '\'' +
                '}';
    }
}
