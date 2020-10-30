package Rever.PillX.Medicine;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

/*
 * Dosage times for a medicine, includes construction fields as well as list of times for frontend usage
 */
public class DosageTimes {

    //region Pill Date & Time construction fields
    public boolean intervalUsage;
    public LocalDate startDate;
    public LocalDate endDate;
    public List<LocalTime> time;
    public enum Intervals{
        DAYS, WEEKS, MONTHS
    }
    public Intervals intervalType;
    public int interval;

    public boolean[] weekdays = new boolean[7];
    //endregion

    public List<PillReminder> pillDateTime = new ArrayList<>();

    /*
     * Construct a DosageTimes object, if intervalUsage == true, use intervalType and interval. If false, use weekdays.
     */
    public DosageTimes(boolean intervalUsage, LocalDate startDate, LocalDate endDate, List<LocalTime> time, Intervals intervalType, int interval, boolean[] weekdays) {
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
        TemporalAmount temporalInterval;
        switch (intervalType) {
            case DAYS:
                temporalInterval = Period.ofDays(interval);
                break;
            case WEEKS:
                temporalInterval = Period.ofWeeks(interval);
                break;
            case MONTHS:
                temporalInterval = Period.ofMonths(interval);
                break;
            default:
                /* NOT REACHED */
                temporalInterval = Period.ofYears(1);
        }
        for (LocalTime timeOfDay: time) {
            LocalDateTime thisLoopTime = startDate.atTime(timeOfDay);
            while (thisLoopTime.isBefore(endDate.atTime(timeOfDay))) {
                pillDateTime.add(new PillReminder(thisLoopTime));
                thisLoopTime = thisLoopTime.plus(temporalInterval);
            }
        }
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
                    for (LocalTime timeOfDay : time) {
                        pillDateTime.add(new PillReminder(currentLoopDate.atTime(timeOfDay)));
                    }
                }
            }
        }
    }
}
