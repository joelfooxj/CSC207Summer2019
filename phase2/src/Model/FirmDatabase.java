package Model;

public class FirmDatabase extends TemplateDatabase<Firm> {
    public void addFirm(String firmName) {
        super.addItem(new Firm(super.getCurrID(), firmName));
    }

    /**
     * returns a firm based on its id
     *
     * @param id - id of firm
     * @return Firm object
     */
    public Firm getFirmByID(long id) {
        return super.getItemByID(id);
    }

    /**
     * returns a firm based on its name
     *
     * @param firmName - name of firm
     * @return Firm object
     */
    public Firm getFirmByFirmName(String firmName) {
        for (Firm firm : this) {
            if (firm.getFirmName().equals(firmName)) {
                return firm;
            }
        }
        return null;
    }
}
