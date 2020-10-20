package Rever.PillX.Medicine;

import java.time.LocalDateTime;

public class PillReminder {
    public LocalDateTime time;
    public boolean taken;

    public PillReminder(LocalDateTime time) {
        this.time = time;
        this.taken = false;
    }

    public PillReminder(LocalDateTime time, boolean taken) {
        this.time = time;
        this.taken = taken;
    }

    public void take() {
        taken = true;
    }

    public void untake() {
        taken = false;
    }
}
