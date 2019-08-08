package Model.FirmPackage;

import java.io.Serializable;

public class Firm implements Serializable {
    private long firmId;
    private String firmName;

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

    @Override
    public String toString() {
        return "[" + getFirmId() + "] " + getFirmName();
    }

}
