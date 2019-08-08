package Model.FirmPackage;

import Model.TemplateDatabase;

public class FirmDatabase extends TemplateDatabase<Firm> {
    public void addFirm(String firmName) {
        super.addItem(new Firm(super.getCurrID(), firmName));
    }

    /**
     * returns a firm based on its name
     *
     * @param firmName - name of firm
     * @return Firm object
     */
    public Firm getFirmByFirmName(String firmName) {
        for (Firm firm : super.getListOfItems()) {
            if (firm.getFirmName().equals(firmName)) {
                return firm;
            }
        }
        return null;
    }
}
