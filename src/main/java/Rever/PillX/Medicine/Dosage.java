package Rever.PillX.Medicine;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Dosage {

    public boolean intervalUsage;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDateTime time;
    public enum Intervals{
        HOUR, DAY, WEEK, MONTH
    }

    public Intervals intervalType;
    public int interval;
    public int[] weekdays = new int[7];

    public Dosage(boolean intervalUsage, LocalDate startDate, LocalDate endDate, LocalDateTime time, Intervals intervalType, int interval, int[] weekdays) {
        this.intervalUsage = intervalUsage;
        if (intervalUsage) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.intervalType = intervalType;
            this.interval = interval;
        } else {
            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.weekdays = weekdays;
        }
    }
}
