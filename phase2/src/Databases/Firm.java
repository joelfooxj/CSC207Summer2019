package Databases;

import java.io.Serializable;
import java.util.ArrayList;

public class Firm implements Serializable {
    private long firmId;
    private String firmName;
    private ArrayList<String> locations = new ArrayList<>();

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

    public void addLocation(String location) {
        this.locations.add(location);
    }
}
