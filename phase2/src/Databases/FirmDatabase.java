package Databases;

public class FirmDatabase extends AbstractDatabase<Firm> {
    public void addFirm(String firmName) {
        super.addItem(new Firm(super.getCurrID(), firmName));
    }

    public Firm getFirmByID(long id) {
        return super.getItemByID(id);
    }

    public Firm getFirmByFirmName(String firmName) {
        for (Long i = 0L; i < super.getCurrID(); i++) {

            if (super.getItemByID(i).getFirmName().equals(firmName)) {
                return super.getItemByID(i);
            }
        }
        return null;
    }
}
