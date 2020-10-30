package Rever.PillX.Medicine;

import java.time.LocalDateTime;

/*
 * A class that represents a reminder for a pill to be taken, includes time and taken state
 */
public class PillReminder {
    public LocalDateTime time;
    public boolean taken;

    public PillReminder() { }

    public PillReminder(LocalDateTime time) {
        this.time = time;
        this.taken = false;
    }

    public PillReminder(LocalDateTime time, boolean taken) {
        this.time = time;
        this.taken = taken;
    }

    public boolean getTaken() {
        return taken;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void take() {
        taken = true;
    }

    public void untake() {
        taken = false;
    }
}
