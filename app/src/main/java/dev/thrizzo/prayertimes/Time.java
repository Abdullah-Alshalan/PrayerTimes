package dev.thrizzo.prayertimes;

public class Time {
    boolean is12;
    public int hour;
    public int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public static Time parse(String in) {
        Time time = new Time(0, 0);
        time.is12 = in.contains("am") || in.contains("pm");

        time.hour += Integer.parseInt(in.split(":")[0]);
        if (in.contains("pm") && time.hour != 12) {
            time.hour += 12;
        }
        in = in.split(" ")[0];
        time.minute += Integer.parseInt(in.split(":")[1]);

        return time;
    }

    public boolean isBiggerThan(Time t) {
        if (this.hour > t.hour)
            return true;
        if (this.hour == t.hour && this.minute > t.minute)
            return true;
        return false;
    }

    @Override
    public String toString() {
        String s;
        if (is12) {
            if (hour >= 12)
                s = (hour - 12) + ":" + minute + " pm";
            else
                s = (hour) + ":" + minute + " am";
        } else
            s = (hour) + ":" + minute;


        return s;
    }

    public Time minus(Time other) {
        Time t = new Time(this.hour, this.minute);
        t.hour -= other.hour;
        t.minute -= other.minute;
        while (t.minute < 0) {
            t.minute += 60;
            t.hour -= 1;
        }
        return t;
    }

    public Time plus(Time other) {
        Time t = new Time(this.hour, this.minute);
        t.hour += other.hour;
        t.minute += other.minute;
        while (t.minute >= 60) {
            t.minute -= 60;
            t.hour += 1;
        }
        return t;
    }
}
