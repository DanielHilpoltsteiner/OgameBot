package ogamebot.online;

/**
 *
 */
public class PlayerStats {
    public int highScore = -1;
    private int position;
    private int points;
    private int honor;
    private String name;
    private String honorName;

    PlayerStats(int position, int points, int honor, String name, String honorName) {
        this.position = position;
        this.points = points;
        this.honor = honor;
        this.name = name;
        this.honorName = honorName;
    }

    int getPosition() {
        return position;
    }

    int getPoints() {
        return points;
    }

    int getHonor() {
        return honor;
    }

    String getName() {
        return name;
    }

    String getHonorName() {
        return honorName;
    }
}
