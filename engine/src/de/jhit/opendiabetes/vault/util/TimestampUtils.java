/*
 * Copyright (C) 2017 Jens Heuschkel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.jhit.opendiabetes.vault.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author juehv
 */
public class TimestampUtils {

    public static final String TIME_FORMAT_LIBRE_DE = "yyyy.MM.dd HH:mm";

    public static Date createCleanTimestamp(String dateTime, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date rawDate = df.parse(dateTime);
        return createCleanTimestamp(rawDate);
    }

    public static String timestampToString(Date timestamp, String format) {
        return new SimpleDateFormat(format).format(timestamp);
    }

    public static Date createCleanTimestamp(Date rawDate) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(rawDate);
        // round to 5 minutes
        //        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        //        int mod = unroundedMinutes % 5;
        //        calendar.add(Calendar.MINUTE, mod < 3 ? -mod : (5 - mod));
        // round to 1 minute
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date addMinutesToTimestamp(Date timestamp, long minutes) {
        return new Date(addMinutesToTimestamp(timestamp.getTime(), minutes));
    }

    public static long addMinutesToTimestamp(long timestamp, long minutes) {
        timestamp += minutes * 60000; // 1 m = 60000 ms
        return timestamp;
    }

    public static Date fromLocalDate(LocalDate inputDate) {
        return fromLocalDate(inputDate, 0);
    }

    public static LocalTime dateToLocalTime(Date inputDate) {
        //https://blog.progs.be/542/date-to-java-time
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(inputDate.getTime()), ZoneId.systemDefault()).toLocalTime();
    }

    public static Date fromLocalDate(LocalDate inputDate, long offestInMilliseconds) {
        Date tmpInputDate = Date.from(Instant.from(inputDate
                .atStartOfDay(ZoneId.systemDefault())));
        if (offestInMilliseconds > 0) {
            tmpInputDate = new Date(tmpInputDate.getTime() + offestInMilliseconds);
        }
        return tmpInputDate;
    }

    public static int getHourOfDay(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteOfHour(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        return cal.get(Calendar.MINUTE);
    }

    /**
     *
     * @param startTime Start of the time span
     * @param endTime end of the time span
     * @param timePoint checks if this time point is within timespan
     * @param respectToDate if false, date will be ignored and just hour and
     * minutes are checked
     * @return
     */
    public static boolean withinDateTimeSpan(Date startTime, Date endTime,
            Date timePoint) {
        return startTime.before(timePoint) && endTime.after(timePoint)
                || startTime.equals(timePoint) || endTime.equals(timePoint);

    }

    public static boolean withinTimeSpan(LocalTime startTime, LocalTime endTime, LocalTime timepoint) {
        if (startTime.isBefore(endTime)) {
            // timespan is wihtin a day
            return (timepoint.isAfter(startTime) || timepoint.equals(startTime))
                    && (timepoint.isBefore(endTime) || timepoint.equals(endTime));
        } else {
            // timespan is not within a day (through midnight, e.g.  23:30 - 0:15)
            return (timepoint.isAfter(startTime) || timepoint.equals(startTime))
                    || (timepoint.isBefore(endTime) || timepoint.equals(endTime));
        }
    }

    public static boolean withinTimeSpan(LocalTime startTime, LocalTime endTime, Date timepoint) {

        LocalTime tp = dateToLocalTime(timepoint);
        if (startTime.isBefore(endTime)) {
            // timespan is wihtin a day
            return (tp.isAfter(startTime) || tp.equals(startTime))
                    && (tp.isBefore(endTime) || tp.equals(endTime));
        } else {
            // timespan is not within a day (through midnight, e.g.  23:30 - 0:15)
            return (tp.isAfter(startTime) || tp.equals(startTime))
                    || (tp.isBefore(endTime) || tp.equals(endTime));
        }
    }

}
