/*
 * Copyright (C) 2017 OpenDiabetes
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.plugin.util;

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
 * This class implements timestamps.
 */
public final class TimestampUtils {

    /**
     * Private constructor to hinder default constructor creation.
     */
    private TimestampUtils() { }

    /**
     * Constructor for a clean timestamp.
     *
     * @param dateTime The data and time for the timestamp.
     * @param format   The format of the dateTime string.
     * @return The Date
     * @throws ParseException Gets thrown if the dateTime can not be parsed.
     */
    public static Date createCleanTimestamp(final String dateTime, final String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date rawDate = df.parse(dateTime);
        return createCleanTimestamp(rawDate);
    }

    /**
     * Utility to convert a timestamp from Date to String.
     *
     * @param timestamp The timestamp to be converted.
     * @param format    The format of the timestamp.
     * @return The timestamp as a string.
     */
    public static String timestampToString(final Date timestamp, final String format) {
        return new SimpleDateFormat(format).format(timestamp);
    }

    /**
     * Method to create a clean timestamp from a date object.
     *
     * @param rawDate The date to create a timestamp of.
     * @return The cleaned timestamp.
     */
    public static Date createCleanTimestamp(final Date rawDate) {
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

    /**
     * Method to add minutes to a Date timestamp.
     *
     * @param timestamp The timestamp to add the minutes to.
     * @param minutes   The minuted to be added.
     * @return The timestamp with minutes.
     */
    public static Date addMinutesToTimestamp(final Date timestamp, final long minutes) {
        return new Date(addMinutesToTimestamp(timestamp.getTime(), minutes));
    }

    /**
     * Method to add minuted to a Long timestamp.
     *
     * @param timestamp The timestamp to add the minutes to.
     * @param minutes   The minutes to be added.
     * @return The timestamp with minutes.
     */
    public static long addMinutesToTimestamp(final long timestamp, final long minutes) {
        long tmpTimestamp = timestamp;
        final int msPerMin = 60000;
        tmpTimestamp += minutes * msPerMin; // 1 m = 60000 ms
        return tmpTimestamp;
    }

    /**
     * Gets the date from a local date without an offset.
     *
     * @param inputDate The local date.
     * @return The local date in the generally used date.
     */
    public static Date fromLocalDate(final LocalDate inputDate) {
        return fromLocalDate(inputDate, 0);
    }

    /**
     * Method to convert a date to local time.
     *
     * @param inputDate The date to be converted.
     * @return The local time.
     * @see  <a href="https://blog.progs.be/542/date-to-java-time">https://blog.progs.be/542/date-to-java-time</a>
     */
    public static LocalTime dateToLocalTime(final Date inputDate) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(inputDate.getTime()), ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Gets the date from a local date with an offset.
     *
     * @param inputDate The local date.
     * @param offsetInMilliseconds The offset of the timestamp used in the local date.
     * @return The local date in the generally used date.
     */
    public static Date fromLocalDate(final LocalDate inputDate, final long offsetInMilliseconds) {
        Date tmpInputDate = Date.from(Instant.from(inputDate.atStartOfDay(ZoneId.systemDefault())));
        if (offsetInMilliseconds > 0) {
            tmpInputDate = new Date(tmpInputDate.getTime() + offsetInMilliseconds);
        }
        return tmpInputDate;
    }

    /**
     * Gets the hours of a day from a timestamp.
     *
     * @param timestamp The timestamp to get the hours from.
     * @return Nr of hours of a day in the timestamp.
     */
    public static int getHourOfDay(final Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Gets the minutes of an hour from a timestamp.
     *
     * @param timestamp The timestamp to get the minutes from.
     * @return Nr of minutes of a hour in the timestamp.
     */
    public static int getMinuteOfHour(final Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * Method to check whether a give point in time is within a span of time.
     *
     * @param startTime Start of the time span.
     * @param endTime   end of the time span.
     * @param timePoint checks if this time point is within timespan.
     * @return True if the time is within the borders.
     */
    public static boolean withinDateTimeSpan(final Date startTime, final Date endTime, final Date timePoint) {
        return startTime.before(timePoint) && endTime.after(timePoint)
                || startTime.equals(timePoint) || endTime.equals(timePoint);

    }

    /**
     * Method to check whether a give point in time is within a span of time.
     *
     * @param startTime Start of the time span.
     * @param endTime   end of the time span.
     * @param timePoint checks if this time point is within timespan.
     * @return True if the time is within the borders.
     */
    public static boolean withinTimeSpan(final LocalTime startTime, final LocalTime endTime, final LocalTime timePoint) {
        if (startTime.isBefore(endTime)) {
            // timespan is wihtin a day
            return (timePoint.isAfter(startTime) || timePoint.equals(startTime))
                    && (timePoint.isBefore(endTime) || timePoint.equals(endTime));
        } else {
            // timespan is not within a day (through midnight, e.g.  23:30 - 0:15)
            return (timePoint.isAfter(startTime) || timePoint.equals(startTime))
                    || (timePoint.isBefore(endTime) || timePoint.equals(endTime));
        }
    }

    /**
     * Method to check whether a give point in time is within a span of time.
     *
     * @param startTime Start of the time span.
     * @param endTime   end of the time span.
     * @param timePoint checks if this time point is within timespan.
     * @return True if the time is within the borders.
     */
    public static boolean withinTimeSpan(final LocalTime startTime, final LocalTime endTime, final Date timePoint) {

        LocalTime tp = dateToLocalTime(timePoint);
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

    /**
     * Copies the specified date for secure getting and setting.
     * This addresses the possible malicious code vulnerability introduced by the mutability of dates.
     *
     * @param date The date to be copied.
     * @return The safe copy of the date.
     */
    public static Date copyTimestamp(final Date date) {
        return new Date(date.getTime());
    }

}
