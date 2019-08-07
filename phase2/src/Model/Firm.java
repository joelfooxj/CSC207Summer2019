package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Firm implements Serializable {
    private long firmId;
    private String firmName;
    private ArrayList<String> locations = new ArrayList<>();

    /**
     * constructor for Firm
     *
     * @param firmId   - id of firm
     * @param firmName - name of firm
     */
    public Firm(long firmId, String firmName) {
        this.firmId = firmId;
        this.firmName = firmName;
    }


    public long getFirmId() {
        return this.firmId;
    }

    public String getFirmName() {
        return this.firmName;
    }

    /**
     * Firms can have multiple locations. This method adds a new location.
     *
     * @param location - location of firm
     */
    public void addLocation(String location) {
        this.locations.add(location);
    }

    @Override
    public String toString() {
        return "[" + getFirmId() + "] " + getFirmName();
    }

}
