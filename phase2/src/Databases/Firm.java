package Databases;

import java.io.Serializable;

public class Firm implements Serializable {
    private long firmId;
    private String firmName;

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
}
