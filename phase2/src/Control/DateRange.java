package Control;

import java.time.LocalDate;

public class DateRange {

    private LocalDate startDate;
    private LocalDate endDate;

    DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;

        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
