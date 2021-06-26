package autroid.business.model.bean;

import java.io.Serializable;

public class UserPointsBE implements Serializable {

    private int level;
    private int points;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
