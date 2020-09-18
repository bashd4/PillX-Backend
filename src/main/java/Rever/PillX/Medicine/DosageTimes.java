package Rever.PillX.Medicine;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class DosageTimes {


    public boolean intervalUsage;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalTime time;
    public enum Intervals{
        HOUR, DAY, WEEK, MONTH
    }

    public Intervals intervalType;
    public int interval;
    public boolean[] weekdays = new boolean[7];

    public List<LocalDateTime> pillDateTimes = new ArrayList<>();

    public DosageTimes(boolean intervalUsage, LocalDate startDate, LocalDate endDate, LocalTime time, Intervals intervalType, int interval, boolean[] weekdays) {
        this.intervalUsage = intervalUsage;
        if (intervalUsage) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.intervalType = intervalType;
            this.interval = interval;
            extrapolateDatesFromIntervals();
        } else {
            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.weekdays = weekdays;
            extrapolateDatesFromWeekdays();
        }

    }

    private void extrapolateDatesFromIntervals() {

    }

    private void extrapolateDatesFromWeekdays() {
        LocalDate currentLoopDate = startDate;
        while (currentLoopDate.isBefore(endDate)) {
            for (int i = 0; i < weekdays.length; i++) {
                if (weekdays[i]) {
                    currentLoopDate = currentLoopDate.with(TemporalAdjusters.next(DayOfWeek.of(i)));
                    if (!currentLoopDate.isBefore(endDate)) {
                        return;
                    }
                    pillDateTimes.add(currentLoopDate.atTime(time));
                }
            }
        }
    }
}
