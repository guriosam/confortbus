package sam.gurio.confortbus.objects;

public class Bus {

    private String lineName;
    private int totalSeats;
    private int takenSeats;
    private int totalUp;
    private int takenUp;
    private String seats;
    private int way;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(int takenSeats) {
        this.takenSeats = takenSeats;
    }

    public int getTotalUp() {
        return totalUp;
    }

    public void setTotalUp(int totalUp) {
        this.totalUp = totalUp;
    }

    public int getTakenUp() {
        return takenUp;
    }

    public void setTakenUp(int takenUp) {
        this.takenUp = takenUp;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }
}
